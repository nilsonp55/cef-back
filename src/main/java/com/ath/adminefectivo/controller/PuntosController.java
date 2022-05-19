package com.ath.adminefectivo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.delegate.IPuntosDelegate;
import com.ath.adminefectivo.dto.PuntosDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.Puntos;
import com.querydsl.core.types.Predicate;

/**
 * Controlador responsable de exponer los metodos referentes a los Puntos 
 * @author cesar.castano
 */
@RestController
@RequestMapping("${endpoints.Puntos}")
public class PuntosController {

	@Autowired
	IPuntosDelegate puntosDelegate;
	
	/**
	 * Servicio encargado de retornar la consulta de todos los Puntos
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<PuntosDTO>>>
	 * @author cesar.castano
	 */
	@GetMapping(value = "${endpoints.Puntos.consultar}")
	public ResponseEntity<ApiResponseADE<List<PuntosDTO>>> getPuntos(@QuerydslPredicate(root = Puntos.class) Predicate predicate) {
		List<PuntosDTO> consulta = puntosDelegate.getPuntos(predicate);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}
