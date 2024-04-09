package com.ath.adminefectivo.delegate;

import org.springframework.data.domain.Page;
import com.ath.adminefectivo.dto.ParametrosFiltroConciliacionCostoDTO;
import com.ath.adminefectivo.dto.compuestos.OperacionesLiquidacionProcesamientoDTO;



public interface IConciliacionOperacionesProcesamientoDelegate {
	
	 /**
     * Delegate responsable de consultar las operaciones de liquidacion
     * @return Page<OperacionesLiquidacionProcesamientoDTO>
     * @author jorge.capera
	 * @param ParametrosFiltroConciliacionCostoDTO
     */
	
	Page<OperacionesLiquidacionProcesamientoDTO> getLiquidacionConciliadaProcesamiento(ParametrosFiltroConciliacionCostoDTO filtros);
	
	Page<OperacionesLiquidacionProcesamientoDTO> getLiquidacionRemitidasNoIdentificadasProcesamiento(ParametrosFiltroConciliacionCostoDTO filtros);

	Page<OperacionesLiquidacionProcesamientoDTO> getLiquidadasNoCobradasProcesamiento(ParametrosFiltroConciliacionCostoDTO filtros);

	Page<OperacionesLiquidacionProcesamientoDTO> getIdentificadasConDiferenciasProcesamiento(ParametrosFiltroConciliacionCostoDTO filtros);


}
