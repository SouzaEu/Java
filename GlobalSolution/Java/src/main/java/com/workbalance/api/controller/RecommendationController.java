package com.workbalance.api.controller;

import com.workbalance.api.dto.recommendation.GenerateRecommendationsRequest;
import com.workbalance.api.dto.recommendation.RecommendationResponse;
import com.workbalance.domain.service.RecommendationService;
import com.workbalance.infra.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recommendations")
@RequiredArgsConstructor
@Tag(name = "Recommendations", description = "Well-being recommendation endpoints")
@SecurityRequirement(name = "bearerAuth")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping
    @Operation(summary = "Get user's recommendations")
    public ResponseEntity<List<RecommendationResponse>> getRecommendations(
            @AuthenticationPrincipal UserPrincipal principal,
            @Parameter(description = "Maximum number of recommendations to return")
            @RequestParam(required = false) Integer limit
    ) {
        List<RecommendationResponse> response = recommendationService.getRecommendations(principal, limit);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/generate")
    @Operation(summary = "Generate new recommendations based on recent mood entries")
    public ResponseEntity<List<RecommendationResponse>> generateRecommendations(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody GenerateRecommendationsRequest request
    ) {
        List<RecommendationResponse> response = recommendationService.generateRecommendations(principal, request);
        return ResponseEntity.ok(response);
    }
}
