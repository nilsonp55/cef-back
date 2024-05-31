package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.PuntosCostosDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.PuntosCostos;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.IPuntosCostosRepository;
import com.ath.adminefectivo.service.IPuntosCostosService;
import com.querydsl.core.types.Predicate;

@Service
public class PuntosCostosServiceImpl implements IPuntosCostosService {

	@Autowired
	IPuntosCostosRepository puntosCostosRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PuntosCostosDTO> getPuntosCostos(Predicate predicate) {
		var puntosCostosEntity = puntosCostosRepository.findAll(predicate);
		List<PuntosCostosDTO> puntosCostosDTO = new ArrayList<>();
		puntosCostosEntity.forEach(puntoCosto ->
			puntosCostosDTO.add(PuntosCostosDTO.CONVERTER_DTO.apply(puntoCosto))
		);
		return puntosCostosDTO;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public PuntosCostosDTO getPuntosCostosById(Integer idPuntoCosto) {
		PuntosCostos puntosCostosEntity = puntosCostosRepository.findById(idPuntoCosto).get();
		if(Objects.isNull(puntosCostosEntity)) {
			throw new NegocioException(ApiResponseCode.ERROR_PUNTOS_COSTOS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_PUNTOS_COSTOS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_PUNTOS_COSTOS_NO_ENCONTRADO.getHttpStatus());
		}
		return PuntosCostosDTO.CONVERTER_DTO.apply(puntosCostosEntity);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PuntosCostosDTO guardarPuntosCostos(PuntosCostosDTO puntosCostosDTO) {
		PuntosCostos puntosCostosEntity = PuntosCostosDTO.CONVERTER_ENTITY.apply(puntosCostosDTO);
		return PuntosCostosDTO.CONVERTER_DTO.apply(puntosCostosRepository.save(puntosCostosEntity));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PuntosCostosDTO actualizarPuntosCostos(PuntosCostosDTO puntosCostosDTO) {
		return this.guardarPuntosCostos(puntosCostosDTO);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean eliminarPuntosCostos(Integer idPuntoCosto) {
		PuntosCostos puntosCostosEntity = puntosCostosRepository.findById(idPuntoCosto).get();
		
		puntosCostosEntity.setEstado(Dominios.ESTADO_GENERAL_ELIMINADO);
		PuntosCostos puntosCostosActualizado = puntosCostosRepository.save(puntosCostosEntity);
		
		if(!Objects.isNull(puntosCostosActualizado)) {
			return (puntosCostosActualizado.getEstado() == Dominios.ESTADO_GENERAL_ELIMINADO);			
		}else {
			return false;
		}
	}
	
}
