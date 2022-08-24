package com.ath.adminefectivo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.delegate.ILiquidarDiaDelegate;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;

/**
 * Controlador responsable de exponer los metodos referentes a la liquidacion de costos
 * 
 * @author cesar.castano
 */
@RestController
@RequestMapping("${endpoints.LiquidarDia}")
public class LiquidarDiaContoller {

	@Autowired
	ILiquidarDiaDelegate liquidarDiaDelegate;
	
	/**
	 * Servicio encargado de procesar la liquidacion de costos TDV
	 * @return ResponseEntity<ApiResponseADE<Boolean>>
	 * @author cesar.castano
	 */
	@GetMapping(value = "${endpoints.LiquidarDia.procesar}")
	public ResponseEntity<ApiResponseADE<Boolean>> procesarLiquidacionCostos() {

		Boolean respuesta = liquidarDiaDelegate.procesarLiquidacionCostos();
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(respuesta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));	
	}
}
