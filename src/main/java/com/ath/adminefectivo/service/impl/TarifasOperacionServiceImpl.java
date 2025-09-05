package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.TarifasOperacionDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.TarifasOperacion;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.ITarifasOperacionRepository;
import com.ath.adminefectivo.service.ITarifasOperacionService;
import com.querydsl.core.types.Predicate;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class TarifasOperacionServiceImpl implements ITarifasOperacionService {

	@Autowired
	ITarifasOperacionRepository tarifasOperacionRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<TarifasOperacionDTO> getTarifasOperacion(Predicate predicate, Pageable pageable) {

		Page<TarifasOperacion> tarifaOperacion = tarifasOperacionRepository.findAll(
			    predicate,
			    PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("idTarifasOperacion").ascending())
			);
		
		return new PageImpl<>(tarifaOperacion.getContent().stream().map(TarifasOperacionDTO
		.CONVERTER_DTO).toList(), tarifaOperacion.getPageable(), tarifaOperacion.getTotalElements());

	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TarifasOperacionDTO getTarifasOperacionById(Integer idTarifaOperacion) {
		TarifasOperacion tarifaOperacionEntity = tarifasOperacionRepository.findById(idTarifaOperacion).get();
		if(Objects.isNull(tarifaOperacionEntity)) {
			throw new NegocioException(ApiResponseCode.ERROR_TARIFAS_OPERACION_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_TARIFAS_OPERACION_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_TARIFAS_OPERACION_NO_ENCONTRADO.getHttpStatus());
		}
		return TarifasOperacionDTO.CONVERTER_DTO.apply(tarifaOperacionEntity);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TarifasOperacionDTO guardarTarifasOperacion(TarifasOperacionDTO tarifasOperacionDTO) {
		TarifasOperacion tarifaOperacionEntity = TarifasOperacionDTO.CONVERTER_ENTITY.apply(tarifasOperacionDTO);
		return TarifasOperacionDTO.CONVERTER_DTO.apply(tarifasOperacionRepository.save(tarifaOperacionEntity));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TarifasOperacionDTO actualizarTarifasOperacion(TarifasOperacionDTO tarifasOperacionDTO) {
		return this.guardarTarifasOperacion(tarifasOperacionDTO);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean eliminarTarifasOperacion(Integer idTarifaOperacion) {
		TarifasOperacion tarifaOperacionEntity = tarifasOperacionRepository.findById(idTarifaOperacion).get();
		
		tarifaOperacionEntity.setEstado(Dominios.ESTADO_GENERAL_ELIMINADO);
		tarifasOperacionRepository.save(tarifaOperacionEntity);
		
		return (tarifaOperacionEntity.getEstado() == Dominios.ESTADO_GENERAL_ELIMINADO);
		
	}

	@Override
	public List<TarifasOperacionDTO> getTarifasOperacionByCodigoBancoAndCodigoTdv(int codigoBanco, String codigoTdv,
			Date fechaSistema) {
		log.debug("codigoBanco = "+codigoBanco+" codigoTdv = "+codigoTdv);
		List<TarifasOperacion> tarifasOperacionEntity = tarifasOperacionRepository.findByBancoAndTransportadoraAndComisionAndFajado(codigoBanco, codigoTdv);
		List<TarifasOperacionDTO> tarifasOperacionDTO = new ArrayList<>();
		tarifasOperacionEntity.forEach(tarifaEntity -> 
			tarifasOperacionDTO.add(TarifasOperacionDTO.CONVERTER_DTO.apply(tarifaEntity))
		);
		return tarifasOperacionDTO;
	}
}
