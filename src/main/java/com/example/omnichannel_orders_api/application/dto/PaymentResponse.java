package com.example.omnichannel_orders_api.application.dto;

import com.example.omnichannel_orders_api.domain.enums.PaymentStatus;
import com.example.omnichannel_orders_api.domain.model.Payment;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentResponse(
        UUID id,
        UUID orderId,
        PaymentStatus status,
        BigDecimal amount,
        Instant requestedAt,
        Instant processedAt
) {
    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getStatus(),
                payment.getAmount(),
                payment.getRequestedAt(),
                payment.getProcessedAt()
        );
    }
}
