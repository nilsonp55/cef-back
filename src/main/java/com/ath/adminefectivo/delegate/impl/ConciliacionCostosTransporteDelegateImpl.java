package com.ath.adminefectivo.delegate.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.delegate.IConciliacionCostosTransporteDelegate;
import com.ath.adminefectivo.dto.ParametrosFiltroConciliacionCostoDTO;
import com.ath.adminefectivo.dto.RegistroOperacionConciliacionDTO;
import com.ath.adminefectivo.dto.compuestos.ConciliacionCostosTransporteDTO;
import com.ath.adminefectivo.dto.compuestos.OperacionesLiquidacionTransporteDTO;
import com.ath.adminefectivo.dto.compuestos.RegistrosConciliacionListDTO;
import com.ath.adminefectivo.service.ICostosTransporteService;

@Service
public class ConciliacionCostosTransporteDelegateImpl implements IConciliacionCostosTransporteDelegate {

	@Autowired
	ICostosTransporteService costosTransporteService;

	@Override
	public List<ConciliacionCostosTransporteDTO> conciliadas(String entidad, String identificacion) {
		return costosTransporteService.conciliadasDto(entidad, identificacion);
	}

	@Override
	public Page<OperacionesLiquidacionTransporteDTO> getLiquidacionConciliadaTransporte(ParametrosFiltroConciliacionCostoDTO filtros) {

		return costosTransporteService.getLiquidacionConciliadaTransporte(filtros);
	}

	@Override
	public Page<OperacionesLiquidacionTransporteDTO> getLiquidacionRemitidasNoIdentificadasTransporte(ParametrosFiltroConciliacionCostoDTO filtros) {

		return costosTransporteService.getLiquidacionRemitidasNoIdentificadasTransporte(filtros);
	}

	@Override
	public Page<OperacionesLiquidacionTransporteDTO> getLiquidadasNoCobradasTransporte(ParametrosFiltroConciliacionCostoDTO filtros) {

		return costosTransporteService.getLiquidadasNoCobradasTransporte(filtros);
	}

	@Override
	public Page<OperacionesLiquidacionTransporteDTO> getIdentificadasConDiferenciasTransporte(ParametrosFiltroConciliacionCostoDTO filtros) {

		return costosTransporteService.getIdentificadasConDiferenciasTransporte(filtros);
	}
	
	@Override
	public List<RegistroOperacionConciliacionDTO> desconciliar(RegistrosConciliacionListDTO registros) {

		return costosTransporteService.desconciliar(registros);
	}
	
	@Override
	public List<RegistroOperacionConciliacionDTO> remitidasAceptarRechazar(RegistrosConciliacionListDTO registros) {

		return costosTransporteService.remitidasAceptarRechazar(registros);
	}

}

