package com.workbalance.api.dto.mood;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoodEntryResponse {

    private UUID id;
    private UUID userId;
    private LocalDate date;
    private Integer mood;
    private Integer stress;
    private Integer productivity;
    private String notes;
    private List<String> tags;
    private Instant createdAt;
    private Instant updatedAt;
}
