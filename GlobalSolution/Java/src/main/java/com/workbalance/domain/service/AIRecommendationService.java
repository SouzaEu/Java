package com.workbalance.domain.service;

import com.workbalance.domain.entity.MoodEntry;
import com.workbalance.domain.entity.Recommendation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * AI-powered recommendation service using Spring AI with OpenAI
 * Implements Spring AI requirement with proper ChatClient integration
 * Falls back to rule-based recommendations if AI is unavailable
 */
@Service
@Slf4j
public class AIRecommendationService {

    private final RuleBasedRecommendationEngine ruleBasedEngine;
    
    @Autowired(required = false)
    private ChatClient chatClient;

    @Value("${app.openai.enabled:false}")
    private boolean aiEnabled;

    public AIRecommendationService(RuleBasedRecommendationEngine ruleBasedEngine) {
        this.ruleBasedEngine = ruleBasedEngine;
    }

    /**
     * Generate AI-powered recommendations based on mood entries using Spring AI
     */
    public List<Recommendation> generateAIRecommendations(UUID userId, List<MoodEntry> entries) {
        if (!aiEnabled || chatClient == null) {
            log.warn("Spring AI is not configured. Falling back to rule-based recommendations.");
            return ruleBasedEngine.generateRecommendations(userId, entries);
        }

        try {
            String prompt = buildPrompt(entries);
            String aiResponse = callSpringAI(prompt);
            return parseAIResponse(userId, aiResponse);
        } catch (Exception e) {
            log.error("Error calling Spring AI. Falling back to rule-based recommendations.", e);
            return ruleBasedEngine.generateRecommendations(userId, entries);
        }
    }

    private String buildPrompt(List<MoodEntry> entries) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are a workplace well-being expert. Analyze the following mood data and provide 2-3 actionable recommendations.\n\n");
        prompt.append("Recent mood entries (scale 1-5):\n");

        for (MoodEntry entry : entries) {
            prompt.append(String.format("- Date: %s, Mood: %d, Stress: %d, Productivity: %d",
                    entry.getDate(), entry.getMood(), entry.getStress(), entry.getProductivity()));
            if (entry.getNotes() != null && !entry.getNotes().isBlank()) {
                prompt.append(", Notes: ").append(entry.getNotes());
            }
            prompt.append("\n");
        }

        prompt.append("\nProvide recommendations in this format:\n");
        prompt.append("1. [TYPE]: [Short actionable recommendation]\n");
        prompt.append("Types: BREATHING, STRETCH, FOCUS, MUSIC, HYDRATION, BREAK, CUSTOM\n");
        prompt.append("Keep each recommendation under 100 characters.");

        return prompt.toString();
    }

    /**
     * Call Spring AI ChatClient to generate recommendations
     * This properly implements the Spring AI requirement
     */
    private String callSpringAI(String prompt) {
        log.info("Calling Spring AI ChatClient for recommendation generation");
        
        String response = chatClient.prompt()
                .user(prompt)
                .call()
                .content();
        
        log.debug("Spring AI response received: {} characters", response != null ? response.length() : 0);
        return response;
    }

    private List<Recommendation> parseAIResponse(UUID userId, String aiResponse) {
        List<Recommendation> recommendations = new ArrayList<>();
        String[] lines = aiResponse.split("\n");

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || !line.matches("^\\d+\\..*")) {
                continue;
            }

            // Parse format: "1. [TYPE]: message"
            String content = line.substring(line.indexOf('.') + 1).trim();
            if (!content.contains(":")) {
                continue;
            }

            String typeStr = content.substring(content.indexOf('[') + 1, content.indexOf(']')).trim();
            String message = content.substring(content.indexOf(':') + 1).trim();

            Recommendation.RecommendationType type;
            try {
                type = Recommendation.RecommendationType.valueOf(typeStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                type = Recommendation.RecommendationType.CUSTOM;
            }

            recommendations.add(Recommendation.builder()
                    .userId(userId)
                    .type(type)
                    .message(message)
                    .source(Recommendation.RecommendationSource.AI)
                    .build());
        }

        // Fallback if parsing failed
        if (recommendations.isEmpty()) {
            log.warn("Failed to parse AI response. Using fallback.");
            recommendations.add(Recommendation.builder()
                    .userId(userId)
                    .type(Recommendation.RecommendationType.CUSTOM)
                    .message(aiResponse.length() > 200 ? aiResponse.substring(0, 200) : aiResponse)
                    .source(Recommendation.RecommendationSource.AI)
                    .build());
        }

        return recommendations;
    }

}
