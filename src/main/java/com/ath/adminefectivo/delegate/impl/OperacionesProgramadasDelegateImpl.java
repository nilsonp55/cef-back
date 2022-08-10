package com.ath.adminefectivo.delegate.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.delegate.IOperacionesProgramadasDelegate;
import com.ath.adminefectivo.dto.ArchivosCargadosDTO;
import com.ath.adminefectivo.dto.LogProcesoDiarioDTO;
import com.ath.adminefectivo.dto.OperacionesProgramadasDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.service.IArchivosCargadosService;
import com.ath.adminefectivo.service.IDominioService;
import com.ath.adminefectivo.service.ILogProcesoDiarioService;
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String generarOperacionesProgramadas(String idArchivo) {

		List<ArchivosCargadosDTO> listadoArchivosCargados = archivosCargadosService.getArchivosCargadosSinProcesar(idArchivo.toUpperCase().trim());
		if (!Objects.isNull(listadoArchivosCargados)) {
			List<OperacionesProgramadasDTO> operacionesProgramadas = operacionesProgramadasService
					.generarOperacionesProgramadas(listadoArchivosCargados);
			if (!operacionesProgramadas.isEmpty()) {
				return Constantes.MENSAJE_GENERO_OPERACIONES_PROGRAMADAS_CORRECTO;
			}
		}
		return Constantes.MENSAJE_NO_SE_ENCONTRARON_ARCHIVOS_OP;
	}
	

	private void validarExistenciaArchivos(List<ArchivosCargadosDTO> listadoArchivosCargados) {
		var fechaDia = new Date();
		if (listadoArchivosCargados.size() == 3) {
			ArchivosCargadosDTO archivoISRPO = listadoArchivosCargados.stream().filter(
					listado -> listado.getIdModeloArchivo().toUpperCase().trim().equals(Dominios.TIPO_ARCHIVO_ISRPO)
					&& new SimpleDateFormat("dd-MM-yyyy").format(listado.getFechaArchivo())
						.equals(new SimpleDateFormat("dd-MM-yyyy").format(fechaDia))
					&& listado.getEstadoCargue().equals("OK")).findFirst().orElse(null);

			ArchivosCargadosDTO archivoISRPC = listadoArchivosCargados.stream().filter(
					listado -> listado.getIdModeloArchivo().toUpperCase().trim().equals(Dominios.TIPO_ARCHIVO_ISRPC)
					&& new SimpleDateFormat("dd-MM-yyyy").format(listado.getFechaArchivo())
					.equals(new SimpleDateFormat("dd-MM-yyyy").format(fechaDia))
					&& listado.getEstadoCargue().equals("OK")).findFirst().orElse(null);

			ArchivosCargadosDTO archivoIPPSV = listadoArchivosCargados.stream().filter(
					listado -> listado.getIdModeloArchivo().toUpperCase().trim().equals(Dominios.TIPO_ARCHIVO_IPPSV)
					&& new SimpleDateFormat("dd-MM-yyyy").format(listado.getFechaArchivo())
					.equals(new SimpleDateFormat("dd-MM-yyyy").format(fechaDia))
					&& listado.getEstadoCargue().equals("OK")).findFirst().orElse(null);

			if ((Objects.isNull(archivoISRPO)) || (Objects.isNull(archivoISRPC)) || (Objects.isNull(archivoIPPSV))) {
				throw new NegocioException(ApiResponseCode.ERROR_FALTAN_ARCHIVOS_POR_CARGAR.getCode(),
						ApiResponseCode.ERROR_FALTAN_ARCHIVOS_POR_CARGAR.getDescription(),
						ApiResponseCode.ERROR_FALTAN_ARCHIVOS_POR_CARGAR.getHttpStatus());
			}
		}else {
			throw new NegocioException(ApiResponseCode.ERROR_FALTAN_ARCHIVOS_POR_CARGAR.getCode(),
					ApiResponseCode.ERROR_FALTAN_ARCHIVOS_POR_CARGAR.getDescription(),
					ApiResponseCode.ERROR_FALTAN_ARCHIVOS_POR_CARGAR.getHttpStatus());
		}
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

}
