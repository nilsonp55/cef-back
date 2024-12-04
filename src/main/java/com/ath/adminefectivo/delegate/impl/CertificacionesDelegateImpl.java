package com.ath.adminefectivo.delegate.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.delegate.ICertificacionesDelegate;
import com.ath.adminefectivo.dto.DetallesDefinicionArchivoDTO;
import com.ath.adminefectivo.dto.LogProcesoDiarioDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.ArchivosCargados;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.service.IArchivosCargadosService;
import com.ath.adminefectivo.service.IAuditoriaProcesosService;
import com.ath.adminefectivo.service.IDetalleDefinicionArchivoService;
import com.ath.adminefectivo.service.IDominioService;
import com.ath.adminefectivo.service.ILogProcesoDiarioService;
import com.ath.adminefectivo.service.IOperacionesCertificadasService;
import com.ath.adminefectivo.service.IParametroService;

@Service
public class CertificacionesDelegateImpl implements ICertificacionesDelegate {

	@Autowired
	IArchivosCargadosService archivosCargadosService;

	@Autowired
	IOperacionesCertificadasService operacionesCertificadasService;

	@Autowired
	ILogProcesoDiarioService logProcesoDiarioService;

	@Autowired
	IParametroService parametroService;
	
	@Autowired
	IAuditoriaProcesosService auditoriaProcesosService;

	@Autowired
	IDominioService dominioService;

