package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.FondosDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.Fondos;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.IFondosRepository;
import com.ath.adminefectivo.service.IFondosService;
import com.ath.adminefectivo.service.IPuntosCodigoTdvService;
import com.querydsl.core.types.Predicate;

@Service
public class FondosServiceImpl implements IFondosService {

	@Autowired
	IFondosRepository fondosRepository;

	@Autowired
	IPuntosCodigoTdvService puntosCodigoTdvService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<FondosDTO> getFondos(Predicate predicate) {
		var fondos = fondosRepository.findAll(predicate);
		List<FondosDTO> listFondosDto = new ArrayList<>();
		fondos.forEach(entity -> listFondosDto.add(FondosDTO.CONVERTER_DTO.apply(entity)));
		return listFondosDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Fondos getDatosFondos(String codigoFondo) {
		var fondo = fondosRepository.findByCodigoPunto(
				puntosCodigoTdvService.getEntidadPuntoCodigoTDV(codigoFondo).getCodigoPunto());
		if (Objects.isNull(fondo)) {
			throw new NegocioException(ApiResponseCode.ERROR_FONDOS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_FONDOS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_FONDOS_NO_ENCONTRADO.getHttpStatus());

		}
		return fondo;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Fondos getCodigoFondo(String nombreTransportadora, Integer codigoCompensacion, String codigoCiudad) {
		
		return fondosRepository.obtenerCodigoFondoTDV(
				nombreTransportadora, codigoCompensacion, codigoCiudad);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Fondos getCodigoFondo(String nombreTransportadora, String tipoPuntoBanco, String nombreBanco,
			String nombreCiudad) {
		
		return fondosRepository.obtenerCodigoFondoTDV1(
				nombreTransportadora, tipoPuntoBanco, nombreBanco, nombreCiudad);
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean getCodigoPuntoFondo(Integer codigoPunto) {
		Boolean estado = true;
		var fondo = fondosRepository.findByCodigoPunto(codigoPunto);
		if (fondo == null) {
			estado = false;
		}
		return estado;
	}

	@Override
	public Fondos getCodigoFondoCertificacion(String codigoTransportadora, String tipoPuntoBanco, String nombreBanco,
			String codigoCiudad) {

		return fondosRepository.obtenerCodigoFondoTDV2(
				codigoTransportadora, tipoPuntoBanco, nombreBanco, codigoCiudad);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public FondosDTO getFondoByCodigoPunto(Integer codigoPunto) {
		var fondo = fondosRepository.findByCodigoPunto(codigoPunto);
		if (Objects.isNull(fondo)) {
			throw new NegocioException(ApiResponseCode.ERROR_FONDOS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_FONDOS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_FONDOS_NO_ENCONTRADO.getHttpStatus());

		}
		return FondosDTO.CONVERTER_DTO.apply(fondo);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Fondos getEntidadFondo(Integer codigoPunto) {
		var fondo = fondosRepository.findByCodigoPunto(codigoPunto);
		if (Objects.isNull(fondo)) {
			throw new NegocioException(ApiResponseCode.ERROR_FONDOS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_FONDOS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_FONDOS_NO_ENCONTRADO.getHttpStatus());

		}
		return fondo;
	}
}
