package com.ath.adminefectivo.service.impl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ath.adminefectivo.dto.ParametrosLiquidacionCostoDTO;
import com.ath.adminefectivo.dto.compuestos.EstimadoClasificacionCostosDTO;
import com.ath.adminefectivo.entities.EstadoConciliacionParametrosLiquidacion;
import com.ath.adminefectivo.entities.ParametrosLiquidacionCosto;
import com.ath.adminefectivo.entities.ParametrosLiquidacionCostoFlat;
import com.ath.adminefectivo.repositories.IDetallesLiquidacionCostoRepository;
import com.ath.adminefectivo.repositories.IParametrosLiquidacionCostosRepository;
import com.ath.adminefectivo.repositories.IParametrosLiquidacionCostosRepositoryFlat;
import com.ath.adminefectivo.repositories.IValoresLiquidadosFlatRepository;
import com.ath.adminefectivo.service.IEstadoConciliacionParametrosLiquidacionService;
import com.ath.adminefectivo.service.IParametrosLiquidacionCostosService;
import com.ath.adminefectivo.service.IValoresLiquidadosService;

@Service
public class ParametrosLiquidacionCostosServiceImpl implements IParametrosLiquidacionCostosService {

	@Autowired
	IValoresLiquidadosService valoresLiquidadosService;

	@Autowired
	IParametrosLiquidacionCostosRepository parametrosLiquidacionCostosRepository;

	@Autowired
	IParametrosLiquidacionCostosRepositoryFlat parametrosLiquidacionCostosRepositoryFlat;
	
	@Autowired
	IValoresLiquidadosFlatRepository valoresLiquidadosFlatRepository;

	@Autowired
	IDetallesLiquidacionCostoRepository detallesLiquidacionCostoRepository;
	
	@Autowired
	IEstadoConciliacionParametrosLiquidacionService estadoConciliacionParametrosLiquidacionService;
	

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
	public ParametrosLiquidacionCostoFlat getParametrosLiquidacionCostosByIdFlat(Long idLiquidacion) {
		return parametrosLiquidacionCostosRepositoryFlat.findById(idLiquidacion).orElse(null); 
	}
	
	@Transactional
	@Override
	public ParametrosLiquidacionCostoFlat f2eliminarParametrosLiquidacionCostos(
	        ParametrosLiquidacionCostoFlat eliminar, 
	        EstadoConciliacionParametrosLiquidacion estadoConciliacionLiqTransporte) {

	    // Guarda el estado de cómo estaba el parámetro de liquidación antes del cambio
	    estadoConciliacionParametrosLiquidacionService.save(estadoConciliacionLiqTransporte);
	    
	 // Consultar y eliminar `DetallesLiquidacionCosto`
	    var detallesLiq = detallesLiquidacionCostoRepository.consultarPorIdLiquidacion(eliminar.getIdLiquidacionFlat());
	    if (Objects.nonNull(detallesLiq) && !detallesLiq.isEmpty()) {
	        // Sincronización antes de la eliminación en batch
	        detallesLiquidacionCostoRepository.deleteAll(detallesLiq);
	        detallesLiquidacionCostoRepository.flush();
	    }
	    
	    // Consultar y desvincular `ValoresLiquidadosFlat`
	    var valorLiq = valoresLiquidadosFlatRepository.consultarPorIdLiquidacion(eliminar.getIdLiquidacionFlat());
	    if (Objects.nonNull(valorLiq)) {
	        // Desvincular la relación antes de eliminar
	        if (Objects.nonNull(valorLiq.getParametrosLiquidacionCostoFlat())) {
	            valorLiq.setParametrosLiquidacionCostoFlat(null);

	            // Guardar y sincronizar
	            valoresLiquidadosFlatRepository.save(valorLiq);
	            valoresLiquidadosFlatRepository.flush(); // sincronizar para actualizar referencia en DB
	        }

	        // Proceder con la eliminación de `valorLiq`
	        valoresLiquidadosFlatRepository.delete(valorLiq);
	    }
	    
	    // Finalmente, eliminar el objeto `ParametrosLiquidacionCostoFlat`
	    if (parametrosLiquidacionCostosRepositoryFlat.existsById(eliminar.getIdLiquidacionFlat())) {
	        parametrosLiquidacionCostosRepositoryFlat.delete(eliminar);
	    }

	    return eliminar;
	}
	
	@Override
	public ParametrosLiquidacionCostoFlat f2eliminarParametrosLiquidacionCostos(ParametrosLiquidacionCostoFlat eliminar) {
				
		// Consultar y eliminar `DetallesLiquidacionCosto`
	    var detallesLiq = detallesLiquidacionCostoRepository.consultarPorIdLiquidacion(eliminar.getIdLiquidacionFlat());
	    if (Objects.nonNull(detallesLiq) && !detallesLiq.isEmpty()) {
	        // Sincronización antes de la eliminación en batch
	        detallesLiquidacionCostoRepository.deleteAll(detallesLiq);
	        detallesLiquidacionCostoRepository.flush();
	    }
	    
	    // Consultar y desvincular `ValoresLiquidadosFlat`
	    var valorLiq = valoresLiquidadosFlatRepository.consultarPorIdLiquidacion(eliminar.getIdLiquidacionFlat());
	    if (Objects.nonNull(valorLiq)) {
	        // Desvincular la relación antes de eliminar
	        if (Objects.nonNull(valorLiq.getParametrosLiquidacionCostoFlat())) {
	            valorLiq.setParametrosLiquidacionCostoFlat(null);

	            // Guardar y sincronizar
	            valoresLiquidadosFlatRepository.save(valorLiq);
	            valoresLiquidadosFlatRepository.flush(); // sincronizar para actualizar referencia en DB
	        }

	        // Proceder con la eliminación de `valorLiq`
	        valoresLiquidadosFlatRepository.delete(valorLiq);
	    }
	    
	    // Finalmente, eliminar el objeto `ParametrosLiquidacionCostoFlat`
	    if (parametrosLiquidacionCostosRepositoryFlat.existsById(eliminar.getIdLiquidacionFlat())) {
	        parametrosLiquidacionCostosRepositoryFlat.delete(eliminar);
	    }

	    return eliminar;
		
	}


	@Override
	public ParametrosLiquidacionCosto f2actualizarParametrosLiquidacionCostos(ParametrosLiquidacionCosto actualizar) {
		
		return parametrosLiquidacionCostosRepository.save(actualizar);
		
	}

	@Override
	public ParametrosLiquidacionCostoFlat f2actualizarParametrosLiquidacionCostosFlat (ParametrosLiquidacionCostoFlat actualizar) {
		
		return parametrosLiquidacionCostosRepositoryFlat.save(actualizar);
		
	}	
}
