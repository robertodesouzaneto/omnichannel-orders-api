package com.example.omnichannel_orders_api.api.controller;

import com.example.omnichannel_orders_api.application.dto.*;
import com.example.omnichannel_orders_api.application.service.UnitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/units")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Units", description = "Store unit and stock management")
public class UnitController {

    private final UnitService unitService;

    @GetMapping
    @Operation(summary = "List all active units")
    public List<UnitResponse> listActive() {
        return unitService.listActive();
    }

    @PostMapping
    @Operation(summary = "Create a new unit (MANAGER or ADMIN)")
    public ResponseEntity<UnitResponse> create(@Valid @RequestBody UnitRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(unitService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a unit (MANAGER or ADMIN)")
    public UnitResponse update(@PathVariable UUID id, @Valid @RequestBody UnitRequest request) {
        return unitService.update(id, request);
    }

    @GetMapping("/{id}/stock")
    @Operation(summary = "List stock for a unit")
    public List<StockResponse> listStock(@PathVariable UUID id) {
        return unitService.listStock(id);
    }

    @PostMapping("/{id}/stock")
    @Operation(summary = "Set stock quantity for a product in a unit (MANAGER or ADMIN)")
    public ResponseEntity<StockResponse> setStock(
            @PathVariable UUID id,
            @Valid @RequestBody StockRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(unitService.setStock(id, request));
    }
}
