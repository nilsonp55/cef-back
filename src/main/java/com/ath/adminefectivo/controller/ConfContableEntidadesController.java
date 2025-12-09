package com.ath.adminefectivo.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.dto.ConfContableEntidadesDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.ConfContableEntidades;
import com.ath.adminefectivo.service.IConfContableEntidadesService;
import com.querydsl.core.types.Predicate;

/**
 * Controlador responsable de exponer los metodos referentes a las ConfContableEntidades 
 * @author bayron.perez
 */

@RestController
@RequestMapping("${endpoints.ConfContableEntidades}")
public class ConfContableEntidadesController {

	@Autowired
	IConfContableEntidadesService confContableEntidadesService;

	/**
	 * Servicio encargado de retornar la consulta de todos los ConfContableEntidades
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<ConfContableEntidadesDTO>>>
	 * @author cesar.castano
	 */
	@GetMapping(value = "${endpoints.ConfContableEntidades.consultar}")
	public ResponseEntity<ApiResponseADE<List<ConfContableEntidadesDTO>>> getConfContableEntidades(@QuerydslPredicate(root = ConfContableEntidades.class) Predicate predicate) {
		List<ConfContableEntidadesDTO> consulta = confContableEntidadesService.getAllConfContableEntidades(predicate);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Servicio encargado de retornar la consulta de una tiposCuenta
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<ConfContableEntidadesDTO>>>
	 * @author cesar.castano
	 */
	@GetMapping(value = "${endpoints.ConfContableEntidades.consultarById}")
	public ResponseEntity<ApiResponseADE<ConfContableEntidadesDTO>> getConfContableEntidadesById(@RequestParam(required = true) Long idConfContableEntidades) {
		ConfContableEntidadesDTO consulta = confContableEntidadesService.getConfContableEntidadesById(idConfContableEntidades);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Servicio encargado de guardar tiposCuenta
	 * 
	 * @return ResponseEntity<ApiResponseADE<ConfContableEntidadesDTO>>
	 * @author Bayron Andres Perez M
	 */
	@PostMapping(value = "${endpoints.ConfContableEntidades.guardar}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<ConfContableEntidadesDTO>> postConfContableEntidades(@RequestBody ConfContableEntidadesDTO confContableEntidadesDTO) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<ConfContableEntidadesDTO>(confContableEntidadesService.saveConfContableEntidades(confContableEntidadesDTO),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));

	}
	
	/**
	 * Servicio encargado de guardar tiposCuenta
	 * 
	 * @return ResponseEntity<ApiResponseADE<ConfContableEntidadesDTO>>
	 * @author Bayron Andres Perez M
	 */
	@PostMapping(value = "${endpoints.ConfContableEntidades.actualizar}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<ConfContableEntidadesDTO>> putConfContableEntidades(@RequestBody ConfContableEntidadesDTO confContableEntidadesDTO) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<ConfContableEntidadesDTO>(confContableEntidadesService.putConfContableEntidades(confContableEntidadesDTO),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));

	}
	
	@DeleteMapping(value = "/{idContableEntidades}")
	public ResponseEntity<ApiResponseADE<Void>> deleteConfContableEntidades(
			@PathVariable @NotNull @Valid Long idContableEntidades) {

		confContableEntidadesService.deleteConfContableEntidadesById(idContableEntidades);		
		return ResponseEntity.status(HttpStatus.NO_CONTENT)
				.body(new ApiResponseADE<Void>(null, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
}
