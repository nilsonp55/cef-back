package com.ath.adminefectivo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.delegate.IBancosDelegate;
import com.ath.adminefectivo.delegate.IFuncionesDinamicasDelegate;
import com.ath.adminefectivo.dto.BancosDTO;
import com.ath.adminefectivo.dto.FuncionesDinamicasDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.Bancos;
import com.querydsl.core.types.Predicate;

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
	@GetMapping(value = "${endpoints.FuncionesDinamicas.ejecutar}")
	public ResponseEntity<ApiResponseADE<List<String>>> ejecutarFuncionDinamica(@RequestParam("idFuncion") int idFuncion, @RequestParam("parametros") List<String> parametros) {
		
		List<String> consulta = funcionesDinamicasDelegate.ejecutarFuncionDinamica(idFuncion, parametros);
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}
