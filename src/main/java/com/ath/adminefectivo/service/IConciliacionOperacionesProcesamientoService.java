package com.ath.adminefectivo.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ath.adminefectivo.dto.compuestos.OperacionesLiquidacionProcesamientoDTO;
import com.ath.adminefectivo.dto.compuestos.OperacionesLiquidacionTransporteDTO;

public interface IConciliacionOperacionesProcesamientoService {
	
	
	Page<OperacionesLiquidacionProcesamientoDTO> getLiquidacionConciliadaProcesamiento(String entidad, Date fecha_servicio_transporte,Date fecha_servicio_transporte_final,
			String identificacion_cliente, String razon_social, Integer codigo_punto_cargo, String nombre_punto_cargo,
			String ciudad_fondo, String nombre_tipo_servicio, String moneda_divisa, String estado, Pageable page);
	
	Page<OperacionesLiquidacionProcesamientoDTO> getLiquidacionRemitidasNoIdentificadasProcesamiento(String entidad,
			Date fecha_servicio_transporte, Date fecha_servicio_transporte_final, String identificacion_cliente,
			String razon_social, Integer codigo_punto_cargo, String nombre_punto_cargo, String ciudad_fondo,
			String nombre_tipo_servicio, String moneda_divisa, String estado, Pageable page);

	Page<OperacionesLiquidacionProcesamientoDTO> getLiquidadasNoCobradasProcesamiento(String entidad,
			Date fecha_servicio_transporte, Date fecha_servicio_transporte_final, String identificacion_cliente,
			String razon_social, Integer codigo_punto_cargo, String nombre_punto_cargo, String ciudad_fondo,
			String nombre_tipo_servicio, String moneda_divisa, String estado, Pageable page);

	Page<OperacionesLiquidacionProcesamientoDTO> getIdentificadasConDiferenciasProcesamiento(String entidad,
			Date fecha_servicio_transporte, Date fecha_servicio_transporte_final, String identificacion_cliente,
			String razon_social, Integer codigo_punto_cargo, String nombre_punto_cargo, String ciudad_fondo,
			String nombre_tipo_servicio, String moneda_divisa, String estado, Pageable page);

}
