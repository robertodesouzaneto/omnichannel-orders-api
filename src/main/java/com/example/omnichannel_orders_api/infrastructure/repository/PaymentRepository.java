package com.example.omnichannel_orders_api.infrastructure.repository;

import com.example.omnichannel_orders_api.domain.enums.PaymentStatus;
import com.example.omnichannel_orders_api.domain.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    @Query("SELECT p FROM Payment p WHERE p.order.id = :orderId AND p.status IN ('REQUESTED', 'APPROVED')")
    Optional<Payment> findActiveByOrderId(UUID orderId);

    Optional<Payment> findByIdAndStatus(UUID id, PaymentStatus status);
}
