package com.example.omnichannel_orders_api.application.dto;

import com.example.omnichannel_orders_api.domain.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record AuthResponse(
        String token,
        UUID userId,
        String name,
        @Schema(example = "CUSTOMER")
        UserRole role
) {}
