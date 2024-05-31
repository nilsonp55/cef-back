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
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.dto.AuditoriaLoginDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.CuentasPuc;
import com.ath.adminefectivo.service.AuditoriaLoginService;
import com.querydsl.core.types.Predicate;

/**
 * Controlador responsable de exponer los metodos referentes a la Auditoria de login 
 * @author bayron.perez
 */
@RestController
@RequestMapping("${endpoints.AuditoriaLogin}")
public class AuditoriaLoginController {

	@Autowired
	private AuditoriaLoginService auditoriaLoginService;
	
	
	/**
	 * Servicio encargado de retornar la consulta de todos los AuditoriaLogin
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<AuditoriaLoginDTO>>>
	 * @author bayron.perez
	 */
	@GetMapping(value = "${endpoints.AuditoriaLogin.consultar}")
	public ResponseEntity<ApiResponseADE<List<AuditoriaLoginDTO>>> getAllAuditoriaLogin(@QuerydslPredicate(root = CuentasPuc.class) Predicate predicate) {
		List<AuditoriaLoginDTO> consulta = auditoriaLoginService.getAllAuditoriaLogin(predicate);
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
	@PostMapping(value = "${endpoints.AuditoriaLogin.guardar}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<AuditoriaLoginDTO>> postAuditoriaLogin(@RequestBody AuditoriaLoginDTO auditoriaLoginDTO) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<AuditoriaLoginDTO>(auditoriaLoginService.saveAuditoriaLogin(AuditoriaLoginDTO.CONVERTER_ENTITY.apply(auditoriaLoginDTO)),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));

	}
}
