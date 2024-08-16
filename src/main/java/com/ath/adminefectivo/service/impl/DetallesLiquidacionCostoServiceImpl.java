package com.ath.adminefectivo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.entities.DetallesLiquidacionCosto;
import com.ath.adminefectivo.entities.DetallesLiquidacionCostoFlatEntity;
import com.ath.adminefectivo.repositories.IDetallesLiquidacionCostoRepository;
import com.ath.adminefectivo.repositories.IDetallesLiquidacionCostoRepositoryFlat;
import com.ath.adminefectivo.service.IDetallesLiquidacionCostoService;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class DetallesLiquidacionCostoServiceImpl implements IDetallesLiquidacionCostoService {

  @Autowired
  IDetallesLiquidacionCostoRepository detallesLiquidacionCostoRepository;

  @Autowired
  IDetallesLiquidacionCostoRepositoryFlat detallesLiquidacionCostoRepositoryFlat;

  	/**
	 * Metodo encargado de actualizar la tabla de detalles
	 * 
	 * @param DetallesLiquidacionCosto
	 * @return DetallesLiquidacionCosto
	 * @author hector.mercado
	 */
	@Override
	public DetallesLiquidacionCosto f2actualizarvaloresLiquidadosRepository(DetallesLiquidacionCosto actualizar) {
		
		return detallesLiquidacionCostoRepository.save(actualizar);
		
	}
	
	/**
	 * Metodo encargado de actualizar la tabla de detalles
	 * 
	 * @param Long
	 * @return DetallesLiquidacionCosto
	 * @author hector.mercado
	 */
	@Override
	public List<DetallesLiquidacionCosto> consultarPorIdLiquidacion(Long idLiquidacion) {
		
		return detallesLiquidacionCostoRepository.consultarPorIdLiquidacion(idLiquidacion);
	}

	/**
	 * Metodo encargado de actualizar la tabla de detalles (flat)
	 * 
	 * @param Long
	 * @return DetallesLiquidacionCosto
	 * @author jose.pabon
	 */
	@Override
	public List<DetallesLiquidacionCostoFlatEntity> consultarPorIdLiquidacionFlat (Long idLiquidacion) {
		
		return detallesLiquidacionCostoRepositoryFlat.consultarPorIdLiquidacion(idLiquidacion);
	}

	

	/**
	 * Metodo encargado de actualizar una lista devalores de la tabla de detalles 
	 * 
	 * @param List<DetallesLiquidacionCostoFlatEntity>
	 * @return DetallesLiquidacionCosto
	 * @author jose.pabon
	 */
	@Override
	public List<DetallesLiquidacionCostoFlatEntity> f2actualizarListaDetallesValoresLiquidados(List<DetallesLiquidacionCostoFlatEntity> valoresDetallesLiquidacionCostos) {
		
		return detallesLiquidacionCostoRepositoryFlat.saveAll(valoresDetallesLiquidacionCostos);
	}
	
	
  
}
