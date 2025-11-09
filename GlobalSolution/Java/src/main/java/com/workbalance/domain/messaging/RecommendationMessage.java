package com.workbalance.domain.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

/**
 * Message DTO for asynchronous recommendation generation
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationMessage implements Serializable {
    
    private UUID userId;
    private Integer lastDays;
    private String requestId;
}
