package com.ath.adminefectivo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.delegate.IDesconciliacionOperacionesDelegate;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;

/**
 * Controlador responsable de exponer los metodos referentes al proceso de
 * desconciliacion
 * 
 * @author cesar.castano
 */
@RestController
@RequestMapping("${endpoints.conciliacion}")
public class DesconciliacionOperacionesController {

	@Autowired
	IDesconciliacionOperacionesDelegate desconciliacionOperacionesDelegate;
	
	/**
	 * Controlador que permite persistir las operaciones a desconciliar
	 * @param List<Integer>
	 * @return Boolean
	 * @author cesar.castano
	 */
	@PostMapping(value = "${endpoints.conciliacion.desconciliacion}", 
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<Boolean>> procesoDesconciliacion(
			@RequestBody(required = true) List<Integer> operacionesADesconciliar) {

		Boolean respuesta = desconciliacionOperacionesDelegate.procesoDesconciliacion(operacionesADesconciliar);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(respuesta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}
