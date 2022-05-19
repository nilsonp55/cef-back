package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.PuntosDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.Puntos;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.repositories.IPuntosRepository;
import com.ath.adminefectivo.service.IPuntosService;
import com.querydsl.core.types.Predicate;

@Service
public class PuntosServiceImpl implements IPuntosService {

	@Autowired
	IPuntosRepository puntosRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PuntosDTO> getPuntos(Predicate predicate) {
		var puntos = puntosRepository.findAll(predicate);
		List<PuntosDTO> listPuntosDto = new ArrayList<>();
		puntos.forEach(entity -> listPuntosDto.add(PuntosDTO.CONVERTER_DTO.apply(entity)));
		return listPuntosDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Puntos getEntidadPunto(String tipoPunto, Integer codigoPunto) {
		var puntosOpt = puntosRepository.findByCodigoPuntoAndTipoPunto(codigoPunto, tipoPunto);
		if (Objects.isNull(puntosOpt)) {
			throw new AplicationException(ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getHttpStatus());
		} 
		return puntosOpt;

	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getNombrePunto(String tipoPunto, Integer codigoPunto) {
		var puntosOpt = puntosRepository.findByCodigoPuntoAndTipoPunto(codigoPunto, tipoPunto);
		if (Objects.isNull(puntosOpt)) {
			throw new AplicationException(ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getHttpStatus());
		} 
		return puntosOpt.getNombrePunto();
	}
	
}
