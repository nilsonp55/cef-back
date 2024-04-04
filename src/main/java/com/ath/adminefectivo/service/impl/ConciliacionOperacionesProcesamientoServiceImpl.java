package com.ath.adminefectivo.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.dto.compuestos.OperacionesLiquidacionProcesamientoDTO;
import com.ath.adminefectivo.entities.OperacionesLiquidacionProcesamientoEntity;
import com.ath.adminefectivo.repositories.IConciliacionOperacionesProcesamientoRepository;
import com.ath.adminefectivo.service.IConciliacionOperacionesProcesamientoService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ConciliacionOperacionesProcesamientoServiceImpl implements IConciliacionOperacionesProcesamientoService {

	@Autowired
	IConciliacionOperacionesProcesamientoRepository operacionesLiquidacion;

	@Override
	public Page<OperacionesLiquidacionProcesamientoDTO> getLiquidacionConciliadaProcesamiento(String entidad,
			Date fecha_servicio_transporte, Date fecha_servicio_transporte_final, String identificacion_cliente,
			String razon_social, Integer codigo_punto_cargo, String nombre_punto_cargo, String ciudad_fondo,
			String nombre_tipo_servicio, String moneda_divisa, String estado, Pageable page) {
		
		LocalDateTime ldt_fecha_servicio_transporte = convertToLocalDateTime(fecha_servicio_transporte);
		LocalDateTime ldt_fecha_servicio_transporte_final = convertToLocalDateTime(fecha_servicio_transporte_final);
		
		var consulta = operacionesLiquidacion.conciliadasLiquidadasProcesamiento(entidad, ldt_fecha_servicio_transporte,
				ldt_fecha_servicio_transporte_final, identificacion_cliente, razon_social, codigo_punto_cargo,
				nombre_punto_cargo, ciudad_fondo, nombre_tipo_servicio, moneda_divisa, estado,
				Constantes.OPERACIONES_LIQUIDACION_CONCILIADAS, page);

		return liquidacion(consulta, page);
	}

	@Override
	public Page<OperacionesLiquidacionProcesamientoDTO> getLiquidacionRemitidasNoIdentificadasProcesamiento(
			String entidad, Date fecha_servicio_transporte, Date fecha_servicio_transporte_final,
			String identificacion_cliente, String razon_social, Integer codigo_punto_cargo, String nombre_punto_cargo,
			String ciudad_fondo, String nombre_tipo_servicio, String moneda_divisa, String estado, Pageable page) {
		
		LocalDateTime ldt_fecha_servicio_transporte = convertToLocalDateTime(fecha_servicio_transporte);
		LocalDateTime ldt_fecha_servicio_transporte_final = convertToLocalDateTime(fecha_servicio_transporte_final);
		
		var consulta = operacionesLiquidacion.conciliadasLiquidadasProcesamiento(entidad, ldt_fecha_servicio_transporte,
				ldt_fecha_servicio_transporte_final, identificacion_cliente, razon_social, codigo_punto_cargo,
				nombre_punto_cargo, ciudad_fondo, nombre_tipo_servicio, moneda_divisa, estado,
				Constantes.OPERACIONES_LIQUIDACION_REMITIDAS_NO_IDENTIFICADAS, page);

		return liquidacion(consulta, page);
	}

	@Override
	public Page<OperacionesLiquidacionProcesamientoDTO> getLiquidadasNoCobradasProcesamiento(String entidad,
			Date fecha_servicio_transporte, Date fecha_servicio_transporte_final, String identificacion_cliente,
			String razon_social, Integer codigo_punto_cargo, String nombre_punto_cargo, String ciudad_fondo,
			String nombre_tipo_servicio, String moneda_divisa, String estado, Pageable page) {
		
		LocalDateTime ldt_fecha_servicio_transporte = convertToLocalDateTime(fecha_servicio_transporte);
		LocalDateTime ldt_fecha_servicio_transporte_final = convertToLocalDateTime(fecha_servicio_transporte_final);
		
		var consulta = operacionesLiquidacion.conciliadasLiquidadasProcesamiento(entidad, ldt_fecha_servicio_transporte,
				ldt_fecha_servicio_transporte_final, identificacion_cliente, razon_social, codigo_punto_cargo,
				nombre_punto_cargo, ciudad_fondo, nombre_tipo_servicio, moneda_divisa, estado,
				Constantes.OPERACIONES_LIQUIDACION_LIQUIDADAS_NO_COBRADAS, page);

		return liquidacion(consulta, page);
	}

	@Override
	public Page<OperacionesLiquidacionProcesamientoDTO> getIdentificadasConDiferenciasProcesamiento(String entidad,
			Date fecha_servicio_transporte, Date fecha_servicio_transporte_final, String identificacion_cliente,
			String razon_social, Integer codigo_punto_cargo, String nombre_punto_cargo, String ciudad_fondo,
			String nombre_tipo_servicio, String moneda_divisa, String estado, Pageable page) {
		
		LocalDateTime ldt_fecha_servicio_transporte = convertToLocalDateTime(fecha_servicio_transporte);
		LocalDateTime ldt_fecha_servicio_transporte_final = convertToLocalDateTime(fecha_servicio_transporte_final);
		
		var consulta = operacionesLiquidacion.conciliadasLiquidadasProcesamiento(entidad, ldt_fecha_servicio_transporte,
				ldt_fecha_servicio_transporte_final, identificacion_cliente, razon_social, codigo_punto_cargo,
				nombre_punto_cargo, ciudad_fondo, nombre_tipo_servicio, moneda_divisa, estado,
				Constantes.OPERACIONES_LIQUIDACION_IDENTIFICADAS_CON_DIFERENCIAS, page);

		return liquidacion(consulta, page);
	}

	private LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                      .atZone(ZoneId.systemDefault())
                      .toLocalDateTime();
    }
	
	private Page<OperacionesLiquidacionProcesamientoDTO> liquidacion(
			Page<OperacionesLiquidacionProcesamientoEntity> liquidadas, Pageable page) {

		return new PageImpl<>(
				liquidadas.getContent().stream().map(e -> OperacionesLiquidacionProcesamientoDTO.CONVERTER_DTO.apply(e))
						.collect(Collectors.<OperacionesLiquidacionProcesamientoDTO>toList()),
				page, liquidadas.getTotalElements());
	}

}
