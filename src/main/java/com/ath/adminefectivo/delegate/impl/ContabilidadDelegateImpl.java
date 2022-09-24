package com.ath.adminefectivo.delegate.impl;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.constantes.Parametros;
import com.ath.adminefectivo.delegate.IContabilidadDelegate;
import com.ath.adminefectivo.dto.OperacionesProgramadasDTO;
import com.ath.adminefectivo.dto.TransaccionesInternasDTO;
import com.ath.adminefectivo.dto.compuestos.OperacionIntradiaDTO;
import com.ath.adminefectivo.dto.compuestos.ProcesoErroresContablesDTO;
import com.ath.adminefectivo.dto.compuestos.ResultadoErroresContablesDTO;
import com.ath.adminefectivo.dto.compuestos.ContabilidadDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.service.IContabilidadService;
import com.ath.adminefectivo.service.IDominioService;
import com.ath.adminefectivo.service.IErroresContablesService;
import com.ath.adminefectivo.service.IFestivosNacionalesService;
import com.ath.adminefectivo.service.IOperacionesProgramadasService;
import com.ath.adminefectivo.service.IParametroService;
import com.ath.adminefectivo.service.ITransaccionesInternasService;
import com.ath.adminefectivo.utils.UtilsString;

import antlr.Utils;

@Service
public class ContabilidadDelegateImpl implements IContabilidadDelegate {

	@Autowired
	IOperacionesProgramadasService operacionesProgramadasService;
	
	@Autowired
	IContabilidadService contabilidadService;
	
	@Autowired
	ITransaccionesInternasService transaccionesInternas;
	
	@Autowired
	IParametroService parametroService;
	
	@Autowired
	IDominioService dominioService;
	
	@Autowired
	IFestivosNacionalesService festivosNacionalesService;
	
