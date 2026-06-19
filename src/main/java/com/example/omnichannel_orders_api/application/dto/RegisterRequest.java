package com.example.omnichannel_orders_api.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank @Email
        String email,

        @NotBlank @Size(min = 8, message = "Password must be at least 8 characters")
        @Schema(example = "senha1234")
        String password,

        @NotBlank
        String name,

        @NotNull
        @AssertTrue(message = "You must accept the data processing terms to register")
        Boolean consentAccepted
) {}
