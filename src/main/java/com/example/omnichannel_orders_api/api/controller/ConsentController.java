package com.example.omnichannel_orders_api.api.controller;

import com.example.omnichannel_orders_api.application.dto.ConsentResponse;
import com.example.omnichannel_orders_api.application.service.ConsentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consent")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Consent", description = "LGPD — data processing consent management")
public class ConsentController {

    private final ConsentService consentService;

    @PostMapping("/accept")
    @Operation(summary = "Accept data processing terms")
    public ResponseEntity<ConsentResponse> accept() {
        return ResponseEntity.ok(consentService.accept());
    }

    @PostMapping("/revoke")
    @Operation(summary = "Revoke data processing consent")
    public ResponseEntity<ConsentResponse> revoke() {
        return ResponseEntity.ok(consentService.revoke());
    }

    @GetMapping("/my")
    @Operation(summary = "Get authenticated user's consent history")
    public List<ConsentResponse> getHistory() {
        return consentService.getHistory();
    }
}
