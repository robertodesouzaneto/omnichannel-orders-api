package com.example.omnichannel_orders_api.infrastructure.repository;

import com.example.omnichannel_orders_api.domain.model.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {

    @Query("""
            SELECT a FROM AuditLog a
            WHERE (:action IS NULL OR a.action = :action)
              AND (:entityType IS NULL OR a.entityType = :entityType)
              AND (:userId IS NULL OR a.userId = :userId)
            ORDER BY a.createdAt DESC
            """)
    Page<AuditLog> findWithFilters(String action, String entityType, UUID userId, Pageable pageable);
}
