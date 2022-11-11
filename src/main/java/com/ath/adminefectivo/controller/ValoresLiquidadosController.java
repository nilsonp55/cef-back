package com.ath.adminefectivo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.delegate.IPuntosDelegate;
import com.ath.adminefectivo.dto.CreatePuntosDTO;
import com.ath.adminefectivo.dto.PuntosDTO;
import com.ath.adminefectivo.dto.ValoresLiquidadosDTO;
import com.ath.adminefectivo.dto.compuestos.RespuestaLiquidarCostosDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.service.impl.ValoresLiquidadosServicioImpl;

/**
 * Controlador responsable de exponer los metodos referentes a los Puntos 
 * @author bayron.perez
 */
@RestController
@RequestMapping("${endpoints.ValoresLiquidacion}")
public class ValoresLiquidadosController {

	@Autowired
	ValoresLiquidadosServicioImpl valoresLiquidadosServicioImpl;

	/**
	 * Servicio encargado de realizar la operacionde costos
	 * 
	 * @return ResponseEntity<Ok>
	 * @author Bayron Andres Perez M
	 */
	@PostMapping(value = "${endpoints.ValoresLiquidacion.costos}", 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<ValoresLiquidadosDTO>> procesarCostos() {
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<ValoresLiquidadosDTO>(valoresLiquidadosServicioImpl.procesarPackageCostos(),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
								.description(ApiResponseCode.SUCCESS.getDescription()).build()));

	}
	
}
