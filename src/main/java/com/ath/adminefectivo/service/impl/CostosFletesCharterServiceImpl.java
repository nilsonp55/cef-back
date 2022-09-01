package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.ParametrosLiquidacionCostoDTO;
import com.ath.adminefectivo.entities.ParametrosLiquidacionCosto;
import com.ath.adminefectivo.repositories.ICostosFletesCharterRepository;
import com.ath.adminefectivo.service.ICostosFleteCharterService;
import com.ath.adminefectivo.service.IValoresLiquidadosService;

@Service
public class CostosFletesCharterServiceImpl implements ICostosFleteCharterService{

	@Autowired
	ICostosFletesCharterRepository costosFletesCharterRepository;
	
	@Autowired
	IValoresLiquidadosService valoresLiquidadosService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ParametrosLiquidacionCostoDTO> ConsultarCostosFleteCharter(Date fechaInicial, Date fechaFinal) {

		String escala = Dominios.COSTO_FLETE_CHARTER;
		List<ParametrosLiquidacionCostoDTO> listCostosCharter = new ArrayList<>();

		List<ParametrosLiquidacionCosto> costoCharter = costosFletesCharterRepository
				.findByEscalaAndFechaEjecucionBetween(escala, fechaInicial, fechaFinal);
		for (ParametrosLiquidacionCosto parametrosLiquidacionCosto : costoCharter) {
			listCostosCharter.add(ParametrosLiquidacionCostoDTO.CONVERTER_DTO.apply(parametrosLiquidacionCosto));
		}
		return listCostosCharter;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean GrabarCostosFleteCharter(Date fechaInicial, Date fechaFinal) {
		return this.ActualizarCostosFletesCharter(this.ConsultarCostosFleteCharter(fechaInicial, fechaFinal));
	}
	
	/**
	 * Metodo que se encarga de actualizar el costo flete por charter en Valores Liquidados
	 * @param parametrosLiquidacion
	 * @return Boolean
	 * @author prv_ccastano
	 */
	private Boolean ActualizarCostosFletesCharter(List<ParametrosLiquidacionCostoDTO> parametrosLiquidacion) {
		for (ParametrosLiquidacionCostoDTO parametros : parametrosLiquidacion) {
			valoresLiquidadosService.ActualizaCostosFletesCharter(parametros.CONVERTER_ENTITY.apply(parametros));
		}
		return true;
	}

	

}
