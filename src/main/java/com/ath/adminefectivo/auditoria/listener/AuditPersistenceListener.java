package com.ath.adminefectivo.auditoria.listener;

import com.ath.adminefectivo.auditoria.context.AuditData;
import com.ath.adminefectivo.auditoria.utils.AuditReadyEvent;
import com.ath.adminefectivo.auditoria.utils.HttpStatusDescripcion;
import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.entities.audit.AuditLogProcessEntity;
import com.ath.adminefectivo.entities.audit.AuditoriaLogEntity;
import com.ath.adminefectivo.entities.audit.EntityChange;
import com.ath.adminefectivo.repositories.AuditLogProcessRepository;
import com.ath.adminefectivo.repositories.AuditoriaLogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.UUID;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AuditPersistenceListener {

    private static final Logger logger = LoggerFactory.getLogger(AuditPersistenceListener.class);

    private final AuditoriaLogRepository auditoriaLogRepository;
    private final AuditLogProcessRepository auditLogProcessRepository;

	public AuditPersistenceListener(AuditoriaLogRepository auditoriaLogRepository,
			AuditLogProcessRepository auditLogProcessRepository) {
		this.auditoriaLogRepository = auditoriaLogRepository;
		this.auditLogProcessRepository = auditLogProcessRepository;
}

    @EventListener
    public void handleAuditReadyEvent(AuditReadyEvent event) {
        AuditData data = event.getAuditData();

        if (data == null) {
            logger.warn("handleAuditReadyEvent: AuditData es null, no se persiste");
            return;
        }

        // Validación crítica: aseguramos que el estado HTTP siempre esté presente
        if (data.getEstadoHttp() == null) {
            logger.warn("handleAuditReadyEvent: AuditData recibido sin estadoHttp, no se persiste");
            return;
        }

        persistAudit(data);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void persistAudit(AuditData data) {
        try {

            boolean isProcess = Boolean.TRUE.equals(data.getIsProcess());

            // ==============================
            // CASO 1: isProcess = true
            // Guardar SOLO UN registro
            // ==============================
            if (isProcess) {
                persistSingleAudit(data, null); // registro global del proceso
                return;
            }

            // ==============================
            // CASO 2: isProcess = false
            // Guardar un registro por cada modificación
            // ==============================

            // No hay cambios → registro normal
            if (data.getCambios() == null || data.getCambios().isEmpty()) {
                persistSingleAudit(data, null);
                return;
            }

            // Guardar en orden natural del negocio
            Collections.reverse(data.getCambios());

            // Guardar un registro por cada EntityChange
            for (EntityChange change : data.getCambios()) {
                persistSingleAudit(data, change);
            }

        } catch (Exception e) {
            logger.error("Error guardando auditoría: {}", e.getMessage(), e);
        }
    }
    
    private void persistSingleAudit(AuditData data, EntityChange change) throws JsonProcessingException {

        boolean isProcess = Boolean.TRUE.equals(data.getIsProcess());
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        if (!isProcess) {
            // ===============================
            // AUDITORÍA ADMINISTRATIVOS
            // ===============================
            AuditoriaLogEntity entity = new AuditoriaLogEntity();
            
            entity.setFechaHora(data.getFechaHora());
            entity.setIpOrigen(data.getIpOrigen());
            entity.setUsuario(data.getUsuario() != null ? data.getUsuario() : "System");
            entity.setOpcionMenu(data.getOpcionMenu());
            //entity.setAccionHttp(resolveOperacion(data));
            entity.setAccionHttp(change != null ? change.getOperacion() : resolveOperacion(data));
            entity.setCodigoProceso(UUID.fromString(data.getCodigoProceso()));
            entity.setNombreProceso(data.getNombreProceso());
            entity.setEstadoHttp(HttpStatusDescripcion.descripcionDe(data.getEstadoHttp()));
            entity.setEstadoOperacion(data.getEstadoOperacion());
            entity.setMensajeRespuesta(data.getMensajeRespuesta());

            entity.setPeticion(data.getPeticion());
            entity.setRespuesta(data.getRespuesta());

            if (change != null) {
                entity.setValorAnterior(pretty(mapper, change.getValorAnterior()));
                entity.setValorNuevo(pretty(mapper, change.getValorNuevo()));
                entity.setTabla(change.getEntidad());
                entity.setIdTabla(change.getIdEntidad());
            }

            // ===============================
            // Registrar eventos internos
            // ===============================
            if (data.getEventosInternos() != null && !data.getEventosInternos().isEmpty()) {
                String eventosJson = mapper.writerWithDefaultPrettyPrinter()
                                          .writeValueAsString(data.getEventosInternos());
                entity.setEventosInternos(eventosJson);
            }

            auditoriaLogRepository.save(entity);

        } else {
            // ===============================
            // AUDITORÍA PROCESOS
            // ===============================
            AuditLogProcessEntity entity = new AuditLogProcessEntity();

            entity.setFechaHoraProc(data.getFechaHora());
            entity.setIpOrigenProc(data.getIpOrigen());
            entity.setUsuarioProc(data.getUsuario() != null ? data.getUsuario() : "System");
            entity.setOpcionMenuProc(data.getOpcionMenu());
            //entity.setAccionHttpProc(resolveOperacion(data));
            entity.setAccionHttpProc(change != null ? change.getOperacion() : resolveOperacion(data));
            entity.setCodigoProcesoProc(UUID.fromString(data.getCodigoProceso()));
            entity.setNombreProcesoProc(data.getNombreProceso());
            entity.setEstadoHttpProc(HttpStatusDescripcion.descripcionDe(data.getEstadoHttp()));
            entity.setEstadoOperacionProc(data.getEstadoOperacion());
            entity.setMensajeRespuestaProc(data.getMensajeRespuesta());

            entity.setPeticionProc(data.getPeticion());
            entity.setRespuestaProc(data.getRespuesta());

            if (change != null) {
                entity.setValorAnteriorProc(pretty(mapper, change.getValorAnterior()));
                entity.setValorNuevoProc(pretty(mapper, change.getValorNuevo()));
                entity.setTablaProc(change.getEntidad());
                entity.setIdTablaProc(change.getIdEntidad());
            }

            // ===============================
            // Registrar eventos internos
            // ===============================
            if (data.getEventosInternos() != null && !data.getEventosInternos().isEmpty()) {
                String eventosJson = mapper.writerWithDefaultPrettyPrinter()
                                          .writeValueAsString(data.getEventosInternos());
                entity.setEventosInternosProc(eventosJson);
            }

            auditLogProcessRepository.save(entity);
        }
    }

    private String resolveOperacion(AuditData data) {
        if (data == null) {
            return Constantes.DESCONOCIDO;
        }

        // Validar si viene una operación explícita en los cambios
        if (data.getCambios() != null 
                && !data.getCambios().isEmpty() 
                && data.getCambios().get(0).getOperacion() != null 
                && !data.getCambios().get(0).getOperacion().isBlank()) {
            return data.getCambios().get(0).getOperacion();
        }

        // Fallback: traducir método HTTP
        String metodo = data.getMetodo();
        if (metodo == null) {
            return Constantes.DESCONOCIDO;
        }

        return switch (metodo.toUpperCase()) {
            case Constantes.POST   -> Constantes.CREAR;
            case Constantes.PUT    -> Constantes.ACTUALIZAR;
            case Constantes.DELETE -> Constantes.ELIMINAR;
            default       -> metodo; // otros (GET, OPTIONS, etc.)
        };
    }
    
    private String pretty(ObjectMapper mapper, Object jsonString) {
        if (jsonString == null) return null;
        try {
            if (jsonString instanceof String s) {
                return mapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(mapper.readValue(s, Object.class));
            }
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonString);
        } catch (Exception e) {
            return jsonString.toString();
        }
    }


}