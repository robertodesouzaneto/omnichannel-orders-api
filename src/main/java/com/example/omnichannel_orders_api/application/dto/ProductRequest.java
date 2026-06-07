package com.example.omnichannel_orders_api.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductRequest(

        @NotBlank
        String name,

        String description,

        @NotNull @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
        BigDecimal price,

        @NotNull
        UUID categoryId,

        String imageUrl,

        Boolean active
) {}
