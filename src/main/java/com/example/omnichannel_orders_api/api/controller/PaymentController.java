package com.example.omnichannel_orders_api.api.controller;

import com.example.omnichannel_orders_api.application.dto.PaymentCallbackRequest;
import com.example.omnichannel_orders_api.application.dto.PaymentResponse;
import com.example.omnichannel_orders_api.application.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Payment request and callback processing")
public class PaymentController {

    private final PaymentService paymentService;

    @Value("${gateway.secret}")
    private String gatewaySecret;

    @PostMapping("/request/{orderId}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Request payment for an order")
    public ResponseEntity<PaymentResponse> requestPayment(@PathVariable UUID orderId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.requestPayment(orderId));
    }

    @PostMapping("/callback")
    @Operation(summary = "Process payment gateway callback (authenticated via X-Gateway-Token header)")
    public ResponseEntity<PaymentResponse> callback(
            @RequestHeader("X-Gateway-Token") String token,
            @Valid @RequestBody PaymentCallbackRequest request) {

        if (!gatewaySecret.equals(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid gateway token");
        }

        return ResponseEntity.ok(paymentService.processCallback(request));
    }
}
