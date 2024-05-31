package com.ath.adminefectivo.delegate;

import java.util.List;
import org.springframework.data.domain.Page;

import com.ath.adminefectivo.dto.ParametrosFiltroCostoTransporteDTO;
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

	Page<OperacionesLiquidacionTransporteDTO> getLiquidacionConciliadaTransporte(ParametrosFiltroCostoTransporteDTO filtros);

	Page<OperacionesLiquidacionTransporteDTO> getLiquidacionRemitidasNoIdentificadasTransporte(ParametrosFiltroCostoTransporteDTO filtros);

	Page<OperacionesLiquidacionTransporteDTO> getLiquidadasNoCobradasTransporte(ParametrosFiltroCostoTransporteDTO filtros);

	Page<OperacionesLiquidacionTransporteDTO> getIdentificadasConDiferenciasTransporte(ParametrosFiltroCostoTransporteDTO filtros);
	
	public List<RegistroOperacionConciliacionDTO> desconciliar(RegistrosConciliacionListDTO entidad);
	
	public List<RegistroOperacionConciliacionDTO> remitidasAceptarRechazar(RegistrosConciliacionListDTO entidad);

}
