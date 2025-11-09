package com.workbalance.domain.service;

import com.workbalance.api.dto.mood.CreateMoodEntryRequest;
import com.workbalance.api.dto.mood.MoodEntryListResponse;
import com.workbalance.api.dto.mood.MoodEntryResponse;
import com.workbalance.api.dto.mood.UpdateMoodEntryRequest;
import com.workbalance.domain.entity.MoodEntry;
import com.workbalance.domain.exception.MoodEntryAlreadyExistsException;
import com.workbalance.domain.exception.ResourceNotFoundException;
import com.workbalance.infra.repository.MoodEntryRepository;
import com.workbalance.infra.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MoodEntryService {

    private final MoodEntryRepository moodEntryRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = "userMoodEntries", key = "#principal.id + '-' + #page + '-' + #size + '-' + #from + '-' + #to")
    public MoodEntryListResponse getMoodEntries(
            UserPrincipal principal,
            int page,
            int size,
            LocalDate from,
            LocalDate to
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MoodEntry> moodEntries;

        if (from != null || to != null) {
            moodEntries = moodEntryRepository.findByUserIdAndDateRange(
                    principal.getId(),
                    from,
                    to,
                    pageable
            );
        } else {
            moodEntries = moodEntryRepository.findByUserIdOrderByDateDesc(
                    principal.getId(),
                    pageable
            );
        }

        return MoodEntryListResponse.builder()
                .items(moodEntries.getContent().stream()
                        .map(this::mapToResponse)
                        .toList())
                .total(moodEntries.getTotalElements())
                .build();
    }

    @Transactional
    @CacheEvict(value = {"moodEntries", "userMoodEntries"}, allEntries = true)
    public MoodEntryResponse createMoodEntry(UserPrincipal principal, CreateMoodEntryRequest request) {
        log.debug("Creating mood entry: userId={}, date={}, mood={}, stress={}, productivity={}", 
                principal.getId(), request.getDate(), request.getMood(), request.getStress(), request.getProductivity());
        
        // Check for duplicate entry (business rule)
        if (moodEntryRepository.existsByUserIdAndDate(principal.getId(), request.getDate())) {
            log.warn("Duplicate mood entry attempt: userId={}, date={}", principal.getId(), request.getDate());
            throw new MoodEntryAlreadyExistsException(request.getDate());
        }
        
        // Date validation is now handled by @NotOlderThan annotation in DTO

        MoodEntry moodEntry = MoodEntry.builder()
                .userId(principal.getId())
                .date(request.getDate())
                .mood(request.getMood())
                .stress(request.getStress())
                .productivity(request.getProductivity())
                .notes(request.getNotes())
                .tags(request.getTags() != null ? request.getTags() : new ArrayList<>())
                .build();

        moodEntry = moodEntryRepository.save(moodEntry);
        log.info("Mood entry created successfully: id={}, userId={}, date={}", 
                moodEntry.getId(), principal.getId(), request.getDate());
        return mapToResponse(moodEntry);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "moodEntries", key = "#id")
    public MoodEntryResponse getMoodEntry(UserPrincipal principal, UUID id) {
        MoodEntry moodEntry = moodEntryRepository.findByIdAndUserId(id, principal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("MoodEntry", "id", id));

        return mapToResponse(moodEntry);
    }

    @Transactional
    @CacheEvict(value = {"moodEntries", "userMoodEntries"}, allEntries = true)
    public MoodEntryResponse updateMoodEntry(
            UserPrincipal principal,
            UUID id,
            UpdateMoodEntryRequest request
    ) {
        MoodEntry moodEntry = moodEntryRepository.findByIdAndUserId(id, principal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("MoodEntry", "id", id));

        if (request.getMood() != null) {
            moodEntry.setMood(request.getMood());
        }
        if (request.getStress() != null) {
            moodEntry.setStress(request.getStress());
        }
        if (request.getProductivity() != null) {
            moodEntry.setProductivity(request.getProductivity());
        }
        if (request.getNotes() != null) {
            moodEntry.setNotes(request.getNotes());
        }
        if (request.getTags() != null) {
            moodEntry.setTags(request.getTags());
        }

        moodEntry = moodEntryRepository.save(moodEntry);
        return mapToResponse(moodEntry);
    }

    @Transactional
    @CacheEvict(value = {"moodEntries", "userMoodEntries"}, allEntries = true)
    public void deleteMoodEntry(UserPrincipal principal, UUID id) {
        MoodEntry moodEntry = moodEntryRepository.findByIdAndUserId(id, principal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("MoodEntry", "id", id));

        moodEntryRepository.delete(moodEntry);
    }

    private MoodEntryResponse mapToResponse(MoodEntry moodEntry) {
        return MoodEntryResponse.builder()
                .id(moodEntry.getId())
                .userId(moodEntry.getUserId())
                .date(moodEntry.getDate())
                .mood(moodEntry.getMood())
                .stress(moodEntry.getStress())
                .productivity(moodEntry.getProductivity())
                .notes(moodEntry.getNotes())
                .tags(moodEntry.getTags())
                .createdAt(moodEntry.getCreatedAt())
                .updatedAt(moodEntry.getUpdatedAt())
                .build();
    }
}
