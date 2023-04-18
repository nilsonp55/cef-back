package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.ParametrosLiquidacionCostoDTO;
import com.ath.adminefectivo.dto.ValorLiquidadoDTO;
import com.ath.adminefectivo.dto.compuestos.EstimadoClasificacionCostosDTO;
import com.ath.adminefectivo.entities.ParametrosLiquidacionCosto;
import com.ath.adminefectivo.entities.ValoresLiquidados;
import com.ath.adminefectivo.repositories.IParametrosLiquidacionCostosRepository;
import com.ath.adminefectivo.service.IParametrosLiquidacionCostosService;
import com.ath.adminefectivo.service.IValoresLiquidadosService;

@Service
public class ParametrosLiquidacionCostosServiceImpl implements IParametrosLiquidacionCostosService {

	@Autowired
	IParametrosLiquidacionCostosRepository parametrosLiquidacionCostosRepository;
	
	@Autowired
	IValoresLiquidadosService valoresLiquidadosService;
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EstimadoClasificacionCostosDTO consultaEstimadosCostos(String transportadora, int bancoAval, int mesI,
			int anioI) {
		return parametrosLiquidacionCostosRepository.consultaEstimadosCostos(transportadora, bancoAval, mesI, anioI);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ParametrosLiquidacionCostoDTO> consultarParametrosLiquidacionCostos(Date fechaSistema) {
		List<ParametrosLiquidacionCosto> parametrosLiquidacion = parametrosLiquidacionCostosRepository.findByFechaConcilia(fechaSistema);
		
		List<ParametrosLiquidacionCostoDTO> respuesta = new ArrayList<>();
		
		parametrosLiquidacion.forEach(parametroLiquidacion ->{
			respuesta.add(ParametrosLiquidacionCostoDTO.CONVERTER_DTO.apply(parametroLiquidacion));
		});
		respuesta.forEach(x ->{
			x.setValoresLiquidadosDTO(valoresLiquidadosService.consultarValoresLiquidadosPorIdLiquidacion(x.getIdLiquidacion()));
		});
		
		return respuesta;
	}

	@Override
	public ParametrosLiquidacionCosto getParametrosLiquidacionCostosById(Long idLiquidacion) {
		return parametrosLiquidacionCostosRepository.findById(idLiquidacion).orElse(null); 
	}

	
	
	
}
