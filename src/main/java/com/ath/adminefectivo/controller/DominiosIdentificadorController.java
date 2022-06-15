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
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.delegate.IDominioIdentificadorDelegate;
import com.ath.adminefectivo.dto.DominioIdentificadorDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;

/**
 * Controlador responsable de exponer los metodos referentes a la creacipon de los
 * dominios identificadores parametrizables de la app
 * 
 * @author Bayron Andres Perez Muñoz
 */
@RestController
@RequestMapping("${endpoints.DominiosIdentificador}")
public class DominiosIdentificadorController {
	
	@Autowired
	IDominioIdentificadorDelegate dominioIdentificadorDelegate;
	
	
	/**
	 * Consulta los dominios maestro almacenados en repositorio  filtrados por estado
	 * 
	 * @param predicate
	 * @return ResponseEntity<ApiResponseADE<List<DominioIdentificadorDTO>>>
	 * @author Bayron Andres Perez Muñoz
	 */
	@GetMapping(value = "${endpoints.DominiosIdentificador.obtener-todos}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<List<DominioIdentificadorDTO>>> obtenerDominiosIdentificador(
			@PathVariable("estado") String estado) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<List<DominioIdentificadorDTO>>(dominioIdentificadorDelegate.obtenerDominiosIdentificador(estado),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
								.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Consulta un dominio maestro almacenados en repositorio  filtrados por id unico
	 * 
	 * @param idDominioIdentificador
	 * @return ResponseEntity<ApiResponseADE<LDominioIdentificadorDTO>>
	 * @author Bayron Andres Perez Muñoz
	 */
	@GetMapping(value = "${endpoints.DominiosIdentificador.obtener-unico}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<DominioIdentificadorDTO>> obtenerDominioIdentificadorById(
			@PathVariable("id") Integer id) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<DominioIdentificadorDTO>(dominioIdentificadorDelegate.obtenerDominioIdentificadorById(id),
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
	@PostMapping(value = "${endpoints.DominiosIdentificador.guardar}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<DominioIdentificadorDTO>> persistirDominioIdentificador(@RequestBody DominioIdentificadorDTO dominioIdentificadorDto) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<DominioIdentificadorDTO>(dominioIdentificadorDelegate.persistirDominioIdentificador(dominioIdentificadorDto),
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
	@PutMapping(value = "${endpoints.DominiosIdentificador.guardar}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<DominioIdentificadorDTO>> actualizarDominioIdentificador(@RequestBody DominioIdentificadorDTO dominioIdentificadorDto) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<DominioIdentificadorDTO>(dominioIdentificadorDelegate.persistirDominioIdentificador(dominioIdentificadorDto),
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
	@DeleteMapping(value = "${endpoints.DominiosIdentificador.eliminar}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<Boolean>> eliminarDominioIdentificador(@PathVariable("id") Integer id) {

		var dominioEliminado = dominioIdentificadorDelegate.eliminarDominioIdentificador(id);
		return ResponseEntity.status(HttpStatus.OK).body(
				new ApiResponseADE<>(dominioEliminado, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

}
