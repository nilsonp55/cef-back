package com.ath.adminefectivo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.dto.CentroCiudadDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.CentroCiudad;
import com.ath.adminefectivo.service.ICentroCiudadService;
import com.querydsl.core.types.Predicate;

/**
 * Controller para gestionar los CentroCiudad
 * @author cesar.castano
 */

@RestController
@RequestMapping("${endpoints.CentroCiudad}")
public class CentroCiudadController {

	@Autowired
	ICentroCiudadService centroCiudadService;

	/**
	 * Servicio encargado de retornar la consulta de todos los CentroCiudad
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<CentroCiudadDTO>>>
	 * @author cesar.castano
	 */
	@GetMapping(value = "${endpoints.CentroCiudad.consultar}")
	public ResponseEntity<ApiResponseADE<List<CentroCiudadDTO>>> getCentroCiudad(@QuerydslPredicate(root = CentroCiudad.class) Predicate predicate) {
		List<CentroCiudadDTO> consulta = centroCiudadService.getCentrosCiudades(predicate);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Servicio encargado de retornar la consulta de una tiposCuenta
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<CentroCiudadDTO>>>
	 * @author cesar.castano
	 */
	@GetMapping(value = "${endpoints.CentroCiudad.consultarByCiudadAndBanco}")
	public ResponseEntity<ApiResponseADE<CentroCiudadDTO>> getCentroCiudadById(@RequestParam(required = true) Integer idCentroCiudad,
			@RequestParam(required = true) Integer bancoAval) {
		
		CentroCiudadDTO consulta = centroCiudadService.getCentroCiudadById(idCentroCiudad);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Servicio encargado de guardar tiposCuenta
	 * 
	 * @return ResponseEntity<ApiResponseADE<CentroCiudadDTO>>
	 * @author Bayron Andres Perez M
	 */
	@PostMapping(value = "${endpoints.CentroCiudad.guardar}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<CentroCiudadDTO>> postCentroCiudad(@RequestBody CentroCiudadDTO centroCiudadDTO) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<CentroCiudadDTO>(centroCiudadService.postCentroCiudad(centroCiudadDTO),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));

	}
	
	/**
	 * Servicio encargado de guardar tiposCuenta
	 * 
	 * @return ResponseEntity<ApiResponseADE<CentroCiudadDTO>>
	 * @author Bayron Andres Perez M
	 */
	@PostMapping(value = "${endpoints.CentroCiudad.actualizar}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<CentroCiudadDTO>> putCentroCiudad(@RequestBody CentroCiudadDTO centroCiudadDTO) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<CentroCiudadDTO>(centroCiudadService.putCentroCiudad(centroCiudadDTO),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));

	}
	
}
