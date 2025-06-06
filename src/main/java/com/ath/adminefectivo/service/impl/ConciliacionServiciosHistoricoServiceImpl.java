package com.ath.adminefectivo.service.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.ConciliacionServiciosHistoricoDTO;
import com.ath.adminefectivo.entities.ConciliacionServicios;
import com.ath.adminefectivo.repositories.IConciliacionServiciosHistoricoRepository;
import com.ath.adminefectivo.service.IConciliacionServiciosHistoricoService;
import com.ath.adminefectivo.service.IDominioService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ConciliacionServiciosHistoricoServiceImpl implements IConciliacionServiciosHistoricoService{

	@Autowired
	IConciliacionServiciosHistoricoRepository conciliacionServiciosHistoricoRepository;
	
	@Autowired
	IDominioService dominioService;
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public Boolean crearRegistroEnConciliacionHistorico(Optional<ConciliacionServicios> regConciliado) {
		try {
			var conciliacion = new ConciliacionServiciosHistoricoDTO();
			conciliacion.setEstado(dominioService.valorTextoDominio(
					Constantes.DOMINIO_ESTADO_CONCILIACION, Dominios.ESTADO_CONCILIACION_NO_CONCILIADO));
			conciliacion.setFechaConciliacion(regConciliado.get().getFechaConciliacion());
			conciliacion.setFechaCreacion(new java.util.Date());
			conciliacion.setFechaModificacion(new java.util.Date());
			conciliacion.setIdCertificacion(regConciliado.get().getIdCertificacion());
			conciliacion.setIdConciliacion(regConciliado.get().getIdConciliacion());
			conciliacion.setIdOperacion(regConciliado.get().getIdOperacion());
			conciliacion.setTipoConciliacion(dominioService.valorTextoDominio(
										Constantes.DOMINIO_TIPOS_CONCILIACION, 
										Dominios.TIPO_CONCILIACION_MANUAL));
			conciliacion.setUsuarioCreacion("user1");
			conciliacion.setUsuarioModificacion("user1");
			conciliacionServiciosHistoricoRepository
					.save(ConciliacionServiciosHistoricoDTO.CONVERTER_ENTITY.apply(conciliacion));
		} catch (Exception e) {
			log.error("Failed to create registro en conciliacion historico: {}", e.getMessage());
		}
		return true;
	}

}
