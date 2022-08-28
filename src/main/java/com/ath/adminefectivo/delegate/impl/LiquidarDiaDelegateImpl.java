package com.ath.adminefectivo.delegate.impl;

import org.springframework.stereotype.Service;

import com.ath.adminefectivo.delegate.ILiquidarDiaDelegate;
import com.ath.adminefectivo.service.ILiquidarDiaService;

@Service
public class LiquidarDiaDelegateImpl implements ILiquidarDiaDelegate {

	ILiquidarDiaService liquidarDiaService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean procesarLiquidacionCostos() {
		liquidarDiaService.ProcesarLiquidacionCostos();
		return true;
	}

}
