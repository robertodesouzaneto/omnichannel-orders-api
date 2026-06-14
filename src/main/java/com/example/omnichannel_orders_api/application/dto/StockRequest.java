package com.example.omnichannel_orders_api.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record StockRequest(

        @NotNull
        UUID productId,

        @Min(value = 0, message = "Quantity must be zero or greater")
        int quantity
) {}
