package com.ath.adminefectivo.controller;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
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
	 * Retorna lista de Log Proceso Diario de fecha de proceso activa y/o vigente
	 * @return lista Log Proceso Diario
	 * @author prv_nparra
	 */
	@GetMapping(value = "${endpoints.ProcesoDiario.consultarPorFechaDiaProceso}")
	public ResponseEntity<ApiResponseADE<List<LogProcesoDiarioDTO>>> getLogProcesoDiarioPorFechaProceso() {
		
		List<LogProcesoDiarioDTO> consulta = procesoDiarioService.getFechaProcesoVigente();
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Servicio encargado de retornar la consulta de todos los procesos diarios
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<PuntosDTO>>>
	 * @author cesar.castano
	 */
	@GetMapping(value = "${endpoints.ProcesoDiario.consultar}")
	public ResponseEntity<ApiResponseADE<List<LogProcesoDiarioDTO>>> getLogProcesosDiarios(
			@QuerydslPredicate(root = LogProcesoDiario.class) Predicate predicate) {
		List<LogProcesoDiarioDTO> consulta = procesoDiarioService.getLogsProcesosDiarios(predicate);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Permite crear un nuevo registro en LogProcesoDiario
	 * @param logProcesoDiario registro LogProceso para crear
	 * @return Registro de LogProceso actualizado
	 * @author prv_nparra
	 */
    @PostMapping(value = "${endpoints.ProcesoDiario.guardar}")
    public ResponseEntity<ApiResponseADE<LogProcesoDiarioDTO>> createLogProcesoDiario(
        @RequestBody LogProcesoDiarioDTO logProcesoDiario) {
      LogProcesoDiarioDTO actualizado =  procesoDiarioService.persistirLogProcesoDiario(logProcesoDiario);
      
      return ResponseEntity.status(HttpStatus.OK)
          .body(new ApiResponseADE<>(actualizado, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
                  .description(ApiResponseCode.SUCCESS.getDescription()).build()));
    }
    
    /**
	 * Permite Actualizar un registro existente en LogProcesoDiario
	 * @param logProcesoDiario registro LogProceso para actualizar 
	 * @return Registro de LogProceso actualizado
	 * @author prv_nparra
	 */
    @PutMapping(value = "${endpoints.ProcesoDiario.actualizar}")
    public ResponseEntity<ApiResponseADE<LogProcesoDiarioDTO>> updateLogProcesoDiario(
        @RequestBody LogProcesoDiarioDTO logProcesoDiario) {
      LogProcesoDiarioDTO actualizado =  procesoDiarioService.actualizarLogProcesoDiario(logProcesoDiario);
      
      return ResponseEntity.status(HttpStatus.OK)
          .body(new ApiResponseADE<>(actualizado, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
                  .description(ApiResponseCode.SUCCESS.getDescription()).build()));
    }
    
    /**
     * Eliminar registro de LogProcesoDiario por Id de registro
     * @param idLogProcesoDiario Id del registro en la tabla de LogProceso Diario
     * @return Status HTTP con resultado de la operacion de eliminar registro
     * @author prv_nparra
     */
	@DeleteMapping(value = "${endpoints.ProcesoDiario.eliminar}")
	public ResponseEntity<ApiResponseADE<Object>> deleteLogProcesoDiario(
			@PathVariable @NotNull @Valid Long idLogProcesoDiario) {

		Boolean resultado = procesoDiarioService.eliminarLogProcesoDiario(idLogProcesoDiario);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<Object>(resultado, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Consulta la tabla de LogProcesoDiario para retornar una lista de fechas procesadas, 
	 * representando las fechas en las cuales se ha realizado procesos
	 * @return Lista de fechas (Date) 
	 * @author prv_nparra
	 */
	@GetMapping(value = "${endpoints.ProcesoDiario.consultarFechasProcesadas}")
	public ResponseEntity<ApiResponseADE<List<Date>>> getLogProcesoDiarioFechasProcesadas() {
		List<Date> resultado = procesoDiarioService.getLogProcesoDiarioFechasProcesadas();
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<List<Date>>(resultado, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	} 
}
