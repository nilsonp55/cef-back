package com.ath.adminefectivo.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.delegate.IConciliacionOperacionesProcesamientoDelegate;
import com.ath.adminefectivo.dto.RegistroAceptarRechazarDTO;
import com.ath.adminefectivo.dto.RegistroOperacionConciliacionDTO;
import com.ath.adminefectivo.dto.ParametrosFiltroCostoProcesamientoDTO;
import com.ath.adminefectivo.dto.compuestos.OperacionesLiquidacionProcesamientoDTO;
import com.ath.adminefectivo.dto.compuestos.RegistrosAceptarRechazarListDTO;
import com.ath.adminefectivo.dto.compuestos.RegistrosConciliacionListDTO;
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
			@RequestParam(required = false) String codigoPuntoCargo,
			@RequestParam(required = false) String nombrePuntoCargo,
			@RequestParam(required = false) String ciudadFondo,
			@RequestParam(required = false) String nombreTipoServicio,
			@RequestParam(required = false) String monedaDivisa, @RequestParam(required = false) String estado,
			Pageable page) {

		var filtrosConciliadasPr = ParametrosFiltroCostoProcesamientoDTO.create(entidad, fechaServicioTransporte, fechaServicioTransporteFinal, identificacionCliente, razonSocial,
	            codigoPuntoCargo, nombrePuntoCargo, ciudadFondo, nombreTipoServicio, monedaDivisa, estado, page);
		
		var consulta = operacionesLiquidacionDelegate.getLiquidacionConciliadaProcesamiento(filtrosConciliadasPr);
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
			@RequestParam(required = false) String codigoPuntoCargo,
			@RequestParam(required = false) String nombrePuntoCargo,
			@RequestParam(required = false) String ciudadFondo,
			@RequestParam(required = false) String nombreTipoServicio,
			@RequestParam(required = false) String monedaDivisa, @RequestParam(required = false) String estado,
			Pageable page) {

		var filtrosNoIdentificadasPr = ParametrosFiltroCostoProcesamientoDTO.create(entidad, fechaServicioTransporte, fechaServicioTransporteFinal, identificacionCliente, razonSocial,
	            codigoPuntoCargo, nombrePuntoCargo, ciudadFondo, nombreTipoServicio, monedaDivisa, estado, page);
		
		var consulta = operacionesLiquidacionDelegate.getLiquidacionRemitidasNoIdentificadasProcesamiento(filtrosNoIdentificadasPr);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	@GetMapping(value = "${endpoints.conciliacion.procesadas.eliminadas}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<Page<OperacionesLiquidacionProcesamientoDTO>>> getEliminadasProcesamiento(
			@RequestParam(required = false) String entidad,
			@RequestParam(required = true) Date fechaServicioTransporte,
			@RequestParam(required = true) Date fechaServicioTransporteFinal,
			@RequestParam(required = false) String identificacionCliente,
			@RequestParam(required = false) String razonSocial,
			@RequestParam(required = false) String codigoPuntoCargo,
			@RequestParam(required = false) String nombrePuntoCargo,
			@RequestParam(required = false) String ciudadFondo,
			@RequestParam(required = false) String nombreTipoServicio,
			@RequestParam(required = false) String monedaDivisa, @RequestParam(required = false) String estado,
			Pageable page) {

		var filtrosEliminadasPr = ParametrosFiltroCostoProcesamientoDTO.create(entidad, fechaServicioTransporte, fechaServicioTransporteFinal, identificacionCliente, razonSocial,
	            codigoPuntoCargo, nombrePuntoCargo, ciudadFondo, nombreTipoServicio, monedaDivisa, estado, page);

		var consulta = operacionesLiquidacionDelegate.getEliminadasProcesamiento(filtrosEliminadasPr);
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
			@RequestParam(required = false) String codigoPuntoCargo,
			@RequestParam(required = false) String nombrePuntoCargo,
			@RequestParam(required = false) String ciudadFondo,
			@RequestParam(required = false) String nombreTipoServicio,
			@RequestParam(required = false) String monedaDivisa, @RequestParam(required = false) String estado,
			Pageable page) {

		var filtrosNoCobradasPr = ParametrosFiltroCostoProcesamientoDTO.create(entidad, fechaServicioTransporte, fechaServicioTransporteFinal, identificacionCliente, razonSocial,
	            codigoPuntoCargo, nombrePuntoCargo, ciudadFondo, nombreTipoServicio, monedaDivisa, estado, page);
		
		var consulta = operacionesLiquidacionDelegate.getLiquidadasNoCobradasProcesamiento(filtrosNoCobradasPr);
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
			@RequestParam(required = false) String codigoPuntoCargo,
			@RequestParam(required = false) String nombrePuntoCargo,
			@RequestParam(required = false) String ciudadFondo,
			@RequestParam(required = false) String nombreTipoServicio,
			@RequestParam(required = false) String monedaDivisa, @RequestParam(required = false) String estado,
			Pageable page) {
		
		var filtrosConDiferenciasPr = ParametrosFiltroCostoProcesamientoDTO.create(entidad, fechaServicioTransporte, fechaServicioTransporteFinal, identificacionCliente, razonSocial,
	            codigoPuntoCargo, nombrePuntoCargo, ciudadFondo, nombreTipoServicio, monedaDivisa, estado, page);

		var consulta = operacionesLiquidacionDelegate.getIdentificadasConDiferenciasProcesamiento(filtrosConDiferenciasPr);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	
	/**
	 * Método encargado de Desconciliar los registros conciliados manualmente
	 * @param registrosDesconciliar
	 * @return ResponseEntity<ApiResponseADE<ValidacionArchivoDTO>>
	 * @author hector.mercado
	 */
	@PutMapping(value = "${endpoints.conciliacion.procesadas.desconciliar}")
	public ResponseEntity<ApiResponseADE<List<RegistroOperacionConciliacionDTO>>> desconciliar(
			@RequestBody RegistrosConciliacionListDTO registrosDesconciliar) {

		var respuesta = operacionesLiquidacionDelegate.desconciliar(registrosDesconciliar);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(respuesta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Método encargado de Aceptar los registros remitidos por la transportadora
	 * @param idMaestroDefinicion
	 * @param nombreArchivo
	 * @return ResponseEntity<ApiResponseADE<ValidacionArchivoDTO>>
	 * @author hector.mercado
	 */
	@PutMapping(value = "${endpoints.conciliacion.procesadas.remitidas-aceptar-rechazar}")
	public ResponseEntity<ApiResponseADE<List<RegistroOperacionConciliacionDTO>>> remitidasAceptarRechazar(
			@RequestBody RegistrosConciliacionListDTO registrosAceptar) {

		var respuesta = operacionesLiquidacionDelegate.remitidasAceptarRechazar(registrosAceptar);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(respuesta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	@PutMapping(value = "${endpoints.conciliacion.procesadas.identificadas-con-diferencia-aceptar-rechazar}")
	public ResponseEntity<ApiResponseADE<List<RegistroAceptarRechazarDTO>>> identificadasConDiferenciaAceptarRechazar(
			@RequestBody RegistrosAceptarRechazarListDTO registrosAceptar) {

		var respuesta = operacionesLiquidacionDelegate.identificadasConDiferenciaAceptarRechazar(registrosAceptar);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(respuesta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Método encargado de Aceptar los registros remitidos por la transportadora
	 * @param RegistrosConciliacionListDTO
	 * @return List<RegistroOperacionConciliacionDTO>
	 * @author hector.mercado
	 */
	@PutMapping(value = "${endpoints.conciliacion.procesadas.liquidadas-eliminar-rechazar}")
	public ResponseEntity<ApiResponseADE<List<RegistroOperacionConciliacionDTO>>> liquidadasEliminarRechazar(
			@RequestBody RegistrosConciliacionListDTO registrosEliminar) {

		var respuesta = operacionesLiquidacionDelegate.liquidadasEliminarRechazar(registrosEliminar);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(respuesta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Método encargado de Reintegrar registros liquidados eliminados
	 * @param RegistrosConciliacionListDTO
	 * @return List<RegistroOperacionConciliacionDTO>
	 * @author jose.pabon
	 */
	@PutMapping(value = "${endpoints.conciliacion.procesadas.liquidadas-reintegrar}")
	public ResponseEntity<ApiResponseADE<List<RegistroOperacionConciliacionDTO>>> reintegrarLiquidadas(
			@RequestBody RegistrosConciliacionListDTO registrosConciliación) {

		var respuesta = operacionesLiquidacionDelegate.reintegrarLiquidadas(registrosConciliación);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(respuesta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
}
