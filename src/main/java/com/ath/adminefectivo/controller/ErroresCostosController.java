package com.ath.adminefectivo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.ErroresCostos;
import com.ath.adminefectivo.service.ErroresCostosService;

@RestController
@RequestMapping("${endpoints.ErroresCostos}")
public class ErroresCostosController {

	@Autowired
	ErroresCostosService erroresCostosService;
	
	/**
	 * Metodo encargado de realizar una busqueda de errorew por idSeqGrupo
	 * 
	 * @param idSeqGrupo
	 * @return ResponseEntity<ApiResponseADE<list idSeqGrupo>>
	 * @author bayron.perez
	 */
	@GetMapping(value = "${endpoints.ErroresCostos.consultar}")
	public ResponseEntity<ApiResponseADE<List<ErroresCostos>>> obtenerErroresCostosByIdSeqGrupo(@RequestParam(required = true) Integer idSeqGrupo) {

		List<ErroresCostos> consulta = erroresCostosService.obtenerErroresCostosByIdSeqGrupo(idSeqGrupo);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}
