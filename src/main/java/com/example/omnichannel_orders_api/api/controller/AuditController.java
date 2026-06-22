package com.example.omnichannel_orders_api.api.controller;

import com.example.omnichannel_orders_api.application.dto.AuditLogResponse;
import com.example.omnichannel_orders_api.application.dto.PageResponse;
import com.example.omnichannel_orders_api.application.service.AuditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/audit")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Audit", description = "Audit log — sensitive actions (MANAGER, ADMIN)")
public class AuditController {

    private final AuditService auditService;

    @GetMapping
    @Operation(summary = "List audit log entries with optional filters (MANAGER, ADMIN)")
    public PageResponse<AuditLogResponse> listAll(
            @Parameter(description = "Filter by action (e.g. ORDER_CREATED, ORDER_STATUS_CHANGED)")
            @RequestParam(required = false) String action,
            @Parameter(description = "Filter by entity type (e.g. Order)")
            @RequestParam(required = false) String entityType,
            @Parameter(description = "Filter by user ID")
            @RequestParam(required = false) UUID userId,
            @PageableDefault(size = 50) Pageable pageable) {
        return auditService.listAll(action, entityType, userId, pageable);
    }
}
