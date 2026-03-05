package com.example.audit.repository;

import com.example.audit.model.AuditEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditEventRepository extends MongoRepository<AuditEvent, String> {

    List<AuditEvent> findByAuditKey2OrderByCreatedAtDesc(String auditKey2);

    List<AuditEvent> findByEntityIdOrderByCreatedAtDesc(String entityId);
}
