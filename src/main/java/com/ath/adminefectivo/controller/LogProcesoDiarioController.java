package com.ath.adminefectivo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.dto.LogProcesoDiarioDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.LogProcesoDiario;
import com.ath.adminefectivo.service.ILogProcesoDiarioService;
import com.querydsl.core.types.Predicate;

/**
 * Controlador responsable de exponer los metodos referentes a los procesos 
 * diarios en ejecucion
 * @author Bayron Andres Perez M
 */
@RestController
@RequestMapping("${endpoints.ProcesoDiario}")
public class LogProcesoDiarioController {

	@Autowired
	ILogProcesoDiarioService procesoDiarioService;
	
	
	/**
	 * Servicio encargado de retornar la consulta de todos los procesos diarios
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<PuntosDTO>>>
	 * @author cesar.castano
	 */
	@GetMapping(value = "${endpoints.ProcesoDiario.consultar}")
	public ResponseEntity<ApiResponseADE<List<LogProcesoDiarioDTO>>> getLogProcesosDiarios(
			@QuerydslPredicate(root = LogProcesoDiario.class) Predicate predicate) {
		List<LogProcesoDiarioDTO> consulta = procesoDiarioService.getLogsProcesosDierios(predicate);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}
