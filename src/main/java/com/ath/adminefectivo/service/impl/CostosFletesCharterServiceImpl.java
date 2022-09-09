package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.ParametrosLiquidacionCostoDTO;
import com.ath.adminefectivo.dto.compuestos.costosCharterDTO;
import com.ath.adminefectivo.entities.ParametrosLiquidacionCosto;
import com.ath.adminefectivo.repositories.ICostosFletesCharterRepository;
import com.ath.adminefectivo.service.IBancosService;
import com.ath.adminefectivo.service.ICostosFleteCharterService;
import com.ath.adminefectivo.service.IPuntosService;
import com.ath.adminefectivo.service.ITransportadorasService;
import com.ath.adminefectivo.service.IValoresLiquidadosService;

@Service
public class CostosFletesCharterServiceImpl implements ICostosFleteCharterService{

	@Autowired
	ICostosFletesCharterRepository costosFletesCharterRepository;
	
	@Autowired
	IValoresLiquidadosService valoresLiquidadosService;
	
	@Autowired
	IBancosService bancosService;
	
	@Autowired
	ITransportadorasService transportadorasService;
	
	@Autowired
	IPuntosService puntosService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ParametrosLiquidacionCostoDTO> ConsultarCostosFleteCharter(Date fechaInicial, Date fechaFinal) {

		String escala = Dominios.COSTO_FLETE_CHARTER;
		List<ParametrosLiquidacionCostoDTO> listCostosCharter = new ArrayList<>();

		List<ParametrosLiquidacionCosto> costoCharter = costosFletesCharterRepository
				.findByEscalaAndFechaEjecucionBetween(escala, fechaInicial, fechaFinal);
		for (ParametrosLiquidacionCosto parametros : costoCharter) {
			ParametrosLiquidacionCostoDTO parametrosDTO = new ParametrosLiquidacionCostoDTO();
			parametrosDTO = ParametrosLiquidacionCostoDTO.CONVERTER_DTO.apply(parametros);
			parametrosDTO.setNombreBanco(bancosService.getAbreviatura(parametros.getCodigoBanco()));
			parametrosDTO.setNombreTdv(transportadorasService.getNombreTransportadora(
					parametros.getCodigoTdv()));
			parametrosDTO.setNombrePuntoOrigen(puntosService.getNombrePunto(
					parametros.getTipoPunto(), parametros.getPuntoOrigen()));
			parametrosDTO.setNombrePuntoDestino(puntosService.getNombrePunto(
					parametros.getTipoPunto(), parametros.getPuntoDestino()));
			listCostosCharter.add(parametrosDTO);
		}
		return listCostosCharter;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean GrabarCostosFleteCharter(List<costosCharterDTO> costosCharter) {
		
		for (costosCharterDTO costos : costosCharter) {
			
			valoresLiquidadosService.ActualizaCostosFletesCharter(costos);
		}
		return true;
	}

	

}
