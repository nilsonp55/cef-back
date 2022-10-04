package com.ath.adminefectivo.delegate.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.delegate.IEscalasDelegate;
import com.ath.adminefectivo.delegate.ITarifasOperacionDelegate;
import com.ath.adminefectivo.dto.EscalasDTO;
import com.ath.adminefectivo.dto.TarifasOperacionDTO;
import com.ath.adminefectivo.service.IEscalasService;
import com.ath.adminefectivo.service.ITarifasOperacionService;
import com.querydsl.core.types.Predicate;

@Service
public class EscalasDelegateImpl implements IEscalasDelegate{

	@Autowired
	IEscalasService escalasService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<EscalasDTO> getEscalas(Predicate predicate) {
		return escalasService.getEscalas(predicate);
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
