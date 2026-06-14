package com.example.omnichannel_orders_api.domain.model;

import com.example.omnichannel_orders_api.domain.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, updatable = false)
    private Instant requestedAt;

    private Instant processedAt;

    @PrePersist
    void prePersist() {
        this.requestedAt = Instant.now();
        this.status = PaymentStatus.REQUESTED;
    }

    public void approve() {
        this.status = PaymentStatus.APPROVED;
        this.processedAt = Instant.now();
    }

    public void reject() {
        this.status = PaymentStatus.REJECTED;
        this.processedAt = Instant.now();
    }
}
