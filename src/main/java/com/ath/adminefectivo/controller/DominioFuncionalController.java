package com.ath.adminefectivo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.dto.DominioMaestroDto;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.service.impl.DominioServiceImpl;

/**
 * Controlador responsable de los unicos servicios de dominios que exiisten 
 * @author bayronPerez
 */
@RestController
@RequestMapping("${endpoints.DominioFuncional}")
public class DominioFuncionalController {

	@Autowired
	private DominioServiceImpl dominioServiceImpl;
	
	
	/**
	 * Consulta un dominio maestro almacenados en repositorio  filtrados por id unico
	 * 
	 * @param idDominioMaestro
	 * @return ResponseEntity<ApiResponseADE<LDominioMaestroDto>>
	 * @author Bayron Andres Perez Muñoz
	 */
	@GetMapping(value = "${endpoints.DominioFuncional.consultar}/{dominio}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<List<String>>> obtenerDominioMaestroById(
			@PathVariable("dominio") String dominio) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<List<String>>(dominioServiceImpl.consultaListValoresPorDominio(dominio),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
								.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}
