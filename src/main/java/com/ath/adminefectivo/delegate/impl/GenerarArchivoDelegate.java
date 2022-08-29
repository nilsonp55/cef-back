package com.ath.adminefectivo.delegate.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Parametros;
import com.ath.adminefectivo.delegate.IGenerarArchivoDelegate;
import com.ath.adminefectivo.dto.RespuestaContableDTO;
import com.ath.adminefectivo.dto.TransaccionesContablesDTO;
import com.ath.adminefectivo.entities.TransaccionesContables;
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
	public ByteArrayInputStream generarArchivo(Date fecha, String tipoContabilidad, String codBanco) {
		//RespuestaContableDTO registros = new RespuestaContableDTO();
		ByteArrayInputStream registros = null;
		//fecha actual
		//Date fechaActual = parametroService.valorParametroDate(Parametros.FECHA_DIA_ACTUAL_PROCESO);
		//valida el tipoContabilidad
		String tipoC = transaccionesContablesService.findBytipoProceso(tipoContabilidad);
		if(!Objects.isNull(tipoC)) {
			registros = generarArchivoService.generarArchivo(fecha, tipoC, codBanco);
		}
		
		return registros;
		
	}
}
