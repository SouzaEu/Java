package com.workbalance.domain.service;

import com.workbalance.api.dto.recommendation.GenerateRecommendationsRequest;
import com.workbalance.api.dto.recommendation.RecommendationResponse;
import com.workbalance.domain.entity.MoodEntry;
import com.workbalance.domain.entity.Recommendation;
import com.workbalance.domain.messaging.RecommendationMessage;
import com.workbalance.infra.messaging.RecommendationProducer;
import com.workbalance.infra.repository.MoodEntryRepository;
import com.workbalance.infra.repository.RecommendationRepository;
import com.workbalance.infra.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecommendationServiceTest {

    @Mock
    private RecommendationRepository recommendationRepository;

    @Mock
    private MoodEntryRepository moodEntryRepository;

    @Mock
    private AIRecommendationService aiRecommendationService;

    @Mock
    private RecommendationProducer recommendationProducer;

    @InjectMocks
    private RecommendationService recommendationService;

    private UserPrincipal principal;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        principal = new UserPrincipal(userId, "test@example.com", "password", List.of());
    }

    @Test
    void shouldGetRecommendationsWithLimit() {
        // Given
        Recommendation recommendation = Recommendation.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .type(Recommendation.RecommendationType.BREATHING)
                .message("Take a deep breath")
                .source(Recommendation.RecommendationSource.RULE)
                .build();

        when(recommendationRepository.findByUserIdOrderByCreatedAtDesc(eq(userId), any(PageRequest.class)))
                .thenReturn(List.of(recommendation));

        // When
        List<RecommendationResponse> result = recommendationService.getRecommendations(principal, 5);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMessage()).isEqualTo("Take a deep breath");
        verify(recommendationRepository).findByUserIdOrderByCreatedAtDesc(eq(userId), any(PageRequest.class));
    }

    @Test
    void shouldGenerateRecommendationsAsynchronously() {
        // Given
        ReflectionTestUtils.setField(recommendationService, "asyncEnabled", true);
        GenerateRecommendationsRequest request = new GenerateRecommendationsRequest();
        request.setLastDays(7);

        // When
        List<RecommendationResponse> result = recommendationService.generateRecommendations(principal, request);

        // Then
        assertThat(result).isEmpty();
        verify(recommendationProducer).sendRecommendationRequest(any(RecommendationMessage.class));
        verify(aiRecommendationService, never()).generateAIRecommendations(any(), any());
    }

    @Test
    void shouldGenerateRecommendationsSynchronously() {
        // Given
        ReflectionTestUtils.setField(recommendationService, "asyncEnabled", false);
        GenerateRecommendationsRequest request = new GenerateRecommendationsRequest();
        request.setLastDays(7);

        MoodEntry moodEntry = MoodEntry.builder()
                .userId(userId)
                .date(LocalDate.now())
                .mood(4)
                .stress(2)
                .productivity(4)
                .build();

        Recommendation recommendation = Recommendation.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .type(Recommendation.RecommendationType.HYDRATION)
                .message("Stay hydrated")
                .source(Recommendation.RecommendationSource.AI)
                .build();

        when(moodEntryRepository.findByUserIdAndDateBetweenOrderByDateDesc(
                eq(userId), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(moodEntry));
        when(aiRecommendationService.generateAIRecommendations(eq(userId), any()))
                .thenReturn(List.of(recommendation));
        when(recommendationRepository.saveAll(any())).thenReturn(List.of(recommendation));

        // When
        List<RecommendationResponse> result = recommendationService.generateRecommendations(principal, request);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMessage()).isEqualTo("Stay hydrated");
        verify(aiRecommendationService).generateAIRecommendations(eq(userId), any());
        verify(recommendationRepository).saveAll(any());
    }
}
