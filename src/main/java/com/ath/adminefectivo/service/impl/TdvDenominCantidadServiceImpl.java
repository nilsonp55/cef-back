package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.TdvDenominCantidadDTO;

import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.TdvDenominCantidad;
import com.ath.adminefectivo.exception.ConflictException;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.ITdvDenominCantidadRepository;
import com.ath.adminefectivo.service.ITdvDenominCantidadService;
import com.querydsl.core.types.Predicate;

@Service
public class TdvDenominCantidadServiceImpl implements ITdvDenominCantidadService {

	@Autowired
	ITdvDenominCantidadRepository tdvDenominCantidadRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TdvDenominCantidadDTO> getTdvDenominCantidad(Predicate predicate) {
		var tdvDenominCantidadEntity = tdvDenominCantidadRepository.findAll(predicate);
		List<TdvDenominCantidadDTO> tdvDenominCantidadDTO = new ArrayList<>();
		tdvDenominCantidadEntity.forEach(escala ->{
			tdvDenominCantidadDTO.add(TdvDenominCantidadDTO.CONVERTER_DTO.apply(escala));
		});
		return tdvDenominCantidadDTO;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TdvDenominCantidadDTO getTdvDenominCantidadById(Integer idTdvDenominCantidad) {
		TdvDenominCantidad tdvDenominCantidadEntity = tdvDenominCantidadRepository.findById(idTdvDenominCantidad).get();
		if(Objects.isNull(tdvDenominCantidadEntity)) {
			throw new NegocioException(ApiResponseCode.ERROR_ESCALAS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_ESCALAS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_ESCALAS_NO_ENCONTRADO.getHttpStatus());
		}
		return TdvDenominCantidadDTO.CONVERTER_DTO.apply(tdvDenominCantidadEntity);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TdvDenominCantidadDTO guardarTdvDenominCantidad(TdvDenominCantidadDTO tdvDenominCantidadDTO) {
		if (tdvDenominCantidadDTO.getIdTdvDenCant() != null && tdvDenominCantidadRepository
				.existsById(tdvDenominCantidadDTO.getIdTdvDenCant())) {		
			throw new ConflictException(ApiResponseCode.ERROR_DENOMINACION_CANTIDAD_EXIST.getDescription());		
		}
		
		TdvDenominCantidad tdvDenominCantidadEntity = TdvDenominCantidadDTO.CONVERTER_ENTITY.
				apply(tdvDenominCantidadDTO);
		return TdvDenominCantidadDTO.CONVERTER_DTO.apply(tdvDenominCantidadRepository.
				save(tdvDenominCantidadEntity));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TdvDenominCantidadDTO actualizarTdvDenominCantidad(TdvDenominCantidadDTO tdvDenominCantidadDTO) {
		if (tdvDenominCantidadDTO.getIdTdvDenCant() == null && !tdvDenominCantidadRepository
				.existsById(tdvDenominCantidadDTO.getIdTdvDenCant())) {		
			throw new ConflictException(ApiResponseCode.ERROR_DENOMINACION_CANTIDAD_NO_EXIST.getDescription());		
		}

		TdvDenominCantidad tdvDenominCantidadEntity = TdvDenominCantidadDTO.CONVERTER_ENTITY.
				apply(tdvDenominCantidadDTO);
		return TdvDenominCantidadDTO.CONVERTER_DTO.apply(tdvDenominCantidadRepository.
				save(tdvDenominCantidadEntity));	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean eliminarTdvDenominCantidad(Integer idTdvDenominCantidad) {
		TdvDenominCantidad tdvDenominCantidadEntity = tdvDenominCantidadRepository.
				findById(idTdvDenominCantidad).get();
		
		tdvDenominCantidadEntity.setEstado(Dominios.ESTADO_GENERAL_ELIMINADO);
		TdvDenominCantidad tdvDenominCantidadActualizado = tdvDenominCantidadRepository.save(tdvDenominCantidadEntity);
		
		if(!Objects.isNull(tdvDenominCantidadActualizado)) {
			if(tdvDenominCantidadActualizado.getEstado() == Dominios.ESTADO_GENERAL_ELIMINADO) {
				return true;
			}else {
				return false;
			}
			
		}else {
			return false;
		}
	}
	
}
