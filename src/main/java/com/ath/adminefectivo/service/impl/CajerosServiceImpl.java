package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.CajerosDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.ICajerosRepository;
import com.ath.adminefectivo.repositories.jdbc.ICajerosJdbcRepository;
import com.ath.adminefectivo.service.ICajerosService;
import com.querydsl.core.types.Predicate;

@Service
public class CajerosServiceImpl implements ICajerosService{

	@Autowired
	ICajerosRepository cajerosRepository;
	
	@Autowired
	ICajerosJdbcRepository cajerosJdbcRepository;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CajerosDTO> getCajeros(Predicate predicate) {
		var cajeros = cajerosRepository.findAll(predicate);
		List<CajerosDTO> listCajerosDto = new ArrayList<>();
		cajeros.forEach(entity -> {var cajerosDTO = new CajerosDTO();
									cajerosDTO = CajerosDTO.CONVERTER_DTO.apply(entity);
									listCajerosDto.add(cajerosDTO);
									});
		return listCajerosDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getCodigoPunto(String codigoCajero) {
		var cajeros = cajerosRepository.findByCodigoATM(codigoCajero);
		if (Objects.isNull(cajeros)) {
			throw new NegocioException(ApiResponseCode.ERROR_CAJEROS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_CAJEROS_NO_ENCONTRADO.getDescription()+ " Cajero no encontrado con codigo = "+codigoCajero,
					ApiResponseCode.ERROR_CAJEROS_NO_ENCONTRADO.getHttpStatus());
		} else {
			return cajeros.getCodigoPunto();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean getCodigoPuntoCajero(Integer codigoPunto) {
		Boolean estado = true;
		var cajeros = cajerosRepository.findByCodigoPunto(codigoPunto);
		if (Objects.isNull(cajeros)) {
			estado = false;
		}
		return estado;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean getCodigoPuntoCajeroJdbc(Integer codigoPunto) {		
		return cajerosJdbcRepository.existsByCodigoPunto(codigoPunto);	}

}
