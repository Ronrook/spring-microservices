package com.microservice.user.infrastructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AuditLogger {

    private static final Logger log = LoggerFactory.getLogger("AUDIT");

    public void logSuccess(String action, String user) {
        log.info("[AUDIT] action={} | user={} | status=SUCCESS", action, user);
    }

    public void logSuccess(String action, String user, String detail) {
        log.info("[AUDIT] action={} | user={} | status=SUCCESS | detail={}", action, user, detail);
    }

    public void logFailure(String action, String user, String reason) {
        log.warn("[AUDIT] action={} | user={} | status=FAILED | reason={}", action, user, reason);
    }

    public void logAccess(String action, String user, String resource) {
        log.info("[AUDIT] action={} | user={} | resource={} | status=SUCCESS", action, user, resource);
    }
}
