package com.example.omnichannel_orders_api.api.controller;

import com.example.omnichannel_orders_api.application.dto.CreateOrderRequest;
import com.example.omnichannel_orders_api.application.dto.OrderResponse;
import com.example.omnichannel_orders_api.application.dto.PageResponse;
import com.example.omnichannel_orders_api.application.dto.UpdateOrderStatusRequest;
import com.example.omnichannel_orders_api.application.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Orders", description = "Order management")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @Operation(summary = "List all orders (MANAGER, ADMIN)")
    public PageResponse<OrderResponse> listAll(
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        return orderService.listAll(pageable);
    }

    @PostMapping
    @Operation(summary = "Create a new order")
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID")
    public OrderResponse getById(@PathVariable UUID id) {
        return orderService.getById(id);
    }

    @GetMapping("/my")
    @Operation(summary = "List authenticated customer's orders (paginated)")
    public PageResponse<OrderResponse> listMyOrders(
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        return orderService.listMyOrders(pageable);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update order status (KITCHEN, MANAGER, ADMIN)")
    public OrderResponse updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateOrderStatusRequest request) {
        return orderService.updateStatus(id, request);
    }
}
