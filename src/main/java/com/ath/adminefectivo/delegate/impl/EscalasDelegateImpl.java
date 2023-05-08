package com.ath.adminefectivo.delegate.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.delegate.IEscalasDelegate;
import com.ath.adminefectivo.dto.EscalasDTO;
import com.ath.adminefectivo.service.IEscalasService;
import com.querydsl.core.types.Predicate;

@Service
public class EscalasDelegateImpl implements IEscalasDelegate{

	@Autowired
	IEscalasService escalasService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<EscalasDTO> getEscalas(Predicate predicate, Pageable pageable) {
		return escalasService.getEscalas(predicate, pageable);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EscalasDTO getEscalasById(Integer idEscalas) {
		return escalasService.getEscalasById(idEscalas);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public EscalasDTO guardarEscalas(EscalasDTO escalasDTO) {
		return escalasService.guardarEscalas(escalasDTO);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public EscalasDTO actualizarEscalas(EscalasDTO escalasDTO) {
		return escalasService.actualizarEscalas(escalasDTO);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean eliminarEscalas(Integer idEscalas) {
		return escalasService.eliminarEscalas(idEscalas);
	}
	
}
