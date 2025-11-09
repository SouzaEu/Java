package com.workbalance.domain.service;

import com.workbalance.api.dto.recommendation.GenerateRecommendationsRequest;
import com.workbalance.api.dto.recommendation.RecommendationResponse;
import com.workbalance.domain.entity.MoodEntry;
import com.workbalance.domain.entity.Recommendation;
import com.workbalance.domain.messaging.RecommendationMessage;
import com.workbalance.infra.messaging.RecommendationProducer;
import com.workbalance.infra.repository.MoodEntryRepository;
import com.workbalance.infra.repository.RecommendationRepository;
import com.workbalance.infra.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final MoodEntryRepository moodEntryRepository;
    private final AIRecommendationService aiRecommendationService;
    private final RecommendationProducer recommendationProducer;

    @Value("${app.recommendations.async:true}")
    private boolean asyncEnabled;

    @Transactional(readOnly = true)
    @Cacheable(value = "recommendations", key = "#principal.id + '-' + #limit")
    public List<RecommendationResponse> getRecommendations(UserPrincipal principal, Integer limit) {
        List<Recommendation> recommendations;

        if (limit != null && limit > 0) {
            recommendations = recommendationRepository.findByUserIdOrderByCreatedAtDesc(
                    principal.getId(),
                    PageRequest.of(0, limit)
            );
        } else {
            recommendations = recommendationRepository.findByUserIdOrderByCreatedAtDesc(
                    principal.getId()
            );
        }

        return recommendations.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    @CacheEvict(value = "recommendations", allEntries = true)
    public List<RecommendationResponse> generateRecommendations(
            UserPrincipal principal,
            GenerateRecommendationsRequest request
    ) {
        int lastDays = request.getLastDays() != null ? request.getLastDays() : 7;

        // If async is enabled, send to queue and return empty list
        if (asyncEnabled) {
            log.info("Sending recommendation generation request to queue: userId={}", principal.getId());
            RecommendationMessage message = RecommendationMessage.builder()
                    .userId(principal.getId())
                    .lastDays(lastDays)
                    .requestId(java.util.UUID.randomUUID().toString())
                    .build();
            recommendationProducer.sendRecommendationRequest(message);
            return List.of(); // Return empty, recommendations will be generated asynchronously
        }

        // Synchronous generation with AI
        LocalDate startDate = LocalDate.now().minusDays(lastDays);
        LocalDate endDate = LocalDate.now();

        List<MoodEntry> recentEntries = moodEntryRepository.findByUserIdAndDateBetweenOrderByDateDesc(
                principal.getId(),
                startDate,
                endDate
        );

        List<Recommendation> recommendations = aiRecommendationService.generateAIRecommendations(
                principal.getId(),
                recentEntries
        );

        recommendations = recommendationRepository.saveAll(recommendations);

        return recommendations.stream()
                .map(this::mapToResponse)
                .toList();
    }

    private RecommendationResponse mapToResponse(Recommendation recommendation) {
        return RecommendationResponse.builder()
                .id(recommendation.getId())
                .userId(recommendation.getUserId())
                .type(recommendation.getType().name().toLowerCase())
                .message(recommendation.getMessage())
                .actionUrl(recommendation.getActionUrl())
                .scheduledAt(recommendation.getScheduledAt())
                .source(recommendation.getSource().name().toLowerCase())
                .createdAt(recommendation.getCreatedAt())
                .build();
    }
}
