package com.ath.adminefectivo.auditoria.utils;

import org.springframework.context.ApplicationEvent;
import com.ath.adminefectivo.auditoria.context.AuditData;

public class AuditReadyEvent extends ApplicationEvent {
    private final AuditData auditData;

    public AuditReadyEvent(Object source, AuditData auditData) {
        super(source);
        this.auditData = auditData;
    }

    public AuditData getAuditData() {
        return auditData;
    }
}
