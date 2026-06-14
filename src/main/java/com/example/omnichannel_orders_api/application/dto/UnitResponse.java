package com.example.omnichannel_orders_api.application.dto;

import com.example.omnichannel_orders_api.domain.model.Unit;

import java.time.Instant;
import java.util.UUID;

public record UnitResponse(
        UUID id,
        String name,
        String address,
        boolean active,
        Instant createdAt
) {
    public static UnitResponse from(Unit u) {
        return new UnitResponse(u.getId(), u.getName(), u.getAddress(), u.isActive(), u.getCreatedAt());
    }
}
