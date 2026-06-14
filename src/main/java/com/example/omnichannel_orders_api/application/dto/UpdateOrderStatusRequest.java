package com.example.omnichannel_orders_api.application.dto;

import com.example.omnichannel_orders_api.domain.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusRequest(
        @NotNull
        @Schema(allowableValues = {"READY", "DELIVERED", "CANCELED"}, example = "READY")
        OrderStatus status
) {}
