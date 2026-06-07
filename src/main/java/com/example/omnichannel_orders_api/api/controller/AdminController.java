package com.example.omnichannel_orders_api.api.controller;

import com.example.omnichannel_orders_api.application.dto.CreateUserRequest;
import com.example.omnichannel_orders_api.application.dto.UserResponse;
import com.example.omnichannel_orders_api.application.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin", description = "Administrative operations (ADMIN only)")
public class AdminController {

    private final UserService userService;

    @PostMapping("/users")
    @Operation(summary = "Create a staff account (ADMIN only)")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createStaff(request));
    }
}
