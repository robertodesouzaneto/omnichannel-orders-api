package com.example.omnichannel_orders_api.application.dto;

import com.example.omnichannel_orders_api.domain.enums.OrderChannel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CreateOrderRequest(

        @NotNull
        UUID unitId,

        @NotNull
        OrderChannel channel,

        @NotEmpty(message = "Order must have at least one item")
        @Valid
        List<OrderItemRequest> items
) {}
