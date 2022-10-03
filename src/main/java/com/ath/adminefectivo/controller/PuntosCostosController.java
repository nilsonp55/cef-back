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

import com.ath.adminefectivo.delegate.IPuntosCostosDelegate;
import com.ath.adminefectivo.dto.PuntosCostosDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.PuntosCostos;
import com.querydsl.core.types.Predicate;

/**
 * Controlador responsable de exponer los metodos referentes a los puntos costos
 * @author duvan.naranjo
 */
@RestController
@RequestMapping("${endpoints.PuntosCostos}")
public class PuntosCostosController {

	@Autowired
	IPuntosCostosDelegate puntosCostosDelegate;

	/**
	 * Servicio encargado de retornar la consulta de todos los puntos costos
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<PuntosCostosDTO>>>
	 * @author duvan.naranjo
	 */
	@GetMapping(value = "${endpoints.PuntosCostos.consultar}")
	public ResponseEntity<ApiResponseADE<List<PuntosCostosDTO>>> getPuntosCostos(@QuerydslPredicate(root = PuntosCostos.class) Predicate predicate) {
		List<PuntosCostosDTO> consulta = puntosCostosDelegate.getPuntosCostos(predicate);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Servicio encargado de guardar los puntos costos
	 * 
	 * @return ResponseEntity<ApiResponseADE<PuntosCostosDTO>>
	 * @author duvan.naranjo
	 */
	@PostMapping(value = "${endpoints.PuntosCostos.guardar}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<PuntosCostosDTO>> guardarPuntosCostos(@RequestBody PuntosCostosDTO puntosCostosDTO) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<PuntosCostosDTO>(puntosCostosDelegate.guardarPuntosCostos(puntosCostosDTO),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));

	}
	
	/**
	 * Servicio encargado de retornar la consulta de un punto costo por id
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<PuntosCostosDTO>>>
	 * @author duvan.naranjo
	 */
	@GetMapping(value = "${endpoints.PuntosCostos.consultar}/{id}")
	public ResponseEntity<ApiResponseADE<PuntosCostosDTO>> getPuntoById(@RequestParam("id") Integer idPuntosCostos) {
		PuntosCostosDTO consulta = puntosCostosDelegate.getPuntosCostosById(idPuntosCostos);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Servicio encargado de actualizar un punto costo
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<PuntosCostosDTO>>>
	 * @author duvan.naranjo
	 */
	@PutMapping(value = "${endpoints.PuntosCostos.actualizar}")
	public ResponseEntity<ApiResponseADE<PuntosCostosDTO>> actualizar(@RequestBody PuntosCostosDTO puntosCostosDTO) {
		PuntosCostosDTO consulta = puntosCostosDelegate.actualizarPuntosCostos(puntosCostosDTO);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Servicio encargado de retornar la consulta de un punto costo por id
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<boolean>>>
	 * @author duvan.naranjo
	 */
	@DeleteMapping(value = "${endpoints.PuntosCostos.eliminar}/{id}")
	public ResponseEntity<ApiResponseADE<Boolean>> eliminar(@RequestParam("id") Integer idPuntosCostos) {
		boolean consulta = puntosCostosDelegate.eliminarPuntosCostos(idPuntosCostos);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}
