package com.ath.adminefectivo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.dto.CiudadesDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.Ciudades;
import com.ath.adminefectivo.service.ICiudadesService;
import com.querydsl.core.types.Predicate;

/**
 * Controlador responsable de exponer los metodos referentes a las Ciudades 
 * @author cesar.castano
 */
@RestController
@RequestMapping("${endpoints.Ciudad}")
public class CiudadesController {

	@Autowired
	ICiudadesService ciudadesService;
	
	/**
	 * Servicio encargado de retornar la consulta de todos las Ciudades
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<CiudadDTO>>>
	 * @author cesar.castano
	 */
	@GetMapping(value = "${endpoints.Ciudad.consultar}")
	public ResponseEntity<ApiResponseADE<List<CiudadesDTO>>> getCiudades(@QuerydslPredicate(root = Ciudades.class) Predicate predicate) {
		List<CiudadesDTO> consulta = ciudadesService.getCiudades(predicate);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}
