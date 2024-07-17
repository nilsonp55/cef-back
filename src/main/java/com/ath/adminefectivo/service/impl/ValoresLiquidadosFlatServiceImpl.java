package com.ath.adminefectivo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.entities.ValoresLiquidadosFlatEntity;
import com.ath.adminefectivo.repositories.IValoresLiquidadosFlatRepository;
import com.ath.adminefectivo.service.IValoresLiquidadosFlatService;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ValoresLiquidadosFlatServiceImpl implements IValoresLiquidadosFlatService {

  @Autowired
  IValoresLiquidadosFlatRepository valoresLiquidadosFlatRepository;

  	/**
	 * Metodo encargado de actualizar la tabla de valores liquidados 
	 * 
	 * @param ValoresLiquidados
	 * @return ValoresLiquidados
	 * @author hector.mercado
	 */
	@Override
	public ValoresLiquidadosFlatEntity f2actualizarvaloresLiquidadosRepository(ValoresLiquidadosFlatEntity actualizar) {
		
		return valoresLiquidadosFlatRepository.save(actualizar);
		
	}
	
	/**
	 * Metodo encargado de actualizar la tabla de valores liquidados 
	 * 
	 * @param Long
	 * @return ValoresLiquidadosFlatEntity
	 * @author hector.mercado
	 */
	@Override
	public ValoresLiquidadosFlatEntity consultarPorIdLiquidacion(Long idLiquidacion) {
		
		return valoresLiquidadosFlatRepository.consultarPorIdLiquidacion(idLiquidacion);
	}
	
	/**
	 * Metodo encargado de actualizar la tabla de valores liquidados 
	 * 
	 * @param idLiquidacion
	 * @return ValoresLiquidadosFlatEntity
	 * @author jose.pabon
	 */
	@Override
	public ValoresLiquidadosFlatEntity getParametrosValoresLiquidadosByIdLiquidacion(Long idLiquidacion) {
		
		return valoresLiquidadosFlatRepository.consultarPorIdLiquidacion(idLiquidacion);
		
	}
}
