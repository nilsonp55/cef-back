package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.CiudadesDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.repositories.ICiudadesRepository;
import com.ath.adminefectivo.service.ICiudadesService;
import com.querydsl.core.types.Predicate;

@Service
public class CiudadServiceImpl implements ICiudadesService{

	@Autowired
	ICiudadesRepository ciudadesRepository;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CiudadesDTO> getCiudades(Predicate predicate) {
		var ciudades = ciudadesRepository.findAll(predicate);
		List<CiudadesDTO> listCiudadesDto = new ArrayList<>();
		ciudades.forEach(entity -> listCiudadesDto.add(CiudadesDTO.CONVERTER_DTO.apply(entity)));
		return listCiudadesDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getNombreCiudad(String codigoCiudad) {
		
		var ciudadOpt = ciudadesRepository.findById(codigoCiudad);
		if (ciudadOpt.isPresent() && Objects.nonNull(ciudadOpt.get().getNombreCiudad())) {
			return ciudadOpt.get().getNombreCiudad();
		} else {
			throw new AplicationException(ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getHttpStatus());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCodigoCiudad(String nombre) {
		var ciudadOpt = ciudadesRepository.findByNombreCiudad(nombre);
		if (Objects.isNull(ciudadOpt)) {
			throw new AplicationException(ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getHttpStatus());
		} else {
			return ciudadOpt.getCodigoDANE();
		}
	}
}
