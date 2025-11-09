package com.workbalance.domain.service;

import com.workbalance.api.dto.mood.CreateMoodEntryRequest;
import com.workbalance.api.dto.mood.MoodEntryResponse;
import com.workbalance.domain.entity.MoodEntry;
import com.workbalance.domain.exception.MoodEntryAlreadyExistsException;
import com.workbalance.domain.exception.ResourceNotFoundException;
import com.workbalance.infra.repository.MoodEntryRepository;
import com.workbalance.infra.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MoodEntryServiceTest {

    @Mock
    private MoodEntryRepository moodEntryRepository;

    @InjectMocks
    private MoodEntryService moodEntryService;

    private UserPrincipal principal;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        principal = new UserPrincipal(userId, "test@example.com", "password", null);
    }

    @Test
    void createMoodEntry_Success() {
        // Arrange
        CreateMoodEntryRequest request = new CreateMoodEntryRequest();
        request.setDate(LocalDate.now());
        request.setMood(4);
        request.setStress(2);
        request.setProductivity(5);

        MoodEntry savedEntry = MoodEntry.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .date(request.getDate())
                .mood(request.getMood())
                .stress(request.getStress())
                .productivity(request.getProductivity())
                .build();

        when(moodEntryRepository.existsByUserIdAndDate(userId, request.getDate())).thenReturn(false);
        when(moodEntryRepository.save(any(MoodEntry.class))).thenReturn(savedEntry);

        // Act
        MoodEntryResponse response = moodEntryService.createMoodEntry(principal, request);

        // Assert
        assertNotNull(response);
        assertEquals(savedEntry.getId(), response.getId());
        assertEquals(request.getMood(), response.getMood());
        verify(moodEntryRepository).save(any(MoodEntry.class));
    }

    @Test
    void createMoodEntry_ThrowsExceptionWhenAlreadyExists() {
        // Arrange
        CreateMoodEntryRequest request = new CreateMoodEntryRequest();
        request.setDate(LocalDate.now());
        request.setMood(4);
        request.setStress(2);
        request.setProductivity(5);

        when(moodEntryRepository.existsByUserIdAndDate(userId, request.getDate())).thenReturn(true);

        // Act & Assert
        assertThrows(MoodEntryAlreadyExistsException.class, () -> {
            moodEntryService.createMoodEntry(principal, request);
        });
        verify(moodEntryRepository, never()).save(any());
    }

    @Test
    void getMoodEntry_Success() {
        // Arrange
        UUID entryId = UUID.randomUUID();
        MoodEntry entry = MoodEntry.builder()
                .id(entryId)
                .userId(userId)
                .date(LocalDate.now())
                .mood(4)
                .stress(2)
                .productivity(5)
                .build();

        when(moodEntryRepository.findByIdAndUserId(entryId, userId)).thenReturn(Optional.of(entry));

        // Act
        MoodEntryResponse response = moodEntryService.getMoodEntry(principal, entryId);

        // Assert
        assertNotNull(response);
        assertEquals(entryId, response.getId());
        assertEquals(4, response.getMood());
    }

    @Test
    void getMoodEntry_ThrowsExceptionWhenNotFound() {
        // Arrange
        UUID entryId = UUID.randomUUID();
        when(moodEntryRepository.findByIdAndUserId(entryId, userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            moodEntryService.getMoodEntry(principal, entryId);
        });
    }

    @Test
    void deleteMoodEntry_Success() {
        // Arrange
        UUID entryId = UUID.randomUUID();
        MoodEntry entry = MoodEntry.builder()
                .id(entryId)
                .userId(userId)
                .date(LocalDate.now())
                .mood(4)
                .stress(2)
                .productivity(5)
                .build();

        when(moodEntryRepository.findByIdAndUserId(entryId, userId)).thenReturn(Optional.of(entry));

        // Act
        moodEntryService.deleteMoodEntry(principal, entryId);

        // Assert
        verify(moodEntryRepository).delete(entry);
    }
}
