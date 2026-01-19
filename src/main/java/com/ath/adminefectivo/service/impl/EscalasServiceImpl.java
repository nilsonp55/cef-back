package com.ath.adminefectivo.service.impl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.EscalasDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.Escalas;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.IEscalasRepository;
import com.ath.adminefectivo.service.IEscalasService;
import com.querydsl.core.types.Predicate;

@Service
public class EscalasServiceImpl implements IEscalasService {

	@Autowired
	IEscalasRepository escalasRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<EscalasDTO> getEscalas(Predicate predicate, Pageable pageable) {

		Page<Escalas> escala = escalasRepository.findAll(predicate, pageable);
		
		return new PageImpl<>(escala.getContent().stream().map(EscalasDTO
		.CONVERTER_DTO).toList(), escala.getPageable(), escala.getTotalElements());


		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public EscalasDTO getEscalasById(Integer idEscalas) {
		Escalas escalasEntity = escalasRepository.findById(Long.valueOf(idEscalas)).get();
		if(Objects.isNull(escalasEntity)) {
			throw new NegocioException(ApiResponseCode.ERROR_ESCALAS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_ESCALAS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_ESCALAS_NO_ENCONTRADO.getHttpStatus());
		}
		return EscalasDTO.CONVERTER_DTO.apply(escalasEntity);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EscalasDTO guardarEscalas(EscalasDTO escalasDTO) {
		Escalas escalasEntity = EscalasDTO.CONVERTER_ENTITY.apply(escalasDTO);
		return EscalasDTO.CONVERTER_DTO.apply(escalasRepository.save(escalasEntity));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EscalasDTO actualizarEscalas(EscalasDTO escalasDTO) {
		return this.guardarEscalas(escalasDTO);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean eliminarEscalas(Integer idEscalas) {
		Escalas escalasEntity = escalasRepository.findById(Long.valueOf(idEscalas)).get();
		
		escalasEntity.setEstado(Dominios.ESTADO_GENERAL_ELIMINADO);
		Escalas escalasActualizado = escalasRepository.save(escalasEntity);
		
		return (escalasActualizado.getEstado() == Dominios.ESTADO_GENERAL_ELIMINADO);
	}
	
}
