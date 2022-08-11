package com.ath.adminefectivo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	/**
	 * Consulta un id de proceso diario en repositorio
	 * 
	 * @param id
	 * @return ResponseEntity<ApiResponseADE<LogProcesoDiarioDTO>>
	 * @author cesar.castano
	 */
	@GetMapping(value = "${endpoints.ProcesoDiario.consultar}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<LogProcesoDiarioDTO>> obtenerLogProcesoDiarioById(
			@PathVariable("id") Long id) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<LogProcesoDiarioDTO>(procesoDiarioService.obtenerLogProcesoDiarioById(id),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
								.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Servicio encargado de persistir un log proceso diario
	 * 
	 * @param logProcesoDiarioDTO
	 * @return ResponseEntity<ApiResponseADE<LogProcesoDiarioDTO>>
	 * @author cesar.castano
	 */
	@PostMapping(value = "${endpoints.ProcesoDiario.guardar}", consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<LogProcesoDiarioDTO>> persistirLogProcesoDiario(
									@RequestBody LogProcesoDiarioDTO logProcesoDiarioDTO) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<LogProcesoDiarioDTO>(
						procesoDiarioService.persistirLogProcesoDiario(logProcesoDiarioDTO),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
								.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Servicio encargado de actualizar un log proceso diario 
	 * 
	 * @param logProcesoDiarioDTO
	 * @return ResponseEntity<ApiResponseADE<LogProcesoDiarioDTO>>
	 * @author cesar.castano
	 */
	@PutMapping(value = "${endpoints.ProcesoDiario.guardar}", consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<LogProcesoDiarioDTO>> actualizarLogProcesoDiario(
									@RequestBody LogProcesoDiarioDTO logProcesoDiarioDTO) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<LogProcesoDiarioDTO>(procesoDiarioService.
						actualizarLogProcesoDiario(logProcesoDiarioDTO),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
								.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}


	/**
	 * Servicio encargado de eliminar un log proceso diario por id
	 * 
	 * @param id
	 * @return ResponseEntity<ApiResponseADE<Boolean>>
	 * @author cesar.castano
	 */
	@DeleteMapping(value = "${endpoints.ProcesoDiario.eliminar}/{id}")
	public ResponseEntity<ApiResponseADE<Boolean>> eliminarLogProcesoDiario(@PathVariable("id") Long id) {

		var logProcesoDairioEliminado = procesoDiarioService.eliminarLogProcesoDiario(id);

		return ResponseEntity.status(HttpStatus.OK).body(
				new ApiResponseADE<>(logProcesoDairioEliminado, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}
