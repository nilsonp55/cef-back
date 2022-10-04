package com.ath.adminefectivo.delegate.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.delegate.ITdvDenominCantidadDelegate;
import com.ath.adminefectivo.dto.TdvDenominCantidadDTO;
import com.ath.adminefectivo.service.ITdvDenominCantidadService;
import com.querydsl.core.types.Predicate;

@Service
public class TdvDenominCantidadDelegateImpl implements ITdvDenominCantidadDelegate{

	@Autowired
	ITdvDenominCantidadService tdvDenominCantidadService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TdvDenominCantidadDTO> getTdvDenominCantidad(Predicate predicate) {
		return tdvDenominCantidadService.getTdvDenominCantidad(predicate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TdvDenominCantidadDTO getTdvDenominCantidadById(Integer idTdvDenominCantidad) {
		return tdvDenominCantidadService.getTdvDenominCantidadById(idTdvDenominCantidad);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TdvDenominCantidadDTO guardarTdvDenominCantidad(TdvDenominCantidadDTO tdvDenominCantidadDTO) {
		return tdvDenominCantidadService.guardarTdvDenominCantidad(tdvDenominCantidadDTO);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TdvDenominCantidadDTO actualizarTdvDenominCantidad(TdvDenominCantidadDTO tdvDenominCantidadDTO) {
		return tdvDenominCantidadService.actualizarTdvDenominCantidad(tdvDenominCantidadDTO);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean eliminarTdvDenominCantidad(Integer idTdvDenominCantidad) {
		return tdvDenominCantidadService.eliminarTdvDenominCantidad(idTdvDenominCantidad);
	}
	
}
