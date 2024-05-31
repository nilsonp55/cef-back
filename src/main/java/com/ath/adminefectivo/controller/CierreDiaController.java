package com.ath.adminefectivo.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.delegate.ICierreDiaDelegate;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;

/**
 * Controlador responsable de exponer los servicos del proceso de cierre de dia
 * 
 * @author CamiloBenavides
 */
@RestController
@RequestMapping("${endpoints.CierreDia}")
public class CierreDiaController {

	@Autowired
	ICierreDiaDelegate cierreDiaDelegate;

	/**
	 * Servicio encargado de realizar el cierre de dia, modificando el parametros de
	 * dia actual por el siguiente dia habil, si se cumplen las condiciones de
	 * cierre.
	 * 
	 * @return ResponseEntity<ApiResponseADE<Date>>
	 * @author CamiloBenavides
	 */
	@PostMapping(value = "${endpoints.CierreDia.cerrar}")
	public ResponseEntity<ApiResponseADE<Date>> cerrarDia() {
		var consulta = cierreDiaDelegate.cerrarDia();
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

}
