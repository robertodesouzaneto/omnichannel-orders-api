package com.example.omnichannel_orders_api.application.dto;

import com.example.omnichannel_orders_api.domain.enums.ConsentAction;
import com.example.omnichannel_orders_api.domain.model.Consent;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

public record ConsentResponse(
        UUID id,
        @Schema(example = "GRANTED") ConsentAction action,
        Instant createdAt
) {
    public static ConsentResponse from(Consent consent) {
        return new ConsentResponse(
                consent.getId(),
                consent.getAction(),
                consent.getCreatedAt()
        );
    }
}
