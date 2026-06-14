package com.example.omnichannel_orders_api.application.dto;

import com.example.omnichannel_orders_api.domain.model.Stock;

import java.util.UUID;

public record StockResponse(
        UUID id,
        UUID unitId,
        String unitName,
        UUID productId,
        String productName,
        int quantity
) {
    public static StockResponse from(Stock s) {
        return new StockResponse(
                s.getId(),
                s.getUnit().getId(),
                s.getUnit().getName(),
                s.getProduct().getId(),
                s.getProduct().getName(),
                s.getQuantity()
        );
    }
}
