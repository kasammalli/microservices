package com.example.audit.model;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * Represents a single audit event for an entity change.
 * Stored in the same database as the microservice (e.g. same MongoDB).
 */
@Document(collection = "audit_events")
@CompoundIndex(name = "audit_key_entity_idx", def = "{'auditKey1': 1, 'auditKey2': 1}")
public class AuditEvent {

    @Id
    private String id;

    @NotBlank
    @Indexed
    private String auditKey1;

    @NotBlank
    @Indexed
    private String auditKey2;

    @NotBlank
    private String action;

    @NotBlank
    private String entityId;

    private String changedAttribute;
    private String currentValue;
    private String newValue;
    private String sourceSystem;
    private String sourceParty;

    @Indexed
    private Instant createdAt;

    public AuditEvent() {
    }

    public AuditEvent(String id, String auditKey1, String auditKey2, String action, String entityId,
                      String changedAttribute, String currentValue, String newValue,
                      String sourceSystem, String sourceParty, Instant createdAt) {
        this.id = id;
        this.auditKey1 = auditKey1;
        this.auditKey2 = auditKey2;
        this.action = action;
        this.entityId = entityId;
        this.changedAttribute = changedAttribute;
        this.currentValue = currentValue;
        this.newValue = newValue;
        this.sourceSystem = sourceSystem;
        this.sourceParty = sourceParty;
        this.createdAt = createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void setCreatedAtIfNull() {
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getAuditKey1() { return auditKey1; }
    public void setAuditKey1(String auditKey1) { this.auditKey1 = auditKey1; }
    public String getAuditKey2() { return auditKey2; }
    public void setAuditKey2(String auditKey2) { this.auditKey2 = auditKey2; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getEntityId() { return entityId; }
    public void setEntityId(String entityId) { this.entityId = entityId; }
    public String getChangedAttribute() { return changedAttribute; }
    public void setChangedAttribute(String changedAttribute) { this.changedAttribute = changedAttribute; }
    public String getCurrentValue() { return currentValue; }
    public void setCurrentValue(String currentValue) { this.currentValue = currentValue; }
    public String getNewValue() { return newValue; }
    public void setNewValue(String newValue) { this.newValue = newValue; }
    public String getSourceSystem() { return sourceSystem; }
    public void setSourceSystem(String sourceSystem) { this.sourceSystem = sourceSystem; }
    public String getSourceParty() { return sourceParty; }
    public void setSourceParty(String sourceParty) { this.sourceParty = sourceParty; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public static final class Builder {
        private String id;
        private String auditKey1;
        private String auditKey2;
        private String action;
        private String entityId;
        private String changedAttribute;
        private String currentValue;
        private String newValue;
        private String sourceSystem;
        private String sourceParty;
        private Instant createdAt;

        public Builder id(String id) { this.id = id; return this; }
        public Builder auditKey1(String auditKey1) { this.auditKey1 = auditKey1; return this; }
        public Builder auditKey2(String auditKey2) { this.auditKey2 = auditKey2; return this; }
        public Builder action(String action) { this.action = action; return this; }
        public Builder entityId(String entityId) { this.entityId = entityId; return this; }
        public Builder changedAttribute(String changedAttribute) { this.changedAttribute = changedAttribute; return this; }
        public Builder currentValue(String currentValue) { this.currentValue = currentValue; return this; }
        public Builder newValue(String newValue) { this.newValue = newValue; return this; }
        public Builder sourceSystem(String sourceSystem) { this.sourceSystem = sourceSystem; return this; }
        public Builder sourceParty(String sourceParty) { this.sourceParty = sourceParty; return this; }
        public Builder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }

        public AuditEvent build() {
            return new AuditEvent(id, auditKey1, auditKey2, action, entityId,
                    changedAttribute, currentValue, newValue, sourceSystem, sourceParty, createdAt);
        }
    }
}
