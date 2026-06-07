package com.example.omnichannel_orders_api.application.dto;

import com.example.omnichannel_orders_api.domain.model.Category;

import java.time.Instant;
import java.util.UUID;

public record CategoryResponse(
        UUID id,
        String name,
        String description,
        boolean active,
        Instant createdAt
) {
    public static CategoryResponse from(Category c) {
        return new CategoryResponse(c.getId(), c.getName(), c.getDescription(), c.isActive(), c.getCreatedAt());
    }
}
