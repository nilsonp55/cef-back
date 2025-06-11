package com.ath.adminefectivo.service;

import java.math.BigInteger;
import java.util.List;
import org.springframework.data.domain.Page;
import com.ath.adminefectivo.dto.RegistroAceptarRechazarDTO;
import com.ath.adminefectivo.dto.RegistroOperacionConciliacionDTO;
import com.ath.adminefectivo.dto.ParametrosFiltroCostoProcesamientoDTO;
import com.ath.adminefectivo.dto.compuestos.OperacionesLiquidacionProcesamientoDTO;
import com.ath.adminefectivo.dto.compuestos.RegistrosAceptarRechazarListDTO;
import com.ath.adminefectivo.dto.compuestos.RegistrosConciliacionListDTO;
import com.ath.adminefectivo.entities.CostosProcesamiento;
import com.ath.adminefectivo.entities.OperacionesLiquidacionProcesamientoEntity;

public interface IConciliacionOperacionesProcesamientoService {
	
	Page<OperacionesLiquidacionProcesamientoDTO> getLiquidacionConciliadaProcesamiento(ParametrosFiltroCostoProcesamientoDTO filtros);
	
	Page<OperacionesLiquidacionProcesamientoDTO> getLiquidacionRemitidasNoIdentificadasProcesamiento(ParametrosFiltroCostoProcesamientoDTO filtros);

	Page<OperacionesLiquidacionProcesamientoDTO> getLiquidadasNoCobradasProcesamiento(ParametrosFiltroCostoProcesamientoDTO filtros);

	Page<OperacionesLiquidacionProcesamientoDTO> getIdentificadasConDiferenciasProcesamiento(ParametrosFiltroCostoProcesamientoDTO filtros);
	
	Page<OperacionesLiquidacionProcesamientoDTO> getEliminadasProcesamiento(ParametrosFiltroCostoProcesamientoDTO filtros);
	
	List<RegistroOperacionConciliacionDTO> desconciliar(RegistrosConciliacionListDTO registros);
	
	List<RegistroOperacionConciliacionDTO> remitidasAceptarRechazar(RegistrosConciliacionListDTO registros);
	
	List<RegistroOperacionConciliacionDTO> liquidadasEliminarRechazar(RegistrosConciliacionListDTO registros);	

	List<RegistroAceptarRechazarDTO> identificadasConDiferenciaAceptarRechazar(RegistrosAceptarRechazarListDTO registros);

	List<RegistroOperacionConciliacionDTO> reintegrarLiquidadasTransporte(RegistrosConciliacionListDTO registros);
	
	List<IDetalleLiquidacionProcesamiento> obtenerDetalleLiquidacionProcesamiento(String modulo, Long idLlave);
	
	List<CostosProcesamiento> obtenerCostoProcesamientoList(String operacion, Long idRegistro);
	
	CostosProcesamiento calcularDiferenciasCostosProcesamiento(List<IDetalleLiquidacionProcesamiento> detalles, CostosProcesamiento costoProcesamiento);
	
	List<RegistroOperacionConciliacionDTO> reintegrarLiquidadasProcesamiento(RegistrosConciliacionListDTO registros);
	
	List<IDetalleLiquidacionProcesamiento> obtenerDetalleProcesamientoPorIdArchivo(Integer idArchivo);
	
	List<IDetalleLiquidacionProcesamiento> obtenerEstadoProcesamientoPorLlave(BigInteger idLlave);
}
