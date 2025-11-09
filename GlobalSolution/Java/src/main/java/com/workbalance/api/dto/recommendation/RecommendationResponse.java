package com.workbalance.api.dto.recommendation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendationResponse {

    private UUID id;
    private UUID userId;
    private String type;
    private String message;
    private String actionUrl;
    private Instant scheduledAt;
    private String source;
    private Instant createdAt;
}
