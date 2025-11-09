package com.workbalance.infra.messaging;

import com.workbalance.domain.entity.MoodEntry;
import com.workbalance.domain.entity.Recommendation;
import com.workbalance.domain.messaging.RecommendationMessage;
import com.workbalance.domain.service.RuleBasedRecommendationEngine;
import com.workbalance.infra.config.RabbitMQConfig;
import com.workbalance.infra.repository.MoodEntryRepository;
import com.workbalance.infra.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Consumer for processing recommendation generation messages from queue
 * Uses centralized RuleBasedRecommendationEngine to avoid code duplication
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationConsumer {

    private final MoodEntryRepository moodEntryRepository;
    private final RecommendationRepository recommendationRepository;
    private final RuleBasedRecommendationEngine recommendationEngine;

    @RabbitListener(queues = RabbitMQConfig.RECOMMENDATIONS_QUEUE)
    public void processRecommendationRequest(RecommendationMessage message) {
        log.info("Processing recommendation request: userId={}, requestId={}", 
                message.getUserId(), message.getRequestId());

        try {
            int lastDays = message.getLastDays() != null ? message.getLastDays() : 7;
            LocalDate startDate = LocalDate.now().minusDays(lastDays);
            LocalDate endDate = LocalDate.now();

            List<MoodEntry> recentEntries = moodEntryRepository.findByUserIdAndDateBetweenOrderByDateDesc(
                    message.getUserId(),
                    startDate,
                    endDate
            );

            // Use centralized recommendation engine
            List<Recommendation> recommendations = recommendationEngine.generateRecommendations(
                    message.getUserId(),
                    recentEntries
            );

            recommendationRepository.saveAll(recommendations);

            log.info("Recommendations generated successfully: userId={}, count={}, requestId={}", 
                    message.getUserId(), recommendations.size(), message.getRequestId());

        } catch (Exception e) {
            log.error("Error processing recommendation request: requestId={}", 
                    message.getRequestId(), e);
            throw e; // Will trigger retry mechanism
        }
    }

}
