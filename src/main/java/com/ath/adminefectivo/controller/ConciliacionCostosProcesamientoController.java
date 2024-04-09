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
import com.ath.adminefectivo.dto.ParametrosFiltroConciliacionCostoDTO;
import com.ath.adminefectivo.dto.compuestos.OperacionesLiquidacionProcesamientoDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;

/**
 * Controlador responsable de exponer las consultas de operaciones Conciliadas -
 * Operaciones Recibidas no Identificadas
 * 
 * @author jorge.capera
 */

@RestController
@RequestMapping("${endpoints.conciliacion.procesadas}")
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
			@RequestParam(required = true) Date fechaServicioTransporte,
			@RequestParam(required = true) Date fechaServicioTransporteFinal,
			@RequestParam(required = false) String identificacionCliente,
			@RequestParam(required = false) String razonSocial,
			@RequestParam(required = false) Integer codigoPuntoCargo,
			@RequestParam(required = false) String nombrePuntoCargo,
			@RequestParam(required = false) String ciudadFondo,
			@RequestParam(required = false) String nombreTipoServicio,
			@RequestParam(required = false) String monedaDivisa, @RequestParam(required = false) String estado,
			Pageable page) {

		var filtros = new ParametrosFiltroConciliacionCostoDTO();
		filtros.setEntidad(entidad);
		filtros.setFechaServicioTransporte(fechaServicioTransporte);
		filtros.setFechaServicioTransporteFinal(fechaServicioTransporteFinal);
		filtros.setIdentificacionCliente(identificacionCliente);
		filtros.setRazonSocial(razonSocial);
		filtros.setCodigoPuntoCargo(codigoPuntoCargo);
		filtros.setNombrePuntoCargo(nombrePuntoCargo);
		filtros.setCiudadFondo(ciudadFondo);
		filtros.setNombreTipoServicio(nombreTipoServicio);
		filtros.setMonedaDivisa(monedaDivisa); 
		filtros.setEstado(estado);
		filtros.setPage(page);
		
		var consulta = operacionesLiquidacionDelegate.getLiquidacionConciliadaProcesamiento(filtros);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}


	@GetMapping(value = "${endpoints.conciliacion.procesadas-remitidasNoIdentifacadas}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<Page<OperacionesLiquidacionProcesamientoDTO>>> getRemitidasNoIdentifacadas(
			@RequestParam(required = false) String entidad,
			@RequestParam(required = true) Date fechaServicioTransporte,
			@RequestParam(required = true) Date fechaServicioTransporteFinal,
			@RequestParam(required = false) String identificacionCliente,
			@RequestParam(required = false) String razonSocial,
			@RequestParam(required = false) Integer codigoPuntoCargo,
			@RequestParam(required = false) String nombrePuntoCargo,
			@RequestParam(required = false) String ciudadFondo,
			@RequestParam(required = false) String nombreTipoServicio,
			@RequestParam(required = false) String monedaDivisa, @RequestParam(required = false) String estado,
			Pageable page) {

		var filtros = new ParametrosFiltroConciliacionCostoDTO();
		filtros.setEntidad(entidad);
		filtros.setFechaServicioTransporte(fechaServicioTransporte);
		filtros.setFechaServicioTransporteFinal(fechaServicioTransporteFinal);
		filtros.setIdentificacionCliente(identificacionCliente);
		filtros.setRazonSocial(razonSocial);
		filtros.setCodigoPuntoCargo(codigoPuntoCargo);
		filtros.setNombrePuntoCargo(nombrePuntoCargo);
		filtros.setCiudadFondo(ciudadFondo);
		filtros.setNombreTipoServicio(nombreTipoServicio);
		filtros.setMonedaDivisa(monedaDivisa); 
		filtros.setEstado(estado);
		filtros.setPage(page);
		
		var consulta = operacionesLiquidacionDelegate.getLiquidacionRemitidasNoIdentificadasProcesamiento(filtros);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	@GetMapping(value = "${endpoints.conciliacion.procesadas-liquidadasNoCobradas}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<Page<OperacionesLiquidacionProcesamientoDTO>>> getLiquidadasNoCobradas(
			@RequestParam(required = false) String entidad,
			@RequestParam(required = true) Date fechaServicioTransporte,
			@RequestParam(required = true) Date fechaServicioTransporteFinal,
			@RequestParam(required = false) String identificacionCliente,
			@RequestParam(required = false) String razonSocial,
			@RequestParam(required = false) Integer codigoPuntoCargo,
			@RequestParam(required = false) String nombrePuntoCargo,
			@RequestParam(required = false) String ciudadFondo,
			@RequestParam(required = false) String nombreTipoServicio,
			@RequestParam(required = false) String monedaDivisa, @RequestParam(required = false) String estado,
			Pageable page) {

		var filtros = new ParametrosFiltroConciliacionCostoDTO();
		filtros.setEntidad(entidad);
		filtros.setFechaServicioTransporte(fechaServicioTransporte);
		filtros.setFechaServicioTransporteFinal(fechaServicioTransporteFinal);
		filtros.setIdentificacionCliente(identificacionCliente);
		filtros.setRazonSocial(razonSocial);
		filtros.setCodigoPuntoCargo(codigoPuntoCargo);
		filtros.setNombrePuntoCargo(nombrePuntoCargo);
		filtros.setCiudadFondo(ciudadFondo);
		filtros.setNombreTipoServicio(nombreTipoServicio);
		filtros.setMonedaDivisa(monedaDivisa); 
		filtros.setEstado(estado);
		filtros.setPage(page);
		
		var consulta = operacionesLiquidacionDelegate.getLiquidadasNoCobradasProcesamiento(filtros);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	@GetMapping(value = "${endpoints.conciliacion.procesadas-identificadasConDiferencias}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<Page<OperacionesLiquidacionProcesamientoDTO>>> getIdentificadasConDiferencias(
			@RequestParam(required = false) String entidad,
			@RequestParam(required = true) Date fechaServicioTransporte,
			@RequestParam(required = true) Date fechaServicioTransporteFinal,
			@RequestParam(required = false) String identificacionCliente,
			@RequestParam(required = false) String razonSocial,
			@RequestParam(required = false) Integer codigoPuntoCargo,
			@RequestParam(required = false) String nombrePuntoCargo,
			@RequestParam(required = false) String ciudadFondo,
			@RequestParam(required = false) String nombreTipoServicio,
			@RequestParam(required = false) String monedaDivisa, @RequestParam(required = false) String estado,
			Pageable page) {
		
		var filtros = new ParametrosFiltroConciliacionCostoDTO();
		filtros.setEntidad(entidad);
		filtros.setFechaServicioTransporte(fechaServicioTransporte);
		filtros.setFechaServicioTransporteFinal(fechaServicioTransporteFinal);
		filtros.setIdentificacionCliente(identificacionCliente);
		filtros.setRazonSocial(razonSocial);
		filtros.setCodigoPuntoCargo(codigoPuntoCargo);
		filtros.setNombrePuntoCargo(nombrePuntoCargo);
		filtros.setCiudadFondo(ciudadFondo);
		filtros.setNombreTipoServicio(nombreTipoServicio);
		filtros.setMonedaDivisa(monedaDivisa); 
		filtros.setEstado(estado);
		filtros.setPage(page);

		var consulta = operacionesLiquidacionDelegate.getIdentificadasConDiferenciasProcesamiento(filtros);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}
