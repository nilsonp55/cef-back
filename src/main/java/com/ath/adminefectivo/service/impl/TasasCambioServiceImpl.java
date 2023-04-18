package com.ath.adminefectivo.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.TasasCambio;
import com.ath.adminefectivo.entities.TasasCambioPK;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.exception.ConflictException;
import com.ath.adminefectivo.repositories.TasasCambioRepository;
import com.ath.adminefectivo.service.TasasCambioService;
import com.querydsl.core.types.Predicate;

@Service
public class TasasCambioServiceImpl implements TasasCambioService {

	@Autowired
	TasasCambioRepository tasasCambioRepository;
	
	
	@Override
	public List<TasasCambio> getTasasCambios(Predicate predicate) {
		var tasasCambios = tasasCambioRepository.findAll(predicate);
		return (List<TasasCambio>) tasasCambios;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TasasCambio getTasasCambioById(TasasCambioPK tasasCambioPK) {
		Optional<TasasCambio> tasa = tasasCambioRepository.findById(tasasCambioPK);
		if (Objects.isNull(tasa)) {
			throw new AplicationException(ApiResponseCode.ERROR_TASAS_CAMBIO_NO_EXIST.getCode(),
					ApiResponseCode.ERROR_TASAS_CAMBIO_NO_EXIST.getDescription(),
					ApiResponseCode.ERROR_TASAS_CAMBIO_NO_EXIST.getHttpStatus());
		} 
		return tasa.get();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TasasCambio postTasasCambio(TasasCambio tasasCambio) {
		TasasCambio tasasCambioResponse = tasasCambioRepository.save(tasasCambio);
		return tasasCambioResponse;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TasasCambio putTasasCambio(TasasCambio tasasCambio) {
		TasasCambio tasasCambioResponse = tasasCambioRepository.save(tasasCambio);
		return tasasCambioResponse;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteTasasCambio(TasasCambio tasasCambio) {
		tasasCambioRepository.delete(tasasCambio);
	}

}

