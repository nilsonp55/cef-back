package com.ath.adminefectivo.delegate.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.delegate.ICostosFleteCharterDelegate;
import com.ath.adminefectivo.dto.ParametrosLiquidacionCostoDTO;
import com.ath.adminefectivo.dto.compuestos.costosCharterDTO;
import com.ath.adminefectivo.service.ICostosFleteCharterService;

@Service
public class CostosFleteCharterDelegateImpl implements ICostosFleteCharterDelegate{

	@Autowired
	ICostosFleteCharterService ICostosFleteCharterService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ParametrosLiquidacionCostoDTO> consultarCostosFleteCharter(Date fechaInicial, Date fechaFinal) {

		return ICostosFleteCharterService.ConsultarCostosFleteCharter(fechaInicial, fechaFinal);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean grabarCostosFleteCharter(List<costosCharterDTO> costosCharter) {

		return ICostosFleteCharterService.GrabarCostosFleteCharter(costosCharter);
	}

}
