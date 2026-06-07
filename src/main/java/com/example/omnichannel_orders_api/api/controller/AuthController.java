package com.example.omnichannel_orders_api.api.controller;

import com.example.omnichannel_orders_api.application.dto.AuthResponse;
import com.example.omnichannel_orders_api.application.dto.LoginRequest;
import com.example.omnichannel_orders_api.application.dto.RegisterRequest;
import com.example.omnichannel_orders_api.application.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "User registration and authentication")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register/customer")
    @Operation(summary = "Create a new customer account", description = "Public endpoint — always creates a CUSTOMER account. Staff accounts are managed via POST /admin/users (ADMIN only).")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate and receive a JWT token")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