	@Autowired
	IErroresContablesService erroresContablesService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContabilidadDTO generarContabilidad(String tipoContabilidad) {
	
		Date fechaSistema = parametroService.valorParametroDate(Constantes.FECHA_DIA_PROCESO);
		Date fechaProcesoInicial = this.obtenerFechaProceso(fechaSistema, tipoContabilidad);
		
		Date fechaProcesoFin = this.obtenerFechaProcesoFinal(tipoContabilidad, fechaSistema);
		
		
		
		System.out.println("fechaProcesoInicial "+fechaProcesoInicial);
		System.out.println("fechaProcesoFin "+fechaProcesoFin);
		
		var operacionesProgramadas = operacionesProgramadasService.getOperacionesProgramadasPorFechas(tipoContabilidad, fechaProcesoInicial,fechaProcesoFin);
				
		if(!operacionesProgramadas.isEmpty()) {
			
			contabilidadService.procesoEliminarExistentes(tipoContabilidad, operacionesProgramadas, fechaSistema, fechaSistema);
			
			int resultado = contabilidadService.generarContabilidad(tipoContabilidad, operacionesProgramadas);
			
			List<OperacionIntradiaDTO> listadoOperacionesProgramadasIntradia = operacionesProgramadasService.consultarOperacionesIntradia(fechaProcesoInicial, fechaProcesoFin);	
			resultado = contabilidadService.generarContabilidadIntradia(tipoContabilidad, listadoOperacionesProgramadasIntradia, resultado);
			resultado = contabilidadService.generarMovimientosContables(fechaSistema, fechaSistema, tipoContabilidad, Dominios.ESTADO_CONTABILIDAD_GENERADO);
		
			
			if(resultado > 0) {
				return contabilidadService.generarRespuestaContabilidad(fechaSistema, tipoContabilidad, "MENSAJE EXITOSO");
			}else {
				return contabilidadService.generarRespuestaContabilidad(fechaSistema, tipoContabilidad, "NO SE GENERARON TRANSACCIONES CONTABLES. ");
			}
		}
		return contabilidadService.generarRespuestaContabilidad(fechaSistema, tipoContabilidad, "NO SE ENCONTRARON OPERACIONES POR PROCESAR PARA LA FECHA");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ResultadoErroresContablesDTO> consultarErroresContables() {
		
		return erroresContablesService.consultarErroresContables();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProcesoErroresContablesDTO procesarErroresContables() {
		List<OperacionesProgramadasDTO> listadoOperacionesProgramadasPM = operacionesProgramadasService.obtenerOperacionesProgramadasConErroresContables("PM");
		System.out.println("listadoOperacionesProgramadasPM   "+listadoOperacionesProgramadasPM.size());
		List<OperacionesProgramadasDTO> listadoOperacionesProgramadasAM = operacionesProgramadasService.obtenerOperacionesProgramadasConErroresContables("AM");

		int resultado = contabilidadService.generarContabilidad("PM", listadoOperacionesProgramadasPM);
		resultado = contabilidadService.generarContabilidad("AM", listadoOperacionesProgramadasAM);
		
		Date fechaSistema = parametroService.valorParametroDate(Constantes.FECHA_DIA_PROCESO);
		Date fechaInicio = this.obtenerFecha2000();
		Date fechaFin = this.obtenerFechaProcesoFinal("PM", fechaSistema);
		
		List<OperacionIntradiaDTO> listadoOperacionesProgramadasIntradia = operacionesProgramadasService.consultarOperacionesIntradia(fechaInicio, fechaFin);	
		resultado = contabilidadService.generarContabilidadIntradia("PM", listadoOperacionesProgramadasIntradia, resultado);
		resultado = contabilidadService.generarContabilidadIntradia("AM", listadoOperacionesProgramadasIntradia, resultado);
		
		resultado = contabilidadService.generarMovimientosContables(fechaInicio, fechaFin, "PM", Dominios.ESTADO_CONTABILIDAD_GENERADO);
		resultado = contabilidadService.generarMovimientosContables(fechaInicio, fechaFin, "AM", Dominios.ESTADO_CONTABILIDAD_GENERADO);
		
		ProcesoErroresContablesDTO respuesta = new ProcesoErroresContablesDTO();
		respuesta.setTransaccionesInternas(this.generarRespuestaProcesoContables());
		respuesta.setErroresContablesActuales(this.consultarErroresContables());

		return respuesta;
	}

	private List<TransaccionesInternasDTO> generarRespuestaProcesoContables() {
		return contabilidadService.generarRespuestaProcesoContables();
	}

	/**
	 * Metodo encargado de realizar el llamado al servicio para saber que dia 
	 * fue el ultimo dia habil.
	 * 
	 * @param fechaProcesoFin
	 * @return Date
	 */
	private Date obtenerFechaProcesoFinal(String tipoContabilidad, Date fechaSistema) {
		if(tipoContabilidad.equals("AM")){
			return UtilsString.restarDiasAFecha(fechaSistema, -1);			
		}else {
			return fechaSistema;
		}
		
	}

	/**
	 * Metodo encargado de obtener la fecha del sistema actual
	 * para aplicarlo al proceso de contabilidad con sus respectivas
	 * validaciones
	 * 
	 * @param tipoContabilidad
	 * @return Date
	 */
	public Date obtenerFechaProceso(Date fechaSistema, String tipoContabilidad) {
		if(!Objects.isNull(fechaSistema)) {
			if(tipoContabilidad.equals("AM")) {
				return festivosNacionalesService.consultarAnteriorHabil(fechaSistema);
			}
			return fechaSistema;
		}else {
			throw new AplicationException(ApiResponseCode.ERROR_FECHA_CONTABILIDAD.getCode(),
					ApiResponseCode.ERROR_FECHA_CONTABILIDAD.getDescription(),
					ApiResponseCode.ERROR_FECHA_CONTABILIDAD.getHttpStatus());
		}

	}
	
	/**
	 * Metodo encargado de obtener la fecha del sistema actual
	 * para aplicarlo al proceso de contabilidad con sus respectivas
	 * validaciones
	 * 
	 * @param tipoContabilidad
	 * @return Date
	 */
	private Date obtenerFecha2000() {
		String valorFecha = "01/01/2000";
		String formatoFecha = dominioService.valorTextoDominio(Constantes.DOMINIO_FORMATO_FECHA, Dominios.FORMATO_FECHA_F1);
		SimpleDateFormat sdf = new SimpleDateFormat(formatoFecha);
		Date fechaSistema = null;
		try {
			fechaSistema = sdf.parse(valorFecha);
		} catch (ParseException e) {
			throw new AplicationException(ApiResponseCode.ERROR_FECHA_CONTABILIDAD.getCode(),
					ApiResponseCode.ERROR_FECHA_CONTABILIDAD.getDescription(),
					ApiResponseCode.ERROR_FECHA_CONTABILIDAD.getHttpStatus());
		}
		if(!Objects.isNull(fechaSistema)) {
			return fechaSistema;
		}
		return null;
	}






}
