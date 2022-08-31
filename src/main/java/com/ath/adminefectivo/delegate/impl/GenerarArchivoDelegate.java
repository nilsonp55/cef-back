package com.ath.adminefectivo.delegate.impl;

import java.io.ByteArrayInputStream;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.delegate.IGenerarArchivoDelegate;
import com.ath.adminefectivo.service.IParametroService;
import com.ath.adminefectivo.service.impl.TransaccionesContablesServiceImpl;
import com.ath.adminefectivo.service.impl.generarArchivoServiceimpl;

/**
 * Delegate responsable del manejo, consulta y para generar
 * archivos contables
 *
 * @author Miller Caro
 */

@Service
public class GenerarArchivoDelegate implements IGenerarArchivoDelegate {
	
	
	@Autowired
	IParametroService parametroService;
	
	@Autowired
	generarArchivoServiceimpl generarArchivoService;
	
	@Autowired
	TransaccionesContablesServiceImpl transaccionesContablesService; 

	@Override
	public ByteArrayInputStream generarArchivo(Date fecha, String tipoContabilidad, int codBanco) {
		//RespuestaContableDTO registros = new RespuestaContableDTO();
		ByteArrayInputStream registros = null;
		//fecha actual
		//Date fechaActual = parametroService.valorParametroDate(Parametros.FECHA_DIA_ACTUAL_PROCESO);
		//valida el tipoContabilidad
	
		
		if(tipoContabilidad.equals("AM") || tipoContabilidad.equals("PM")) {
			registros = generarArchivoService.generarArchivo(fecha, tipoContabilidad, codBanco);
		}
		
		return registros;
		
	}
}
