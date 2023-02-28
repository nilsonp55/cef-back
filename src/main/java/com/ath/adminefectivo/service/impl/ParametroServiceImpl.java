package com.ath.adminefectivo.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.ParametroDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.repositories.ParametroRepository;
import com.ath.adminefectivo.service.IAuditoriaProcesosService;
import com.ath.adminefectivo.service.IParametroService;
import com.querydsl.core.types.Predicate;

@Service
public class ParametroServiceImpl implements IParametroService {

	@Autowired
	ParametroRepository parametroRepository;
	
	@Autowired
	IAuditoriaProcesosService auditoriaProcesosService;
	
	@Autowired
	IParametroService parametroService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ParametroDTO> getParametros(Predicate predicate) {
		var parametros = parametroRepository.findAll(predicate);
		List<ParametroDTO> listParametrosDto = new ArrayList<>();
		parametros.forEach(entity -> listParametrosDto.add(ParametroDTO.CONVERTER_DTO.apply(entity)));
		return listParametrosDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String valorParametro(String codigo) {
		var parametroOpt = parametroRepository.findById(codigo);

		if (parametroOpt.isPresent() && Objects.nonNull(parametroOpt.get().getValor())) {
			return parametroOpt.get().getValor();
		} else {
			throw new AplicationException(ApiResponseCode.ERROR_PARAMETRO_NOT_FOUND.getCode(),
					ApiResponseCode.ERROR_PARAMETRO_NOT_FOUND.getDescription(),
					ApiResponseCode.ERROR_PARAMETRO_NOT_FOUND.getHttpStatus());
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int valorParametroEntero(String codigo) {
		var parametroOpt = parametroRepository.findById(codigo);

		if (parametroOpt.isPresent() && Objects.nonNull(parametroOpt.get().getValor())) {
			try {
				return Integer.valueOf(parametroOpt.get().getValor());
			} catch (NumberFormatException e) {
				auditoriaProcesosService.ActualizarAuditoriaProceso(Dominios.CODIGO_PROCESO_LOG_CERTIFICACION, 
						parametroService.valorParametroDate(Constantes.FECHA_DIA_PROCESO), 
						Constantes.ESTADO_PROCESO_PROCESADO, 
						ApiResponseCode.ERROR_PARAMETRO_NO_ENTERO.getDescription());
				
				throw new AplicationException(ApiResponseCode.ERROR_PARAMETRO_NO_ENTERO.getCode(),
						ApiResponseCode.ERROR_PARAMETRO_NO_ENTERO.getDescription(),
						ApiResponseCode.ERROR_PARAMETRO_NO_ENTERO.getHttpStatus());
			}
		} else {
			auditoriaProcesosService.ActualizarAuditoriaProceso(Dominios.CODIGO_PROCESO_LOG_CERTIFICACION, 
					parametroService.valorParametroDate(Constantes.FECHA_DIA_PROCESO), 
					Constantes.ESTADO_PROCESO_PROCESADO, 
					ApiResponseCode.ERROR_PARAMETRO_NOT_FOUND.getDescription());
			
			throw new AplicationException(ApiResponseCode.ERROR_PARAMETRO_NOT_FOUND.getCode(),
					ApiResponseCode.ERROR_PARAMETRO_NOT_FOUND.getDescription(),
					ApiResponseCode.ERROR_PARAMETRO_NOT_FOUND.getHttpStatus());
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date valorParametroDate(String codigo) {
		var parametroOpt = parametroRepository.findById(codigo);

		if (parametroOpt.isPresent() && Objects.nonNull(parametroOpt.get().getValor())) {
			SimpleDateFormat formato = new SimpleDateFormat(Constantes.FECHA_PATTERN_DD_MM_YYYY_WITH_SLASH);
			try {
				return formato.parse(parametroOpt.get().getValor());
			} catch (ParseException e) {
				throw new AplicationException(ApiResponseCode.ERROR_PARAMETRO_NO_FECHA.getCode(),
						ApiResponseCode.ERROR_PARAMETRO_NO_FECHA.getDescription(),
						ApiResponseCode.ERROR_PARAMETRO_NO_FECHA.getHttpStatus());
			}
		} else {
			throw new AplicationException(ApiResponseCode.ERROR_PARAMETRO_NOT_FOUND.getCode(),
					ApiResponseCode.ERROR_PARAMETRO_NOT_FOUND.getDescription(),
					ApiResponseCode.ERROR_PARAMETRO_NOT_FOUND.getHttpStatus());
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean actualizarValorParametro( String codigo, String valor) {
		var parametroOpt = parametroRepository.findById(codigo);

		if (parametroOpt.isPresent()) {
			var parametroEntity = parametroOpt.get();
			parametroEntity.setValor(valor);
			parametroRepository.save(parametroEntity);
			return true;
		} else {
			throw new AplicationException(ApiResponseCode.ERROR_PARAMETRO_NOT_FOUND.getCode(),
					ApiResponseCode.ERROR_PARAMETRO_NOT_FOUND.getDescription(),
					ApiResponseCode.ERROR_PARAMETRO_NOT_FOUND.getHttpStatus());
		}
	}

}
