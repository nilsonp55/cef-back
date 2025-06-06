package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.FondosDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.Fondos;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.IFondosRepository;
import com.ath.adminefectivo.repositories.jdbc.IFondosJdbcRepository;
import com.ath.adminefectivo.service.IFondosService;
import com.ath.adminefectivo.service.IPuntosCodigoTdvService;
import com.querydsl.core.types.Predicate;

@Service
public class FondosServiceImpl implements IFondosService {

	@Autowired
	IFondosRepository fondosRepository;

	@Autowired
	IPuntosCodigoTdvService puntosCodigoTdvService;
	
	@Autowired
	IFondosJdbcRepository fondosJdbcRepository;
	
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
					ApiResponseCode.ERROR_FONDOS_NO_ENCONTRADO.getDescription() + codigoFondo,
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
		if (Objects.isNull(fondo)) {
			estado = false;
		}
		return estado;
	}

	@Override
	public Fondos getCodigoFondoCertificacion(String codigoTransportadora, String numeroNit, String codigoCiudad){
		try {
			return fondosRepository.obtenerCodigoFondoTDV2(codigoTransportadora, numeroNit, codigoCiudad);
		} catch (Exception e) {
			throw new NegocioException(ApiResponseCode.ERROR_MAS_DE_UN_FONDO.getCode(),
					ApiResponseCode.ERROR_MAS_DE_UN_FONDO.getDescription()+ " con codigoTransportadora = "+codigoTransportadora+", numeroNit = "+numeroNit+", codigoCiudad = "+codigoCiudad,
					ApiResponseCode.ERROR_MAS_DE_UN_FONDO.getHttpStatus());
		} 
	}
	
	@Override
	public Fondos getCodigoFondoCertificacionJdbc(String codigoTransportadora, String numeroNit, String codigoCiudad){
		try {
			return fondosJdbcRepository.findFondoByTransportadoraAndNitAndCiudad(codigoTransportadora, numeroNit, codigoCiudad);
		} catch (Exception e) {
			throw new NegocioException(ApiResponseCode.ERROR_MAS_DE_UN_FONDO.getCode(),
					ApiResponseCode.ERROR_MAS_DE_UN_FONDO.getDescription(), ApiResponseCode.ERROR_MAS_DE_UN_FONDO.getHttpStatus());
		} 
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public FondosDTO getFondoByCodigoPunto(Integer codigoPunto) {
		var fondo = fondosRepository.findByCodigoPunto(codigoPunto);
		if (Objects.isNull(fondo)) {
			throw new NegocioException(ApiResponseCode.ERROR_FONDOS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_FONDOS_NO_ENCONTRADO.getDescription()+ codigoPunto,
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
					ApiResponseCode.ERROR_FONDOS_NO_ENCONTRADO.getDescription()+ codigoPunto,
					ApiResponseCode.ERROR_FONDOS_NO_ENCONTRADO.getHttpStatus());

		}
		return fondo;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<FondosDTO> getFondoByTdvAndBanco(String transportadora, int banco) {
		List<FondosDTO> fondosDTO = new ArrayList<>();
		List<Fondos> fondosEntity = fondosRepository.findByTdvAndBancoAVAL(transportadora, banco);
		if (Objects.isNull(fondosEntity)) {
			throw new NegocioException(ApiResponseCode.ERROR_FONDOS_NO_ENCONTRADO_TDV_AVAL.getCode(),
					ApiResponseCode.ERROR_FONDOS_NO_ENCONTRADO_TDV_AVAL.getDescription()+" TRANSPORTADORA = "+transportadora+" Banco Aval = "+banco,
					ApiResponseCode.ERROR_FONDOS_NO_ENCONTRADO_TDV_AVAL.getHttpStatus());

		}else {
			fondosEntity.forEach(fondo ->
				fondosDTO.add(FondosDTO.CONVERTER_DTO.apply(fondo))
			);
		}
		return fondosDTO;
	}
}
