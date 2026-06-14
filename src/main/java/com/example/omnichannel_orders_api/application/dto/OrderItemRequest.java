package com.example.omnichannel_orders_api.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OrderItemRequest(

        @NotNull
        UUID productId,

        @Min(value = 1, message = "Quantity must be at least 1")
        int quantity
) {}
