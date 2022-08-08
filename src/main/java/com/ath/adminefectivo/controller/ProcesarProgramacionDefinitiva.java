package com.ath.adminefectivo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.delegate.IProgramacionDefinitivaDelegate;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;

/**
 * Controlador responsable de procesar los archivos de programacion definitiva
 * fondos, oficinas y cajeros
 * @author cesar.castano
 */
@RestController
@RequestMapping("${endpoints.ProgramacionDefinitiva}")
public class ProcesarProgramacionDefinitiva {

	@Autowired
	IProgramacionDefinitivaDelegate programacionDefinitivaDelegate;
	
	/**
	 * Servicio encargado de procesar los archivos cargados de fondos, oficinas y cajeros y
	 * persistirlos en la tabla de operaciones programadas
	 * @return ResponseEntity<ApiResponseADE<Boolean>>
	 * @author cesar.castano
	 */
	@GetMapping(value = "${endpoints.ProgramacionDefinitiva.procesar}")
	public ResponseEntity<ApiResponseADE<Boolean>> procesarProgramacionDefinitiva(
			@RequestParam("modeloArchivo") String modeloArchivo,
			@RequestParam("idArchivo") Long idArchivo) {

		Boolean respuesta = programacionDefinitivaDelegate.procesarProgramacionDefinitiva(modeloArchivo, idArchivo);
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(respuesta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));	
	}
}

