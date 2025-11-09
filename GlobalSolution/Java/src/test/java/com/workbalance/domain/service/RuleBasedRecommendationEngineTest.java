package com.workbalance.domain.service;

import com.workbalance.domain.entity.MoodEntry;
import com.workbalance.domain.entity.Recommendation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class RuleBasedRecommendationEngineTest {

    private RuleBasedRecommendationEngine engine;
    private UUID userId;

    @BeforeEach
    void setUp() {
        engine = new RuleBasedRecommendationEngine();
        userId = UUID.randomUUID();
    }

    @Test
    void shouldGenerateStartTrackingRecommendationWhenNoEntries() {
        // Given
        List<MoodEntry> entries = new ArrayList<>();

        // When
        List<Recommendation> recommendations = engine.generateRecommendations(userId, entries);

        // Then
        assertThat(recommendations).hasSize(1);
        assertThat(recommendations.get(0).getType()).isEqualTo(Recommendation.RecommendationType.CUSTOM);
        assertThat(recommendations.get(0).getMessage()).contains("Start tracking");
    }

    @Test
    void shouldGenerateBreathingRecommendationForHighStress() {
        // Given
        List<MoodEntry> entries = List.of(
                createMoodEntry(3, 4, 3),
                createMoodEntry(3, 5, 3)
        );

        // When
        List<Recommendation> recommendations = engine.generateRecommendations(userId, entries);

        // Then
        assertThat(recommendations).isNotEmpty();
        assertThat(recommendations.stream()
                .anyMatch(r -> r.getType() == Recommendation.RecommendationType.BREATHING))
                .isTrue();
    }

    @Test
    void shouldGenerateFocusRecommendationForLowProductivityAndHighStress() {
        // Given
        List<MoodEntry> entries = List.of(
                createMoodEntry(3, 4, 2),
                createMoodEntry(3, 4, 1)
        );

        // When
        List<Recommendation> recommendations = engine.generateRecommendations(userId, entries);

        // Then
        assertThat(recommendations).isNotEmpty();
        assertThat(recommendations.stream()
                .anyMatch(r -> r.getType() == Recommendation.RecommendationType.FOCUS))
                .isTrue();
    }

    @Test
    void shouldGenerateStretchRecommendationForLowMood() {
        // Given
        List<MoodEntry> entries = List.of(
                createMoodEntry(2, 3, 3),
                createMoodEntry(1, 3, 3)
        );

        // When
        List<Recommendation> recommendations = engine.generateRecommendations(userId, entries);

        // Then
        assertThat(recommendations).isNotEmpty();
        assertThat(recommendations.stream()
                .anyMatch(r -> r.getType() == Recommendation.RecommendationType.STRETCH))
                .isTrue();
    }

    @Test
    void shouldGenerateEnergyConservationRecommendationForGoodBalance() {
        // Given
        List<MoodEntry> entries = List.of(
                createMoodEntry(4, 2, 4),
                createMoodEntry(5, 2, 5)
        );

        // When
        List<Recommendation> recommendations = engine.generateRecommendations(userId, entries);

        // Then
        assertThat(recommendations).isNotEmpty();
        assertThat(recommendations.stream()
                .anyMatch(r -> r.getMessage().contains("energy consumption")))
                .isTrue();
    }

    private MoodEntry createMoodEntry(int mood, int stress, int productivity) {
        return MoodEntry.builder()
                .userId(userId)
                .date(LocalDate.now())
                .mood(mood)
                .stress(stress)
                .productivity(productivity)
                .build();
    }
}
