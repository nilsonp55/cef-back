package com.ath.adminefectivo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.delegate.ILogProcesoMensualDelegate;
import com.ath.adminefectivo.dto.LogProcesoDiarioDTO;
import com.ath.adminefectivo.dto.LogProcesoMensualDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.LogProcesoDiario;
import com.ath.adminefectivo.entities.LogProcesoMensual;
import com.ath.adminefectivo.service.ILogProcesoDiarioService;
import com.querydsl.core.types.Predicate;

/**
 * Controlador responsable de exponer los metodos referentes a los procesos 
 * mensuales en ejecucion
 * @author duvan.naranjo
 */
@RestController
@RequestMapping("${endpoints.ProcesoMensual}")
public class LogProcesoMensualController {

	@Autowired
	ILogProcesoMensualDelegate procesoMensualDelegate;
	
	
	/**
	 * Servicio encargado de retornar la consulta de todos los procesos mensuales
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<LogProcesosMensualesDTO>>>
	 * @author duvan.naranjo
	 */
	@GetMapping(value = "${endpoints.ProcesoMensual.consultar}")
	public ResponseEntity<ApiResponseADE<List<LogProcesoMensualDTO>>> getLogProcesosProcesoMensuales(
			@QuerydslPredicate(root = LogProcesoMensual.class) Predicate predicate) {
		List<LogProcesoMensualDTO> consulta = procesoMensualDelegate.getLogsProcesosMensuales(predicate);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Servicio encargado realizar el cierre del proceso mensual recibido
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<LogProcesosMensualesDTO>>>
	 * @author duvan.naranjo
	 */
	@PostMapping(value = "${endpoints.ProcesoMensual.cerrar}")
	public ResponseEntity<ApiResponseADE<String>> cerrarLogProcesoMensual(@RequestParam String proceso) {
		String consulta = procesoMensualDelegate.cerrarLogProcesoMensual(proceso);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}
