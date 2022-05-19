package com.ath.adminefectivo.service.impl;

import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.FechasConciliacionDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.OperacionesCertificadas;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.IOperacionesCertificadasRepository;
import com.ath.adminefectivo.service.IOperacionesCertificadasService;

@Service
public class OperacionesCertificadasServiceImpl implements IOperacionesCertificadasService {

	@Autowired
	IOperacionesCertificadasRepository operacionesCertificadasRepository;

	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public Boolean actualizarEstadoEnCertificadas(Integer idCertificacion, String estado) {

		Optional<OperacionesCertificadas> operaciones = operacionesCertificadasRepository.findById(idCertificacion);
		if (operaciones.isPresent()) {
			try {
				operaciones.get().setEstadoConciliacion(estado);
				operaciones.get().setIdCertificacion(idCertificacion);
				operaciones.get().setFechaModificacion(new Date());
				operaciones.get().setUsuarioModificacion("user1");
				operacionesCertificadasRepository.save(operaciones.get());
			} catch (Exception e) {
				e.getMessage();
			}
			
		} else {
			throw new NegocioException(ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getHttpStatus());
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer numeroOperacionesPorEstadoyFecha(FechasConciliacionDTO fechaConciliacion, String estado) {
		return operacionesCertificadasRepository.countByEstadoConciliacionAndFechaEjecucionBetween(estado,
				fechaConciliacion.getFechaConciliacionInicial(), fechaConciliacion.getFechaConciliacionFinal());
	}
}