	@Autowired
	IDetalleDefinicionArchivoService detalleDefinicionArchivoService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean procesarCertificaciones(String agrupador) {

		Date fechaProceso = parametroService.valorParametroDate(Constantes.FECHA_DIA_PROCESO);
		auditoriaProcesosService.actualizarAuditoriaProceso(Dominios.CODIGO_PROCESO_LOG_CERTIFICACION, 
				fechaProceso, Constantes.ESTADO_PROCESO_INICIO, Constantes.ESTADO_PROCESO_INICIO);
		
		List<Long> archivosCargados = archivosCargadosService
				.listadoIdArchivosCargados(agrupador, fechaProceso, Dominios.ESTADO_VALIDACION_CORRECTO);
		if (archivosCargados.isEmpty()) {
			auditoriaProcesosService.actualizarAuditoriaProceso(Dominios.CODIGO_PROCESO_LOG_CERTIFICACION, 
					fechaProceso, Constantes.ESTADO_PROCESO_ERROR, 
					ApiResponseCode.ERROR_ARCHICOS_CARGADOS_NO_ENCONTRADO.getDescription());
			throw new NegocioException(ApiResponseCode.ERROR_ARCHICOS_CARGADOS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_ARCHICOS_CARGADOS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_ARCHICOS_CARGADOS_NO_ENCONTRADO.getHttpStatus());
		} else {
			validarLogProcesoDiario();
			validarExistenciaArchivos(archivosCargados.size(), fechaProceso);
			this.procesarArchivosCertificaciones(archivosCargados);
			
			// siguiente l�nea, incluye conciliaci�n autom�tica (en procedimientos de BD)
			operacionesCertificadasService.validarNoConciliables();
			
			cambiarEstadoLogProcesoDiario();
			auditoriaProcesosService.actualizarAuditoriaProceso(Dominios.CODIGO_PROCESO_LOG_CERTIFICACION, 
					fechaProceso, Constantes.ESTADO_PROCESO_PROCESADO, 
					Constantes.ESTRUCTURA_OK);
			return true;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String procesarAlcances() {
				
		List<ArchivosCargados> archivosCargados = archivosCargadosService.consultarArchivosPorEstadoCargue(Dominios.ESTADO_VALIDACION_REPROCESO);
		if (archivosCargados.isEmpty()) {
			throw new NegocioException(ApiResponseCode.ERROR_ARCHICOS_CARGADOS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_ARCHICOS_CARGADOS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_ARCHICOS_CARGADOS_NO_ENCONTRADO.getHttpStatus());
		} else {
			return procesarArchivosAlcance(archivosCargados);
		}
	}

	/**
	 * Metodo encargado de validar el log de proceso diario
	 * 
	 * @author cesar.castano
	 */
	private void validarLogProcesoDiario() {
		Date fechaProceso = parametroService.valorParametroDate(Constantes.FECHA_DIA_PROCESO);
		var log = logProcesoDiarioService.obtenerEntidadLogProcesoDiario(Dominios.CODIGO_PROCESO_LOG_DEFINITIVO);
		if (!log.getEstadoProceso().equals(Dominios.ESTADO_PROCESO_DIA_COMPLETO)) {			
			auditoriaProcesosService.actualizarAuditoriaProceso(Dominios.CODIGO_PROCESO_LOG_CERTIFICACION,
					fechaProceso, Constantes.ESTADO_PROCESO_ERROR, 
					ApiResponseCode.ERROR_PROCESO_SIGUE_ABIERTO.getDescription());
			throw new NegocioException(ApiResponseCode.ERROR_PROCESO_SIGUE_ABIERTO.getCode(),
					ApiResponseCode.ERROR_PROCESO_SIGUE_ABIERTO.getDescription(),
					ApiResponseCode.ERROR_PROCESO_SIGUE_ABIERTO.getHttpStatus());
		}

		var logConciliacion = logProcesoDiarioService
				.obtenerEntidadLogProcesoDiario(Dominios.CODIGO_PROCESO_LOG_CERTIFICACION);
		if (logConciliacion.getEstadoProceso().equals(Dominios.ESTADO_PROCESO_DIA_COMPLETO)) {
			auditoriaProcesosService.actualizarAuditoriaProceso(Dominios.CODIGO_PROCESO_LOG_CERTIFICACION,
					fechaProceso, Constantes.ESTADO_PROCESO_ERROR, 
					ApiResponseCode.ERROR_LOGPROCESODIARIO_NO_ENCONTRADO.getDescription());
			throw new NegocioException(ApiResponseCode.ERROR_LOGPROCESODIARIO_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_LOGPROCESODIARIO_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_LOGPROCESODIARIO_NO_ENCONTRADO.getHttpStatus());
		}

	}

	/**
	 * 
	 * @param numArchivosCargados
	 * @param fechaProceso
	 * @author prv_ccastano
	 */
	private void validarExistenciaArchivos(Integer numArchivosCargados, Date fechaProceso) {
		
		Integer valor = parametroService.valorParametroEntero(Constantes.NUMERO_MINIMO_ARCHIVOS_PARA_CIERRE);
		if ( numArchivosCargados < valor) {
			auditoriaProcesosService.actualizarAuditoriaProceso(Dominios.CODIGO_PROCESO_LOG_CERTIFICACION,
					fechaProceso, Constantes.ESTADO_PROCESO_ERROR, 
					ApiResponseCode.ERROR_NO_CUMPLE_MINIMO_ARCHIVOS_CARGADOS_CERTIFICACION.getDescription());
			
			throw new NegocioException(ApiResponseCode.ERROR_NO_CUMPLE_MINIMO_ARCHIVOS_CARGADOS_CERTIFICACION.getCode(),
					ApiResponseCode.ERROR_NO_CUMPLE_MINIMO_ARCHIVOS_CARGADOS_CERTIFICACION.getDescription(),
					ApiResponseCode.ERROR_NO_CUMPLE_MINIMO_ARCHIVOS_CARGADOS_CERTIFICACION.getHttpStatus());
		}
	}

	/**
	 * Metodo encargado de cambiar el estado del log de proceso diario
	 * 
	 * @author cesar.castano
	 */
	private void cambiarEstadoLogProcesoDiario() {
		Date fechaProceso = parametroService.valorParametroDate(Constantes.FECHA_DIA_PROCESO);
		var logProcesoDiario = logProcesoDiarioService
				.obtenerEntidadLogProcesoDiario(Dominios.CODIGO_PROCESO_LOG_CERTIFICACION);
		if (Objects.isNull(logProcesoDiario)) {
			auditoriaProcesosService.actualizarAuditoriaProceso(Dominios.CODIGO_PROCESO_LOG_CERTIFICACION,
					fechaProceso, Constantes.ESTADO_PROCESO_ERROR, 
					ApiResponseCode.ERROR_CODIGO_PROCESO_NO_EXISTE.getDescription());
			throw new NegocioException(ApiResponseCode.ERROR_CODIGO_PROCESO_NO_EXISTE.getCode(),
					ApiResponseCode.ERROR_CODIGO_PROCESO_NO_EXISTE.getDescription(),
					ApiResponseCode.ERROR_CODIGO_PROCESO_NO_EXISTE.getHttpStatus());
		} else {
			logProcesoDiario.setEstadoProceso(Dominios.ESTADO_PROCESO_DIA_COMPLETO);
			logProcesoDiarioService
					.actualizarLogProcesoDiario(LogProcesoDiarioDTO.CONVERTER_DTO.apply(logProcesoDiario));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean procesarArchivosCertificaciones(List<Long> idsArchivosCargados) {

		Date fechaProceso = parametroService.valorParametroDate(Constantes.FECHA_DIA_PROCESO);
		int cuenta = 0;
		ArchivosCargados elemento = null;

		for (Long elementoId : idsArchivosCargados) {
			try {
				cuenta = cuenta + 1;
				// leer los registros de cada archivo
				elemento = archivosCargadosService.consultarArchivoById(elementoId);
				List<DetallesDefinicionArchivoDTO> listadoDetalleArchivo = detalleDefinicionArchivoService
						.consultarDetalleDefinicionArchivoByIdMaestro(elemento.getIdModeloArchivo());
				if (elemento.getIdModeloArchivo().equals(Dominios.TIPO_ARCHIVO_IBBCS)
						|| elemento.getIdModeloArchivo().equals(Dominios.TIPO_ARCHIVO_IBMCS)) {
					operacionesCertificadasService.procesarArchivoBrinks(elemento, listadoDetalleArchivo);
				} else {
					operacionesCertificadasService.procesarArchivoOtrosFondos(elemento, listadoDetalleArchivo);
				}

			auditoriaProcesosService.actualizarAuditoriaProceso(Dominios.CODIGO_PROCESO_LOG_CERTIFICACION,
					fechaProceso, Constantes.ESTADO_PROCESO_PROCESO, "Archivos procesados: " + cuenta);

			} catch (NegocioException nExcep) {
				auditoriaProcesosService.actualizarAuditoriaProceso(Dominios.CODIGO_PROCESO_LOG_CERTIFICACION,
						fechaProceso, Constantes.ESTADO_PROCESO_ERROR, nExcep.getMessage());
				throw nExcep;
			} catch (Exception excep) {
				auditoriaProcesosService.actualizarAuditoriaProceso(Dominios.CODIGO_PROCESO_LOG_CERTIFICACION,
						fechaProceso, Constantes.ESTADO_PROCESO_ERROR, "Error procesando archivo: " + cuenta);
				throw excep;
			}
		}

		return true;
	}


	@Override
	public String  procesarArchivosAlcance(List<ArchivosCargados> archivosCargados) {

		for (ArchivosCargados elemento : archivosCargados) {
			List<DetallesDefinicionArchivoDTO> listadoDetalleArchivo = detalleDefinicionArchivoService
					.consultarDetalleDefinicionArchivoByIdMaestro(elemento.getIdModeloArchivo());
			if (elemento.getIdModeloArchivo().equals(Dominios.TIPO_ARCHIVO_IBBCS)
					|| elemento.getIdModeloArchivo().equals(Dominios.TIPO_ARCHIVO_IBMCS)) {
				operacionesCertificadasService.procesarArchivoBrinks(elemento, listadoDetalleArchivo);
			} else {
				operacionesCertificadasService.procesarArchivoOtrosFondos(elemento, listadoDetalleArchivo);
			}
		}
		return operacionesCertificadasService.procesarArchivosAlcance();
	}
}
