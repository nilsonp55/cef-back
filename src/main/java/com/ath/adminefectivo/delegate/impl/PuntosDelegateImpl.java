package com.ath.adminefectivo.delegate.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.delegate.IPuntosDelegate;
import com.ath.adminefectivo.dto.PuntosDTO;
import com.ath.adminefectivo.service.IPuntosService;
import com.querydsl.core.types.Predicate;

@Service
public class PuntosDelegateImpl implements IPuntosDelegate{

	@Autowired
	IPuntosService puntosService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PuntosDTO> getPuntos(Predicate predicate) {
		return puntosService.getPuntos(predicate);
	}
}
