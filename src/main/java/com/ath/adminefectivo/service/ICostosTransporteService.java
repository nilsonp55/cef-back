package com.ath.adminefectivo.service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;

import com.ath.adminefectivo.dto.ParametrosFiltroCostoTransporteDTO;
import com.ath.adminefectivo.dto.RegistroAceptarRechazarDTO;
import com.ath.adminefectivo.dto.RegistroOperacionConciliacionDTO;
import com.ath.adminefectivo.dto.compuestos.ConciliacionCostosTransporteDTO;
import com.ath.adminefectivo.dto.compuestos.OperacionesLiquidacionTransporteDTO;
import com.ath.adminefectivo.dto.compuestos.RegistrosAceptarRechazarListDTO;
import com.ath.adminefectivo.dto.compuestos.RegistrosConciliacionListDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;
import com.ath.adminefectivo.entities.CostosTransporte;

public interface ICostosTransporteService {
	
	/**
	 * Metodo encargado de realizar la persistencia de una lista en la tabla de costo_transporte 
	 * 
	 * @param List<CostosTransporte>
	 * @return void
	 * @author hector.mercado
	 */	
	Long persistir(ValidacionArchivoDTO validacionArchivo);

	void saveAll(List<CostosTransporte> costo);

	List<CostosTransporte> conciliadas(String entidad, String identificacion);

	List<ConciliacionCostosTransporteDTO> conciliadasDto(String entidad, String identificacion);
	
	/**
	 * Metodo encargado de realizar la busqueda en la tabla de Costos_transporte 
	 * 
	 * @param entidad
	 * @param fechaServicioTransporte
	 * @param codigoPuntoCargo
	 * @param nombrePuntoCargo
	 * @param ciudadFondo
	 * @param nombreTipoServicio
	 * @return List<CostosTransporte>
	 * @author hector.mercado
	 */	
	List<CostosTransporte> findAceptados(String entidad, 
				Date fechaServicioTransporte,
				String codigoPuntoCargo,
				String nombrePuntoCargo,
				String ciudadFondo,
				String nombreTipoServicio);

	Page<OperacionesLiquidacionTransporteDTO> getLiquidacionConciliadaTransporte(ParametrosFiltroCostoTransporteDTO filtros);
	
	Page<OperacionesLiquidacionTransporteDTO> getLiquidacionRemitidasNoIdentificadasTransporte(ParametrosFiltroCostoTransporteDTO filtros);
	
	Page<OperacionesLiquidacionTransporteDTO> getLiquidadasNoCobradasTransporte(ParametrosFiltroCostoTransporteDTO filtros);

	Page<OperacionesLiquidacionTransporteDTO> getIdentificadasConDiferenciasTransporte(ParametrosFiltroCostoTransporteDTO filtros);
	
	Page<OperacionesLiquidacionTransporteDTO> getEliminadasTransporte(ParametrosFiltroCostoTransporteDTO filtros);
	
	List<RegistroOperacionConciliacionDTO> desconciliar(RegistrosConciliacionListDTO registros);
	
	List<RegistroOperacionConciliacionDTO> remitidasAceptarRechazar(RegistrosConciliacionListDTO registros);
	
	List<RegistroOperacionConciliacionDTO> liquidadasEliminarRechazar(RegistrosConciliacionListDTO registros);
	
	List<RegistroAceptarRechazarDTO> identificadasConDiferenciaAceptarRechazar(RegistrosAceptarRechazarListDTO registros);

	List<RegistroOperacionConciliacionDTO> reintegrarLiquidadasTransporte(RegistrosConciliacionListDTO registros);

	List<CostosTransporte> getByIdArchivoCargado(Long idArchivo);
	
	void aceptarConciliacionRegistro(Long idArchivoCargado);
	
	void persistirMaestroLlavesTransporte();
	
	List<IDetalleLiquidacionTransporte> obtenerDetalleLiquidacionTransporte(String modulo,Long idLlave);
	
	<T extends Number> T aplicarAjuste(Number saldoTdv, T valorApp, Class<T> tipo);
	
	CostosTransporte calcularDiferenciasCostos(List<IDetalleLiquidacionTransporte> detalles, CostosTransporte costoTransporte);
	
	//Long obtenerIdPorTipo(List<IDetalleLiquidacionTransporte> detalles, String tipo);
	
	<T> Long obtenerIdPorTipo(List<T> detalles, String tipo);
	
	List<CostosTransporte> obtenerCostoTransporteList(String operacion, Long idRegistro);
	
	List<IDetalleLiquidacionTransporte> obtenerDetalleTransportePorIdArchivo(Integer idArchivo);
	
	List<IDetalleLiquidacionTransporte> obtenerEstadoTransportePorLlave(BigInteger idLlave);
}
