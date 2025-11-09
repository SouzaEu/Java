package com.workbalance.api.dto.mood;

import com.workbalance.infra.validation.NotOlderThan;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMoodEntryRequest {

    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Date must be today or in the past")
    @NotOlderThan(days = 30, message = "Date must be within the last 30 days")
    private LocalDate date;

    @NotNull(message = "Mood is required")
    @Min(value = 1, message = "Mood must be between 1 and 5")
    @Max(value = 5, message = "Mood must be between 1 and 5")
    private Integer mood;

    @NotNull(message = "Stress is required")
    @Min(value = 1, message = "Stress must be between 1 and 5")
    @Max(value = 5, message = "Stress must be between 1 and 5")
    private Integer stress;

    @NotNull(message = "Productivity is required")
    @Min(value = 1, message = "Productivity must be between 1 and 5")
    @Max(value = 5, message = "Productivity must be between 1 and 5")
    private Integer productivity;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;

    @Size(max = 10, message = "Maximum 10 tags allowed")
    private List<@Size(max = 20, message = "Each tag must not exceed 20 characters") String> tags;
}
