package com.workbalance.infra.messaging;

import com.workbalance.domain.messaging.RecommendationMessage;
import com.workbalance.infra.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * Producer for sending recommendation generation messages to queue
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendRecommendationRequest(RecommendationMessage message) {
        log.info("Sending recommendation request to queue: userId={}, requestId={}", 
                message.getUserId(), message.getRequestId());
        
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.RECOMMENDATIONS_EXCHANGE,
                RabbitMQConfig.RECOMMENDATIONS_ROUTING_KEY,
                message
        );
        
        log.info("Recommendation request sent successfully: requestId={}", message.getRequestId());
    }
}
