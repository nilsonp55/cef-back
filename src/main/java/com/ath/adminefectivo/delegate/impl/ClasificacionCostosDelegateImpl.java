package com.ath.adminefectivo.delegate.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.delegate.IClasificacionCostosDelegate;
import com.ath.adminefectivo.dto.compuestos.CostosMensualesClasificacionDTO;
import com.ath.adminefectivo.service.IClasificacionCostosService;

@Service
public class ClasificacionCostosDelegateImpl implements IClasificacionCostosDelegate{

	@Autowired
	IClasificacionCostosService clasificacionCostosService;

	@Override
	public List<CostosMensualesClasificacionDTO> getClasificacionMensualCostos(String transportadora,
			String mesAnio) {
		
		return clasificacionCostosService.getClasificacionMensualCostos(transportadora, mesAnio);
	}

	@Override
	public List<CostosMensualesClasificacionDTO> liquidarClasificacionCostos(
			List<CostosMensualesClasificacionDTO> listadoCostosMensuales) {
		return clasificacionCostosService.liquidarClasificacionCostos(listadoCostosMensuales);
	}

	@Override
	public String guardarClasificacionCostosMensuales(List<CostosMensualesClasificacionDTO> listadoCostosMensuales) {
		return  clasificacionCostosService.guardarClasificacionCostosMensuales(listadoCostosMensuales);
	}
	
	
	
}
