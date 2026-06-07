package com.example.omnichannel_orders_api.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank @Email
        String email,

        @NotBlank
        String password
) {}
