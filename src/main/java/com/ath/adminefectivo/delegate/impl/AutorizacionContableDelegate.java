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
import com.ath.adminefectivo.service.impl.LogProcesoDiarioImpl;
import com.ath.adminefectivo.service.impl.TransaccionesContablesServiceImpl;

@Service
public class AutorizacionContableDelegate implements IAutorizacionContable{

	@Autowired
	LogProcesoDiarioImpl logProceso;
	
	@Autowired
	IParametroService parametroService;
	
	@Override
	public LogProcesoDiarioDTO autorizacionContable(Date fecha, String tipoContabilidad, String estado) {
		
		//obteniendo la fecha del sistema
		Date fechaActual = parametroService.valorParametroDate(Parametros.FECHA_DIA_ACTUAL_PROCESO);	

		
		//validacion del estado log procesos diarios
				LogProcesoDiario estadoAutorizacionPM = logProceso.obtenerEntidadLogProcesoDiario("CONTABILIDAD_PM");
				LogProcesoDiario estadoAutorizacionAM = logProceso.obtenerEntidadLogProcesoDiario("CONTABILIDAD_AM");
				LogProcesoDiarioDTO result = null; 
				TransaccionesContablesServiceImpl validacionContable = new TransaccionesContablesServiceImpl();
				
				if(estado.equals("autorizacion1")) {
					
					String validacionCierre = validacionContable.estadovalidacionContable(estado);
					
						//llamar al metodo de ejecutar el cierre para un banco. 
						//validar que el cierre contable este en el estado cerrado. crear metodo validar cierre contable
						if(estadoAutorizacionPM.getEstadoProceso().equals("POR_AUTORIZAR")) {
							
							if(validacionCierre.isEmpty()) {
								LogProcesoDiarioDTO x = null;
								x.setEstadoProceso("AUTORIZADO");
								result = logProceso.actualizarLogProcesoDiario(x);
							}
							
							
						}						
						if(estadoAutorizacionAM.getEstadoProceso().equals("POR_AUTORIZAR")) {
							
							if(validacionCierre.isEmpty()) {
								LogProcesoDiarioDTO x = null;
								x.setEstadoProceso("AUTORIZADO");
								result = logProceso.actualizarLogProcesoDiario(x);
							}
							
							
						}
				}
 
		return result;
	}

}

