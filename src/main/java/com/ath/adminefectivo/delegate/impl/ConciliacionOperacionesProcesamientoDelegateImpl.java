package com.ath.adminefectivo.delegate.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.ath.adminefectivo.delegate.IConciliacionOperacionesProcesamientoDelegate;
import com.ath.adminefectivo.dto.ParametrosFiltroConciliacionCostoDTO;
import com.ath.adminefectivo.dto.compuestos.OperacionesLiquidacionProcesamientoDTO;
import com.ath.adminefectivo.service.IConciliacionOperacionesProcesamientoService;

@Service
public class ConciliacionOperacionesProcesamientoDelegateImpl implements IConciliacionOperacionesProcesamientoDelegate {

	@Autowired
	IConciliacionOperacionesProcesamientoService operacionesLiquidacion;

	@Override
	public Page<OperacionesLiquidacionProcesamientoDTO> getLiquidacionConciliadaProcesamiento(ParametrosFiltroConciliacionCostoDTO filtros) {
		return operacionesLiquidacion.getLiquidacionConciliadaProcesamiento(filtros);
	}

	@Override
	public Page<OperacionesLiquidacionProcesamientoDTO> getLiquidacionRemitidasNoIdentificadasProcesamiento(
			ParametrosFiltroConciliacionCostoDTO filtros) {
		return operacionesLiquidacion.getLiquidacionRemitidasNoIdentificadasProcesamiento(filtros);
	}

	@Override
	public Page<OperacionesLiquidacionProcesamientoDTO> getLiquidadasNoCobradasProcesamiento(ParametrosFiltroConciliacionCostoDTO filtros) {
		return operacionesLiquidacion.getLiquidadasNoCobradasProcesamiento(filtros);
	}

	@Override
	public Page<OperacionesLiquidacionProcesamientoDTO> getIdentificadasConDiferenciasProcesamiento(ParametrosFiltroConciliacionCostoDTO filtros) {
		return operacionesLiquidacion.getIdentificadasConDiferenciasProcesamiento(filtros);
	}

}
