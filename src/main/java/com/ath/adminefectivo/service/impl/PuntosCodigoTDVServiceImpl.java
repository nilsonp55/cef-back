package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.PuntosCodigoTdvDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.PuntosCodigoTDV;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.repositories.IPuntosCodigoTDVRepository;
import com.ath.adminefectivo.service.IPuntosCodigoTdvService;
import com.ath.adminefectivo.service.IPuntosService;
import com.querydsl.core.types.Predicate;

@Service
public class PuntosCodigoTDVServiceImpl implements IPuntosCodigoTdvService {

	@Autowired
	IPuntosCodigoTDVRepository puntosCodigoTDVRepository;
	
	@Autowired
	IPuntosService puntosService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PuntosCodigoTdvDTO> getPuntosCodigoTDV(Predicate predicate) {
		
		List<PuntosCodigoTDV> puntosCodigoTDV = (List<PuntosCodigoTDV>) puntosCodigoTDVRepository.findAll(predicate);
		List<PuntosCodigoTdvDTO> listPuntosCodigoTDVDto = new ArrayList<>();
		puntosCodigoTDV.forEach(entity -> listPuntosCodigoTDVDto.add(PuntosCodigoTdvDTO.CONVERTER_DTO.apply(entity)));
		return listPuntosCodigoTDVDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PuntosCodigoTDV getEntidadPuntoCodigoTDV(String codigo) {
		var puntosCodigoTDV = puntosCodigoTDVRepository.findByCodigoTDV(codigo);
		if (Objects.isNull(puntosCodigoTDV)) {
			throw new AplicationException(ApiResponseCode.ERROR_PUNTOS_CODIGO_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_PUNTOS_CODIGO_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_PUNTOS_CODIGO_NO_ENCONTRADO.getHttpStatus());
		} else {
			return puntosCodigoTDV;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getCodigoPunto(String codigoPuntoTdv, String codigoTdv, Integer banco_aval) {
		var puntosCodigoTDV = puntosCodigoTDVRepository.findByCodigoPropioTDVAndCodigoTDV(
				codigoPuntoTdv, codigoTdv);
		if (Objects.isNull(puntosCodigoTDV)) {
			return puntosService.getEntidadPunto(banco_aval).getCodigoPunto();
		} else {
			return puntosCodigoTDV.getCodigoPunto();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PuntosCodigoTdvDTO> getPuntosCodigoTdvAll() {
		List<PuntosCodigoTDV> puntosCodigoTDV = puntosCodigoTDVRepository.findAll();
		List<PuntosCodigoTdvDTO> listPuntosCodigoTDVDto = new ArrayList<>();
		puntosCodigoTDV.forEach(entity -> listPuntosCodigoTDVDto.add(PuntosCodigoTdvDTO.CONVERTER_DTO.apply(entity)));
		return listPuntosCodigoTDVDto;
	}

}
