package com.ath.adminefectivo.delegate.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.delegate.IAutorizacionContable;
import com.ath.adminefectivo.dto.LogProcesoDiarioDTO;
import com.ath.adminefectivo.entities.LogProcesoDiario;
import com.ath.adminefectivo.service.IParametroService;
import com.ath.adminefectivo.service.ITransaccionesContablesService;
import com.ath.adminefectivo.service.ITransaccionesInternasService;
import com.ath.adminefectivo.service.IgenerarArchivoService;
import com.ath.adminefectivo.service.impl.LogProcesoDiarioImpl;

@Service
public class AutorizacionContableDelegate implements IAutorizacionContable {

	@Autowired
	LogProcesoDiarioImpl logProceso;

	@Autowired
	IParametroService parametroService;

	@Autowired
	ITransaccionesContablesService transaccionesContablesService;

	@Autowired
	ITransaccionesInternasService transaccionesInternasService;
	
	@Autowired
	IgenerarArchivoService generarArchivoService;

	@Override
	public String autorizacionContable(String tipoContabilidad, String estado) {

		Date fecha = parametroService.valorParametroDate(Constantes.FECHA_DIA_PROCESO);
		
		String contabilidadProceso = "";
		if (tipoContabilidad.equals("PM")) {
			contabilidadProceso = "CONTABILIDAD_PM";
		} else if (tipoContabilidad.equals("AM")) {
			contabilidadProceso = "CONTABILIDAD_AM";
		}

		if (estado.equals("autorizacion1")) {

			if (!transaccionesInternasService.existErroresContablesAllBanco(fecha, tipoContabilidad)) {

				LogProcesoDiario estadoAutorizacion = logProceso.obtenerEntidadLogProcesoDiario(contabilidadProceso);
				if (estadoAutorizacion.getEstadoProceso().equals(Dominios.ESTADO_PROCESO_DIA_PENDIENTE)) {
					String respuesta = transaccionesContablesService.generarComprobanteContable(fecha, tipoContabilidad);
					if(respuesta.equals("OK")) {
						generarArchivoService.generarArchivosCierreContable(fecha, tipoContabilidad);
						estadoAutorizacion.setEstadoProceso(Dominios.ESTADO_PROCESO_DIA_COMPLETO);
						logProceso.actualizarLogProcesoDiario(LogProcesoDiarioDTO.CONVERTER_DTO.apply(estadoAutorizacion));
						return "PROCESO REALIZADO CON EXITO";
					}else {
						return respuesta;
					}	
				}
			}else {
				return "EXISTEN ERRORES CONTABLES";
			}

		}
		return "EL ESTADO NO ES VALIDO";
	}

}
