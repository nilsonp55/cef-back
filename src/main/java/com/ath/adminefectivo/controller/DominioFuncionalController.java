package com.ath.adminefectivo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.dto.DominioDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.Dominio;
import com.ath.adminefectivo.entities.id.DominioPK;
import com.ath.adminefectivo.service.IDominioService;
import com.querydsl.core.types.Predicate;

/**
 * Controlador responsable de los unicos servicios de dominios que exiisten 
 * @author bayronPerez
 */
@RestController
@RequestMapping("${endpoints.DominioFuncional}")
public class DominioFuncionalController {
	
	@Autowired
	IDominioService dominioService;

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
				.body(new ApiResponseADE<List<String>>(dominioService.consultaListValoresPorDominio(dominio),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
								.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}


	//getDominios
	@GetMapping(value = "${endpoints.DominioFuncional.consultarXDominio}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<List<DominioDTO>>> getDominios(
			@QuerydslPredicate(root = Dominio.class) Predicate predicate) {

		var consulta1 = dominioService.getDominios(predicate);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta1, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
		
	/**
	 * Servicio encargado de persistir un dominio para ser parametrizado en DB 
	 * 
	 * @param files
	 * @return ResponseEntity<ApiResponseADE<Boolean>>
	 * @author Bayron Andres Perez Muñoz
	 */
	@PostMapping(value = "${endpoints.DominioFuncional.guardar}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<String>> persistirDominio(@RequestBody DominioDTO dominioDto) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<String>(dominioService.persistirDominio(dominioDto),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
								.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Servicio encargado de actualizar un dominio para ser parametrizado en DB 
	 * 
	 * @param files
	 * @return ResponseEntity<ApiResponseADE<Boolean>>
	 * @author Bayron Andres Perez Muñoz
	 */
	@PutMapping(value = "${endpoints.DominioFuncional.guardar}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<String>> actualizarDominio(@RequestBody DominioDTO dominioDto) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<String>(dominioService.persistirDominio(dominioDto),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
								.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	
	/**
	 * Servicio encargado de eliminar un dominio por su codigo indentificador
	 * 
	 * @param idDominio
	 * @return ResponseEntity<ApiResponseADE<Boolean>>
	 * @author Bayron Andres Perez Muñoz
	 */
	@DeleteMapping(value = "${endpoints.DominioFuncional.eliminar}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<Boolean>> eliminarDominioIdentificador(@RequestParam("dominio") String dominio, @RequestParam("codigo") String codigo) {

		var dominioEliminado = dominioService.eliminarDominio(new DominioPK(dominio, codigo));
		return ResponseEntity.status(HttpStatus.OK).body(
				new ApiResponseADE<>(dominioEliminado, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

}
