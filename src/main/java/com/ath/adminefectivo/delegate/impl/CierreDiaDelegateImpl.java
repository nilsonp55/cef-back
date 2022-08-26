package com.ath.adminefectivo.delegate.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Parametros;
import com.ath.adminefectivo.delegate.ICierreDiaDelegate;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.service.IFestivosNacionalesService;
import com.ath.adminefectivo.service.ILogProcesoDiarioService;
import com.ath.adminefectivo.service.IParametroService;

@Service
public class CierreDiaDelegateImpl implements ICierreDiaDelegate {

	@Autowired
	IFestivosNacionalesService festivosNacionalesService;

	@Autowired
	ILogProcesoDiarioService logProcesoDiarioService;

	@Autowired
	IParametroService parametroService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date cerrarDia() {
		Date fechaActual = parametroService.valorParametroDate(Parametros.FECHA_DIA_ACTUAL_PROCESO);

			Date nuevaFecha = festivosNacionalesService.consultarSiguienteHabil(fechaActual);
			DateFormat dateFormat = new SimpleDateFormat(Constantes.FECHA_PATTERN_DD_MM_YYYY_WITH_SLASH);
			String nuevaFechaString = dateFormat.format(nuevaFecha);
			parametroService.actualizarValorParametro(Parametros.FECHA_DIA_ACTUAL_PROCESO, nuevaFechaString);
			return nuevaFecha;

	}

}
