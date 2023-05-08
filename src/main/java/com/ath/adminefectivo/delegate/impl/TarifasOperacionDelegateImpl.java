package com.ath.adminefectivo.delegate.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.delegate.ITarifasOperacionDelegate;
import com.ath.adminefectivo.dto.TarifasOperacionDTO;
import com.ath.adminefectivo.service.ITarifasOperacionService;
import com.querydsl.core.types.Predicate;

@Service
public class TarifasOperacionDelegateImpl implements ITarifasOperacionDelegate{

	@Autowired
	ITarifasOperacionService tarifasOperacionService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<TarifasOperacionDTO> getTarifasOperacion(Predicate predicate, Pageable pageable) {
		return tarifasOperacionService.getTarifasOperacion(predicate, pageable);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TarifasOperacionDTO getTarifasOperacionById(Integer idTarifaOperacion) {
		return tarifasOperacionService.getTarifasOperacionById(idTarifaOperacion);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TarifasOperacionDTO guardarTarifasOperacion(TarifasOperacionDTO tarifasOperacionDTO) {
		return tarifasOperacionService.guardarTarifasOperacion(tarifasOperacionDTO);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TarifasOperacionDTO actualizarTarifasOperacion(TarifasOperacionDTO tarifasOperacionDTO) {
		return tarifasOperacionService.actualizarTarifasOperacion(tarifasOperacionDTO);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean eliminarTarifasOperacion(Integer idTarifaOperacion) {
		return tarifasOperacionService.eliminarTarifasOperacion(idTarifaOperacion);
	}




	
}
