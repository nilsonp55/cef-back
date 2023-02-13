package com.ath.adminefectivo.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.dto.AuditoriaProcesosDTO;
import com.ath.adminefectivo.entities.AuditoriaProcesos;
import com.ath.adminefectivo.entities.id.AuditoriaProcesosPK;
import com.ath.adminefectivo.repositories.IAuditoriaProcesosRepository;
import com.ath.adminefectivo.service.IAuditoriaProcesosService;
import com.ath.adminefectivo.service.IDominioService;


@Service
public class AuditoriaProcesosServiceImpl implements IAuditoriaProcesosService {

	@Autowired
	IAuditoriaProcesosRepository auditoriaProcesosRepository;
	
	@Autowired
	IDominioService dominioService;
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AuditoriaProcesosDTO consultarAuditoriaPorProceso(String codigoProceso, Date fechaSistema) {
		
		AuditoriaProcesos auditoriaProceso = auditoriaProcesosRepository.findById(new AuditoriaProcesosPK(codigoProceso, fechaSistema)).orElse(null);
		
		if(Objects.isNull(auditoriaProceso)) {
			//lanza excepcion de que no existe
		}
		
		if(auditoriaProceso.getEstadoProceso().equals(Constantes.ESTADO_PROCESO_PROCESO)) {
			//lanza excepcion de que ya se esta ejecutando el proceso 
		}	
		return AuditoriaProcesosDTO.CONVERTER_DTO.apply(auditoriaProceso);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void ActualizarAuditoriaProceso(String codigoProceso, Date fechaSistema, String estado, String mensaje) {
		
		AuditoriaProcesos auditoriaProceso = auditoriaProcesosRepository.findById(new AuditoriaProcesosPK(codigoProceso, fechaSistema)).orElse(null);
		
		if(Objects.isNull(auditoriaProceso)) {
			//lanza excepcion de que no existe
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
					.fechaCreacion(new Date()).fechaModificacion(new Date())
					.id(new AuditoriaProcesosPK(codigoProceso, fechaSistema))
					.usuarioCreacion("ATH")
					.build();
		}else {
			auditoriaProceso.setEstadoProceso(Constantes.ESTADO_PROCESO_PENDIENTE);
			
		}	
		auditoriaProcesosRepository.save(auditoriaProceso);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void crearTodosAuditoriaProcesos(Date fechaSistema) {
		List<String> dominiosAuditoria = dominioService.consultaListValoresPorDominio(Constantes.DOMINIO_AUDITORIA_PROCESOS);
		dominiosAuditoria.forEach(dominioAudi ->{
			this.crearAuditoriaProceso(dominioAudi, fechaSistema);
		});
	}
	
	
	
	
	
}
