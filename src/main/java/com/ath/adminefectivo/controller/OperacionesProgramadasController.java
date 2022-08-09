package com.ath.adminefectivo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	 * @return ResponseEntity<ApiResponseADE<List<BancosDTO>>>
	 * @author duvan.naranjo
	 */
	@GetMapping(value = "${endpoints.OperacionesProgramadas.procesar}/{idArchivo}")
	public ResponseEntity<ApiResponseADE<String>> generarOperacionesProgramadas(@PathVariable("idArchivo") String idArchivo) {
		var resultadoOperacionProgramadas = operacionesProgramadasDelegate.generarOperacionesProgramadas(idArchivo);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(resultadoOperacionProgramadas,
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
								.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}
