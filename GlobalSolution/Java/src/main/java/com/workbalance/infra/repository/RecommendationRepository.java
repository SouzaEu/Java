package com.workbalance.infra.repository;

import com.workbalance.domain.entity.Recommendation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, UUID> {

    List<Recommendation> findByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);

    List<Recommendation> findByUserIdOrderByCreatedAtDesc(UUID userId);
}
