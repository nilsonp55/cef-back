package com.ath.adminefectivo.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.RespuestaContableDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.LogProcesoDiario;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.service.ICierreContabilidadService;
import com.ath.adminefectivo.service.ITransaccionesInternasService;
@Service
public class CierreContabilidadServiceImpl implements ICierreContabilidadService {

	@Autowired
	LogProcesoDiarioImpl logProceso;
	
	@Autowired
	ITransaccionesInternasService transaccionesInternasService;
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean validacionTipoContabilidad(String tipoContabilidad) {
		LogProcesoDiario cierrePre;
		//String cierrePre;
		if(tipoContabilidad.equals("PM")) {
			//aqui se valida que la carga preliminar este cerrada
			cierrePre = logProceso.obtenerEntidadLogProcesoDiario(Dominios.CODIGO_PROCESO_LOG_PRELIMINAR);
			
			if(!cierrePre.getEstadoProceso().equals(Dominios.ESTADO_PROCESO_DIA_COMPLETO)) {
				throw new NegocioException(ApiResponseCode.ERROR_ESTADO_CARGA_PRELIMINAR.getCode(),
                        ApiResponseCode.ERROR_ESTADO_CARGA_PRELIMINAR.getDescription(),
                        ApiResponseCode.ERROR_ESTADO_CARGA_PRELIMINAR.getHttpStatus());
				
			}else {
				LogProcesoDiario cierreContabilidad = logProceso.obtenerEntidadLogProcesoDiario(Dominios.CODIGO_PROCESO_LOG_CONTABILIDAD_PM);
				if(cierreContabilidad.getEstadoProceso().equals(Dominios.ESTADO_PROCESO_DIA_COMPLETO)) {
					throw new NegocioException(ApiResponseCode.PROCESO_CONTABILIDAD_CERRADA.getCode(),
		                    ApiResponseCode.PROCESO_CONTABILIDAD_CERRADA.getDescription(),
		                    ApiResponseCode.PROCESO_CONTABILIDAD_CERRADA.getHttpStatus());
					
				}
			}
		}else if(tipoContabilidad.equals("AM")) {
					//validacion que el estado conciliacion esta cerrara AM
					//VALIDACION QUE EL ESTADO DE LA CONCILIACION ESTE CERRADA AM
					cierrePre = logProceso.obtenerEntidadLogProcesoDiario("CONCILIACION");
					
					if(!cierrePre.getEstadoProceso().equals(Dominios.ESTADO_PROCESO_DIA_COMPLETO)) {
						throw new NegocioException(ApiResponseCode.ERROR_ESTADO_CARGA_CONCILIACION.getCode(),
		                        ApiResponseCode.ERROR_ESTADO_CARGA_CONCILIACION.getDescription(),
		                        ApiResponseCode.ERROR_ESTADO_CARGA_CONCILIACION.getHttpStatus());
						
					}else {
						LogProcesoDiario cierreContabilidad = logProceso.obtenerEntidadLogProcesoDiario(Dominios.CODIGO_PROCESO_LOG_CONTABILIDAD_AM);
						if(cierreContabilidad.getEstadoProceso().equals(Dominios.ESTADO_PROCESO_DIA_COMPLETO)) {
							throw new NegocioException(ApiResponseCode.PROCESO_CONTABILIDAD_CERRADA.getCode(),
				                    ApiResponseCode.PROCESO_CONTABILIDAD_CERRADA.getDescription(),
				                    ApiResponseCode.PROCESO_CONTABILIDAD_CERRADA.getHttpStatus());
							
						}
					}
				}
		else {
			//personalizar el tipo de contabilidad errada debe ser am o pm
			throw new NegocioException(ApiResponseCode.ERROR_TIPO_CONTABILIDAD.getCode(),
	                 ApiResponseCode.ERROR_TIPO_CONTABILIDAD.getDescription(),
	                 ApiResponseCode.ERROR_TIPO_CONTABILIDAD.getHttpStatus());
			}
	

			return true;
		
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean existsErroresContablesByTipoContabilidadAndFecha(Date fechaSistema, String tipoContabilidad,
			int codBanco) {		
		if(codBanco > 0) {
			//VALIDA ERRORES CONTABLES CON UN SOLO BANCO
			return transaccionesInternasService.existErroresContablesByBanco(fechaSistema, tipoContabilidad, codBanco);
		}
		else if(codBanco == 0 ){
			//VALIDACION ERROES CONTABLES POR BANCOS
			return transaccionesInternasService.existErroresContablesAllBanco(fechaSistema, tipoContabilidad);
			
		}
		return false;
	}
		
	}
	

