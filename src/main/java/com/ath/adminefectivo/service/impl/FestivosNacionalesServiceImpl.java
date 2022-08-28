package com.ath.adminefectivo.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.repositories.FestivosNacionalesRepository;
import com.ath.adminefectivo.service.IDominioService;
import com.ath.adminefectivo.service.IFestivosNacionalesService;

@Service
public class FestivosNacionalesServiceImpl implements IFestivosNacionalesService {

	@Autowired
	FestivosNacionalesRepository festivosNacionalesRepository;
	
	@Autowired
	IDominioService dominioService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean esFestivo(Date fecha) {
		var festivoOpt = festivosNacionalesRepository.findById(fecha);
		return festivoOpt.isPresent();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Date> consultarFestivosVigentes(Date fecha) {
		return festivosNacionalesRepository.consultarFestivosVigentes(Objects.isNull(fecha) ? new Date() : fecha);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date consultarSiguienteHabil(Date fechaReferencia) {
		
		var dominios =	dominioService.consultaListValoresNumPorDominio(Dominios.DOMINIO_DIAS_NO_HABILES);		
		List<Integer> finDeSemana = dominios.stream().map(Double::intValue).toList();
		Calendar calendar = DateUtils.toCalendar(fechaReferencia);

		for (int i = 0; i < Constantes.NUMERO_MAXIMO_CICLOS;) {

			calendar.add(Calendar.DATE, 1);
			int day = calendar.get(Calendar.DAY_OF_WEEK);

			if (!finDeSemana.contains(day) && !this.esFestivo(calendar.getTime())) {
				return calendar.getTime();
			}
		}

		throw new AplicationException(ApiResponseCode.ERROR_CALCULO_DIA_HABIL.getCode(),
				ApiResponseCode.ERROR_CALCULO_DIA_HABIL.getDescription(),
				ApiResponseCode.ERROR_CALCULO_DIA_HABIL.getHttpStatus());

	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date consultarAnteriorHabil(Date fechaReferencia) {
		
		var dominios =	dominioService.consultaListValoresNumPorDominio(Dominios.DOMINIO_DIAS_NO_HABILES);		
		List<Integer> finDeSemana = dominios.stream().map(Double::intValue).toList();
		Calendar calendar = DateUtils.toCalendar(fechaReferencia);

		for (int i = 0; i < Constantes.NUMERO_MAXIMO_CICLOS;) {

			calendar.add(Calendar.DATE, - 1);
			int day = calendar.get(Calendar.DAY_OF_WEEK);

			if (!finDeSemana.contains(day) && !this.esFestivo(calendar.getTime())) {
				return calendar.getTime();
			}
		}

		throw new AplicationException(ApiResponseCode.ERROR_CALCULO_DIA_HABIL.getCode(),
				ApiResponseCode.ERROR_CALCULO_DIA_HABIL.getDescription(),
				ApiResponseCode.ERROR_CALCULO_DIA_HABIL.getHttpStatus());

	}
}
