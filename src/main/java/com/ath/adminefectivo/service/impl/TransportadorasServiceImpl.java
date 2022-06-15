package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.TransportadorasDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.repositories.ITransportadorasRepository;
import com.ath.adminefectivo.service.ITransportadorasService;
import com.querydsl.core.types.Predicate;

@Service
public class TransportadorasServiceImpl implements ITransportadorasService {

	@Autowired
	ITransportadorasRepository transportadorasRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TransportadorasDTO> getTransportadoras(Predicate predicate) {
		
		var transportadoras = transportadorasRepository.findAll(predicate);
		List<TransportadorasDTO> listTransportadorasDto = new ArrayList<>();
		transportadoras.forEach(entity -> listTransportadorasDto.add(TransportadorasDTO.CONVERTER_DTO.apply(entity)));
		return listTransportadorasDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getNombreTransportadora(String codigo) {
		
		var transportadora = transportadorasRepository.findById(codigo);
		if (transportadora.isPresent() && Objects.nonNull(transportadora.get().getCodigo())) {
			return transportadora.get().getNombreTransportadora();
		} else {
			throw new AplicationException(ApiResponseCode.ERROR_TRANSPORTADORAS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_TRANSPORTADORAS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_TRANSPORTADORAS_NO_ENCONTRADO.getHttpStatus());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getcodigoTransportadora(String nombre) {
		var transportadora = transportadorasRepository.findByNombreTransportadora(nombre);
		if (transportadora == null) {
			throw new AplicationException(ApiResponseCode.ERROR_TRANSPORTADORAS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_TRANSPORTADORAS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_TRANSPORTADORAS_NO_ENCONTRADO.getHttpStatus());
		} else {
			return transportadora.getCodigo();
		}
	}
}
