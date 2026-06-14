package com.example.omnichannel_orders_api.application.dto;

import com.example.omnichannel_orders_api.domain.enums.OrderChannel;
import com.example.omnichannel_orders_api.domain.enums.OrderStatus;
import com.example.omnichannel_orders_api.domain.model.Order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        UUID customerId,
        String customerName,
        UUID unitId,
        String unitName,
        OrderChannel channel,
        OrderStatus status,
        BigDecimal total,
        List<OrderItemResponse> items,
        Instant createdAt,
        Instant updatedAt
) {
    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getCustomer().getId(),
                order.getCustomer().getName(),
                order.getUnit().getId(),
                order.getUnit().getName(),
                order.getChannel(),
                order.getStatus(),
                order.getTotal(),
                order.getItems().stream().map(OrderItemResponse::from).toList(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}
