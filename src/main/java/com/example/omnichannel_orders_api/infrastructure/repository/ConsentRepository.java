package com.example.omnichannel_orders_api.infrastructure.repository;

import com.example.omnichannel_orders_api.domain.model.Consent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConsentRepository extends JpaRepository<Consent, UUID> {
    List<Consent> findAllByUserIdOrderByCreatedAtDesc(UUID userId);
    Optional<Consent> findTopByUserIdOrderByCreatedAtDesc(UUID userId);
}
