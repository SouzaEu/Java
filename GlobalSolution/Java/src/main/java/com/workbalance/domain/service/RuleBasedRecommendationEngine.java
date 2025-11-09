package com.workbalance.domain.service;

import com.workbalance.domain.entity.MoodEntry;
import com.workbalance.domain.entity.Recommendation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Rule-based recommendation engine
 * Centralized logic for generating recommendations based on mood patterns
 * Eliminates code duplication between AIRecommendationService and RecommendationConsumer
 */
@Component
@Slf4j
public class RuleBasedRecommendationEngine {

    /**
     * Generate rule-based recommendations from mood entries
     */
    public List<Recommendation> generateRecommendations(UUID userId, List<MoodEntry> entries) {
        List<Recommendation> recommendations = new ArrayList<>();

        if (entries.isEmpty()) {
            recommendations.add(createRecommendation(
                    userId,
                    Recommendation.RecommendationType.CUSTOM,
                    "Start tracking your mood daily to receive personalized recommendations!"
            ));
            return recommendations;
        }

        // Calculate metrics
        long highStressCount = entries.stream().filter(e -> e.getStress() >= 4).count();
        long lowProductivityCount = entries.stream().filter(e -> e.getProductivity() <= 2).count();
        long lowMoodCount = entries.stream().filter(e -> e.getMood() <= 2).count();
        double avgStress = entries.stream().mapToInt(MoodEntry::getStress).average().orElse(0);
        double avgMood = entries.stream().mapToInt(MoodEntry::getMood).average().orElse(0);
        double avgProductivity = entries.stream().mapToInt(MoodEntry::getProductivity).average().orElse(0);

        log.debug("Mood metrics - avgStress: {}, avgMood: {}, avgProductivity: {}, highStressCount: {}, lowMoodCount: {}",
                avgStress, avgMood, avgProductivity, highStressCount, lowMoodCount);

        // Rule 1: High stress pattern
        if (highStressCount >= 2) {
            recommendations.add(createRecommendation(userId,
                    Recommendation.RecommendationType.BREATHING,
                    "Your stress levels have been high. Try a 3-minute breathing exercise."));
        }

        // Rule 2: Low productivity with high stress
        if (lowProductivityCount >= 2 && avgStress >= 3.5) {
            recommendations.add(createRecommendation(userId,
                    Recommendation.RecommendationType.FOCUS,
                    "Silence notifications for 20 minutes and try a focus playlist."));
        }

        // Rule 3: Low mood pattern
        if (lowMoodCount >= 2) {
            recommendations.add(createRecommendation(userId,
                    Recommendation.RecommendationType.STRETCH,
                    "Take a 5-minute stretch break to reset your posture and mood."));
        }

        // Rule 4: Consistently high stress
        if (avgStress >= 4.0) {
            recommendations.add(createRecommendation(userId,
                    Recommendation.RecommendationType.BREAK,
                    "Consider taking a longer break. Your stress levels are consistently high."));
        }

        // Rule 5: Low mood average
        if (avgMood <= 2.5) {
            recommendations.add(createRecommendation(userId,
                    Recommendation.RecommendationType.MUSIC,
                    "Listen to uplifting music to boost your mood."));
        }

        // Rule 6: Energy conservation tip (sustainable energy connection)
        if (avgProductivity >= 4.0 && avgStress <= 2.5) {
            recommendations.add(createRecommendation(userId,
                    Recommendation.RecommendationType.CUSTOM,
                    "Great work-life balance! Consider leaving on time to reduce office energy consumption."));
        }

        // Default positive recommendation
        if (recommendations.isEmpty()) {
            recommendations.add(createRecommendation(userId,
                    Recommendation.RecommendationType.HYDRATION,
                    "You're doing great! Remember to stay hydrated throughout the day."));
        }

        log.info("Generated {} rule-based recommendations for user {}", recommendations.size(), userId);
        return recommendations;
    }

    private Recommendation createRecommendation(UUID userId, Recommendation.RecommendationType type, String message) {
        return Recommendation.builder()
                .userId(userId)
                .type(type)
                .message(message)
                .source(Recommendation.RecommendationSource.RULE)
                .build();
    }
}
