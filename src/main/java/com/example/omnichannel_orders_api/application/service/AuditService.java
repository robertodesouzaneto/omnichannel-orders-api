package com.example.omnichannel_orders_api.application.service;

import com.example.omnichannel_orders_api.application.dto.AuditLogResponse;
import com.example.omnichannel_orders_api.application.dto.PageResponse;
import com.example.omnichannel_orders_api.domain.model.AuditLog;
import com.example.omnichannel_orders_api.infrastructure.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(UUID userId, String action, String entityType, UUID entityId, String details) {
        AuditLog entry = AuditLog.builder()
                .userId(userId)
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .details(details)
                .build();
        auditLogRepository.save(entry);
    }

    public PageResponse<AuditLogResponse> listAll(String action, String entityType, UUID userId, Pageable pageable) {
        return PageResponse.from(
                auditLogRepository.findWithFilters(action, entityType, userId, pageable)
                        .map(AuditLogResponse::from)
        );
    }
}
