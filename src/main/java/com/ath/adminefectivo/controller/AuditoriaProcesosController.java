package com.ath.adminefectivo.controller;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.dto.AuditoriaProcesosDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.service.IAuditoriaProcesosService;

import lombok.extern.log4j.Log4j2;

/**
 * Controlador responsable de exponer los metodos referentes a la auditoria
 * Procesos
 * 
 * @author duvan.naranjo
 */
@RestController
@RequestMapping("${endpoints.AuditoriaProcesos}")
@Log4j2
public class AuditoriaProcesosController {

	@Autowired
	IAuditoriaProcesosService auditoriaProcesosService;

	/**
	 * Servicio encargado de retornar la consulta de un proceso por codigoProceso y
	 * fechaSistema
	 * 
	 * @return ResponseEntity<ApiResponseADE<AuditoriaProcesosDTO>>
	 * @author duvan.naranjo
	 */
	@GetMapping(value = "${endpoints.AuditoriaProcesos.consultaPorProceso}")
	public ResponseEntity<ApiResponseADE<AuditoriaProcesosDTO>> consultarAuditoriaPorProceso(
			@RequestParam String codigoProceso, @RequestParam Date fechaSistema) {
		log.debug("Auditoria proceso - codigo: {} - fecha: {}", codigoProceso, fechaSistema);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(
						auditoriaProcesosService.consultarAuditoriaPorProceso(codigoProceso),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
								.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Realiza la consulta en la base de datos para generar la lista de fechas que se han 
	 * procesado y/o generado por el proceso de cierre de fecha proceso.
	 * @return Lista de Fechas procesadas en la aplicacion
	 * @author prv_nparra
	 */
	@GetMapping(value = "${endpoints.AuditoriaProcesos.consultarFechasProcesadas}")
	public ResponseEntity<ApiResponseADE<List<Date>>> consultarFechasProcesadas() {
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(
						auditoriaProcesosService.consultarFechasProcesadas(),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
								.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Servicio encargado de crear un nuevo registro de AuditoriaProcesos
	 * 
	 * @param auditoriaProcesosDTO
	 * @return ResponseEntity<ApiResponseADE<Void>>
	 */
	@PostMapping(value = "${endpoints.AuditoriaProcesos.crud}")
	public ResponseEntity<ApiResponseADE<AuditoriaProcesosDTO>> crearAuditoriaProceso(
			@RequestBody AuditoriaProcesosDTO auditoriaProcesosDTO) {
		log.debug("Creando AuditoriaProceso - id: {}", auditoriaProcesosDTO.getId());

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponseADE<>(auditoriaProcesosService.crearAuditoriaProceso(auditoriaProcesosDTO),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
								.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * 
	 * @param codigoProceso
	 * @param fechaProceso
	 * @return
	 */
	@DeleteMapping(value = "${endpoints.AuditoriaProcesos.crud}")
	public ResponseEntity<ApiResponseADE<Void>> deleteAuditoriaProceso(
			@PathVariable @NotNull @Valid String codigoProceso, @PathVariable @NotNull @Valid Date fechaProceso) {
		log.debug("Eliminando AuditoriaProceso - codigoProceso: {} - fechaProceso: {}", codigoProceso, codigoProceso);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT)
				.body(new ApiResponseADE<>(null,
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
								.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}
