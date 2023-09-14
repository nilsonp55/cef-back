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

import com.ath.adminefectivo.dto.EscalasDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.Escalas;
import com.ath.adminefectivo.service.IEscalasService;
import com.querydsl.core.types.Predicate;

/**
 * Controlador responsable de exponer los metodos referentes a los puntos costos
 * @author duvan.naranjo
 */
@RestController
@RequestMapping("${endpoints.Escalas}")
public class EscalasController {

	@Autowired
	IEscalasService escalasService;

	/**
	 * Servicio encargado de retornar la consulta de todas las escalas
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<EscalasDTO>>>
	 * @author duvan.naranjo
	 */
	@GetMapping(value = "${endpoints.Escalas.consultar}")
	public ResponseEntity<ApiResponseADE<Page<EscalasDTO>>> getEscalas(@QuerydslPredicate(root = Escalas.class) Predicate predicate, Pageable page) {
		Page<EscalasDTO> consulta = escalasService.getEscalas(predicate, page);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Servicio encargado de guardar las escalas
	 * 
	 * @return ResponseEntity<ApiResponseADE<EscalasDTO>>
	 * @author duvan.naranjo
	 */
	@PostMapping(value = "${endpoints.Escalas.guardar}", consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<EscalasDTO>> guardarEscalas(@RequestBody EscalasDTO escalasDTO) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<EscalasDTO>(escalasService.guardarEscalas(escalasDTO),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));

	}
	
	/**
	 * Servicio encargado de retornar la consulta de una escala por id
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<EscalasDTO>>>
	 * @author duvan.naranjo
	 */
	@GetMapping(value = "${endpoints.Escalas.consultar}/{id}")
	public ResponseEntity<ApiResponseADE<EscalasDTO>> getEscalasById(@RequestParam("id") Integer idEscalas) {
		EscalasDTO consulta = escalasService.getEscalasById(idEscalas);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Servicio encargado de actualizar una escala
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<EscalasDTO>>>
	 * @author duvan.naranjo
	 */
	@PutMapping(value = "${endpoints.Escalas.actualizar}")
	public ResponseEntity<ApiResponseADE<EscalasDTO>> actualizar(@RequestBody EscalasDTO escalasDTO) {
		EscalasDTO consulta = escalasService.actualizarEscalas(escalasDTO);
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
	@DeleteMapping(value = "${endpoints.Escalas.eliminar}/{id}")
	public ResponseEntity<ApiResponseADE<Boolean>> eliminar(@RequestParam("id") Integer idEscalas) {
		boolean consulta = escalasService.eliminarEscalas(idEscalas);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}
