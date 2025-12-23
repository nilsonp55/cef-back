package com.ath.adminefectivo.auditoria.utils;

import java.util.ArrayList;

import com.ath.adminefectivo.auditoria.context.AuditData;
import com.ath.adminefectivo.auditoria.context.AuditoriaContext;
import com.ath.adminefectivo.constantes.Constantes;

public class AuditoriaHelper {

	private static final String WARNING = "WARNING";
    private static final String ACTION = "ACTION";
    private static final String ERROR = "ERROR";
    
    private AuditoriaHelper() {
    	throw new IllegalStateException("Utility class");
	}

    /**
     * Registra un error interno en la auditoría sin lanzar excepción.
     */
    public static void registrarErrorInterno(String mensaje) {
        registrarEventoInterno(ERROR, mensaje);
    }

    /**
     * Registra una advertencia interna (warning) en la auditoría.
     */
    public static void registrarWarning(String mensaje) {
        registrarEventoInterno(WARNING, mensaje);
    }

    /**
     * Registra una acción interna que se considera relevante para auditoría.
     */
    public static void registrarAccion(String mensaje) {
        registrarEventoInterno(ACTION, mensaje);
    }

    /**
     * Maneja el almacenamiento del evento en el contexto de auditoría.
     */
    private static void registrarEventoInterno(String tipo, String mensaje) {
        AuditData audit = AuditoriaContext.get();
        if (audit == null) return;

        // Inicializa estructura
        if (audit.getEventosInternos() == null) {
            audit.setEventosInternos(new ArrayList<>());
        }

        audit.getEventosInternos().add(new EventoInterno(tipo, mensaje));

        // Si es error, marcar el estado de la operación
        if (tipo.equals(ERROR)) {
            audit.setEstadoOperacion(Constantes.ERROR_GENERAL);
            audit.setMensajeRespuesta(mensaje);
        }
    }

    /**
     * Modelo para eventos internos.
     */
    public static class EventoInterno {
        private String tipo;
        private String mensaje;

        public EventoInterno(String tipo, String mensaje) {
            this.tipo = tipo;
            this.mensaje = mensaje;
        }

        public String getTipo() { return tipo; }
        public String getMensaje() { return mensaje; }
    }
}