package com.ath.adminefectivo.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.delegate.impl.AutorizacionContableDelegate;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;



@RestController
@RequestMapping("${endpoints.AutorizacionCierre}")
public class AutorizacionCierreController {

	@Autowired
	AutorizacionContableDelegate autorizacionContableDelegate;
	
	@GetMapping(value = "${endpoints.AutorizacionCierre.autorizacion}")
	public ResponseEntity<ApiResponseADE<String>> getGenerarContabilidad(
			@RequestParam Date fecha,
			@RequestParam String tipoContabilidad,
			@RequestParam String estado
			) {

		String consulta = autorizacionContableDelegate.autorizacionContable(tipoContabilidad,estado);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}
