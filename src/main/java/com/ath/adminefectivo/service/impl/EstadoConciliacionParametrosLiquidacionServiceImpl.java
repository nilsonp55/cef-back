package com.ath.adminefectivo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.entities.EstadoConciliacionParametrosLiquidacion;
import com.ath.adminefectivo.repositories.IEstadoConciliacionParametrosLiquidacionRepository;
import com.ath.adminefectivo.service.IEstadoConciliacionParametrosLiquidacionService;

@Service
public class EstadoConciliacionParametrosLiquidacionServiceImpl
		implements IEstadoConciliacionParametrosLiquidacionService {

	
	@Autowired
	IEstadoConciliacionParametrosLiquidacionRepository estadoConciliacionParametrosLiquidacionRepository;
	
	
	/**
	 * Servicio encargado de buscar una liquidacion en EstadoConciliacionParametrosLiquidacion
	 * 
	 * @param idLiquidacion
	 * @return List<EstadoConciliacionParametrosLiquidacion>
	 * @author hector.mercado
	 */
	public List<EstadoConciliacionParametrosLiquidacion> buscarLiquidacion(Long idLiquidacion, Integer estado) {
		return estadoConciliacionParametrosLiquidacionRepository.findByIdLiquidacionAndEstado(idLiquidacion, estado);
	}


	@Override
	public EstadoConciliacionParametrosLiquidacion save(EstadoConciliacionParametrosLiquidacion registro) {
		return estadoConciliacionParametrosLiquidacionRepository.save(registro);
	}


}
