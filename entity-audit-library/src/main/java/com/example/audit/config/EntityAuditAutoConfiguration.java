package com.example.audit.config;

import com.example.audit.repository.AuditEventRepository;
import com.example.audit.service.AuditService;
import com.example.audit.service.impl.AsyncAuditPersistenceDelegate;
import com.example.audit.service.impl.AsyncAuditServiceImpl;
import jakarta.validation.Validator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

import static jakarta.validation.Validation.buildDefaultValidatorFactory;

@Configuration
@ConditionalOnClass(AuditEventRepository.class)
@EnableConfigurationProperties(AuditConfigurationProperties.class)
@EnableAsync
public class EntityAuditAutoConfiguration {

    @Bean(name = "auditTaskExecutor")
    @ConditionalOnMissingBean(name = "auditTaskExecutor")
    public Executor auditTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("audit-");
        executor.initialize();
        return executor;
    }

    @Bean
    @ConditionalOnMissingBean(Validator.class)
    public Validator auditValidator() {
        return buildDefaultValidatorFactory().getValidator();
    }

    @Bean
    @ConditionalOnMissingBean(AsyncAuditPersistenceDelegate.class)
    public AsyncAuditPersistenceDelegate asyncAuditPersistenceDelegate(
            AuditEventRepository auditEventRepository,
            AuditConfigurationProperties config) {
        return new AsyncAuditPersistenceDelegate(auditEventRepository, config);
    }

    @Bean
    @ConditionalOnMissingBean(AuditService.class)
    public AuditService auditService(Validator validator,
                                      AsyncAuditPersistenceDelegate persistenceDelegate) {
        return new AsyncAuditServiceImpl(validator, persistenceDelegate);
    }
}
