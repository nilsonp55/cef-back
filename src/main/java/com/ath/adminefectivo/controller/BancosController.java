package com.ath.adminefectivo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.delegate.IBancosDelegate;
import com.ath.adminefectivo.dto.BancosDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.Bancos;
import com.querydsl.core.types.Predicate;

/**
 * Controlador responsable de exponer los metodos referentes a los Bancos
 * @author cesar.castano
 */
@RestController
@RequestMapping("${endpoints.Bancos}")
public class BancosController {

	@Autowired
	IBancosDelegate bancosDelegate;
	
	/**
	 * Servicio encargado de retornar la consulta de todos los Bancos
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<BancosDTO>>>
	 * @author cesar.castano
	 */
	@GetMapping(value = "${endpoints.Bancos.consultar}")
	public ResponseEntity<ApiResponseADE<List<BancosDTO>>> getBancos(@QuerydslPredicate(root = Bancos.class) Predicate predicate) {
		List<BancosDTO> consulta = bancosDelegate.getBancos(predicate);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}
