package com.example.omnichannel_orders_api.application.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PaymentCallbackRequest(
        @NotNull UUID paymentId,
        @NotNull Boolean approved
) {}
