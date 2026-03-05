package com.example.audit.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Binds audit-service-configurations.yml.
 * Entities, actions and attributes are configurable per entity type.
 */
@ConfigurationProperties(prefix = "audit")
public class AuditConfigurationProperties {

    private Map<String, EntityAuditConfig> entities = Map.of();

    public Map<String, EntityAuditConfig> getEntities() {
        return entities;
    }

    public void setEntities(Map<String, EntityAuditConfig> entities) {
        this.entities = entities != null ? entities : Map.of();
    }

    public static class EntityAuditConfig {
        private List<String> actions = new ArrayList<>();
        private List<String> attributes = new ArrayList<>();

        public List<String> getActions() {
            return actions;
        }

        public void setActions(List<String> actions) {
            this.actions = actions != null ? actions : new ArrayList<>();
        }

        public List<String> getAttributes() {
            return attributes;
        }

        public void setAttributes(List<String> attributes) {
            this.attributes = attributes != null ? attributes : new ArrayList<>();
        }
    }
}
