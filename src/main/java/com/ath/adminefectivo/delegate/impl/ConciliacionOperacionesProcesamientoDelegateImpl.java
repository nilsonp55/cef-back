package com.ath.adminefectivo.delegate.impl;

import java.awt.print.Pageable;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.ath.adminefectivo.delegate.IConciliacionOperacionesProcesamientoDelegate;
import com.ath.adminefectivo.dto.RegistroAceptarRechazarDTO;
import com.ath.adminefectivo.dto.RegistroOperacionConciliacionDTO;
import com.ath.adminefectivo.dto.ParametrosFiltroCostoProcesamientoDTO;
import com.ath.adminefectivo.dto.compuestos.OperacionesLiquidacionProcesamientoDTO;
import com.ath.adminefectivo.dto.compuestos.RegistrosConciliacionListDTO;
import com.ath.adminefectivo.service.IConciliacionOperacionesProcesamientoService;
import com.ath.adminefectivo.dto.compuestos.RegistrosAceptarRechazarListDTO;
import com.ath.adminefectivo.dto.compuestos.RegistrosConciliacionListDTO;
import com.ath.adminefectivo.service.IConciliacionOperacionesProcesamientoService;
import lombok.extern.log4j.Log4j2;

@Service
public class ConciliacionOperacionesProcesamientoDelegateImpl implements IConciliacionOperacionesProcesamientoDelegate {

	@Autowired
	IConciliacionOperacionesProcesamientoService operacionesLiquidacion;

	@Override
	public Page<OperacionesLiquidacionProcesamientoDTO> getLiquidacionConciliadaProcesamiento(ParametrosFiltroCostoProcesamientoDTO filtros) {
		return operacionesLiquidacion.getLiquidacionConciliadaProcesamiento(filtros);
	}

	@Override
	public Page<OperacionesLiquidacionProcesamientoDTO> getLiquidacionRemitidasNoIdentificadasProcesamiento(
			ParametrosFiltroCostoProcesamientoDTO filtros) {
		return operacionesLiquidacion.getLiquidacionRemitidasNoIdentificadasProcesamiento(filtros);
	}

	@Override
	public Page<OperacionesLiquidacionProcesamientoDTO> getLiquidadasNoCobradasProcesamiento(ParametrosFiltroCostoProcesamientoDTO filtros) {
		return operacionesLiquidacion.getLiquidadasNoCobradasProcesamiento(filtros);
	}

	@Override
	public Page<OperacionesLiquidacionProcesamientoDTO> getIdentificadasConDiferenciasProcesamiento(ParametrosFiltroCostoProcesamientoDTO filtros) {
		return operacionesLiquidacion.getIdentificadasConDiferenciasProcesamiento(filtros);
	}

	@Override
	public Page<OperacionesLiquidacionProcesamientoDTO> getEliminadasProcesamiento(ParametrosFiltroCostoProcesamientoDTO filtros) {
		return operacionesLiquidacion.getEliminadasProcesamiento(filtros);
	}
	
	@Override
	public List<RegistroOperacionConciliacionDTO> desconciliar(RegistrosConciliacionListDTO registros) {
		return operacionesLiquidacion.desconciliar(registros);
	}
	
	@Override
	public List<RegistroOperacionConciliacionDTO> remitidasAceptarRechazar(RegistrosConciliacionListDTO registros) {
		return operacionesLiquidacion.remitidasAceptarRechazar(registros);
	}

	@Override
	public List<RegistroOperacionConciliacionDTO> liquidadasEliminarRechazar(RegistrosConciliacionListDTO registros) {
		return operacionesLiquidacion.liquidadasEliminarRechazar(registros);
	}
	
	@Override
	public List<RegistroAceptarRechazarDTO> identificadasConDiferenciaAceptarRechazar(RegistrosAceptarRechazarListDTO registros) {
		return operacionesLiquidacion.identificadasConDiferenciaAceptarRechazar(registros);
	}


}
