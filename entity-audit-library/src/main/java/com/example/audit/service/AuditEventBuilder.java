package com.example.audit.service;

import com.example.audit.model.AuditEvent;

/**
 * Fluent builder for creating audit events with the standard key pattern.
 * Use from microservices to build events and pass to {@link AuditService#saveAuditEventAsync(AuditEvent)}.
 */
public final class AuditEventBuilder {

    private String auditKey1;
    private String entityType;
    private String entityId;
    private String action;
    private String changedAttribute;
    private String currentValue;
    private String newValue;
    private String sourceSystem;
    private String sourceParty;

    public static AuditEventBuilder create() {
        return new AuditEventBuilder();
    }

    public AuditEventBuilder auditKey1(String auditKey1) {
        this.auditKey1 = auditKey1;
        return this;
    }

    public AuditEventBuilder entity(String entityType, String entityId) {
        this.entityType = entityType;
        this.entityId = entityId;
        return this;
    }

    public AuditEventBuilder action(String action) {
        this.action = action;
        return this;
    }

    public AuditEventBuilder changedAttribute(String changedAttribute) {
        this.changedAttribute = changedAttribute;
        return this;
    }

    public AuditEventBuilder currentValue(String currentValue) {
        this.currentValue = currentValue;
        return this;
    }

    public AuditEventBuilder newValue(String newValue) {
        this.newValue = newValue;
        return this;
    }

    public AuditEventBuilder sourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
        return this;
    }

    public AuditEventBuilder sourceParty(String sourceParty) {
        this.sourceParty = sourceParty;
        return this;
    }

    public AuditEvent build() {
        String auditKey2 = (entityType != null && entityId != null)
                ? "audit:" + entityType + ":" + entityId
                : null;
        return AuditEvent.builder()
                .auditKey1(auditKey1 != null ? auditKey1 : "")
                .auditKey2(auditKey2 != null ? auditKey2 : "")
                .entityId(entityId != null ? entityId : "")
                .action(action != null ? action : "")
                .changedAttribute(changedAttribute != null ? changedAttribute : "N/A")
                .currentValue(currentValue != null ? currentValue : "N/A")
                .newValue(newValue != null ? newValue : "N/A")
                .sourceSystem(sourceSystem)
                .sourceParty(sourceParty)
                .build();
    }
}
