package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.ParametrosLiquidacionCostoDTO;
import com.ath.adminefectivo.dto.compuestos.CostosCharterDTO;
import com.ath.adminefectivo.entities.ParametrosLiquidacionCosto;
import com.ath.adminefectivo.repositories.ICostosFletesCharterRepository;
import com.ath.adminefectivo.service.IBancosService;
import com.ath.adminefectivo.service.ICostosFleteCharterService;
import com.ath.adminefectivo.service.IDominioService;
import com.ath.adminefectivo.service.IPuntosService;
import com.ath.adminefectivo.service.ITransportadorasService;
import com.ath.adminefectivo.service.IValoresLiquidadosService;

@Service
public class CostosFletesCharterServiceImpl implements ICostosFleteCharterService {

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

  @Autowired
  IDominioService dominioService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ParametrosLiquidacionCostoDTO> consultarCostosFleteCharter(Date fechaInicial, Date fechaFinal) {

		String escalaCharter = dominioService.valorTextoDominio(Constantes.DOMINIO_ESCALAS,
				Dominios.ESCALA_AEREO_CHARTER);
		String escalaComercial = dominioService.valorTextoDominio(Constantes.DOMINIO_ESCALAS,
				Dominios.ESCALA_AEREO_COMERCIAL);

		List<ParametrosLiquidacionCostoDTO> listCostosCharter = new ArrayList<>();

		List<ParametrosLiquidacionCosto> costoCharter = costosFletesCharterRepository
				.findByEscalaAndFechaEjecucionBetween(escalaCharter, fechaInicial, fechaFinal);

		costoCharter.addAll(costosFletesCharterRepository.findByEscalaAndFechaEjecucionBetween(escalaComercial,
				fechaInicial, fechaFinal));

		for (ParametrosLiquidacionCosto parametros : costoCharter) {
			ParametrosLiquidacionCostoDTO parametrosDTO = ParametrosLiquidacionCostoDTO.CONVERTER_DTO.apply(parametros);

			parametrosDTO.setNombreBanco(bancosService.getAbreviatura(parametros.getCodigoBanco()));
			parametrosDTO.setNombreTdv(transportadorasService.getNombreTransportadora(parametros.getCodigoTdv()));
			parametrosDTO.setNombreFondo(puntosService.getNombrePunto(parametros.getPuntoOrigen()));
			parametrosDTO.setNombrePunto(puntosService.getNombrePunto(parametros.getPuntoDestino()));
			parametrosDTO.setValoresLiquidadosDTO(
					valoresLiquidadosService.consultarValoresLiquidadosPorIdLiquidacion(parametros.getIdLiquidacion()));

			listCostosCharter.add(parametrosDTO);
		}
		return listCostosCharter;
	}

  /**
   * {@inheritDoc}
   */
  @Override
  public Boolean grabarCostosFleteCharter(CostosCharterDTO costosCharter) {
    valoresLiquidadosService.actualizaCostosFletesCharter(costosCharter);
    return true;
  }



}
