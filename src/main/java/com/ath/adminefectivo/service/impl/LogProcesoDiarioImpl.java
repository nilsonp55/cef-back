package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.constantes.Parametros;
import com.ath.adminefectivo.dto.LogProcesoDiarioDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.LogProcesoDiario;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.exception.ConflictException;
import com.ath.adminefectivo.repositories.LogProcesoDiarioRepository;
import com.ath.adminefectivo.service.ILogProcesoDiarioService;
import com.ath.adminefectivo.service.IParametroService;
import com.querydsl.core.types.Predicate;

/**
 * Servicios responsables de exponer los metodos referentes a los procesos 
 * diarios en ejecucion
 * @author Bayron Andres Perez M
 */

@Service
public class LogProcesoDiarioImpl implements ILogProcesoDiarioService {

	@Autowired
	LogProcesoDiarioRepository logProcesoDiarioRepository;

	@Autowired
	IParametroService parametroService;

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<LogProcesoDiarioDTO> getLogsProcesosDierios(Predicate predicate) {
		var logProcesoDiario = logProcesoDiarioRepository.findAll(predicate);
		
		List<LogProcesoDiarioDTO> listLogProcesoDiarioDto = new ArrayList<>();
		logProcesoDiario.forEach(entity -> listLogProcesoDiarioDto.add(LogProcesoDiarioDTO.CONVERTER_DTO.apply(entity)));
		return listLogProcesoDiarioDto;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean esDiaCompleto(Date diaCierre) {
		int numProcesosTotales = parametroService.valorParametroEntero(Parametros.NUMERO_PROCESOS_TOTALES_DIA);

		int numProcesosDia = logProcesoDiarioRepository.countByFechaFinalizacionAndEstadoProceso(diaCierre,
				Dominios.ESTADO_PROCESO_DIA_COMPLETO);

		return numProcesosTotales == numProcesosDia;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LogProcesoDiarioDTO obtenerLogProcesoDiarioById(Long id) {
		if (Objects.isNull(id)) {
			throw new ConflictException(ApiResponseCode.GENERIC_ERROR.getDescription());
		}
		var logProcesoDiario = logProcesoDiarioRepository.findById(id).get();
		if (Objects.isNull(logProcesoDiario)) {
			throw new AplicationException(ApiResponseCode.ERROR_LOGPROCESODIARIO_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_LOGPROCESODIARIO_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_LOGPROCESODIARIO_NO_ENCONTRADO.getHttpStatus());
		}
		return LogProcesoDiarioDTO.CONVERTER_DTO.apply(logProcesoDiario);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LogProcesoDiarioDTO persistirLogProcesoDiario(LogProcesoDiarioDTO logProcesoDiarioDTO) {
		if (!Objects.isNull(logProcesoDiarioDTO.getIdLogProceso()) && 
			logProcesoDiarioRepository.existsById(logProcesoDiarioDTO.getIdLogProceso())) {
			throw new AplicationException(ApiResponseCode.ERROR_LOGPROCESODIARIO_YA_EXISTE.getCode(),
					ApiResponseCode.ERROR_LOGPROCESODIARIO_YA_EXISTE.getDescription(),
					ApiResponseCode.ERROR_LOGPROCESODIARIO_YA_EXISTE.getHttpStatus());
		}
		return LogProcesoDiarioDTO.CONVERTER_DTO.apply(logProcesoDiarioRepository.
				save(LogProcesoDiarioDTO.CONVERTER_ENTITY.apply(logProcesoDiarioDTO)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LogProcesoDiarioDTO actualizarLogProcesoDiario(LogProcesoDiarioDTO logProcesoDiarioDTO) {
		if (Objects.isNull(logProcesoDiarioDTO.getIdLogProceso())) {
			throw new AplicationException(ApiResponseCode.ERROR_LOGPROCESODIARIO_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_LOGPROCESODIARIO_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_LOGPROCESODIARIO_NO_ENCONTRADO.getHttpStatus());
		}
		return LogProcesoDiarioDTO.CONVERTER_DTO.apply(logProcesoDiarioRepository.
					save(LogProcesoDiarioDTO.CONVERTER_ENTITY.apply(logProcesoDiarioDTO)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean eliminarLogProcesoDiario(Long id) {
		if (Objects.isNull(id)) {
			throw new ConflictException(ApiResponseCode.GENERIC_ERROR.getDescription());
		}
		try {
			logProcesoDiarioRepository.deleteById(id);
			return true;
		} catch (Exception e) {
			throw new ConflictException(ApiResponseCode.GENERIC_ERROR.getDescription());
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public LogProcesoDiario obtenerEntidadLogProcesoDiario(String codigoProceso) {
		var logProcesoDiario = logProcesoDiarioRepository.findByCodigoProceso(codigoProceso);
		if(Objects.isNull(logProcesoDiario)) {
			throw new AplicationException(ApiResponseCode.ERROR_LOGPROCESODIARIO_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_LOGPROCESODIARIO_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_LOGPROCESODIARIO_NO_ENCONTRADO.getHttpStatus());
		}
		return logProcesoDiario;
	}

}
