package com.ath.adminefectivo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.dto.CuentasPucDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.CuentasPuc;
import com.ath.adminefectivo.service.ICuentasPucService;
import com.querydsl.core.types.Predicate;

/**
 * Controlador responsable de exponer los metodos referentes a las CuentasPuc 
 * @author cesar.castano
 */
@RestController
@RequestMapping("${endpoints.Cuentaspuc}")
public class CuentasPucController {

	@Autowired
	ICuentasPucService cuentasPucService;

	/**
	 * Servicio encargado de retornar la consulta de todos los CuentasPuc
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<CuentasPucDTO>>>
	 * @author cesar.castano
	 */
	@GetMapping(value = "${endpoints.Cuentaspuc.consultar}")
	public ResponseEntity<ApiResponseADE<List<CuentasPucDTO>>> getCuentasPuc(@QuerydslPredicate(root = CuentasPuc.class) Predicate predicate) {
		List<CuentasPucDTO> consulta = cuentasPucService.getCuentasPuc(predicate);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Servicio encargado de retornar la consulta de una cuentaPuc
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<CuentasPucDTO>>>
	 * @author cesar.castano
	 */
	@GetMapping(value = "${endpoints.Cuentaspuc.consultarById}")
	public ResponseEntity<ApiResponseADE<CuentasPucDTO>> getCuentasPucById(@RequestParam(required = true) Long idCuentasPuc) {
		CuentasPucDTO consulta = cuentasPucService.getCuentasPucById(idCuentasPuc);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Servicio encargado de guardar cuentaPuc
	 * 
	 * @return ResponseEntity<ApiResponseADE<CuentasPucDTO>>
	 * @author Bayron Andres Perez M
	 */
	@PostMapping(value = "${endpoints.Cuentaspuc.guardar}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<CuentasPucDTO>> postCuentasPuc(@RequestBody CuentasPucDTO cuentasPucDTO) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<CuentasPucDTO>(cuentasPucService.postCuentasPuc(cuentasPucDTO),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));

	}
	
	/**
	 * Servicio encargado de guardar cuentaPuc
	 * 
	 * @return ResponseEntity<ApiResponseADE<CuentasPucDTO>>
	 * @author Bayron Andres Perez M
	 */
	@PostMapping(value = "${endpoints.Cuentaspuc.actualizar}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<CuentasPucDTO>> putCuentasPuc(@RequestBody CuentasPucDTO cuentasPucDTO) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<CuentasPucDTO>(cuentasPucService.putCuentasPuc(cuentasPucDTO),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));

	}
	
}
