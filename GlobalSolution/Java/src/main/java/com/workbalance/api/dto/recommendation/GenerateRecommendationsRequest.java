package com.workbalance.api.dto.recommendation;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenerateRecommendationsRequest {

    @Min(value = 1, message = "lastDays must be at least 1")
    @Max(value = 30, message = "lastDays must not exceed 30")
    private Integer lastDays = 7;
}
