package com.ath.adminefectivo.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.dto.AuditoriaProcesosDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.AuditoriaProcesos;
import com.ath.adminefectivo.entities.id.AuditoriaProcesosPK;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.IAuditoriaProcesosRepository;
import com.ath.adminefectivo.service.IAuditoriaProcesosService;
import com.ath.adminefectivo.service.IDominioService;
import com.ath.adminefectivo.service.IParametroService;

import lombok.extern.log4j.Log4j2;


@Service
@Log4j2
public class AuditoriaProcesosServiceImpl implements IAuditoriaProcesosService {

	@Autowired
	IAuditoriaProcesosRepository auditoriaProcesosRepository;
	
	@Autowired
	IDominioService dominioService;
	
	@Autowired
	IParametroService parametroService;
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AuditoriaProcesosDTO consultarAuditoriaPorProceso(String codigoProceso) {
		
		Date fechaSistema = parametroService.valorParametroDate(Constantes.FECHA_DIA_PROCESO);

		AuditoriaProcesos auditoriaProceso = auditoriaProcesosRepository
				.findById(new AuditoriaProcesosPK(codigoProceso, fechaSistema)).orElse(null);

		if (Objects.isNull(auditoriaProceso)) {
			log.error("No se encontro proceso: {} - fechaSistema: {}", codigoProceso, fechaSistema.toString());
			throw new NegocioException(ApiResponseCode.ERROR_PROCESO_NO_EXISTE.getCode(),
					ApiResponseCode.ERROR_PROCESO_NO_EXISTE.getDescription(),
					ApiResponseCode.ERROR_PROCESO_NO_EXISTE.getHttpStatus());
		}

		return AuditoriaProcesosDTO.CONVERTER_DTO.apply(auditoriaProceso);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void actualizarAuditoriaProceso(String codigoProceso, Date fechaSistema, String estado, String mensaje) {
		
		AuditoriaProcesos auditoriaProceso = auditoriaProcesosRepository.findById(new AuditoriaProcesosPK(codigoProceso, fechaSistema)).orElse(null);
		
		if(Objects.isNull(auditoriaProceso)) {
			throw new NegocioException(ApiResponseCode.ERROR_PROCESO_NO_EXISTE.getCode(),
					ApiResponseCode.ERROR_PROCESO_NO_EXISTE.getDescription(),
					ApiResponseCode.ERROR_PROCESO_NO_EXISTE.getHttpStatus());
		}
		
		if(auditoriaProceso.getEstadoProceso().equals(Constantes.ESTADO_PROCESO_INICIO )&& estado.equals(Constantes.ESTADO_PROCESO_INICIO)) {
			throw new NegocioException(ApiResponseCode.ERROR_PROCESO_YA_CERRADO.getCode(),
					ApiResponseCode.ERROR_PROCESO_YA_CERRADO.getDescription(),
					ApiResponseCode.ERROR_PROCESO_YA_CERRADO.getHttpStatus());
		}
		
		auditoriaProceso.setEstadoProceso(estado);
		auditoriaProceso.setMensaje(mensaje);
		
		auditoriaProcesosRepository.save(auditoriaProceso);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void crearAuditoriaProceso(String codigoProceso, Date fechaSistema) {
		
		AuditoriaProcesos auditoriaProceso = auditoriaProcesosRepository.findById(new AuditoriaProcesosPK(codigoProceso, fechaSistema)).orElse(null);
		
		if(Objects.isNull(auditoriaProceso)) {
			auditoriaProceso = AuditoriaProcesos.builder().estadoProceso(Constantes.ESTADO_PROCESO_PENDIENTE)
					.fechaCreacion(fechaSistema).fechaModificacion(new Date())
					.id(new AuditoriaProcesosPK(codigoProceso, fechaSistema))
					.usuarioCreacion("ATH")
					.build();
		}else {
			auditoriaProceso.setEstadoProceso(Constantes.ESTADO_PROCESO_PENDIENTE);
			auditoriaProceso.setFechaCreacion(fechaSistema);
			auditoriaProceso.setFechaModificacion(new Date());
			
		}	
		auditoriaProcesosRepository.save(auditoriaProceso);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void crearTodosAuditoriaProcesos(Date fechaSistema) {
		List<String> dominiosAuditoria = dominioService.consultaListValoresPorDominio(Constantes.DOMINIO_AUDITORIA_PROCESOS);
		dominiosAuditoria.forEach(dominioAudi ->
			this.crearAuditoriaProceso(dominioAudi, fechaSistema)
		);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Date> consultarFechasProcesadas() {
		return auditoriaProcesosRepository.auditoriaProcesosFechasProcesadas();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public AuditoriaProcesosDTO crearAuditoriaProceso(AuditoriaProcesosDTO auditoriaProcesosDTO) {
		log.debug("crear auditoriaProcesos: {} - ",auditoriaProcesosDTO.toString());
		return AuditoriaProcesosDTO.CONVERTER_DTO.apply(
				auditoriaProcesosRepository.save(AuditoriaProcesosDTO.CONVERTER_ENTITY.apply(auditoriaProcesosDTO)));
	}
	
    /**
     * {@inheritDoc}
     */
    @Override
    public List<AuditoriaProcesosDTO> consultarPorFechaProceso(Date fechaProceso) {
      log.debug("Auditoria procesos por fecha de proceso: {}", fechaProceso);

      return auditoriaProcesosRepository.findByFechaProceso(fechaProceso).stream()
          .map(entity -> AuditoriaProcesosDTO.CONVERTER_DTO.apply(entity)).toList();

    }
}
