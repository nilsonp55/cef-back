package com.ath.adminefectivo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.delegate.IFuncionesDinamicasDelegate;
import com.ath.adminefectivo.dto.FuncionesDinamicasDTO;
import com.ath.adminefectivo.dto.compuestos.RequestFuncionesDinamicasDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;

/**
 * Controlador responsable de exponer los metodos referentes a las Funciones Dinamicas
 * @author duvan.naranjo
 */
@RestController
@RequestMapping("${endpoints.FuncionesDinamicas}")
public class FuncionesDinamicasController {

	@Autowired
	IFuncionesDinamicasDelegate funcionesDinamicasDelegate;
	
	/**
	 * Servicio encargado de retornar la consulta de todas las funciones 
	 * dinamicas activas registradas
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<FuncionesDinamicasDTO>>>
	 * @author duvan.naranjo
	 */
	@GetMapping(value = "${endpoints.FuncionesDinamicas.consultar}")
	public ResponseEntity<ApiResponseADE<List<FuncionesDinamicasDTO>>> obtenerFuncionesDinamicas() {
		
		List<FuncionesDinamicasDTO> consulta = funcionesDinamicasDelegate.obtenerFuncionesDinamicasActivas();
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Controlador encargado de retornar la ejecutar la funcion recibida con
	 * los parametros permitidos
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<String>>>
	 * @author duvan.naranjo
	 */
	@PostMapping(value = "${endpoints.FuncionesDinamicas.ejecutar}", consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<List<String>>> ejecutarFuncionDinamica(@RequestBody RequestFuncionesDinamicasDTO requestFuncionesDinamicasDTO) {
		
		List<String> consulta = funcionesDinamicasDelegate.ejecutarFuncionDinamica(requestFuncionesDinamicasDTO.getIdFuncion(), requestFuncionesDinamicasDTO.getParametros());
		
		return ResponseEntity.status(HttpStatus.OK)	
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}
