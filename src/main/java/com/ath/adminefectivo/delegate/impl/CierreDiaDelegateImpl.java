package com.ath.adminefectivo.delegate.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.constantes.Parametros;
import com.ath.adminefectivo.delegate.ICierreDiaDelegate;
import com.ath.adminefectivo.dto.LogProcesoDiarioDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.ArchivosCargados;
import com.ath.adminefectivo.entities.LogProcesoDiario;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.LogProcesoDiarioRepository;
import com.ath.adminefectivo.service.IArchivosCargadosService;
import com.ath.adminefectivo.service.IAuditoriaProcesosService;
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
	LogProcesoDiarioRepository logProcesoDiarioRepository;

	@Autowired
	IArchivosCargadosService archivosCargadosService;

	@Autowired
	IAuditoriaProcesosService auditoriaProcesosService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public Date cerrarDia() {
		Date fechaActual = parametroService.valorParametroDate(Parametros.FECHA_DIA_ACTUAL_PROCESO);

		if (logProcesoDiarioService.esDiaCompleto(fechaActual)) {
			Date nuevaFecha = festivosNacionalesService.consultarSiguienteHabil(fechaActual);
			DateFormat dateFormat = new SimpleDateFormat(Constantes.FECHA_PATTERN_DD_MM_YYYY_WITH_SLASH);
			String nuevaFechaString = dateFormat.format(nuevaFecha);
			parametroService.actualizarValorParametro(Parametros.FECHA_DIA_ACTUAL_PROCESO, nuevaFechaString);

			// Se actualizan los registros para logProcesoDiario
			List<LogProcesoDiarioDTO> listLogProcesoDiarioDto = logProcesoDiarioService
					.getLogsProcesosDiariosByFechaProceso(fechaActual);

			List<LogProcesoDiario> listLogProcesoDiario = new ArrayList<>();
			listLogProcesoDiarioDto.forEach(entity -> {
				entity.setIdLogProceso(null);
				entity.setFechaCreacion(nuevaFecha);
				entity.setFechaFinalizacion(nuevaFecha);
				entity.setEstadoProceso(Dominios.ESTADO_PROCESO_DIA_PENDIENTE);
				listLogProcesoDiario.add(LogProcesoDiarioDTO.CONVERTER_ENTITY.apply(entity));
			});
			logProcesoDiarioRepository.saveAll(listLogProcesoDiario);

			List<ArchivosCargados> archivosCargados = archivosCargadosService.consultarArchivosPorFecha(fechaActual);
			archivosCargados.forEach(archivoCargado -> {
				archivoCargado.setEstado(Constantes.ESTADO_ARCHIVO_HISTORICO);
				archivosCargadosService.actualizarArchivosCargados(archivoCargado);
			});

			archivosCargados = archivosCargadosService
					.consultarArchivosPorEstadoCargue(Dominios.ESTADO_VALIDACION_FUTURO);
			archivosCargados.forEach(archivoCargado -> {
				archivoCargado.setEstadoCargue(Dominios.ESTADO_VALIDACION_CORRECTO);
				archivoCargado.setFechaArchivo(nuevaFecha);
				archivosCargadosService.actualizarArchivosCargados(archivoCargado);
			});

			auditoriaProcesosService.crearTodosAuditoriaProcesos(nuevaFecha);

			return nuevaFecha;

		} else {
			throw new NegocioException(ApiResponseCode.ERROR_PROCESOS_NO_COMPLETADOS.getCode(),
					ApiResponseCode.ERROR_PROCESOS_NO_COMPLETADOS.getDescription(),
					ApiResponseCode.ERROR_PROCESOS_NO_COMPLETADOS.getHttpStatus());
		}

	}

}
