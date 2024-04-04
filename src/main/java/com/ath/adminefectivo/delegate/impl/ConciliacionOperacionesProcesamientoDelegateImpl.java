package com.ath.adminefectivo.delegate.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.delegate.IConciliacionOperacionesProcesamientoDelegate;
import com.ath.adminefectivo.dto.compuestos.OperacionesLiquidacionProcesamientoDTO;
import com.ath.adminefectivo.dto.compuestos.OperacionesLiquidacionTransporteDTO;
import com.ath.adminefectivo.service.IConciliacionOperacionesProcesamientoService;
import com.querydsl.core.types.Predicate;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ConciliacionOperacionesProcesamientoDelegateImpl implements IConciliacionOperacionesProcesamientoDelegate {

	@Autowired
	IConciliacionOperacionesProcesamientoService operacionesLiquidacion;

	@Override
	public Page<OperacionesLiquidacionProcesamientoDTO> getLiquidacionConciliadaProcesamiento(String entidad,
			Date fecha_servicio_transporte, Date fecha_servicio_transporte_final, String identificacion_cliente,
			String razon_social, Integer codigo_punto_cargo, String nombre_punto_cargo, String ciudad_fondo,
			String nombre_tipo_servicio, String moneda_divisa, String estado, Pageable page) {
		return operacionesLiquidacion.getLiquidacionConciliadaProcesamiento(entidad, fecha_servicio_transporte,
				fecha_servicio_transporte_final, identificacion_cliente, razon_social, codigo_punto_cargo,
				nombre_punto_cargo, ciudad_fondo, nombre_tipo_servicio, moneda_divisa, estado, page);
	}

	@Override
	public Page<OperacionesLiquidacionProcesamientoDTO> getLiquidacionRemitidasNoIdentificadasProcesamiento(
			String entidad, Date fecha_servicio_transporte, Date fecha_servicio_transporte_final,
			String identificacion_cliente, String razon_social, Integer codigo_punto_cargo, String nombre_punto_cargo,
			String ciudad_fondo, String nombre_tipo_servicio, String moneda_divisa, String estado, Pageable page) {
		return operacionesLiquidacion.getLiquidacionRemitidasNoIdentificadasProcesamiento(entidad, fecha_servicio_transporte,
				fecha_servicio_transporte_final, identificacion_cliente, razon_social, codigo_punto_cargo,
				nombre_punto_cargo, ciudad_fondo, nombre_tipo_servicio, moneda_divisa, estado, page);
	}

	@Override
	public Page<OperacionesLiquidacionProcesamientoDTO> getLiquidadasNoCobradasProcesamiento(String entidad,
			Date fecha_servicio_transporte, Date fecha_servicio_transporte_final, String identificacion_cliente,
			String razon_social, Integer codigo_punto_cargo, String nombre_punto_cargo, String ciudad_fondo,
			String nombre_tipo_servicio, String moneda_divisa, String estado, Pageable page) {
		return operacionesLiquidacion.getLiquidadasNoCobradasProcesamiento(entidad, fecha_servicio_transporte,
				fecha_servicio_transporte_final, identificacion_cliente, razon_social, codigo_punto_cargo,
				nombre_punto_cargo, ciudad_fondo, nombre_tipo_servicio, moneda_divisa, estado, page);
	}

	@Override
	public Page<OperacionesLiquidacionProcesamientoDTO> getIdentificadasConDiferenciasProcesamiento(String entidad,
			Date fecha_servicio_transporte, Date fecha_servicio_transporte_final, String identificacion_cliente,
			String razon_social, Integer codigo_punto_cargo, String nombre_punto_cargo, String ciudad_fondo,
			String nombre_tipo_servicio, String moneda_divisa, String estado, Pageable page) {
		return operacionesLiquidacion.getIdentificadasConDiferenciasProcesamiento(entidad, fecha_servicio_transporte,
				fecha_servicio_transporte_final, identificacion_cliente, razon_social, codigo_punto_cargo,
				nombre_punto_cargo, ciudad_fondo, nombre_tipo_servicio, moneda_divisa, estado, page);
	}

}
