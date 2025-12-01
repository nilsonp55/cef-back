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
import java.util.UUID;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AuditPersistenceListener {

    private static final Logger logger = LoggerFactory.getLogger(AuditPersistenceListener.class);

    private final AuditoriaLogRepository auditoriaLogRepository;
    private final AuditLogProcessRepository auditLogProcessRepository;

	public AuditPersistenceListener(AuditoriaLogRepository auditoriaLogRepository,
			AuditLogProcessRepository auditLogProcessRepository, AuditorAware<String> auditorAware) {
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
            if (Boolean.TRUE.equals(data.getIsProcess())) {
                AuditLogProcessEntity entity = buildProcessEntity(data);
                auditLogProcessRepository.save(entity);
                logger.debug("Auditoría de proceso persistida correctamente para {}", data.getCodigoProceso());
            } else {
                AuditoriaLogEntity entity = buildAdminEntity(data);
                auditoriaLogRepository.save(entity);
                logger.debug("Auditoría admin persistida correctamente para {}", data.getCodigoProceso());
            }
        } catch (Exception e) {
            logger.error("Error guardando auditoría: {}", e.getMessage(), e);
        }
    }
    
    private AuditoriaLogEntity buildAdminEntity(AuditData data) throws JsonProcessingException {
        AuditoriaLogEntity entity = new AuditoriaLogEntity();
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        entity.setFechaHora(data.getFechaHora());
        entity.setIpOrigen(data.getIpOrigen());
        entity.setUsuario(data.getUsuario() != null ? data.getUsuario() : "System");
        entity.setOpcionMenu(data.getOpcionMenu());
        entity.setAccionHttp(resolveOperacion(data));
        entity.setCodigoProceso(UUID.fromString(data.getCodigoProceso()));
        entity.setNombreProceso(data.getNombreProceso());
        entity.setEstadoHttp(HttpStatusDescripcion.descripcionDe(data.getEstadoHttp()));
        entity.setEstadoOperacion(data.getEstadoOperacion());
        entity.setMensajeRespuesta(data.getMensajeRespuesta());

        entity.setPeticion(serializeObject(mapper, data.getPeticion()));
        entity.setRespuesta(serializeObject(mapper, data.getRespuesta()));

        applyChanges(mapper, data, entity::setValorAnterior, entity::setValorNuevo);

        return entity;
    }

    private AuditLogProcessEntity buildProcessEntity(AuditData data) throws JsonProcessingException {
        AuditLogProcessEntity entity = new AuditLogProcessEntity();
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        entity.setFechaHoraProc(data.getFechaHora());
        entity.setIpOrigenProc(data.getIpOrigen());
        entity.setUsuarioProc(data.getUsuario() != null ? data.getUsuario() : "System");
        entity.setOpcionMenuProc(data.getOpcionMenu());
        entity.setAccionHttpProc(resolveOperacion(data));
        entity.setCodigoProcesoProc(UUID.fromString(data.getCodigoProceso()));
        entity.setNombreProcesoProc(data.getNombreProceso());
        entity.setEstadoHttpProc(HttpStatusDescripcion.descripcionDe(data.getEstadoHttp()));
        entity.setEstadoOperacionProc(data.getEstadoOperacion());
        entity.setMensajeRespuestaProc(data.getMensajeRespuesta());

        entity.setPeticionProc(serializeObject(mapper, data.getPeticion()));
        entity.setRespuestaProc(serializeObject(mapper, data.getRespuesta()));

        applyChanges(mapper, data, entity::setValorAnteriorProc, entity::setValorNuevoProc);

        return entity;
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
    
    private String serializeObject(ObjectMapper mapper, Object obj) throws JsonProcessingException {
        if (obj == null) return null;
        if (obj instanceof String s) {
            return s;
        }
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }

	private <T> void applyChanges(ObjectMapper mapper, AuditData data, Consumer<T> setValorAnterior,
			Consumer<T> setValorNuevo) {
		if (data.getCambios() == null || data.getCambios().isEmpty()) {
			setValorAnterior.accept(null);
			setValorNuevo.accept(null);
			return;
		}
		try {
			EntityChange first = data.getCambios().get(0);
			EntityChange last = data.getCambios().get(data.getCambios().size() - 1);

			if (first.getValorAnterior() != null) {
				setValorAnterior.accept((T) prettyPrint(mapper, first.getValorAnterior()));
			}
			if (last.getValorNuevo() != null) {
				setValorNuevo.accept((T) prettyPrint(mapper, last.getValorNuevo()));
			}
		} catch (Exception e) {
			logger.warn("Error procesando cambios: {}", e.getMessage());
			setValorAnterior.accept(null);
			setValorNuevo.accept(null);
		}
	}

    private String prettyPrint(ObjectMapper mapper, Object value) throws JsonProcessingException {
        if (value instanceof String s) {
            return mapper.writerWithDefaultPrettyPrinter()
                         .writeValueAsString(mapper.readValue(s, Object.class));
        }
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
    }

}