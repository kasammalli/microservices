package com.example.audit.service.impl;

import com.example.audit.model.AuditEvent;
import com.example.audit.service.AuditService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Set;

public class AsyncAuditServiceImpl implements AuditService {

    private static final Logger log = LoggerFactory.getLogger(AsyncAuditServiceImpl.class);

    private final Validator validator;
    private final AsyncAuditPersistenceDelegate persistenceDelegate;

    public AsyncAuditServiceImpl(Validator validator,
                                  AsyncAuditPersistenceDelegate persistenceDelegate) {
        this.validator = validator;
        this.persistenceDelegate = persistenceDelegate;
    }

    @Override
    public void saveAuditEventAsync(AuditEvent event) {
        if (event == null) return;
        event.setCreatedAtIfNull();
        Set<ConstraintViolation<AuditEvent>> violations = validator.validate(event);
        if (!violations.isEmpty()) {
            log.warn("Audit event validation failed: {}", violations);
            return;
        }
        persistenceDelegate.persistAsync(event);
    }
}
