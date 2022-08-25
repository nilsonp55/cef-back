package com.ath.adminefectivo.delegate.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
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
import com.ath.adminefectivo.service.ILogProcesoDiarioService;
import com.ath.adminefectivo.service.IMaestroDefinicionArchivoService;
import com.ath.adminefectivo.service.IOperacionesProgramadasService;

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
	
<<<<<<< HEAD
=======
	@Autowired
	IParametroService parametroService;
	
>>>>>>> 630dd22c07819645beda0dab45f2315ee0d3011e
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
				if (!operacionesProgramadas.isEmpty()) {
					return Constantes.MENSAJE_GENERO_OPERACIONES_PROGRAMADAS_CORRECTO;
				}
			}
		}
		cambiarEstadoLogProcesoDiario();
		return Constantes.MENSAJE_NO_SE_ENCONTRARON_ARCHIVOS_OP;
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
			logProcesoDiarioService.actualizarLogProcesoDiario(
					LogProcesoDiarioDTO.CONVERTER_DTO.apply(logProcesoDiario));
		}
	}
	
	private void validarExistenciayFechaArchivos(String agrupador) {
<<<<<<< HEAD
		List<ArchivosCargados> listadoArchivosCargados = archivosCargadosService
									.listadoArchivosCargadosSinProcesarDefinitiva(agrupador);
		if (agrupador.equals(Dominios.AGRUPADOR_DEFINICION_ARCHIVOS_PRELIMINARES) &&
			(listadoArchivosCargados.size() == 0 || listadoArchivosCargados.size() > 1)){
=======
		
		Date fechaArchivo = parametroService.valorParametroDate(Parametros.FECHA_DIA_ACTUAL_PROCESO);
		if(Dominios.AGRUPADOR_DEFINICION_ARCHIVOS_DEFINITIVO.equals(agrupador)) {
			//TODO restar días no hábiles en lugar de 1
			fechaArchivo = UtilsString.restarDiasAFecha(fechaArchivo,-1);
		}
		
		List<ArchivosCargados> listadoArchivosCargados = archivosCargadosService
							.listadoArchivosCargadosSinProcesarDefinitiva(agrupador, fechaArchivo,
																Dominios.ESTADO_VALIDACION_CORRECTO);
		if (agrupador.equals(Dominios.AGRUPADOR_DEFINICION_ARCHIVOS_PRELIMINARES) &&
			(listadoArchivosCargados.size() == 0 || 
			listadoArchivosCargados.size() > Constantes.NUMERO_ARCHIVOS_CARGADOS_PRELIMINAR)){
>>>>>>> 630dd22c07819645beda0dab45f2315ee0d3011e
			throw new NegocioException(ApiResponseCode.ERROR_FALTAN_ARCHIVOS_POR_CARGAR.getCode(),
					ApiResponseCode.ERROR_FALTAN_ARCHIVOS_POR_CARGAR.getDescription(),
					ApiResponseCode.ERROR_FALTAN_ARCHIVOS_POR_CARGAR.getHttpStatus());
		}
		if (agrupador.equals(Dominios.AGRUPADOR_DEFINICION_ARCHIVOS_DEFINITIVO) && 
<<<<<<< HEAD
			(listadoArchivosCargados.size() == 0 || listadoArchivosCargados.size() != 2)) {
=======
			(listadoArchivosCargados.size() == 0 || 
			listadoArchivosCargados.size() != Constantes.NUMERO_ARCHIVOS_CARGADOS_DEFINITIVA)) {
>>>>>>> 630dd22c07819645beda0dab45f2315ee0d3011e
			throw new NegocioException(ApiResponseCode.ERROR_FALTAN_ARCHIVOS_POR_CARGAR.getCode(),
						ApiResponseCode.ERROR_FALTAN_ARCHIVOS_POR_CARGAR.getDescription(),
						ApiResponseCode.ERROR_FALTAN_ARCHIVOS_POR_CARGAR.getHttpStatus());
		}
	}

}
