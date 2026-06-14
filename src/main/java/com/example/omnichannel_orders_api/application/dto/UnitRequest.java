package com.example.omnichannel_orders_api.application.dto;

import jakarta.validation.constraints.NotBlank;

public record UnitRequest(

        @NotBlank
        String name,

        @NotBlank
        String address
) {}
