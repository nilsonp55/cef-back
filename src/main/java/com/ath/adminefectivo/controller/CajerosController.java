package com.ath.adminefectivo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.dto.CajerosDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.Cajeros;
import com.ath.adminefectivo.service.ICajerosService;
import com.querydsl.core.types.Predicate;

/**
 * Controlador responsable de exponer los metodos referentes a los Cajeros
 * @author cesar.castano
 */
@RestController
@RequestMapping("${endpoints.Cajeros}")
public class CajerosController {

	@Autowired
	ICajerosService cajerosService;
	
	/**
	 * Servicio encargado de retornar la consulta de todos los Cajeros
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<CajerosDTO>>>
	 * @author cesar.castano
	 */
	@GetMapping(value = "${endpoints.Cajeros.consultar}")
	public ResponseEntity<ApiResponseADE<List<CajerosDTO>>> getCajeros(
			@QuerydslPredicate(root = Cajeros.class) Predicate predicate) {
		
		List<CajerosDTO> consulta = cajerosService.getCajeros(predicate);
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}
