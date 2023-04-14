package com.ath.adminefectivo.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
						auditoriaProcesosService.consultarAuditoriaPorProceso(codigoProceso, fechaSistema),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
								.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}
