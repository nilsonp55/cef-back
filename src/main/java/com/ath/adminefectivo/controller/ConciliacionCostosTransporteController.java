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

import com.ath.adminefectivo.delegate.IConciliacionCostosTransporteDelegate;
import com.ath.adminefectivo.dto.ParametrosFiltroCostoTransporteDTO;
import com.ath.adminefectivo.dto.RegistroAceptarRechazarDTO;
import com.ath.adminefectivo.dto.RegistroOperacionConciliacionDTO;
import com.ath.adminefectivo.dto.compuestos.OperacionesLiquidacionTransporteDTO;
import com.ath.adminefectivo.dto.compuestos.RegistrosAceptarRechazarListDTO;
import com.ath.adminefectivo.dto.compuestos.RegistrosConciliacionListDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;

/**
 * Controlador responsable de exponer los metodos referentes al proceso de
 * conciliacion de costos
 * 
 * @author hector.mercado
 */
@RestController
@RequestMapping("${endpoints.conciliacion.transporte}")
public class ConciliacionCostosTransporteController {

	@Autowired
	IConciliacionCostosTransporteDelegate conciliacionCostosTransporteDelegate;
	
	/**
	 * Metodo encargado de retornar la lista de todas los registros conciliados
	 * 
	 * @return Page<ConciliacionCostosTransporteDTO>
	 * @param page
	 * @param predicate
	 * @author hector.mercado
	 */

