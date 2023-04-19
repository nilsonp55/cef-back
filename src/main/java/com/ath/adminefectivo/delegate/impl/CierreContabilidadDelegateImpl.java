package com.ath.adminefectivo.delegate.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.delegate.ICierreContabilidadDelegate;
import com.ath.adminefectivo.dto.RespuestaContableDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.service.IParametroService;
import com.ath.adminefectivo.service.impl.CierreContabilidadServiceImpl;
import com.ath.adminefectivo.service.impl.LogProcesoDiarioImpl;
import com.ath.adminefectivo.service.impl.TransaccionesContablesServiceImpl;

@Service
public class CierreContabilidadDelegateImpl implements ICierreContabilidadDelegate {

	@Autowired
	IParametroService parametroService;

	@Autowired
	TransaccionesContablesServiceImpl transaccionesContablesService;

	@Autowired
	LogProcesoDiarioImpl logProceso;

	@Autowired
	CierreContabilidadServiceImpl cierreContabilidadService;

	@Override
	public List<RespuestaContableDTO> cerrarContabilidad(Date fechaSistema, String tipoContabilidad, int codBanco,
			String fase) {
		List<RespuestaContableDTO> respuesta = null;

		fechaSistema = parametroService.valorParametroDate(Constantes.FECHA_DIA_PROCESO);

		// VALIDA QUE LA CARGA PELIMINAR ESTE CERRADA SI ES PM
		// O SI ES PM QUE LA CONCILIACION ESTE CERRADA
		if (fase.equals("INICIAL") && cierreContabilidadService.validacionTipoContabilidad(tipoContabilidad)) {
			if (!cierreContabilidadService.existsErroresContablesByTipoContabilidadAndFecha(fechaSistema,
					tipoContabilidad, codBanco)) {
				respuesta = transaccionesContablesService.getCierreContable(fechaSistema, tipoContabilidad, codBanco);
			} else {
				// personalizar los errores contables
				throw new NegocioException(ApiResponseCode.ERROR_TIPO_CONTABLES.getCode(),
						ApiResponseCode.ERROR_TIPO_CONTABLES.getDescription(),
						ApiResponseCode.ERROR_TIPO_CONTABLES.getHttpStatus());
			}
		}
		return respuesta;
	}
}
