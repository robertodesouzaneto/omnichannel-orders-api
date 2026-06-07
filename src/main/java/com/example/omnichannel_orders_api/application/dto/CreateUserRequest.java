package com.example.omnichannel_orders_api.application.dto;

import com.example.omnichannel_orders_api.domain.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(

        @NotBlank @Email
        String email,

        @NotBlank @Size(min = 8, message = "Password must be at least 8 characters")
        @Schema(example = "senha1234")
        String password,

        @NotBlank
        String name,

        @NotNull
        @Schema(
            description = "Staff role — CUSTOMER is not allowed here",
            allowableValues = {"ATTENDANT", "KITCHEN", "MANAGER", "ADMIN"},
            example = "ATTENDANT"
        )
        UserRole role
) {}
