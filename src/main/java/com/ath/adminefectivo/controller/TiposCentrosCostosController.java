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

import com.ath.adminefectivo.dto.TiposCentrosCostosDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.TiposCentrosCostos;
import com.ath.adminefectivo.service.ITiposCentrosCostosService;
import com.querydsl.core.types.Predicate;

import lombok.extern.log4j.Log4j2;

/**
 * Controlador responsable de exponer los metodos referentes a las TipoCentroCostos 
 * @author bayron.perez
 */

@Log4j2
@RestController
@RequestMapping("${endpoints.TipoCentroCostos}")
public class TiposCentrosCostosController {

	@Autowired
	ITiposCentrosCostosService tiposCentrosCostosService;

	/**
	 * Servicio encargado de retornar la consulta de todos los TiposCentrosCostos
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<TiposCentrosCostosDTO>>>
	 * @author cesar.castano
	 */
	@GetMapping(value = "${endpoints.TipoCentroCostos.consultar}")
	public ResponseEntity<ApiResponseADE<List<TiposCentrosCostosDTO>>> getTiposCentrosCostos(@QuerydslPredicate(root = TiposCentrosCostos.class) Predicate predicate) {
		List<TiposCentrosCostosDTO> consulta = tiposCentrosCostosService.getAllTiposCentrosCostos(predicate);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Servicio encargado de retornar la consulta de una tiposCuenta
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<TiposCentrosCostosDTO>>>
	 * @author cesar.castano
	 */
	@GetMapping(value = "${endpoints.TipoCentroCostos.consultarById}")
	public ResponseEntity<ApiResponseADE<TiposCentrosCostosDTO>> getTiposCentrosCostosById(@RequestParam(required = true) String idTiposCentrosCostos) {
		TiposCentrosCostosDTO consulta = tiposCentrosCostosService.getTiposCentrosCostosById(idTiposCentrosCostos);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Servicio encargado de guardar tiposCuenta
	 * 
	 * @return ResponseEntity<ApiResponseADE<TiposCentrosCostosDTO>>
	 * @author Bayron Andres Perez M
	 */
	@PostMapping(value = "${endpoints.TipoCentroCostos.guardar}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<TiposCentrosCostosDTO>> postTiposCentrosCostos(@RequestBody TiposCentrosCostosDTO tiposCentrosCostosDTO) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<TiposCentrosCostosDTO>(tiposCentrosCostosService.saveTiposCentrosCostos(tiposCentrosCostosDTO),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));

	}
	
	/**
	 * Servicio encargado de guardar tiposCuenta
	 * 
	 * @return ResponseEntity<ApiResponseADE<TiposCentrosCostosDTO>>
	 * @author Bayron Andres Perez M
	 */
	@PostMapping(value = "${endpoints.TipoCentroCostos.actualizar}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<TiposCentrosCostosDTO>> putTiposCentrosCostos(@RequestBody TiposCentrosCostosDTO tiposCentrosCostosDTO) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<TiposCentrosCostosDTO>(tiposCentrosCostosService.putTiposCentrosCostos(tiposCentrosCostosDTO),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));

	}
	
	@DeleteMapping(value = "/{idTipoCentroCosto}")
	public ResponseEntity<ApiResponseADE<Void>> deleteTiposCentrosCostos(@PathVariable @NotNull @Valid String idTipoCentroCosto) {
		log.info("Delete TipoCentrosCostos Id: {}", idTipoCentroCosto);
		tiposCentrosCostosService
				.deleteTiposCentrosCostosById(TiposCentrosCostosDTO.builder().tipoCentro(idTipoCentroCosto).build());
		log.info("Deleted TipoCentrosCostos Id: {}", idTipoCentroCosto);
		return ResponseEntity.status(HttpStatus.NO_CONTENT)
				.body(new ApiResponseADE<Void>(null, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}
