package com.ath.adminefectivo.auditoria.listener;

import com.ath.adminefectivo.auditoria.context.AuditData;
import com.ath.adminefectivo.auditoria.utils.AuditReadyEvent;
import com.ath.adminefectivo.entities.audit.AuditoriaLogEntity;
import com.ath.adminefectivo.repositories.AuditoriaLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AuditPersistenceListener {

	private static final Logger logger = LoggerFactory.getLogger(AuditPersistenceListener.class);

	@Autowired
	AuditoriaLogRepository auditoriaLogRepository;
	
	@Autowired
	private AuditorAware<String> auditorAware;

	private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

	@Autowired
	public AuditPersistenceListener(AuditoriaLogRepository repository) {
		this.auditoriaLogRepository = repository;
	}

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void onAuditReady(AuditReadyEvent event) {
		
		// Guardar en nueva transacción
		persistAudit(event.getAuditData());
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void persistAudit(AuditData data) {
		try {
			AuditoriaLogEntity entity = new AuditoriaLogEntity();
			entity.setFechaHora(data.getFechaHora());
			entity.setIpOrigen(data.getIpOrigen());

			String usuario = data.getUsuario() != null ? data.getUsuario()
					: auditorAware.getCurrentAuditor().orElse("System");
			entity.setUsuario(usuario);

			entity.setOpcionMenu(data.getUri());
			entity.setAccionHttp(data.getMetodo());
			entity.setCodigoProceso(data.getCodigoProceso());
			entity.setNombreProceso(null);
			entity.setEstadoHttp(data.getEstadoHttp().toString());
			entity.setMensajeRespuesta(data.getMensajeRespuesta());
			entity.setPeticion(data.getPeticion());
			entity.setRespuesta(data.getRespuesta());
			entity.setValorAnterior(
					data.getCambios() != null ? mapper.writeValueAsString(data.getCambios().get(0).getValorAnterior())
							: null);
			entity.setValorNuevo(
					data.getCambios() != null ? mapper.writeValueAsString(data.getCambios().get(0).getValorNuevo())
							: null);

			// entity.setUsuario(data.getUsuario());

			auditoriaLogRepository.save(entity);
		} catch (Exception e) {
			logger.error("Error guardando auditoría: {}", e.getMessage(), e);
		}
	}
}
