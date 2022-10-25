package com.ath.adminefectivo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.dto.DominioDTO;
import com.ath.adminefectivo.dto.DominioMaestroDto;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.Dominio;
import com.ath.adminefectivo.service.impl.DominioServiceImpl;
import com.querydsl.core.types.Predicate;

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
	@GetMapping(value = "${endpoints.DominioFuncional.consultar}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<List<String>>> obtenerDominioMaestroById(
			@RequestParam("dominio") String dominio) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<List<String>>(dominioServiceImpl.consultaListValoresPorDominio(dominio),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
								.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}


	//getDominios
	@GetMapping(value = "${endpoints.DominioFuncional.consultarXDominio}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<List<DominioDTO>>> getDominios(
			@QuerydslPredicate(root = Dominio.class) Predicate predicate) {

		var consulta1 = dominioServiceImpl.getDominios(predicate);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta1, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

}
