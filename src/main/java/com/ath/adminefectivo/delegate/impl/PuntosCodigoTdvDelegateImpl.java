package com.ath.adminefectivo.delegate.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.delegate.IPuntosCodigoTdvDelegate;
import com.ath.adminefectivo.dto.PuntosCodigoTdvDTO;
import com.ath.adminefectivo.dto.TarifasOperacionDTO;
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
