package com.ath.adminefectivo.delegate.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.delegate.IClasificacionCostosDelegate;
import com.ath.adminefectivo.delegate.IEscalasDelegate;
import com.ath.adminefectivo.delegate.ITarifasOperacionDelegate;
import com.ath.adminefectivo.dto.EscalasDTO;
import com.ath.adminefectivo.dto.TarifasOperacionDTO;
import com.ath.adminefectivo.dto.compuestos.CostosMensualesClasificacionDTO;
import com.ath.adminefectivo.service.IEscalasService;
import com.ath.adminefectivo.service.ITarifasOperacionService;
import com.querydsl.core.types.Predicate;

@Service
public class ClasificacionCostosDelegateImpl implements IClasificacionCostosDelegate{

	@Autowired
	IEscalasService escalasService;

	@Override
	public List<CostosMensualesClasificacionDTO> getClasificacionMensualPorBanco(String transportadora,
			String mesAnio) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CostosMensualesClasificacionDTO> getClasificacionMensualPorBanco(
			List<CostosMensualesClasificacionDTO> listadoCostosMensuales) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardarClasificacionCostosMensuales(List<CostosMensualesClasificacionDTO> listadoCostosMensuales) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
