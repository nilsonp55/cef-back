package com.ath.adminefectivo.delegate;

import java.util.List;
import org.springframework.data.domain.Page;
import com.ath.adminefectivo.dto.RegistroAceptarRechazarDTO;
import com.ath.adminefectivo.dto.RegistroOperacionConciliacionDTO;
import com.ath.adminefectivo.dto.ParametrosFiltroCostoProcesamientoDTO;
import com.ath.adminefectivo.dto.compuestos.OperacionesLiquidacionProcesamientoDTO;
import com.ath.adminefectivo.dto.compuestos.RegistrosAceptarRechazarListDTO;
import com.ath.adminefectivo.dto.compuestos.RegistrosConciliacionListDTO;



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
	
	Page<OperacionesLiquidacionProcesamientoDTO> getEliminadasProcesamiento(ParametrosFiltroCostoProcesamientoDTO filtros);
	
	List<RegistroOperacionConciliacionDTO> desconciliar(RegistrosConciliacionListDTO registros);
	
	List<RegistroOperacionConciliacionDTO> remitidasAceptarRechazar(RegistrosConciliacionListDTO registros);
	
	List<RegistroOperacionConciliacionDTO> liquidadasEliminarRechazar(RegistrosConciliacionListDTO registros);

	List<RegistroAceptarRechazarDTO> identificadasConDiferenciaAceptarRechazar (RegistrosAceptarRechazarListDTO registros);

	List<RegistroOperacionConciliacionDTO> reintegrarLiquidadas(RegistrosConciliacionListDTO registros);
	
	List<RegistroOperacionConciliacionDTO> reintegrarLiquidadasProcesamiento(RegistrosConciliacionListDTO registros);
}
