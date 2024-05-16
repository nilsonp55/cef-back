package com.ath.adminefectivo.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.FestivosNacionales;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.exception.ConflictException;
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public FestivosNacionales saveFestivosNacionales(FestivosNacionales festivosNacionales) {
		if (festivosNacionales.getFecha() != null && festivosNacionalesRepository
				.existsById(festivosNacionales.getFecha())) {		
			throw new ConflictException("Festivo nacional ya existe");		
		}
		return festivosNacionalesRepository.save(festivosNacionales);
	}
	
	/**
	 * {@inheritDoc}
	 * @throws ParseException 
	 */
	@Override
	public void eliminarFestivosNacionales(String idFestivoNacional) {
		Date fechaDelete;

		try {
			fechaDelete = new SimpleDateFormat("yyyy-MM-dd").parse(idFestivoNacional);
		} catch (ParseException e) {
			throw new AplicationException(Constantes.ERROR_GENERAL, e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		festivosNacionalesRepository.findById(fechaDelete).ifPresentOrElse(festivo -> {
			festivosNacionalesRepository.delete(festivo);
		}, () -> {
			throw new AplicationException(Constantes.ERROR_GENERAL, Constantes.REGISTRO_NO_ENCONTRADO,
					HttpStatus.NOT_FOUND);
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<FestivosNacionales> consultarFestivosNacionales() {
		return festivosNacionalesRepository.findAll();
	}
}
