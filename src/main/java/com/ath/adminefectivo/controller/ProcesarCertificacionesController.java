package com.ath.adminefectivo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.delegate.ICertificacionesDelegate;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;

/**
 * Controlador responsable de procesar los archivos de certificaciones
 * @author cesar.castano
 */
@RestController
@RequestMapping("${endpoints.Certificaciones}")
public class ProcesarCertificacionesController {

	@Autowired
	ICertificacionesDelegate certificacionesDelegate;
	
	/**
	 * Servicio encargado de procesar los archivos cargados de certificaciones y
	 * persistirlos en la tabla de operaciones certificadas
	 * @param agrupador
	 * @return ResponseEntity<ApiResponseADE<Boolean>>
	 * @author cesar.castano
	 */
	@GetMapping(value = "${endpoints.Certificaciones.procesar}")
	public ResponseEntity<ApiResponseADE<Boolean>> procesarCertificaciones(
			@RequestParam("agrupador") String agrupador) {

		Boolean respuesta = certificacionesDelegate.procesarCertificaciones(agrupador);
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(respuesta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));	
	}
	
	/**
	 * Servicio encargado de procesar los archivos cargados por alcance de certificaciones 
	 * @return ResponseEntity<ApiResponseADE<String>>
	 * @author rafael.parra
	 */
	@PostMapping(value = "${endpoints.Certificaciones.procesarAlcances}")
	public ResponseEntity<ApiResponseADE<String>> procesarAlcances() {

		String respuesta = certificacionesDelegate.procesarAlcances();
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(respuesta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));	
	}
}
