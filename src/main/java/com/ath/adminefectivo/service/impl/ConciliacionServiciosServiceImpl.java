package com.ath.adminefectivo.service.impl;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.dto.ConciliacionDTO;
import com.ath.adminefectivo.dto.FechasConciliacionDTO;
import com.ath.adminefectivo.dto.ParametrosConciliacionDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.IConciliacionOperacionesRepository;
import com.ath.adminefectivo.service.IConciliacionServiciosService;
import com.ath.adminefectivo.service.IParametroService;

@Service
public class ConciliacionServiciosServiceImpl implements IConciliacionServiciosService {

	@Autowired
	IConciliacionOperacionesRepository conciliacionServiciosRepository;

	@Autowired
	IParametroService parametroService;
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public Boolean eliminarRegistroConciliacion(Integer idConciliacion) {

		var servicios = conciliacionServiciosRepository.findById(idConciliacion);
		if (servicios.isEmpty()) {
			throw new NegocioException(ApiResponseCode.ERROR_CONCILIADOS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_CONCILIADOS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_CONCILIADOS_NO_ENCONTRADO.getHttpStatus());
		}
		try {
			conciliacionServiciosRepository.delete(servicios.get());
		} catch (Exception e) {
			e.getMessage();
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean crearRegistroConciliacion(ParametrosConciliacionDTO elemento) {
		try {
			Date fechaSistema = parametroService.valorParametroDate(Constantes.FECHA_DIA_PROCESO);
			var conciliacion = new ConciliacionDTO();
			conciliacion.setFechaConciliacion(fechaSistema);
			conciliacion.setFechaModificacion(new Date());
			conciliacion.setIdCertificacion(elemento.getIdCertificacion());
			conciliacion.setIdOperacion(elemento.getIdOperacion());
			conciliacion.setTipoConciliacion(elemento.getTipoConciliacion());
			conciliacion.setUsuarioModificacion("user1");
			conciliacion.setUsuarioCreacion("user1");
			conciliacionServiciosRepository.save(ConciliacionDTO.CONVERTER_ENTITY.apply(conciliacion));
		} catch (Exception e) {
			e.getMessage();
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer numeroOperacionesPorRangoFechas(FechasConciliacionDTO fechaConciliacion) {
		return conciliacionServiciosRepository.countByFechaConciliacionBetween(
				fechaConciliacion.getFechaConciliacionInicial(), fechaConciliacion.getFechaConciliacionFinal());
	}
}
