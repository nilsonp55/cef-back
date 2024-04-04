package com.ath.adminefectivo.delegate;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

	Page<OperacionesLiquidacionTransporteDTO> getLiquidacionConciliadaTransporte(String entidad,
			Date fecha_servicio_transporte, Date fecha_servicio_transporte_final, String identificacion_cliente,
			String razon_social, String codigo_punto_cargo, String nombre_punto_cargo, String ciudad_fondo,
			String nombre_tipo_servicio, String moneda_divisa, String estado, Pageable page);

	Page<OperacionesLiquidacionTransporteDTO> getLiquidacionRemitidasNoIdentificadasTransporte(String entidad,
			Date fecha_servicio_transporte, Date fecha_servicio_transporte_final, String identificacion_cliente,
			String razon_social, String codigo_punto_cargo, String nombre_punto_cargo, String ciudad_fondo,
			String nombre_tipo_servicio, String moneda_divisa, String estado, Pageable page);

	Page<OperacionesLiquidacionTransporteDTO> getLiquidadasNoCobradasTransporte(String entidad,
			Date fecha_servicio_transporte, Date fecha_servicio_transporte_final, String identificacion_cliente,
			String razon_social, String codigo_punto_cargo, String nombre_punto_cargo, String ciudad_fondo,
			String nombre_tipo_servicio, String moneda_divisa, String estado, Pageable page);

	Page<OperacionesLiquidacionTransporteDTO> getIdentificadasConDiferenciasTransporte(String entidad,
			Date fecha_servicio_transporte, Date fecha_servicio_transporte_final, String identificacion_cliente,
			String razon_social, String codigo_punto_cargo, String nombre_punto_cargo, String ciudad_fondo,
			String nombre_tipo_servicio, String moneda_divisa, String estado, Pageable page);
	
	public List<RegistroOperacionConciliacionDTO> desconciliar(RegistrosConciliacionListDTO entidad);
	
	public List<RegistroOperacionConciliacionDTO> remitidasAceptarRechazar(RegistrosConciliacionListDTO entidad);

}
