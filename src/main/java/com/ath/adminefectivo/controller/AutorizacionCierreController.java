package com.ath.adminefectivo.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.delegate.impl.AutorizacionContableDelegate;
import com.ath.adminefectivo.dto.LogProcesoDiarioDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;



@RestController
@RequestMapping("${endpoints.AutorizacionCierre}")
public class AutorizacionCierreController {

	@Autowired
	AutorizacionContableDelegate autorizacionContableDelegate;
	
	@PostMapping(value = "${endpoints.AutorizacionCierre.autorizacion}")
	public ResponseEntity<ApiResponseADE<LogProcesoDiarioDTO>> getGenerarContabilidad(
			@RequestParam(value = "fecha") Date fecha,
			@RequestParam(value = "tipoContabilidad") String tipoContabilidad,
			@RequestParam(value = "estado") String estado
			) {

		LogProcesoDiarioDTO consulta = autorizacionContableDelegate.autorizacionContable(fecha,tipoContabilidad,estado);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}
