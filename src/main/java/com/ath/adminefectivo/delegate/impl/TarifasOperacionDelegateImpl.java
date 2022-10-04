package com.ath.adminefectivo.delegate.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	public List<TarifasOperacionDTO> getTarifasOperacion(Predicate predicate) {
		return tarifasOperacionService.getTarifasOperacion(predicate);
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
