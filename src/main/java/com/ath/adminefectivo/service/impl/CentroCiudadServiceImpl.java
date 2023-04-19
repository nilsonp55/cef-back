package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.CentroCiudadDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.CentroCiudad;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.exception.ConflictException;
import com.ath.adminefectivo.repositories.ICentroCiudadRepository;
import com.ath.adminefectivo.service.ICentroCiudadService;
import com.querydsl.core.types.Predicate;

/**
 * Servicios para gestionar los CentroCiudad
 * @author cesar.castano
 */

@Service
public class CentroCiudadServiceImpl implements ICentroCiudadService {

	@Autowired
	ICentroCiudadRepository centroCiudadRepository;
	
	
	@Override
	public List<CentroCiudadDTO> getCentrosCiudades(Predicate predicate) {
		var cuentas = centroCiudadRepository.findAll(predicate);
		List<CentroCiudadDTO> listcentrosDto = new ArrayList<>();
		cuentas.forEach(entity -> listcentrosDto.add(CentroCiudadDTO.CONVERTER_DTO.apply(entity)));
		return listcentrosDto;
	}

	@Override
	public CentroCiudadDTO getCentroCiudadById(Integer idCentroCiudad) {
		var cuentas = centroCiudadRepository.findById(idCentroCiudad);
		if (Objects.isNull(cuentas)) {
			throw new AplicationException(ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getDescription()+ " no encontrado para Centro Ciudad = "+idCentroCiudad,
					ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getHttpStatus());
		} 
		return CentroCiudadDTO.CONVERTER_DTO.apply(cuentas.get());
	}

	@Override
	public CentroCiudadDTO postCentroCiudad(CentroCiudadDTO centroCiudadDTO) {
		if (centroCiudadDTO.getBancoAval().getCodigoPunto() == null && 
				centroCiudadDTO.getCiudadDane().getCodigoDANE() == null) {		
			throw new ConflictException(ApiResponseCode.ID_NOT_NULL.getDescription());		
		}
		CentroCiudad entity = CentroCiudadDTO.CONVERTER_ENTITY.apply(centroCiudadDTO);
		
		CentroCiudad tipoCuentas = centroCiudadRepository.save(entity);
		
		return CentroCiudadDTO.CONVERTER_DTO.apply(tipoCuentas);
	}

	@Override
	public CentroCiudadDTO putCentroCiudad(CentroCiudadDTO centroCiudadDTO) {
		if (centroCiudadDTO.getBancoAval().getCodigoPunto() == null && centroCiudadDTO.getCiudadDane()
				.getCodigoDANE() == null) {		
			throw new ConflictException(ApiResponseCode.ERROR_TIPOS_CUENTAS_NO_EXIST.getDescription());		
		}
		CentroCiudad tipoCuentas = centroCiudadRepository.save(CentroCiudadDTO.CONVERTER_ENTITY
				.apply(centroCiudadDTO));
		
		return CentroCiudadDTO.CONVERTER_DTO.apply(tipoCuentas);
	}

}
