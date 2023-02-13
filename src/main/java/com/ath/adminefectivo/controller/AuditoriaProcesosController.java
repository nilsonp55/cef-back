package com.ath.adminefectivo.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.delegate.IAuditoriaProcesosDelegate;
import com.ath.adminefectivo.delegate.IBancosDelegate;
import com.ath.adminefectivo.dto.AuditoriaProcesosDTO;
import com.ath.adminefectivo.dto.BancosDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.Bancos;
import com.querydsl.core.types.Predicate;

/**
 * Controlador responsable de exponer los metodos referentes a la auditoria Procesos
 * @author duvan.naranjo
 */
@RestController
@RequestMapping("${endpoints.AuditoriaProcesos}")
public class AuditoriaProcesosController {

	@Autowired
	IAuditoriaProcesosDelegate auditoriaProcesosDelegate;
	
	/**
	 * Servicio encargado de retornar la consulta de un proceso por 
	 * fecha
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<BancosDTO>>>
	 * @author duvan.naranjo
	 */
	@GetMapping(value = "${endpoints.AuditoriaProcesos.consultaPorProceso}")
	public ResponseEntity<ApiResponseADE<AuditoriaProcesosDTO>> consultarAuditoriaPorProceso(@RequestParam String codigoProceso, @RequestParam Date fechaSistema) {
		AuditoriaProcesosDTO consulta = auditoriaProcesosDelegate.consultarAuditoriaPorProceso(codigoProceso, fechaSistema);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}
