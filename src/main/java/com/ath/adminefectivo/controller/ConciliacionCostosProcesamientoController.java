package com.ath.adminefectivo.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.delegate.IConciliacionOperacionesProcesamientoDelegate;
import com.ath.adminefectivo.dto.compuestos.OperacionesLiquidacionProcesamientoDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;

import lombok.extern.log4j.Log4j2;

/**
 * Controlador responsable de exponer las consultas de operaciones Conciliadas -
 * Operaciones Recibidas no Identificadas
 * 
 * @author jorge.capera
 */

@RestController
@RequestMapping("${endpoints.conciliacion.procesadas}")
@Log4j2
public class ConciliacionCostosProcesamientoController {

	@Autowired
	IConciliacionOperacionesProcesamientoDelegate operacionesLiquidacionDelegate;

	/**
	 * Metodo encargado de retornar la lista de todas los operaciones conciliadas
	 * 
	 * @return Page<OperacionesLiquidacionDTO>
	 * @param page
	 * @author jorge.capera
	 */
	@GetMapping(value = "${endpoints.conciliacion.procesadas-conciliadas}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<Page<OperacionesLiquidacionProcesamientoDTO>>> getliquidacionConciliadaProcesamiento(
			@RequestParam(required = false) String entidad,
			@RequestParam(required = true) Date fecha_servicio_transporte,
			@RequestParam(required = true) Date fecha_servicio_transporte_final,
			@RequestParam(required = false) String identificacion_cliente,
			@RequestParam(required = false) String razon_social,
			@RequestParam(required = false) Integer codigo_punto_cargo,
			@RequestParam(required = false) String nombre_punto_cargo,
			@RequestParam(required = false) String ciudad_fondo,
			@RequestParam(required = false) String nombre_tipo_servicio,
			@RequestParam(required = false) String moneda_divisa, @RequestParam(required = false) String estado,
			Pageable page) {

		var consulta = operacionesLiquidacionDelegate.getLiquidacionConciliadaProcesamiento(entidad,
				fecha_servicio_transporte, fecha_servicio_transporte_final, identificacion_cliente, razon_social,
				codigo_punto_cargo, nombre_punto_cargo, ciudad_fondo, nombre_tipo_servicio, moneda_divisa, estado,
				page);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}


	@GetMapping(value = "${endpoints.conciliacion.procesadas-remitidasNoIdentifacadas}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<Page<OperacionesLiquidacionProcesamientoDTO>>> getRemitidasNoIdentifacadas(
			@RequestParam(required = false) String entidad,
			@RequestParam(required = true) Date fecha_servicio_transporte,
			@RequestParam(required = true) Date fecha_servicio_transporte_final,
			@RequestParam(required = false) String identificacion_cliente,
			@RequestParam(required = false) String razon_social,
			@RequestParam(required = false) Integer codigo_punto_cargo,
			@RequestParam(required = false) String nombre_punto_cargo,
			@RequestParam(required = false) String ciudad_fondo,
			@RequestParam(required = false) String nombre_tipo_servicio,
			@RequestParam(required = false) String moneda_divisa, @RequestParam(required = false) String estado,
			Pageable page) {

		var consulta = operacionesLiquidacionDelegate.getLiquidacionRemitidasNoIdentificadasProcesamiento(entidad,
				fecha_servicio_transporte, fecha_servicio_transporte_final, identificacion_cliente, razon_social,
				codigo_punto_cargo, nombre_punto_cargo, ciudad_fondo, nombre_tipo_servicio, moneda_divisa, estado,
				page);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	@GetMapping(value = "${endpoints.conciliacion.procesadas-liquidadasNoCobradas}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<Page<OperacionesLiquidacionProcesamientoDTO>>> getLiquidadasNoCobradas(
			@RequestParam(required = false) String entidad,
			@RequestParam(required = true) Date fecha_servicio_transporte,
			@RequestParam(required = true) Date fecha_servicio_transporte_final,
			@RequestParam(required = false) String identificacion_cliente,
			@RequestParam(required = false) String razon_social,
			@RequestParam(required = false) Integer codigo_punto_cargo,
			@RequestParam(required = false) String nombre_punto_cargo,
			@RequestParam(required = false) String ciudad_fondo,
			@RequestParam(required = false) String nombre_tipo_servicio,
			@RequestParam(required = false) String moneda_divisa, @RequestParam(required = false) String estado,
			Pageable page) {

		var consulta = operacionesLiquidacionDelegate.getLiquidadasNoCobradasProcesamiento(entidad,
				fecha_servicio_transporte, fecha_servicio_transporte_final, identificacion_cliente, razon_social,
				codigo_punto_cargo, nombre_punto_cargo, ciudad_fondo, nombre_tipo_servicio, moneda_divisa, estado,
				page);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	@GetMapping(value = "${endpoints.conciliacion.procesadas-identificadasConDiferencias}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<Page<OperacionesLiquidacionProcesamientoDTO>>> getIdentificadasConDiferencias(
			@RequestParam(required = false) String entidad,
			@RequestParam(required = true) Date fecha_servicio_transporte,
			@RequestParam(required = true) Date fecha_servicio_transporte_final,
			@RequestParam(required = false) String identificacion_cliente,
			@RequestParam(required = false) String razon_social,
			@RequestParam(required = false) Integer codigo_punto_cargo,
			@RequestParam(required = false) String nombre_punto_cargo,
			@RequestParam(required = false) String ciudad_fondo,
			@RequestParam(required = false) String nombre_tipo_servicio,
			@RequestParam(required = false) String moneda_divisa, @RequestParam(required = false) String estado,
			Pageable page) {

		var consulta = operacionesLiquidacionDelegate.getIdentificadasConDiferenciasProcesamiento(entidad,
				fecha_servicio_transporte, fecha_servicio_transporte_final, identificacion_cliente, razon_social,
				codigo_punto_cargo, nombre_punto_cargo, ciudad_fondo, nombre_tipo_servicio, moneda_divisa, estado,
				page);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}
