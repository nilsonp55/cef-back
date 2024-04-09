package com.ath.adminefectivo.delegate;

import java.util.List;
import org.springframework.data.domain.Page;
import com.ath.adminefectivo.dto.ParametrosFiltroConciliacionCostoDTO;
import com.ath.adminefectivo.dto.RegistroOperacionConciliacionDTO;
import com.ath.adminefectivo.dto.compuestos.ConciliacionCostosTransporteDTO;
import com.ath.adminefectivo.dto.compuestos.OperacionesLiquidacionTransporteDTO;
import com.ath.adminefectivo.dto.compuestos.RegistrosConciliacionListDTO;

public interface IConciliacionCostosTransporteDelegate {
	/**
	 * Delegate responsable de consultar los registros conciliados
	 * 
	 * @return List<ArchivosCargadosDTO>
	 * @author hector.mercado
	 */
	List<ConciliacionCostosTransporteDTO> conciliadas(String entidad, String identificacion);

	Page<OperacionesLiquidacionTransporteDTO> getLiquidacionConciliadaTransporte(ParametrosFiltroConciliacionCostoDTO filtros);

	Page<OperacionesLiquidacionTransporteDTO> getLiquidacionRemitidasNoIdentificadasTransporte(ParametrosFiltroConciliacionCostoDTO filtros);

	Page<OperacionesLiquidacionTransporteDTO> getLiquidadasNoCobradasTransporte(ParametrosFiltroConciliacionCostoDTO filtros);

	Page<OperacionesLiquidacionTransporteDTO> getIdentificadasConDiferenciasTransporte(ParametrosFiltroConciliacionCostoDTO filtros);
	
	public List<RegistroOperacionConciliacionDTO> desconciliar(RegistrosConciliacionListDTO entidad);
	
	public List<RegistroOperacionConciliacionDTO> remitidasAceptarRechazar(RegistrosConciliacionListDTO entidad);

}
