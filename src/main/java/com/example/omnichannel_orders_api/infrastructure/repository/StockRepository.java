package com.example.omnichannel_orders_api.infrastructure.repository;

import com.example.omnichannel_orders_api.domain.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StockRepository extends JpaRepository<Stock, UUID> {
    Optional<Stock> findByUnitIdAndProductId(UUID unitId, UUID productId);
}
