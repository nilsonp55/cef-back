package com.ath.adminefectivo.delegate.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.constantes.Parametros;
import com.ath.adminefectivo.delegate.ICierreDiaDelegate;
import com.ath.adminefectivo.dto.LogProcesoDiarioDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.LogProcesoDiario;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.LogProcesoDiarioRepository;
import com.ath.adminefectivo.service.IFestivosNacionalesService;
import com.ath.adminefectivo.service.ILogProcesoDiarioService;
import com.ath.adminefectivo.service.IParametroService;

@Service
public class CierreDiaDelegateImpl implements ICierreDiaDelegate {

	@Autowired
	IFestivosNacionalesService festivosNacionalesService;

	@Autowired
	ILogProcesoDiarioService logProcesoDiarioService;

	@Autowired
	IParametroService parametroService;

	@Autowired
	ILogProcesoDiarioService procesoDiarioService;
	
	@Autowired
	LogProcesoDiarioRepository procesoDiarioRepository;
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date cerrarDia() {
		Date fechaActual = parametroService.valorParametroDate(Parametros.FECHA_DIA_ACTUAL_PROCESO);

		if (logProcesoDiarioService.esDiaCompleto(fechaActual)) {
			Date nuevaFecha = festivosNacionalesService.consultarSiguienteHabil(fechaActual);
			DateFormat dateFormat = new SimpleDateFormat(Constantes.FECHA_PATTERN_DD_MM_YYYY_WITH_SLASH);
			String nuevaFechaString = dateFormat.format(nuevaFecha);
			parametroService.actualizarValorParametro(Parametros.FECHA_DIA_ACTUAL_PROCESO, nuevaFechaString);
			
			//Se actualizan los registros para logProcesoDiario
			List<LogProcesoDiarioDTO> listLogProcesoDiarioDto = procesoDiarioService
					.getLogsProcesosDiariosByFechaProceso(fechaActual);
			
			List<LogProcesoDiario> listLogProcesoDiario = new ArrayList<>();
			listLogProcesoDiarioDto.forEach(entity -> {
				entity.setIdLogProceso(null);
				entity.setFechaCreacion(nuevaFecha);
				entity.setFechaFinalizacion(nuevaFecha);
				entity.setEstadoProceso(Dominios.ESTADO_PROCESO_DIA_PENDIENTE);
				listLogProcesoDiario.add(LogProcesoDiarioDTO.CONVERTER_ENTITY.apply(entity));
			});
			procesoDiarioRepository.saveAll(listLogProcesoDiario);
			
			return nuevaFecha;

		} else {
			throw new NegocioException(ApiResponseCode.ERROR_PROCESOS_NO_COMPLETADOS.getCode(),
					ApiResponseCode.ERROR_PROCESOS_NO_COMPLETADOS.getDescription(),
					ApiResponseCode.ERROR_PROCESOS_NO_COMPLETADOS.getHttpStatus());
		}

	}

}
