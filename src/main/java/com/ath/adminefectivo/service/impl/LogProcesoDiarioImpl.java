package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.constantes.Parametros;
import com.ath.adminefectivo.dto.LogProcesoDiarioDTO;
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

}
