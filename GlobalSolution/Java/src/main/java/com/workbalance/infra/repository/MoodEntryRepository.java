package com.workbalance.infra.repository;

import com.workbalance.domain.entity.MoodEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MoodEntryRepository extends JpaRepository<MoodEntry, UUID> {

    Page<MoodEntry> findByUserIdOrderByDateDesc(UUID userId, Pageable pageable);

    @Query("SELECT m FROM MoodEntry m WHERE m.userId = :userId " +
           "AND (:from IS NULL OR m.date >= :from) " +
           "AND (:to IS NULL OR m.date <= :to) " +
           "ORDER BY m.date DESC")
    Page<MoodEntry> findByUserIdAndDateRange(
        @Param("userId") UUID userId,
        @Param("from") LocalDate from,
        @Param("to") LocalDate to,
        Pageable pageable
    );

    Optional<MoodEntry> findByIdAndUserId(UUID id, UUID userId);

    boolean existsByUserIdAndDate(UUID userId, LocalDate date);

    List<MoodEntry> findByUserIdAndDateBetweenOrderByDateDesc(
        UUID userId,
        LocalDate startDate,
        LocalDate endDate
    );

    long countByUserId(UUID userId);
}
