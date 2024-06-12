package com.ath.adminefectivo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.entities.DetallesLiquidacionCosto;
import com.ath.adminefectivo.entities.ValoresLiquidadosFlatEntity;
import com.ath.adminefectivo.repositories.IDetallesLiquidacionCostoRepository;
import com.ath.adminefectivo.repositories.IValoresLiquidadosFlatRepository;
import com.ath.adminefectivo.service.IDetallesLiquidacionCostoService;
import com.ath.adminefectivo.service.IValoresLiquidadosFlatService;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class DetallesLiquidacionCostoServiceImpl implements IDetallesLiquidacionCostoService {

  @Autowired
  IDetallesLiquidacionCostoRepository detallesLiquidacionCostoRepository;

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
	
	
  
}
