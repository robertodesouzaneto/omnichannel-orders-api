package com.example.omnichannel_orders_api.application.dto;

import com.example.omnichannel_orders_api.domain.enums.UserRole;

import java.util.UUID;

public record AuthResponse(
        String token,
        UUID userId,
        String name,
        UserRole role
) {}
