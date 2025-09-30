package com.ath.adminefectivo.auditoria.context;

public class AuditoriaContext {

    private static final ThreadLocal<AuditData> CONTEXT = new ThreadLocal<>();

    private AuditoriaContext() {}

    public static void set(AuditData data) {
        CONTEXT.set(data);
    }

    public static AuditData get() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
