package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.CentroCiudadDTO;
import com.ath.adminefectivo.repositories.ICentroCiudadPpalRepository;
import com.ath.adminefectivo.service.ICentroCiudadPpalService;
import com.querydsl.core.types.Predicate;

/**
 * Clase Servicios para gestionar los CentroCiudadPpal
 * 
 * @author prv_nparra
 */
@Service
public class CentroCiudadPpalServiceImpl implements ICentroCiudadPpalService {

	private final ICentroCiudadPpalRepository centroCiudadPpalRepository;

	public CentroCiudadPpalServiceImpl(@Autowired ICentroCiudadPpalRepository centroCiudadPpalRepository) {
		this.centroCiudadPpalRepository = centroCiudadPpalRepository;
	}

	@Override
	public List<CentroCiudadDTO> listCentroCiudad(Predicate predicate) {
		List<CentroCiudadDTO> listDto = new ArrayList<CentroCiudadDTO>();
		centroCiudadPpalRepository.findAll(predicate)
				.forEach(entity -> listDto.add(CentroCiudadDTO.CONVERTER_DTO_PPAL.apply(entity)));

		return listDto;
	}

}
