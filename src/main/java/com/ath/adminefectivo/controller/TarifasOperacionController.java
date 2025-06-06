package com.ath.adminefectivo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import com.ath.adminefectivo.dto.TarifasOperacionDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.TarifasOperacion;
import com.ath.adminefectivo.service.ITarifasOperacionService;
import com.querydsl.core.types.Predicate;

/**
 * Controlador responsable de exponer los metodos referentes a las tarifas operacion
 * @author duvan.naranjo
 */
@RestController
@RequestMapping("${endpoints.TarifasOperacion}")
public class TarifasOperacionController {

	@Autowired
	ITarifasOperacionService tarifasOperacionService;

	/**
	 * Servicio encargado de retornar la consulta de todas las tarifas operacion
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<TarifasOperacionDTO>>>
	 * @author duvan.naranjo
	 */
	@GetMapping(value = "${endpoints.TarifasOperacion.consultar}")
	public ResponseEntity<ApiResponseADE<Page<TarifasOperacionDTO>>> getTarifasOperacion(@QuerydslPredicate(root = TarifasOperacion.class) Predicate predicate, Pageable page) {
		Page<TarifasOperacionDTO> consulta = tarifasOperacionService.getTarifasOperacion(predicate, page);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Servicio encargado de guardar las tarifas operacion
	 * 
	 * @return ResponseEntity<ApiResponseADE<TarifasOperacionDTO>>
	 * @author duvan.naranjo
	 */
	@PostMapping(value = "${endpoints.TarifasOperacion.guardar}", consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<TarifasOperacionDTO>> guardarTarifasOperacion(@RequestBody TarifasOperacionDTO tarifasOperacionDTO) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<TarifasOperacionDTO>(tarifasOperacionService.guardarTarifasOperacion(tarifasOperacionDTO),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));

	}
	
	/**
	 * Servicio encargado de retornar la consulta de una tarifa operacion por id
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<TarifasOperacionDTO>>>
	 * @author duvan.naranjo
	 */
	@GetMapping(value = "${endpoints.TarifasOperacion.consultar}/{id}")
	public ResponseEntity<ApiResponseADE<TarifasOperacionDTO>> getPunto(@RequestParam("id") Integer idTarifaOperacion) {
		TarifasOperacionDTO consulta = tarifasOperacionService.getTarifasOperacionById(idTarifaOperacion);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Servicio encargado de actualizar una tarifaOperacion
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<TarifasOperacionDTO>>>
	 * @author duvan.naranjo
	 */
	@PutMapping(value = "${endpoints.TarifasOperacion.actualizar}")
	public ResponseEntity<ApiResponseADE<TarifasOperacionDTO>> actualizar(@RequestBody TarifasOperacionDTO tarifasOperacionDTO) {
		TarifasOperacionDTO consulta = tarifasOperacionService.actualizarTarifasOperacion(tarifasOperacionDTO);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Servicio encargado de retornar la consulta de una tarifa operacion por id
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<TarifasOperacionDTO>>>
	 * @author duvan.naranjo
	 */
	@DeleteMapping(value = "${endpoints.TarifasOperacion.eliminar}/{id}")
	public ResponseEntity<ApiResponseADE<Boolean>> eliminar(@RequestParam("id") Integer idTarifaOperacion) {
		boolean consulta = tarifasOperacionService.eliminarTarifasOperacion(idTarifaOperacion);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}
