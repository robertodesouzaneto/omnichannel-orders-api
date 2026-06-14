package com.example.omnichannel_orders_api.application.service;

import com.example.omnichannel_orders_api.application.dto.PaymentCallbackRequest;
import com.example.omnichannel_orders_api.application.dto.PaymentResponse;
import com.example.omnichannel_orders_api.domain.enums.OrderStatus;
import com.example.omnichannel_orders_api.domain.enums.PaymentStatus;
import com.example.omnichannel_orders_api.domain.model.Order;
import com.example.omnichannel_orders_api.domain.model.Payment;
import com.example.omnichannel_orders_api.infrastructure.repository.OrderRepository;
import com.example.omnichannel_orders_api.infrastructure.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public PaymentResponse requestPayment(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found: " + orderId));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalArgumentException(
                    "Cannot request payment for order with status: " + order.getStatus());
        }

        // Idempotência (decisão I-6): rejeita se já existe pagamento ativo
        paymentRepository.findActiveByOrderId(orderId).ifPresent(existing -> {
            throw new IllegalArgumentException(
                    "An active payment already exists for this order");
        });

        Payment payment = Payment.builder()
                .order(order)
                .amount(order.getTotal())
                .build();

        order.changeStatus(OrderStatus.AWAITING_PAYMENT);
        orderRepository.save(order);

        return PaymentResponse.from(paymentRepository.save(payment));
    }

    @Transactional
    public PaymentResponse processCallback(PaymentCallbackRequest request) {
        Payment payment = paymentRepository.findByIdAndStatus(request.paymentId(), PaymentStatus.REQUESTED)
                .orElseThrow(() -> new NoSuchElementException(
                        "Active payment not found: " + request.paymentId()));

        Order order = payment.getOrder();

        if (request.approved()) {
            payment.approve();
            order.changeStatus(OrderStatus.IN_PREPARATION);
            orderRepository.save(order);
        } else {
            payment.reject();
            // order stays at AWAITING_PAYMENT — operator decides next step
        }

        return PaymentResponse.from(paymentRepository.save(payment));
    }
}
