package com.example.audit.service;

import com.example.audit.model.AuditEvent;

/**
 * Non-blocking API for persisting audit events.
 * Implementations should persist asynchronously so the caller can continue.
 */
public interface AuditService {

    /**
     * Saves the audit event asynchronously. Returns immediately; persistence runs in background.
     * Microservice can continue with next steps without waiting.
     *
     * @param event the audit event to save (must have auditKey1, auditKey2, action, entityId set)
     */
    void saveAuditEventAsync(AuditEvent event);
}
