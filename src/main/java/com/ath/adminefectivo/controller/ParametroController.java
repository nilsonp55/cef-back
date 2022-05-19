package com.ath.adminefectivo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.delegate.IParametroDelegate;
import com.ath.adminefectivo.dto.ParametroDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.Parametro;
import com.querydsl.core.types.Predicate;

/**
 * Controlador responsable de exponer los metodos referentes a los Parametros 
 * @author CamiloBenavides
 */
@RestController
@RequestMapping("${endpoints.Parametro}")
public class ParametroController {
	
	@Autowired
	IParametroDelegate parametroDelegate;
	

	/**
	 * Servicio encargado de retornar la consulta de todos los Parametros del aplicativo
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<ParametroDTO>>>
	 * @author CamiloBenavides
	 */
	@GetMapping(value = "${endpoints.Parametro.consultar}")
	public ResponseEntity<ApiResponseADE<List<ParametroDTO>>> getParametros(@QuerydslPredicate(root = Parametro.class) Predicate predicate) {
		var consulta = parametroDelegate.getParametros(predicate);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

}
