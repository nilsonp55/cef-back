package com.ath.adminefectivo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.MediaType;

import com.ath.adminefectivo.delegate.ITdvDenominCantidadDelegate;
import com.ath.adminefectivo.dto.TdvDenominCantidadDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.TdvDenominCantidad;
import com.querydsl.core.types.Predicate;

/**
 * Controlador responsable de exponer los metodos referentes a los puntos costos
 * @author duvan.naranjo
 */
@RestController
@RequestMapping("${endpoints.TdvDenominCantidad}")
public class TdvDenominCantidadController {

	@Autowired
	ITdvDenominCantidadDelegate tdvDenominCantidadDelegate;

	/**
	 * Servicio encargado de retornar la consulta de todas las TdvDenominCantidad
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<TdvDenominCantidadDTO>>>
	 * @author duvan.naranjo
	 */
	@GetMapping(value = "${endpoints.TdvDenominCantidad.consultar}")
	public ResponseEntity<ApiResponseADE<List<TdvDenominCantidadDTO>>> getTdvDenominCantidad(@QuerydslPredicate(root = TdvDenominCantidad.class) Predicate predicate) {
		List<TdvDenominCantidadDTO> consulta = tdvDenominCantidadDelegate.getTdvDenominCantidad(predicate);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Servicio encargado de guardar las TdvDenominCantidad
	 * 
	 * @return ResponseEntity<ApiResponseADE<TdvDenominCantidadDTO>>
	 * @author duvan.naranjo
	 */
	@PostMapping(value = "${endpoints.TdvDenominCantidad.guardar}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<TdvDenominCantidadDTO>> guardarTdvDenominCantidad(@RequestBody TdvDenominCantidadDTO tdvDenominCantidadDTO) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<TdvDenominCantidadDTO>(tdvDenominCantidadDelegate.guardarTdvDenominCantidad(tdvDenominCantidadDTO),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));

	}
	
	/**
	 * Servicio encargado de retornar la consulta de una escala por id
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<TdvDenominCantidadDTO>>>
	 * @author duvan.naranjo
	 */
	@GetMapping(value = "${endpoints.TdvDenominCantidad.consultar}/{id}")
	public ResponseEntity<ApiResponseADE<TdvDenominCantidadDTO>> getTdvDenominCantidadById(@RequestParam("id") Integer idTdvDenominCantidad) {
		TdvDenominCantidadDTO consulta = tdvDenominCantidadDelegate.getTdvDenominCantidadById(idTdvDenominCantidad);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Servicio encargado de actualizar una escala
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<TdvDenominCantidadDTO>>>
	 * @author duvan.naranjo
	 */
	@PutMapping(value = "${endpoints.TdvDenominCantidad.actualizar}")
	public ResponseEntity<ApiResponseADE<TdvDenominCantidadDTO>> actualizar(@RequestBody TdvDenominCantidadDTO tdvDenominCantidadDTO) {
		TdvDenominCantidadDTO consulta = tdvDenominCantidadDelegate.actualizarTdvDenominCantidad(tdvDenominCantidadDTO);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Servicio encargado de retornar la consulta de una escala por id
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<boolean>>>
	 * @author duvan.naranjo
	 */
	@DeleteMapping(value = "${endpoints.TdvDenominCantidad.eliminar}/{id}")
	public ResponseEntity<ApiResponseADE<Boolean>> eliminar(@RequestParam("id") Integer idTdvDenominCantidad) {
		boolean consulta = tdvDenominCantidadDelegate.eliminarTdvDenominCantidad(idTdvDenominCantidad);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}
