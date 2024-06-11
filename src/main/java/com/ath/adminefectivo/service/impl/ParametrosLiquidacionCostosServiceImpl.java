package com.ath.adminefectivo.service.impl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.ParametrosLiquidacionCostoDTO;
import com.ath.adminefectivo.dto.compuestos.EstimadoClasificacionCostosDTO;
import com.ath.adminefectivo.entities.ParametrosLiquidacionCosto;
import com.ath.adminefectivo.repositories.IDetallesLiquidacionCostoRepository;
import com.ath.adminefectivo.repositories.IParametrosLiquidacionCostosRepository;
import com.ath.adminefectivo.repositories.IValoresLiquidadosFlatRepository;
import com.ath.adminefectivo.service.IParametrosLiquidacionCostosService;
import com.ath.adminefectivo.service.IValoresLiquidadosService;

@Service
public class ParametrosLiquidacionCostosServiceImpl implements IParametrosLiquidacionCostosService {

	@Autowired
	IValoresLiquidadosService valoresLiquidadosService;

	@Autowired
	IParametrosLiquidacionCostosRepository parametrosLiquidacionCostosRepository;
	
	@Autowired
	IValoresLiquidadosFlatRepository valoresLiquidadosFlatRepository;

	@Autowired
	IDetallesLiquidacionCostoRepository detallesLiquidacionCostoRepository;
	

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
		
		parametrosLiquidacion.forEach(parametroLiquidacion ->
			respuesta.add(ParametrosLiquidacionCostoDTO.CONVERTER_DTO.apply(parametroLiquidacion))
		);
		respuesta.forEach(x ->
			x.setValoresLiquidadosDTO(valoresLiquidadosService.consultarValoresLiquidadosPorIdLiquidacion(x.getIdLiquidacion()))
		);
		
		return respuesta;
	}

	@Override
	public Optional<ParametrosLiquidacionCosto> getParametrosLiquidacionCostosById(Long idLiquidacion) {
		return parametrosLiquidacionCostosRepository.findById(idLiquidacion);
	}

	@Override
	public ParametrosLiquidacionCosto f2eliminarParametrosLiquidacionCostos(ParametrosLiquidacionCosto eliminar) {
		
		var valorLiq =  valoresLiquidadosFlatRepository.consultarPorIdLiquidacion(eliminar.getIdLiquidacion());
		if (Objects.nonNull(valorLiq))
		{
			valoresLiquidadosFlatRepository.delete(valorLiq);
		}
		
		var detallesLiq =  detallesLiquidacionCostoRepository.consultarPorIdLiquidacion(eliminar.getIdLiquidacion());
		if (Objects.nonNull(detallesLiq) && !detallesLiq.isEmpty())
		{
			detallesLiquidacionCostoRepository.deleteAll(detallesLiq);
		}
		
		parametrosLiquidacionCostosRepository.delete(eliminar);
		
		return eliminar;
		
	}

	@Override
	public ParametrosLiquidacionCosto f2actualizarParametrosLiquidacionCostos(ParametrosLiquidacionCosto actualizar) {
		
		return parametrosLiquidacionCostosRepository.save(actualizar);
		
	}

	
}
