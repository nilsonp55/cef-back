package com.ath.adminefectivo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.dto.TransportadorasDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.Transportadoras;
import com.ath.adminefectivo.service.ITransportadorasService;
import com.querydsl.core.types.Predicate;

/**
 * Controlador responsable de exponer los metodos referentes a las Transportadoras
 * @author cesar.castano
 */
@RestController
@RequestMapping("${endpoints.Transportadoras}")
public class TransportadorasController {

	@Autowired
	ITransportadorasService transportadorasService;

	/**
	 * Servicio encargado de retornar la consulta de todos las Transportadoras
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<TransportadorasDTO>>>
	 * @param page
	 * @author cesar.castano
	 */
	@GetMapping(value = "${endpoints.Transportadoras.consultar}")
	public ResponseEntity<ApiResponseADE<List<TransportadorasDTO>>> getTransportadoras(
			@QuerydslPredicate(root = Transportadoras.class) Predicate predicate) {
		
		List<TransportadorasDTO> consulta = transportadorasService.getTransportadoras(predicate);
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}
