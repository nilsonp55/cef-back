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

import com.ath.adminefectivo.dto.TiposCuentasDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.TiposCuentas;
import com.ath.adminefectivo.service.ITiposCuentasService;
import com.querydsl.core.types.Predicate;

/**
 * Controlador responsable de exponer los metodos referentes a las TiposCuentas 
 * @author bayron.perez
 */

@RestController
@RequestMapping("${endpoints.Tiposcuentas}")
public class TiposCuentasController {

	@Autowired
	ITiposCuentasService tiposCuentasService;

	/**
	 * Servicio encargado de retornar la consulta de todos los TiposCuentas
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<TiposCuentasDTO>>>
	 * @author cesar.castano
	 */
	@GetMapping(value = "${endpoints.Tiposcuentas.consultar}")
	public ResponseEntity<ApiResponseADE<List<TiposCuentasDTO>>> getTiposCuentas(@QuerydslPredicate(root = TiposCuentas.class) Predicate predicate) {
		List<TiposCuentasDTO> consulta = tiposCuentasService.getAllTiposCuentas(predicate);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Servicio encargado de retornar la consulta de una tiposCuenta
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<TiposCuentasDTO>>>
	 * @author cesar.castano
	 */
	@GetMapping(value = "${endpoints.Tiposcuentas.consultarById}")
	public ResponseEntity<ApiResponseADE<TiposCuentasDTO>> getTiposCuentasById(@RequestParam(required = true) String idTiposCuentas) {
		TiposCuentasDTO consulta = tiposCuentasService.getTiposCuentasById(idTiposCuentas);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Servicio encargado de guardar tiposCuenta
	 * 
	 * @return ResponseEntity<ApiResponseADE<TiposCuentasDTO>>
	 * @author Bayron Andres Perez M
	 */
	@PostMapping(value = "${endpoints.Tiposcuentas.guardar}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<TiposCuentasDTO>> postTiposCuentas(@RequestBody TiposCuentasDTO tiposCuentasDTO) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<TiposCuentasDTO>(tiposCuentasService.saveTiposCuentas(tiposCuentasDTO),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));

	}
	
	/**
	 * Servicio encargado de guardar tiposCuenta
	 * 
	 * @return ResponseEntity<ApiResponseADE<TiposCuentasDTO>>
	 * @author Bayron Andres Perez M
	 */
	@PostMapping(value = "${endpoints.Tiposcuentas.actualizar}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<TiposCuentasDTO>> putTiposCuentas(@RequestBody TiposCuentasDTO tiposCuentasDTO) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<TiposCuentasDTO>(tiposCuentasService.putTiposCuentas(tiposCuentasDTO),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));

	}
	
	@DeleteMapping(value = "/{idTiposCuentas}")
	public ResponseEntity<ApiResponseADE<Void>> deleteTiposCuentas(
			@PathVariable @NotNull @Valid String idTiposCuentas) {
		tiposCuentasService.deleteTiposCuentasById(TiposCuentasDTO.builder().tipoCuenta(idTiposCuentas).build());
		return ResponseEntity.status(HttpStatus.NO_CONTENT)
				.body(new ApiResponseADE<Void>(null, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}
