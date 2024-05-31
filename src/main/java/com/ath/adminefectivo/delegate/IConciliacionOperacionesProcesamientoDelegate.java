package com.ath.adminefectivo.delegate;

import org.springframework.data.domain.Page;
import com.ath.adminefectivo.dto.ParametrosFiltroCostoProcesamientoDTO;
import com.ath.adminefectivo.dto.compuestos.OperacionesLiquidacionProcesamientoDTO;



public interface IConciliacionOperacionesProcesamientoDelegate {
	
	 /**
     * Delegate responsable de consultar las operaciones de liquidacion
     * @return Page<OperacionesLiquidacionProcesamientoDTO>
     * @author jorge.capera
	 * @param ParametrosFiltroCostoProcesamientoDTO
     */
	
	Page<OperacionesLiquidacionProcesamientoDTO> getLiquidacionConciliadaProcesamiento(ParametrosFiltroCostoProcesamientoDTO filtros);
	
	Page<OperacionesLiquidacionProcesamientoDTO> getLiquidacionRemitidasNoIdentificadasProcesamiento(ParametrosFiltroCostoProcesamientoDTO filtros);

	Page<OperacionesLiquidacionProcesamientoDTO> getLiquidadasNoCobradasProcesamiento(ParametrosFiltroCostoProcesamientoDTO filtros);

	Page<OperacionesLiquidacionProcesamientoDTO> getIdentificadasConDiferenciasProcesamiento(ParametrosFiltroCostoProcesamientoDTO filtros);


}
