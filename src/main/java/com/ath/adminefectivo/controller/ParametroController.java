package com.ath.adminefectivo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.dto.ParametroDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.Parametro;
import com.ath.adminefectivo.service.IParametroService;
import com.querydsl.core.types.Predicate;

/**
 * Controlador responsable de exponer los metodos referentes a los Parametros 
 * @author CamiloBenavides
 */
@RestController
@RequestMapping("${endpoints.Parametro}")
public class ParametroController {
	
	@Autowired
	IParametroService parametroService;
	
	/**
	 * Servicio encargado de retornar la consulta de todos los Parametros del aplicativo
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<ParametroDTO>>>
	 * @author CamiloBenavides
	 */
	@GetMapping(value = "${endpoints.Parametro.consultar}")
	public ResponseEntity<ApiResponseADE<List<ParametroDTO>>> getParametros(@QuerydslPredicate(root = Parametro.class) Predicate predicate) {
		var consulta = parametroService.getParametros(predicate);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Servicio encargado de actualizar parametros
	 * 
	 * @return ResponseEntity<ApiResponseADE<ParametroDTO>>
	 * @author cesar.castano
	 */
	@PostMapping(value = "${endpoints.Parametro.actualizar}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<ParametroDTO>> actualizarValorParametro(@RequestBody @Valid ParametroDTO parametro, BindingResult bindingResult) {

		if (bindingResult.hasFieldErrors()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseADE<>(null, ResponseADE.builder()
					.code(Constantes.CAMPO_REQUERIDO).description(Constantes.CAMPO_REQUERIDO)
					.errors(bindingResult.getFieldErrors().stream()
							.map(error -> error.getField().concat(": ").concat(error.getDefaultMessage())).toList())
					.build()));
		}
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<ParametroDTO>(parametroService.actualizarValorParametro(parametro),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));

	}

}
