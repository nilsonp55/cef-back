package com.ath.adminefectivo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.dto.FondosDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.Fondos;
import com.ath.adminefectivo.service.IFondosService;
import com.querydsl.core.types.Predicate;

/**
 * Controlador responsable de exponer los metodos referentes a las Fondos 
 * @author cesar.castano
 */
@RestController
@RequestMapping("${endpoints.Fondos}")
public class FondosController {

	@Autowired
	IFondosService fondosService;
	
	/**
	 * Servicio encargado de retornar la consulta de todos las Fondos
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<FondosDTO>>>
	 * @author cesar.castano
	 */
	@GetMapping(value = "${endpoints.Fondos.consultar}")
	public ResponseEntity<ApiResponseADE<List<FondosDTO>>> getFondos(@QuerydslPredicate(root = Fondos.class) Predicate predicate) {
		List<FondosDTO> consulta = fondosService.getFondos(predicate);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}
