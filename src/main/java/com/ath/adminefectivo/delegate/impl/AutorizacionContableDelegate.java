package com.ath.adminefectivo.delegate.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Parametros;
import com.ath.adminefectivo.delegate.IAutorizacionContable;
import com.ath.adminefectivo.dto.LogProcesoDiarioDTO;
import com.ath.adminefectivo.dto.TransaccionesContablesDTO;
import com.ath.adminefectivo.entities.LogProcesoDiario;
import com.ath.adminefectivo.service.IParametroService;
import com.ath.adminefectivo.service.ITransaccionesContablesService;
import com.ath.adminefectivo.service.impl.LogProcesoDiarioImpl;
import com.ath.adminefectivo.service.impl.TransaccionesContablesServiceImpl;

@Service
public class AutorizacionContableDelegate implements IAutorizacionContable{

	@Autowired
	LogProcesoDiarioImpl logProceso;
	
	@Autowired
	IParametroService parametroService;
	
	@Autowired
	ITransaccionesContablesService transaccionesContablesService;
	
	@Override
	public LogProcesoDiarioDTO autorizacionContable(Date fecha, String tipoContabilidad, String estado) {
		
		LogProcesoDiarioDTO result = null;
		//obteniendo la fecha del sistema
		Date fechaActual = parametroService.valorParametroDate(Parametros.FECHA_DIA_ACTUAL_PROCESO);	
		String contabilidadProceso = "";
		if(tipoContabilidad.equals("PM")) {
			contabilidadProceso = "CONTABILIDAD_PM";
		}else if(tipoContabilidad.equals("AM")) {
			contabilidadProceso = "CONTABILIDAD_AM";
		}
		
		if(estado.equals("autorizacion1")) {
			Integer validacionCierre = transaccionesContablesService.estadovalidacionContable(3);
			LogProcesoDiario estadoAutorizacion = logProceso.obtenerEntidadLogProcesoDiario(contabilidadProceso);
			if(estadoAutorizacion.getEstadoProceso().equals("POR_AUTORIZAR") && validacionCierre == null){
				estadoAutorizacion.setEstadoProceso("AUTORIZADO");
				result = logProceso.actualizarLogProcesoDiario(LogProcesoDiarioDTO.CONVERTER_DTO.apply(estadoAutorizacion));
			}					
							
		}						
 
		return result;
	}

}

