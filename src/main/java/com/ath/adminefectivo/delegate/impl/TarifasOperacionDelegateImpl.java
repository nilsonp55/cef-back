package com.ath.adminefectivo.delegate.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.delegate.IFuncionesDinamicasDelegate;
import com.ath.adminefectivo.delegate.ITarifasOperacionDelegate;
import com.ath.adminefectivo.dto.FuncionesDinamicasDTO;
import com.ath.adminefectivo.dto.TarifasOperacionDTO;
import com.ath.adminefectivo.service.IFuncionesDinamicasService;
import com.querydsl.core.types.Predicate;

@Service
public class TarifasOperacionDelegateImpl implements ITarifasOperacionDelegate{

	@Override
	public TarifasOperacionDTO eliminarTarifasOperacion(Integer idTarifaOperacion) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TarifasOperacionDTO actualizarTarifasOperacion(TarifasOperacionDTO tarifasOperacionDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TarifasOperacionDTO getTarifasOperacionById(Integer idTarifaOperacion) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TarifasOperacionDTO guardarTarifasOperacion(TarifasOperacionDTO tarifasOperacionDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TarifasOperacionDTO> getTarifasOperacion(Predicate predicate) {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * {@inheritDoc}
	 */

	
}
