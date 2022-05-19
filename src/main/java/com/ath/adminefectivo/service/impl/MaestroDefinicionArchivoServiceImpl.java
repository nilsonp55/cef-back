package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.dto.MaestrosDefinicionArchivoDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.MaestroDefinicionArchivoRepository;
import com.ath.adminefectivo.service.IMaestroDefinicionArchivoService;

@Service
public class MaestroDefinicionArchivoServiceImpl implements IMaestroDefinicionArchivoService {

	@Autowired
	MaestroDefinicionArchivoRepository maestrosDefinicionArchivoRepository;
	
	
	@Override
	public MaestrosDefinicionArchivoDTO consultarDefinicionArchivoById(String idMaestroDefinicion) {
		var maestroDefinicion = maestrosDefinicionArchivoRepository.findById(idMaestroDefinicion);

		if (maestroDefinicion.isPresent()) {
			return MaestrosDefinicionArchivoDTO.CONVERTER_DTO.apply(maestroDefinicion.get());
		} else {
			throw new NegocioException(ApiResponseCode.ERROR_MAESTRO_DEFINICION_NO_VALIDO.getCode(),
					ApiResponseCode.ERROR_MAESTRO_DEFINICION_NO_VALIDO.getDescription(),
					ApiResponseCode.ERROR_MAESTRO_DEFINICION_NO_VALIDO.getHttpStatus());
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MaestrosDefinicionArchivoDTO> consultarDefinicionArchivoByAgrupador(String agrupador) {
		var maestrosDefinicion = maestrosDefinicionArchivoRepository.findByAgrupadorAndEstado(agrupador, Constantes.REGISTRO_ACTIVO);

		List<MaestrosDefinicionArchivoDTO> maestrosDefinicionDto = new ArrayList<>();
		maestrosDefinicion.forEach(entity -> maestrosDefinicionDto.add(MaestrosDefinicionArchivoDTO.CONVERTER_DTO.apply(entity)));

		return maestrosDefinicionDto;
	}

}
