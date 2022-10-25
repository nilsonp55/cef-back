package com.ath.adminefectivo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.delegate.IDominioMaestroDelegate;
import com.ath.adminefectivo.dto.DominioMaestroDto;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;

/**
 * Controlador responsable de exponer los metodos referentes a la creacioon de los
 * dominios maestros parametrizables de la app
 * 
 * @author Bayron Andres Perez Muñoz
 */
@RestController
@RequestMapping("${endpoints.DominiosMaestro}")
public class DominiosMaestroController {

	@Autowired
	IDominioMaestroDelegate dominioMaestroDelegate;
	
	
	/**
	 * Consulta los dominios maestro almacenados en repositorio  filtrados por estado
	 * 
	 * @param predicate
	 * @return ResponseEntity<ApiResponseADE<List<DominioMaestroDto>>>
	 * @author Bayron Andres Perez Muñoz
	 */
	@GetMapping(value = "${endpoints.DominiosMaestro.obtener-todos}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<List<DominioMaestroDto>>> obtenerDominiosMaestro(
			@RequestParam("estado") String estado) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<List<DominioMaestroDto>>(dominioMaestroDelegate.obtenerDominiosMaestro(estado),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
								.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	@GetMapping(value = "${endpoints.DominiosMaestro.obtener}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<List<DominioMaestroDto>>> obtenerTodosDominiosMaestro() {
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<List<DominioMaestroDto>>(dominioMaestroDelegate.obtenerTodosDominiosMaestro(),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
								.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Consulta un dominio maestro almacenados en repositorio  filtrados por id unico
	 * 
	 * @param idDominioMaestro
	 * @return ResponseEntity<ApiResponseADE<LDominioMaestroDto>>
	 * @author Bayron Andres Perez Muñoz
	 */
	@GetMapping(value = "${endpoints.DominiosMaestro.obtener-unico}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<DominioMaestroDto>> obtenerDominioMaestroById(
			@PathVariable("id") String id) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<DominioMaestroDto>(dominioMaestroDelegate.obtenerDominioMaestroById(id),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
								.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Servicio encargado de persistir un dominio para ser parametrizado en DB 
	 * 
	 * @param files
	 * @return ResponseEntity<ApiResponseADE<Boolean>>
	 * @author Bayron Andres Perez Muñoz
	 */
	@PostMapping(value = "${endpoints.DominiosMaestro.guardar}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<DominioMaestroDto>> persistirDominioMaestro(@RequestBody DominioMaestroDto dominioMaestroDto) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<DominioMaestroDto>(dominioMaestroDelegate.persistirDominioMaestro(dominioMaestroDto),
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
	@PutMapping(value = "${endpoints.DominiosMaestro.actualizar}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<DominioMaestroDto>> actualizarDominioMaestro(@RequestBody DominioMaestroDto dominioMaestroDto) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<DominioMaestroDto>(dominioMaestroDelegate.actualizarDominioMaestro(dominioMaestroDto),
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
	@DeleteMapping(value = "${endpoints.DominiosMaestro.eliminar}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<Boolean>> eliminarDominioMaestro(@PathVariable("id") String id) {

		var dominioEliminado = dominioMaestroDelegate.eliminarDominioMaestro(id);
		return ResponseEntity.status(HttpStatus.OK).body(
				new ApiResponseADE<>(dominioEliminado, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

}
