package com.ath.adminefectivo.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.dto.ParametrosLiquidacionCostoDTO;
import com.ath.adminefectivo.dto.compuestos.CostosCharterDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.service.ICostosFleteCharterService;

import lombok.extern.log4j.Log4j2;

/**
 * Controlador responsable de exponer los metodos referentes al proceso de 
 * Costos por Flete Charter
 * @author cesar.castano
 */
@RestController
@RequestMapping("${endpoints.CostosFleteCharter}")
@Log4j2
public class CostosFleteCharterController {
	
	@Autowired
	ICostosFleteCharterService iCostosFleteCharterService;
	
	/**
	 * Consulta los Costos por Flete Charter
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return ResponseEntity<ApiResponseADE<List<ParametrosLiquidacionCostoDTO>>>
	 * @author cesar.castano
	 */
	@GetMapping(value = "${endpoints.CostosFleteCharter.consultar}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<List<ParametrosLiquidacionCostoDTO>>> consultarCostosFleteCharter(
					@RequestParam("fechaInicial") Date fechaInicial, 
					@RequestParam("fechaFinal") Date fechaFinal) {
		log.debug("fechaInicial "+fechaInicial);
		log.debug("fechaFinal "+fechaFinal);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<List<ParametrosLiquidacionCostoDTO>>(
						iCostosFleteCharterService.consultarCostosFleteCharter(fechaInicial, fechaFinal),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
								.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Actualiza los Costos por Flete Charter
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return ResponseEntity<ApiResponseADE<Boolean>>
	 * @author cesar.castano
	 */
	@PostMapping(value = "${endpoints.CostosFleteCharter.grabar}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<Boolean>> grabarCostosFleteCharter(
			@RequestBody(required = true) CostosCharterDTO costosCharter) {
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<Boolean>(
						iCostosFleteCharterService.grabarCostosFleteCharter(costosCharter),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
								.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}
