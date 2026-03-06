package com.example.audit.service.impl;

import com.example.audit.config.AuditConfigurationProperties;
import com.example.audit.model.AuditEvent;
import com.example.audit.repository.AuditEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

public class AsyncAuditPersistenceDelegate {

    private static final Logger log = LoggerFactory.getLogger(AsyncAuditPersistenceDelegate.class);

    private final AuditEventRepository auditEventRepository;
    private final AuditConfigurationProperties config;

    public AsyncAuditPersistenceDelegate(AuditEventRepository auditEventRepository,
                                          AuditConfigurationProperties config) {
        this.auditEventRepository = auditEventRepository;
        this.config = config;
    }

    @Async("auditTaskExecutor")
    public void persistAsync(AuditEvent event) {
        try {
            String entityType = resolveEntityType(event.getAuditKey2());
            if (entityType != null) {
                AuditConfigurationProperties.EntityAuditConfig entityConfig = config.getEntities().get(entityType);
                if (entityConfig != null) {
                    if (!entityConfig.getActions().contains(event.getAction())) {
                        log.debug("Action {} not in configured actions for entity {}; saving anyway", event.getAction(), entityType);
                    }
                    if (event.getChangedAttribute() != null && !"N/A".equals(event.getChangedAttribute())
                            && !entityConfig.getAttributes().contains(event.getChangedAttribute())) {
                        log.debug("Attribute {} not in configured attributes for entity {}; saving anyway", event.getChangedAttribute(), entityType);
                    }
                }
            }
            auditEventRepository.save(event);
        } catch (Exception e) {
            log.error("Failed to persist audit event: {}", event.getAuditKey2(), e);
        }
    }

    private static String resolveEntityType(String auditKey2) {
        if (auditKey2 == null || !auditKey2.startsWith("audit:")) return null;
        String[] parts = auditKey2.split(":", 3);
        return parts.length >= 2 ? parts[1] : null;
    }
}
