package com.ath.adminefectivo.delegate.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.delegate.ICiudadesDelegate;
import com.ath.adminefectivo.dto.CiudadesDTO;
import com.ath.adminefectivo.service.ICiudadesService;
import com.querydsl.core.types.Predicate;

@Service
public class CiudadesDelegateImpl implements ICiudadesDelegate {

	@Autowired
	ICiudadesService ciudadesService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CiudadesDTO> getCiudades(Predicate predicate) {
		return ciudadesService.getCiudades(predicate);
	}
}
