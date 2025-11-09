package com.workbalance.api.controller;

import com.workbalance.api.dto.mood.CreateMoodEntryRequest;
import com.workbalance.api.dto.mood.MoodEntryListResponse;
import com.workbalance.api.dto.mood.MoodEntryResponse;
import com.workbalance.api.dto.mood.UpdateMoodEntryRequest;
import com.workbalance.domain.service.MoodEntryService;
import com.workbalance.infra.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/mood-entries")
@RequiredArgsConstructor
@Tag(name = "Mood Entries", description = "Mood entry tracking endpoints")
@SecurityRequirement(name = "bearerAuth")
public class MoodEntryController {

    private final MoodEntryService moodEntryService;

    @GetMapping
    @Operation(summary = "Get user's mood entries with pagination and optional date filtering")
    public ResponseEntity<MoodEntryListResponse> getMoodEntries(
            @AuthenticationPrincipal UserPrincipal principal,
            @Parameter(description = "Page number (0-indexed)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Start date filter (ISO format)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "End date filter (ISO format)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        MoodEntryListResponse response = moodEntryService.getMoodEntries(principal, page, size, from, to);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create a new mood entry")
    public ResponseEntity<MoodEntryResponse> createMoodEntry(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateMoodEntryRequest request
    ) {
        MoodEntryResponse response = moodEntryService.createMoodEntry(principal, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a specific mood entry by ID")
    public ResponseEntity<MoodEntryResponse> getMoodEntry(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID id
    ) {
        MoodEntryResponse response = moodEntryService.getMoodEntry(principal, id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a mood entry")
    public ResponseEntity<MoodEntryResponse> updateMoodEntry(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID id,
            @Valid @RequestBody UpdateMoodEntryRequest request
    ) {
        MoodEntryResponse response = moodEntryService.updateMoodEntry(principal, id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a mood entry")
    public ResponseEntity<Void> deleteMoodEntry(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID id
    ) {
        moodEntryService.deleteMoodEntry(principal, id);
        return ResponseEntity.noContent().build();
    }
}
