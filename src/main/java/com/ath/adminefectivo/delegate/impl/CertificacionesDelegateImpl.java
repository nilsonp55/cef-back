package com.ath.adminefectivo.delegate.impl;

import java.util.ArrayList;
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
import com.ath.adminefectivo.dto.OperacionesCertificadasDTO;
import com.ath.adminefectivo.entities.OperacionesProgramadas;
import com.ath.adminefectivo.repositories.IOperacionesCertificadasRepository;
import com.ath.adminefectivo.service.IFestivosNacionalesService;
import com.ath.adminefectivo.service.IOperacionesProgramadasService;
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
import lombok.extern.log4j.Log4j2;

@Log4j2
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
	
	@Autowired
	IFestivosNacionalesService festivosNacionalesService;
	
	@Autowired
	IOperacionesProgramadasService OperacionesProgramadasService;

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
			
			certificarProgramaTransporte(fechaProceso);
			
			// siguiente l�nea, incluye conciliaci�n autom�tica (en procedimientos de BD)
			operacionesCertificadasService.validarNoConciliables();
			
			cambiarEstadoLogProcesoDiario();
			auditoriaProcesosService.actualizarAuditoriaProceso(Dominios.CODIGO_PROCESO_LOG_CERTIFICACION, 
					fechaProceso, Constantes.ESTADO_PROCESO_PROCESADO, 
					Constantes.ESTRUCTURA_OK);
			return true;
		}
	}
	
	private void certificarProgramaTransporte(Date fechaProceso) {
		Date fechaAnteriorHabil = festivosNacionalesService.consultarAnteriorHabil(fechaProceso);
		List<OperacionesCertificadasDTO> operacionesCertificadasList = operacionesCertificadasService.findOpCertificadasNotInOpProgramadas(fechaAnteriorHabil);		
		List<OperacionesProgramadas> operacionesProgramadasList = new ArrayList<OperacionesProgramadas>();
		if(!operacionesCertificadasList.isEmpty()) {
			for(OperacionesCertificadasDTO operacionesCertificadas : operacionesCertificadasList) {
				operacionesProgramadasList.add(setOpProgramadasFromOpCertificadas(operacionesCertificadas));
			}			
			OperacionesProgramadasService.saveAll(operacionesProgramadasList);
		}
	}

	private OperacionesProgramadas setOpProgramadasFromOpCertificadas(
			OperacionesCertificadasDTO operacionesCertificadas) {	
		OperacionesProgramadas operacionesProgramadas = new OperacionesProgramadas();			
		operacionesProgramadas.setCodigoFondoTDV(operacionesCertificadas.getCodigoFondoTDV());
		operacionesProgramadas.setCodigoPuntoDestino(operacionesCertificadas.getCodigoPuntoDestino());
		operacionesProgramadas.setCodigoPuntoOrigen(operacionesCertificadas.getCodigoPuntoOrigen());
		operacionesProgramadas.setEntradaSalida(operacionesCertificadas.getEntradaSalida());
		operacionesProgramadas.setEstadoConciliacion(operacionesCertificadas.getEstadoConciliacion());
		operacionesProgramadas.setFechaCreacion(operacionesCertificadas.getFechaCreacion());
		operacionesProgramadas.setFechaDestino(operacionesCertificadas.getFechaEjecucion());
		operacionesProgramadas.setFechaModificacion(operacionesCertificadas.getFechaModificacion());
		operacionesProgramadas.setIdArchivoCargado(operacionesCertificadas.getIdArchivoCargado().intValue());
		operacionesProgramadas.setTipoOperacion(operacionesCertificadas.getTipoOperacion());
		operacionesProgramadas.setUsuarioCreacion(operacionesCertificadas.getUsuarioCreacion());
		operacionesProgramadas.setUsuarioModificacion(operacionesCertificadas.getUsuarioModificacion());					
		operacionesProgramadas.setTipoServicio(operacionesCertificadas.getTipoServicio());			
		operacionesProgramadas.setValorTotal(operacionesCertificadas.getValorTotal());
		operacionesProgramadas.setCodigoServicioTdv(operacionesCertificadas.getCodigoServicioTdv());
		operacionesProgramadas.setTipoPuntoDestino(operacionesCertificadas.getTipoPuntoDestino());
		operacionesProgramadas.setTipoPuntoOrigen(operacionesCertificadas.getTipoPuntoOrigen());
		operacionesProgramadas.setCodigoMoneda(operacionesCertificadas.getMoneda());
		operacionesProgramadas.setBancoAVAL(operacionesCertificadas.getBancoAVAL());
		operacionesProgramadas.setTdv(operacionesCertificadas.getTdv());
		operacionesProgramadas.setEsCambio(false);
		operacionesProgramadas.setEstadoOperacion(Constantes.ESTADO_OPERACION_EJECUTADA);
		return operacionesProgramadas;
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
		  log.debug("idArchivo: {}", elementoId);
			try {
				cuenta = cuenta + 1;
				// leer los registros de cada archivo
				elemento = archivosCargadosService.consultarArchivoById(elementoId);
				List<DetallesDefinicionArchivoDTO> listadoDetalleArchivo = detalleDefinicionArchivoService
						.consultarDetalleDefinicionArchivoByIdMaestro(elemento.getIdModeloArchivo());
				if (elemento.getIdModeloArchivo().equals(Dominios.TIPO_ARCHIVO_IBBCS)
						|| elemento.getIdModeloArchivo().equals(Dominios.TIPO_ARCHIVO_IBMCS)) {
				  log.debug("idArchivo: {} - procesarArchivoBrinks", elementoId);
				  operacionesCertificadasService.procesarArchivoBrinks(elemento, listadoDetalleArchivo);
				} else {
				  log.debug("idArchivo: {} - procesarArchivoOtrosFondos", elementoId);
				  operacionesCertificadasService.procesarArchivoOtrosFondos(elemento, listadoDetalleArchivo);
				}

			auditoriaProcesosService.actualizarAuditoriaProceso(Dominios.CODIGO_PROCESO_LOG_CERTIFICACION,
					fechaProceso, Constantes.ESTADO_PROCESO_PROCESO, "Archivos procesados: " + cuenta);
			log.debug("idArchivo: {}", elementoId);
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
