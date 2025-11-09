package com.workbalance.api.dto.mood;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMoodEntryRequest {

    @Min(value = 1, message = "Mood must be between 1 and 5")
    @Max(value = 5, message = "Mood must be between 1 and 5")
    private Integer mood;

    @Min(value = 1, message = "Stress must be between 1 and 5")
    @Max(value = 5, message = "Stress must be between 1 and 5")
    private Integer stress;

    @Min(value = 1, message = "Productivity must be between 1 and 5")
    @Max(value = 5, message = "Productivity must be between 1 and 5")
    private Integer productivity;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;

    @Size(max = 10, message = "Maximum 10 tags allowed")
    private List<@Size(max = 20, message = "Each tag must not exceed 20 characters") String> tags;
}
