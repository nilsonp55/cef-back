package com.ath.adminefectivo.delegate.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.delegate.IGenerarArchivoDelegate;
import com.ath.adminefectivo.dto.compuestos.RespuestaGenerarArchivoDTO;
import com.ath.adminefectivo.service.IParametroService;
import com.ath.adminefectivo.service.impl.GenerarArchivoServiceimpl;

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
	GenerarArchivoServiceimpl generarArchivoService;
	
	@Override
	public RespuestaGenerarArchivoDTO generarArchivo(String tipoContabilidad, int codBanco) {
	
		Date fecha = parametroService.valorParametroDate(Constantes.FECHA_DIA_PROCESO);		
		if(tipoContabilidad.equals("AM") || tipoContabilidad.equals("PM")) {					
			return generarArchivoService.generarArchivo(fecha, tipoContabilidad, codBanco);			
		}
		return null;
	}
}
