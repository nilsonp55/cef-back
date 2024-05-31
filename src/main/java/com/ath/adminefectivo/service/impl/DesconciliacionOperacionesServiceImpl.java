package com.ath.adminefectivo.service.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.ConciliacionServicios;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.IConciliacionOperacionesRepository;
import com.ath.adminefectivo.service.IConciliacionServiciosHistoricoService;
import com.ath.adminefectivo.service.IConciliacionServiciosService;
import com.ath.adminefectivo.service.IDesconciliacionOperacionesService;
import com.ath.adminefectivo.service.IDominioService;
import com.ath.adminefectivo.service.IOperacionesCertificadasService;
import com.ath.adminefectivo.service.IOperacionesProgramadasService;

@Service
public class DesconciliacionOperacionesServiceImpl implements IDesconciliacionOperacionesService {

	@Autowired
	IConciliacionOperacionesRepository conciliacionServiciosRepository;

	@Autowired
	IDominioService dominioService;

	@Autowired
	IConciliacionServiciosService conciliacionServiciosService;

	@Autowired
	IOperacionesCertificadasService operacionesCertificadasService;

	@Autowired
	IOperacionesProgramadasService operacionesProgramadasService;

	@Autowired
	IConciliacionServiciosHistoricoService conciliacionServiciosHistoricoService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean procesoDesconciliacion(List<Integer> operacionesADesconciliar) {

		for (Integer operaciones : operacionesADesconciliar) {
			var regConciliado = conciliacionServiciosRepository.findById(operaciones);
			if (regConciliado.isPresent()) {
				iniciarProcesoDesconciliacion(regConciliado);
			} else {
				throw new NegocioException(ApiResponseCode.ERROR_CONCILIADOS_NO_ENCONTRADO.getCode(),
						ApiResponseCode.ERROR_CONCILIADOS_NO_ENCONTRADO.getDescription(),
						ApiResponseCode.ERROR_CONCILIADOS_NO_ENCONTRADO.getHttpStatus());
			}
		}
		return true;
	}

	/**
	 * Metodo que se encarga de hacer el proceso de desconciliacion actualizando las
	 * tablas: ConciliacionServiciosHistorico, OperacionesProgramadas,
	 * OperacionesCertificadas, ConciliacionServicios
	 * 
	 * @param regConciliado
	 * @return Boolean
	 * @author cesar.castano
	 */
	@Transactional
	private Boolean iniciarProcesoDesconciliacion(Optional<ConciliacionServicios> regConciliado) {

		conciliacionServiciosHistoricoService.crearRegistroEnConciliacionHistorico(regConciliado);
		if (regConciliado.isPresent()) {
			operacionesProgramadasService.actualizarEstadoEnProgramadas(regConciliado.get().getIdOperacion(),
					dominioService.valorTextoDominio(
							Constantes.DOMINIO_ESTADO_CONCILIACION, 
							Dominios.ESTADO_CONCILIACION_NO_CONCILIADO));
			operacionesCertificadasService.actualizarEstadoEnCertificadas(regConciliado.get().getIdCertificacion(),
					dominioService.valorTextoDominio(
							Constantes.DOMINIO_ESTADO_CONCILIACION, 
							Dominios.ESTADO_CONCILIACION_NO_CONCILIADO));
			conciliacionServiciosService.eliminarRegistroConciliacion(regConciliado.get().getIdConciliacion());
		}
		return true;
	}
}
