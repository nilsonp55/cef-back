package com.ath.adminefectivo.delegate;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ath.adminefectivo.dto.compuestos.OperacionesLiquidacionProcesamientoDTO;



public interface IConciliacionOperacionesProcesamientoDelegate {
	
	 /**
     * Delegate responsable de consultar las operaciones de liquidacion
     * @return List<ArchivosCargadosDTO>
     * @author jorge.capera
	 * @param page 
	 * @param codigo_punto_cargo 
	 * @param fechaFinal 
	 * @param fechaInicial 
     */
	
	Page<OperacionesLiquidacionProcesamientoDTO> getLiquidacionConciliadaProcesamiento(String entidad,
			Date fecha_servicio_transporte, Date fecha_servicio_transporte_final, String identificacion_cliente,
			String razon_social, Integer codigo_punto_cargo, String nombre_punto_cargo, String ciudad_fondo,
			String nombre_tipo_servicio, String moneda_divisa, String estado, Pageable page);
	
	
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
