package com.ath.adminefectivo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.LogProcesoDiario;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.service.ICierreContabilidadService;
@Service
public class CierreContabilidadServiceImpl implements ICierreContabilidadService {

	@Autowired
	LogProcesoDiarioImpl logProceso;
	

	@Override
	public LogProcesoDiario validacionTipoContabilidad(String tipoContabilidad) {
		LogProcesoDiario cierrePre;
		//String cierrePre;
		if(tipoContabilidad.equals("PM")) {
			//aqui se valida que la carga preliminar este cerrada
			//TODO agregar a la siguiente busqueda la fecha
			cierrePre = logProceso.obtenerEntidadLogProcesoDiario("CARG_PRELIMINAR");
			cierrePre.getEstadoProceso();
			if(cierrePre.getEstadoProceso().compareTo("CERRADO")!=0) {
				throw new NegocioException(ApiResponseCode.ERROR_ESTADO_CARGA_PRELIMINAR.getCode(),
                        ApiResponseCode.ERROR_ESTADO_CARGA_PRELIMINAR.getDescription(),
                        ApiResponseCode.ERROR_ESTADO_CARGA_PRELIMINAR.getHttpStatus());
				
			}
		}else if(tipoContabilidad.equals("AM")) {
					//validacion que el estado conciliacion esta cerrara AM
					//VALIDACION QUE EL ESTADO DE LA CONCILIACION ESTE CERRADA AM
					cierrePre = logProceso.obtenerEntidadLogProcesoDiario("CONCILIACION");
					
					if(cierrePre.getEstadoProceso().compareTo("CERRADO")!=0) {
					//if(!cierrePre.equals("CERRADO")) {
						throw new NegocioException(ApiResponseCode.ERROR_ESTADO_CARGA_CONCILIACION.getCode(),
		                        ApiResponseCode.ERROR_ESTADO_CARGA_CONCILIACION.getDescription(),
		                        ApiResponseCode.ERROR_ESTADO_CARGA_CONCILIACION.getHttpStatus());
						
					}
				}
				else {
					//personalizar el tipo de contabilidad errada debe ser am o pm
					throw new NegocioException(ApiResponseCode.ERROR_TIPO_CONTABILIDAD.getCode(),
	                        ApiResponseCode.ERROR_TIPO_CONTABILIDAD.getDescription(),
	                        ApiResponseCode.ERROR_TIPO_CONTABILIDAD.getHttpStatus());
				}

			return cierrePre;
		
	}
		
	}
	

