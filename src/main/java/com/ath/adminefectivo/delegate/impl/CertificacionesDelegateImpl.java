package com.ath.adminefectivo.delegate.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.delegate.ICertificacionesDelegate;
import com.ath.adminefectivo.dto.LogProcesoDiarioDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.ArchivosCargados;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.ArchivosCargadosRepository;
import com.ath.adminefectivo.service.ILogProcesoDiarioService;
import com.ath.adminefectivo.service.IOperacionesCertificadasService;
import com.ath.adminefectivo.service.IParametroService;

@Service
public class CertificacionesDelegateImpl implements ICertificacionesDelegate {

	@Autowired
	ArchivosCargadosRepository archivosCargadosRepository;
	
	@Autowired
	IOperacionesCertificadasService operacionesCertificadasService;
	
	@Autowired
	ILogProcesoDiarioService logProcesoDiarioService;

	@Autowired
	IParametroService parametroService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean procesarCertificaciones(String modeloArchivo, Long idArchivo) {
		
		List<ArchivosCargados> archivosCargados = archivosCargadosRepository.findByIdModeloArchivoAndIdArchivo(
				modeloArchivo, idArchivo);
		if(archivosCargados.isEmpty()) {
				throw new NegocioException(ApiResponseCode.ERROR_ARCHICOS_CARGADOS_NO_ENCONTRADO.getCode(),
							ApiResponseCode.ERROR_ARCHICOS_CARGADOS_NO_ENCONTRADO.getDescription(),
							ApiResponseCode.ERROR_ARCHICOS_CARGADOS_NO_ENCONTRADO.getHttpStatus());
		}else {
			operacionesCertificadasService.procesarArchivosCertificaciones(archivosCargados);
			return true;
		}
	}
	

	/**
	 * Metodo encargado de validar el log de proceso diario
	 * @author cesar.castano
	 */
	private void validarLogProcesoDiario() {
		var log = logProcesoDiarioService.obtenerEntidadLogProcesoDiario(
										Dominios.CODIGO_PROCESO_LOG_DEFINITIVO);
		if (Objects.isNull(log)) {
			throw new NegocioException(ApiResponseCode.ERROR_CODIGO_PROCESO_NO_EXISTE.getCode(),
					ApiResponseCode.ERROR_CODIGO_PROCESO_NO_EXISTE.getDescription(),
					ApiResponseCode.ERROR_CODIGO_PROCESO_NO_EXISTE.getHttpStatus());
		}else {
			if (!log.getEstadoProceso().equals(Dominios.ESTADO_PROCESO_DIA_COMPLETO)) {
				throw new NegocioException(ApiResponseCode.ERROR_PROCESO_SIGUE_ABIERTO.getCode(),
						ApiResponseCode.ERROR_PROCESO_SIGUE_ABIERTO.getDescription(),
						ApiResponseCode.ERROR_PROCESO_SIGUE_ABIERTO.getHttpStatus());
			}
		}
	}

	private void validarExistenciaArchivos(List<ArchivosCargados> archivosCargados) {
		var fechaDia = new Date();
		Integer valor = parametroService.valorParametroEntero(Constantes.NUMERO_MINIMO_ARCHIVOS_PARA_CIERRE);
		if (archivosCargados.size() >= valor) {
			ArchivosCargados archivos = archivosCargados.stream().filter(
					listado -> !listado.getEstadoCargue().equals("OK")
					&& new SimpleDateFormat("dd-MM-yyyy").format(listado.getFechaArchivo())
					.equals(new SimpleDateFormat("dd-MM-yyyy").format(fechaDia))).findFirst().orElse(null);			
			if (Objects.nonNull(archivos)) {
				throw new NegocioException(ApiResponseCode.ERROR_HAY_ARCHIVOS_FALLIDOS_CARGUE_CERTIFICACION.getCode(),
						ApiResponseCode.ERROR_HAY_ARCHIVOS_FALLIDOS_CARGUE_CERTIFICACION.getDescription(),
						ApiResponseCode.ERROR_HAY_ARCHIVOS_FALLIDOS_CARGUE_CERTIFICACION.getHttpStatus());
			}
		}else {
			throw new NegocioException(ApiResponseCode.ERROR_NO_CUMPLE_MINIMO_ARCHIVOS_CARGADOS_CERTIFICACION.getCode(),
					ApiResponseCode.ERROR_NO_CUMPLE_MINIMO_ARCHIVOS_CARGADOS_CERTIFICACION.getDescription(),
					ApiResponseCode.ERROR_NO_CUMPLE_MINIMO_ARCHIVOS_CARGADOS_CERTIFICACION.getHttpStatus());
		}
	}

	/**
	 * Metodo encargado de cambiar el estado del log de proceso diario
	 * @author cesar.castano
	 */
	private void cambiarEstadoLogProcesoDiario() {
		var logProcesoDiario = logProcesoDiarioService.obtenerEntidadLogProcesoDiario(
										Dominios.CODIGO_PROCESO_LOG_CERTIFICACION);
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
