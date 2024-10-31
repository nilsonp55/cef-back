package com.ath.adminefectivo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.UpdateEstadoOperacionesDTO;
import com.ath.adminefectivo.repositories.IOperacionesCertificadasRepository;
import com.ath.adminefectivo.repositories.IOperacionesProgramadasRepository;
import com.ath.adminefectivo.service.IConciliacionOperacionesUpdateService;

import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@Setter
public class ConciliacionOperacionesUpdateService implements IConciliacionOperacionesUpdateService {

	@Autowired
	IOperacionesProgramadasRepository operacionesProgramadasRepository;

	@Autowired
	IOperacionesCertificadasRepository operacionesCertificadasRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UpdateEstadoOperacionesDTO> updateEstadoProgramadas(
			List<UpdateEstadoOperacionesDTO> updateEstadoProgramadasList) {
		log.debug("Service Update estado operaciones programadas");
		updateEstadoProgramadasList.forEach(opDTO -> {
			log.debug("Operacion programada para actualizar Id: {} estado: {}", opDTO.getIdOperacion(),
					opDTO.getEstado());
			operacionesProgramadasRepository.findById(opDTO.getIdOperacion()).ifPresentOrElse(opEntity -> {
				log.debug("Estado actual operacion programada entity id: {} estado: {}", opEntity.getIdOperacion(),
						opEntity.getEstadoConciliacion());
				if(opEntity.getEstadoConciliacion().equals(Dominios.ESTADO_CONCILIACION_CONCILIADO)) {
				  opDTO.setUpdateExitoso(false);
                  opDTO.setResultadoFallido(Constantes.OPERACION_EN_ESTADO_CONCILIADO);
                  log.info(Constantes.OPERACION_EN_ESTADO_CONCILIADO + " - Id: {}", opDTO.getIdOperacion());
				} else {
				  opEntity.setEstadoConciliacion(opDTO.getEstado());
				  operacionesProgramadasRepository.save(opEntity);
                  opDTO.setUpdateExitoso(true);
                  log.info("Operacion programada entity actualizada correctamente: {}", opEntity.getIdOperacion());
				}
			}, () -> {
				opDTO.setUpdateExitoso(false);
				opDTO.setResultadoFallido(Constantes.REGISTRO_NO_ENCONTRADO);
				log.debug("Operacion programada no encotrada: {}", opDTO.getIdOperacion());
			});

		});
		log.debug("Finaliza Service Update estado operaciones programadas");
		return updateEstadoProgramadasList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UpdateEstadoOperacionesDTO> updateEstadoCertificadas(
			List<UpdateEstadoOperacionesDTO> updateEstadoCertificadasList) {
		log.debug("Service Update estado operaciones certificadas");

		updateEstadoCertificadasList.forEach(opDTO -> {
			log.debug("Operacion certificada para actualizar Id: {} estado: {}", opDTO.getIdOperacion(),
					opDTO.getEstado());
			operacionesCertificadasRepository.findById(opDTO.getIdOperacion()).ifPresentOrElse(opEntity -> {
				log.debug("Estado actual operacion certificada entity id: {} estado: {}", opEntity.getIdCertificacion(),
						opEntity.getEstadoConciliacion());
				if(opEntity.getEstadoConciliacion().equals(Dominios.ESTADO_CONCILIACION_CONCILIADO)) {
                  opDTO.setUpdateExitoso(false);
                  opDTO.setResultadoFallido(Constantes.OPERACION_EN_ESTADO_CONCILIADO);
                  log.info(Constantes.OPERACION_EN_ESTADO_CONCILIADO + " - Id: {}", opDTO.getIdOperacion());
                } else {
    				opEntity.setEstadoConciliacion(opDTO.getEstado());
    				operacionesCertificadasRepository.save(opEntity);
    				opDTO.setUpdateExitoso(true);
    				log.info("Operacion certificada entity actualizada correctamente: {}", opEntity.getIdCertificacion());
                }
			}, () -> {
				opDTO.setUpdateExitoso(false);
				opDTO.setResultadoFallido(Constantes.REGISTRO_NO_ENCONTRADO);
				log.debug("Operacion programada no encotrada: {}", opDTO.getIdOperacion());
			});
		});

		return updateEstadoCertificadasList;
	}

}
