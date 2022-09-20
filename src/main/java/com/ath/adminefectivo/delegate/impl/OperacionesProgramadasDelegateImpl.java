package com.ath.adminefectivo.delegate.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.constantes.Parametros;
import com.ath.adminefectivo.delegate.IOperacionesProgramadasDelegate;
import com.ath.adminefectivo.dto.ArchivosCargadosDTO;
import com.ath.adminefectivo.dto.LogProcesoDiarioDTO;
import com.ath.adminefectivo.dto.MaestrosDefinicionArchivoDTO;
import com.ath.adminefectivo.dto.OperacionesProgramadasDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.ArchivosCargados;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.service.IArchivosCargadosService;
import com.ath.adminefectivo.service.IDominioService;
import com.ath.adminefectivo.service.IFestivosNacionalesService;
import com.ath.adminefectivo.service.ILogProcesoDiarioService;
import com.ath.adminefectivo.service.IMaestroDefinicionArchivoService;
import com.ath.adminefectivo.service.IOperacionesProgramadasService;
import com.ath.adminefectivo.service.IParametroService;

@Service
public class OperacionesProgramadasDelegateImpl implements IOperacionesProgramadasDelegate {

	@Autowired
	IOperacionesProgramadasService operacionesProgramadasService;

	@Autowired
	IArchivosCargadosService archivosCargadosService;
	
	@Autowired
	ILogProcesoDiarioService logProcesoDiarioService;

	@Autowired
	IDominioService dominioService;

	@Autowired
	IMaestroDefinicionArchivoService maestroDefinicionArchivoService;
	
	@Autowired
	IParametroService parametroService;

	@Autowired
	IFestivosNacionalesService festivosNacionalesService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String generarOperacionesProgramadas(String agrupador) {

		List<MaestrosDefinicionArchivoDTO> maestroDefinicionArchivo = maestroDefinicionArchivoService
									.consultarDefinicionArchivoByAgrupador(
									Constantes.ESTADO_MAESTRO_DEFINICION_ACTIVO, agrupador);
		validarExistenciayFechaArchivos(agrupador);
			
		for (MaestrosDefinicionArchivoDTO definicionArchivo : maestroDefinicionArchivo) {
			
			List<ArchivosCargadosDTO> listadoArchivosCargados = archivosCargadosService
					.getArchivosCargadosSinProcesar(
					definicionArchivo.getIdMaestroDefinicionArchivo().toUpperCase().trim());
			
			if (!Objects.isNull(listadoArchivosCargados)) {
				List<OperacionesProgramadasDTO> operacionesProgramadas = operacionesProgramadasService
						.generarOperacionesProgramadas(listadoArchivosCargados);	
			}
		}
		cambiarEstadoLogProcesoDiario();
		//return Constantes.MENSAJE_NO_SE_ENCONTRARON_ARCHIVOS_OP;
		return Constantes.MENSAJE_GENERO_OPERACIONES_PROGRAMADAS_CORRECTO;
	}
	
	/**
	 * Metodo encargado de cambiar el estado del log de proceso diario
	 * @author cesar.castano
	 */
	private void cambiarEstadoLogProcesoDiario() {
		var logProcesoDiario = logProcesoDiarioService.obtenerEntidadLogProcesoDiario(
										Dominios.CODIGO_PROCESO_LOG_DEFINITIVO);
		if (Objects.isNull(logProcesoDiario)) {
			throw new NegocioException(ApiResponseCode.ERROR_CODIGO_PROCESO_NO_EXISTE.getCode(),
					ApiResponseCode.ERROR_CODIGO_PROCESO_NO_EXISTE.getDescription(),
					ApiResponseCode.ERROR_CODIGO_PROCESO_NO_EXISTE.getHttpStatus());
		}else {
			logProcesoDiario.setEstadoProceso(Dominios.ESTADO_PROCESO_DIA_COMPLETO);
			logProcesoDiarioService.actualizarLogProcesoDiario(
					LogProcesoDiarioDTO.CONVERTER_DTO.apply(logProcesoDiario));
		}
	}
	
	/**
	 * 
	 * @param agrupador
	 */
	private void validarExistenciayFechaArchivos(String agrupador) {

		Date fechaArchivo = parametroService.valorParametroDate(Parametros.FECHA_DIA_ACTUAL_PROCESO);

		List<ArchivosCargados> listadoArchivosCargados = archivosCargadosService
							.listadoArchivosCargadosSinProcesarDefinitiva(agrupador, fechaArchivo,
																Dominios.ESTADO_VALIDACION_CORRECTO);
		if (agrupador.equals(Dominios.AGRUPADOR_DEFINICION_ARCHIVOS_PRELIMINARES) &&
			(listadoArchivosCargados.size() == 0 || 
			listadoArchivosCargados.size() > Constantes.NUMERO_ARCHIVOS_CARGADOS_PRELIMINAR)){
			throw new NegocioException(ApiResponseCode.ERROR_FALTAN_ARCHIVOS_POR_CARGAR.getCode(),
					ApiResponseCode.ERROR_FALTAN_ARCHIVOS_POR_CARGAR.getDescription(),
					ApiResponseCode.ERROR_FALTAN_ARCHIVOS_POR_CARGAR.getHttpStatus());
		}
		if (agrupador.equals(Dominios.AGRUPADOR_DEFINICION_ARCHIVOS_DEFINITIVO) && 
			(listadoArchivosCargados.size() == 0 || 
			listadoArchivosCargados.size() != Constantes.NUMERO_ARCHIVOS_CARGADOS_DEFINITIVA)) {
			throw new NegocioException(ApiResponseCode.ERROR_FALTAN_ARCHIVOS_POR_CARGAR.getCode(),
						ApiResponseCode.ERROR_FALTAN_ARCHIVOS_POR_CARGAR.getDescription(),
						ApiResponseCode.ERROR_FALTAN_ARCHIVOS_POR_CARGAR.getHttpStatus());
		}
	}
}
