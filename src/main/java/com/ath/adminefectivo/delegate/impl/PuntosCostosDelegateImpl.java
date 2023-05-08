package com.ath.adminefectivo.delegate.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.delegate.IPuntosCostosDelegate;
import com.ath.adminefectivo.dto.PuntosCostosDTO;
import com.ath.adminefectivo.service.IPuntosCostosService;
import com.querydsl.core.types.Predicate;

@Service
public class PuntosCostosDelegateImpl implements IPuntosCostosDelegate{

	@Autowired
	IPuntosCostosService puntosCostosService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PuntosCostosDTO> getPuntosCostos(Predicate predicate) {
		return puntosCostosService.getPuntosCostos(predicate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PuntosCostosDTO getPuntosCostosById(Integer idPuntosCostos) {
		return puntosCostosService.getPuntosCostosById(idPuntosCostos);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public PuntosCostosDTO guardarPuntosCostos(PuntosCostosDTO puntosCostosDTO) {
		return puntosCostosService.guardarPuntosCostos(puntosCostosDTO);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public PuntosCostosDTO actualizarPuntosCostos(PuntosCostosDTO puntosCostosDTO) {
		return puntosCostosService.actualizarPuntosCostos(puntosCostosDTO);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean eliminarPuntosCostos(Integer idPuntosCostos) {
		return puntosCostosService.eliminarPuntosCostos(idPuntosCostos);
	}
	
}
