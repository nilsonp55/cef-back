package com.ath.adminefectivo.delegate.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.delegate.IPuntosCodigoTdvDelegate;
import com.ath.adminefectivo.dto.PuntosCodigoTdvDTO;
import com.ath.adminefectivo.service.IPuntosCodigoTdvService;
import com.querydsl.core.types.Predicate;

@Service
public class PuntosCodigoTdvDelegateImpl implements IPuntosCodigoTdvDelegate {

	@Autowired
	IPuntosCodigoTdvService puntosCodigoTdvService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PuntosCodigoTdvDTO> getPuntosCodigoTDV(Predicate predicate) {
		
		return puntosCodigoTdvService.getPuntosCodigoTDV(predicate);
	}

}
