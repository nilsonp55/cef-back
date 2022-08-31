package com.ath.adminefectivo.delegate.impl;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Parametros;
import com.ath.adminefectivo.delegate.ICierreContabilidadDelegate;
import com.ath.adminefectivo.dto.RespuestaContableDTO;
import com.ath.adminefectivo.dto.TransaccionesContablesDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.LogProcesoDiario;
import com.ath.adminefectivo.entities.TransaccionesContables;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.service.IParametroService;
import com.ath.adminefectivo.service.impl.CierreContabilidadServiceImpl;
import com.ath.adminefectivo.service.impl.LogProcesoDiarioImpl;
import com.ath.adminefectivo.service.impl.TransaccionesContablesServiceImpl;

@Service
public class CierreContabilidadDelegateImpl implements ICierreContabilidadDelegate {

	@Autowired
	IParametroService parametroService;
	/*
	@Autowired
	IparametroDelegate parametroDelegate;
	*/
	@Autowired
	TransaccionesContablesServiceImpl transaccionesContablesService ;
	
	@Autowired
	LogProcesoDiarioImpl logProceso;
	
	@Autowired
	CierreContabilidadServiceImpl cierreContabilidadService;

	@Override
	public List<RespuestaContableDTO> cerrarContabilidad(Date fechaSistema, String tipoContabilidad, int codBanco, String fase) {
		System.out.println("ENTRO 48 DELEGATE");
		List<RespuestaContableDTO> respuesta = null;

			List<RespuestaContableDTO> registros = null;

			if(fase.equals("INICIAL")) {
				//VALIDACION QUE LA CARGA PELIMINAR ESTE CERRADA SI ES PM
				System.out.println("ENTRO 55 DELEGATE");
				// SERVICIO DE VALIDACION TIPOCONTABILIDAD
				LogProcesoDiario tipoContable = cierreContabilidadService.validacionTipoContabilidad(tipoContabilidad);
				
				System.out.println("ENTRO DELEGATE 59 "+tipoContable);
				//tipoContable.getEstadoProceso();
				System.out.println("ENTRO DELEGATE 59 "+tipoContable.getEstadoProceso());
				if(tipoContable.getEstadoProceso().equals("CERRADO")) {
					System.out.println("ENTRO 55 DELEGATE 62 cerrado");
					//VALIDACION ERRORES CONTABLES POR BANCO: 
					if(codBanco > 0) {
						System.out.println("ENTRO 65 DELEGATE");
						registros = transaccionesContablesService.existErroresContablesByBanco(fechaSistema, tipoContabilidad, codBanco);
						System.out.println("ENTRO 67 DELEGATE "+ registros);
					}
					else if(codBanco == 0 ){
						//VALIDACION ERROES CONTABLES POR BANCOS
						registros = transaccionesContablesService.existErroresContablesAllBanco(fechaSistema, tipoContabilidad);
						
					}
					System.out.println("ENTRO 74 DELEGATE registros -- "+ registros);
					if(registros.isEmpty()) {
						System.out.println("ENTRO 76 DELEGATE");
						respuesta = transaccionesContablesService.getCierreContable(fechaSistema,tipoContabilidad,codBanco);
						System.out.println("ENTRO 78 DELEGATE "+respuesta);
					}
					else {
						//personalizar los errores contables
						throw new NegocioException(ApiResponseCode.ERROR_TIPO_CONTABLES.getCode(),
		                        ApiResponseCode.ERROR_TIPO_CONTABLES.getDescription(),
		                        ApiResponseCode.ERROR_TIPO_CONTABLES.getHttpStatus());
					}
				}
				
				}

		return respuesta;
				
	}
	
}
	
	
	
	


