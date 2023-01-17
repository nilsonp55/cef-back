package com.ath.adminefectivo.delegate.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	public Page<PuntosCodigoTdvDTO> getPuntosCodigoTDV(Predicate predicate, Pageable page) {
		
		return puntosCodigoTdvService.getPuntosCodigoTDV(predicate, page);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public PuntosCodigoTdvDTO getPuntosCodigoTdvById(Integer idPuntoCodigoTdv) {
		return puntosCodigoTdvService.getPuntosCodigoTdvById(idPuntoCodigoTdv);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public PuntosCodigoTdvDTO guardarPuntosCodigoTdv(PuntosCodigoTdvDTO puntosCodigoTdvDTO) {
		return puntosCodigoTdvService.guardarPuntosCodigoTdv(puntosCodigoTdvDTO);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public PuntosCodigoTdvDTO actualizarPuntosCodigoTdv(PuntosCodigoTdvDTO puntosCodigoTdvDTO) {
		return puntosCodigoTdvService.actualizarPuntosCodigoTdv(puntosCodigoTdvDTO);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean eliminarPuntosCodigoTdv(Integer idPuntoCodigoTdv) {
		return puntosCodigoTdvService.eliminarPuntosCodigoTdv(idPuntoCodigoTdv);
	}

}