	@GetMapping(value = "${endpoints.conciliacion.transporte.consultar-conciliadas}",  produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<Page<OperacionesLiquidacionTransporteDTO>>> getliquidacionConciliadaTransporte(
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
		
		var filtrosConciliadasTr = ParametrosFiltroCostoTransporteDTO.create(entidad, fechaServicioTransporte, fechaServicioTransporteFinal, identificacionCliente, razonSocial,
                codigoPuntoCargo, nombrePuntoCargo, ciudadFondo, nombreTipoServicio, monedaDivisa, estado, page);
		
		var consulta = conciliacionCostosTransporteDelegate
				.getLiquidacionConciliadaTransporte(filtrosConciliadasTr);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	@GetMapping(value = "${endpoints.conciliacion.transporte.consultar-remitidasNoIdentificadas}",  produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<Page<OperacionesLiquidacionTransporteDTO>>> getliquidacionRemitidasNoIdentificadasTransporte(
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

		var filtrosNoIdentificadasTr = ParametrosFiltroCostoTransporteDTO.create(entidad, fechaServicioTransporte, fechaServicioTransporteFinal, identificacionCliente, razonSocial,
                codigoPuntoCargo, nombrePuntoCargo, ciudadFondo, nombreTipoServicio, monedaDivisa, estado, page);

		
		var consulta = conciliacionCostosTransporteDelegate
				.getLiquidacionRemitidasNoIdentificadasTransporte(filtrosNoIdentificadasTr);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	@GetMapping(value = "${endpoints.conciliacion.transporte.eliminadas}",  produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<Page<OperacionesLiquidacionTransporteDTO>>> getEliminadasTransporte(
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

		var filtrosEliminadasTr = ParametrosFiltroCostoTransporteDTO.create(entidad, fechaServicioTransporte, fechaServicioTransporteFinal, identificacionCliente, razonSocial,
                codigoPuntoCargo, nombrePuntoCargo, ciudadFondo, nombreTipoServicio, monedaDivisa, estado, page);


		var consulta = conciliacionCostosTransporteDelegate.getEliminadasTransporte(filtrosEliminadasTr);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	
	
	
	@GetMapping(value = "${endpoints.conciliacion.transporte.consultar-liquidadasNoCobradas}",  produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<Page<OperacionesLiquidacionTransporteDTO>>> getliquidacionliquidadasNoCobradasTransporte(
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
		
		var filtrosNoCobradasTr = ParametrosFiltroCostoTransporteDTO.create(entidad, fechaServicioTransporte, fechaServicioTransporteFinal, identificacionCliente, razonSocial,
                codigoPuntoCargo, nombrePuntoCargo, ciudadFondo, nombreTipoServicio, monedaDivisa, estado, page);

		var consulta = conciliacionCostosTransporteDelegate
				.getLiquidadasNoCobradasTransporte(filtrosNoCobradasTr);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	@GetMapping(value = "${endpoints.conciliacion.transporte.consultar-identificadasConDiferencias}",  produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<Page<OperacionesLiquidacionTransporteDTO>>> getliquidacionidentificadasConDiferenciasTransporte(
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
		
		var filtrosConDiferenciasTr = ParametrosFiltroCostoTransporteDTO.create(entidad, fechaServicioTransporte, fechaServicioTransporteFinal, identificacionCliente, razonSocial,
                codigoPuntoCargo, nombrePuntoCargo, ciudadFondo, nombreTipoServicio, monedaDivisa, estado, page);


		var consulta = conciliacionCostosTransporteDelegate
				.getIdentificadasConDiferenciasTransporte(filtrosConDiferenciasTr);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	
	/**
	 * Método encargado de Desconciliar los registros conciliados manualmente
	 * @param RegistrosConciliacionListDTO
	 * @return List<RegistroOperacionConciliacionDTO>
	 * @author hector.mercado
	 */
	@PutMapping(value = "${endpoints.conciliacion.transporte.desconciliar}")
	public ResponseEntity<ApiResponseADE<List<RegistroOperacionConciliacionDTO>>> desconciliar(
			@RequestBody RegistrosConciliacionListDTO registrosDesconciliar) {

		var respuesta = conciliacionCostosTransporteDelegate.desconciliar(registrosDesconciliar);

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
	@PutMapping(value = "${endpoints.conciliacion.transporte.remitidas-aceptar-rechazar}")
	public ResponseEntity<ApiResponseADE<List<RegistroOperacionConciliacionDTO>>> remitidasAceptarRechazar(
			@RequestBody RegistrosConciliacionListDTO registrosAceptar) {

		var respuesta = conciliacionCostosTransporteDelegate.remitidasAceptarRechazar(registrosAceptar);

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
	@PutMapping(value = "${endpoints.conciliacion.transporte.liquidadas-eliminar-rechazar}")
	public ResponseEntity<ApiResponseADE<List<RegistroOperacionConciliacionDTO>>> liquidadasEliminarRechazar(
			@RequestBody RegistrosConciliacionListDTO registrosEliminar) {

		var respuesta = conciliacionCostosTransporteDelegate.liquidadasEliminarRechazar(registrosEliminar);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(respuesta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Método encargado de Aceptar y rechazar los registros remitidos por la transportadora
	 * @param RegistrosAceptarRechazarListDTO
	 * @return List<RegistroAceptarRechazarDTO>
	 * @author jose.pabon
	 */
	@PutMapping(value = "${endpoints.conciliacion.transporte.identificadas-con-diferencia-aceptar-rechazar}")
	public ResponseEntity<ApiResponseADE<List<RegistroAceptarRechazarDTO>>> identificadasConDiferenciaAceptarRechazar(
			@RequestBody RegistrosAceptarRechazarListDTO registrosAceptar) {

		var respuesta = conciliacionCostosTransporteDelegate.identificadasConDiferenciaAceptarRechazar(registrosAceptar);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(respuesta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Método encargado de reintegrar registros liquidados eliminados
	 * @param RegistrosConciliacionListDTO
	 * @return List<RegistroOperacionConciliacionDTO>
	 * @author jose.pabon
	 */
	@PutMapping(value = "${endpoints.conciliacion.transporte.liquidadas-reintegrar}")
	public ResponseEntity<ApiResponseADE<List<RegistroOperacionConciliacionDTO>>> reintegrarLiquidadas(
			@RequestBody RegistrosConciliacionListDTO registrosConciliacion) {

		var respuesta = conciliacionCostosTransporteDelegate.reintegrarLiquidadasTransporte(registrosConciliacion);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(respuesta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
}
