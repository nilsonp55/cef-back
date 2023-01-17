package com.ath.adminefectivo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.delegate.IOperacionesProgramadasDelegate;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;

/**
 * Controlador responsable de exponer los metodos referentes al proceso de
 * Operaciones Programadas
 * 
 * @author duvan.naranjo
 */
@RestController
@RequestMapping("${endpoints.OperacionesProgramadas}")
public class OperacionesProgramadasController {

	@Autowired
	IOperacionesProgramadasDelegate operacionesProgramadasDelegate;

	/**
	 * Metodo encargado de realizar la generación de operaciones programadas para el
	 * proceso de programación preliminar
	 * 
	 * @return ResponseEntity<ApiResponseADE<String>>
	 * @author duvan.naranjo
	 */
	@GetMapping(value = "${endpoints.OperacionesProgramadas.procesar}")
	public ResponseEntity<ApiResponseADE<String>> generarOperacionesProgramadas(
									@RequestParam("agrupador") String agrupador) {
		var resultadoOperacionProgramadas = operacionesProgramadasDelegate.generarOperacionesProgramadas(agrupador);
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(resultadoOperacionProgramadas,
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
								.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Metodo encargado de realizar la reapertura de los cierres
	 * (Cierre Definitivo, Cierre Preliminar, Cierre Certificaciones)
	 *  
	 * @return ResponseEntity<ApiResponseADE<List<String>>>
	 * @author duvan.naranjo
	 */
	@GetMapping(value = "${endpoints.OperacionesProgramadas.reabrir}")
	public ResponseEntity<ApiResponseADE<String>> reabrirCierrePorAgrupador(
									@RequestParam("agrupador") String agrupador) {
		var resultadoProcedimientoReabrir = operacionesProgramadasDelegate.reabrirCierrePorAgrupador(agrupador);
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(resultadoProcedimientoReabrir,
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
								.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}
