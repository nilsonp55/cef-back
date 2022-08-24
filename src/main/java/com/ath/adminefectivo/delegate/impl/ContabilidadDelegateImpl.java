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
import com.ath.adminefectivo.dto.compuestos.ContabilidadDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.service.IContabilidadService;
import com.ath.adminefectivo.service.IDominioService;
import com.ath.adminefectivo.service.IOperacionesProgramadasService;
import com.ath.adminefectivo.service.IParametroService;
import com.ath.adminefectivo.service.ITransaccionesInternasService;

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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String generarContabilidad(String tipoContabilidad) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String f1 = "2022-05-28";
		String f2 = "2022-07-19";
		Date fechaInicio = null;
		Date fechaFin = null;
		try {
			fechaInicio = sdf.parse(f1);
			fechaFin = sdf.parse(f2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
		}
		
		Date fechaProceso = this.obtenerFechaProceso(tipoContabilidad);
		
		var operacionesProgramadas = operacionesProgramadasService.getOperacionesProgramadasPorFechas(tipoContabilidad, fechaInicio,fechaFin);
		if(!operacionesProgramadas.isEmpty()) {
			int resultado = contabilidadService.generarContabilidad(tipoContabilidad, operacionesProgramadas);
			
			List<OperacionIntradiaDTO> listadoOperacionesProgramadasIntradia = operacionesProgramadasService.consultarOperacionesIntradia(fechaInicio, fechaFin);	
			resultado = contabilidadService.generarContabilidadIntradia(tipoContabilidad, listadoOperacionesProgramadasIntradia, resultado);
			
			resultado = contabilidadService.generarMovimientosContables(f1, f2, tipoContabilidad, Dominios.ESTADO_CONTABILIDAD_GENERADO,"yyyy-MM-dd" );
			
			
			
			if(resultado > 0) {
				return "MENSAJE EXITOSO";
			}else {
				return "NO SE ENCONTRARON OPERACIONES POR PROCESAR";
			}
		}
		return "NO SE ENCONTRARON OPERACIONES POR PROCESAR";
	}

	private Date obtenerFechaProceso(String tipoContabilidad) {
		String valorFecha = parametroService.valorParametro(Parametros.FECHA_DIA_ACTUAL_PROCESO);
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
		if(!Objects.isNull(fechaSistema) && tipoContabilidad.equals("PM")) {
			return fechaSistema;
		}else if(!Objects.isNull(fechaSistema) && tipoContabilidad.equals("AM")) {
			Calendar c = Calendar.getInstance();
			c.setTime(fechaSistema);
			c.add(Calendar.DATE, -1);
			return c.getTime();
		}
		return null;
	}


}
