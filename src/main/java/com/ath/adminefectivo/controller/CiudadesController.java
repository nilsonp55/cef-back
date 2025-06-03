package com.ath.adminefectivo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping; // Added
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; // Added
import org.springframework.web.bind.annotation.PostMapping; // Added
import org.springframework.web.bind.annotation.PutMapping; // Added
import org.springframework.web.bind.annotation.RequestBody; // Added
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.dto.CiudadesDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.Ciudades;
import com.ath.adminefectivo.service.ICiudadesService;
import com.querydsl.core.types.Predicate;

/**
 * Controlador responsable de exponer los metodos referentes a las Ciudades
 * @author cesar.castano
 */
@RestController
@RequestMapping("${endpoints.Ciudad}")
public class CiudadesController {

	@Autowired
	ICiudadesService ciudadesService;

	/**
	 * Servicio encargado de retornar la consulta de todos las Ciudades
	 *
	 * @return ResponseEntity<ApiResponseADE<List<CiudadDTO>>>
	 * @author cesar.castano
	 */
	@GetMapping(value = "${endpoints.Ciudad.consultar}")
	public ResponseEntity<ApiResponseADE<List<CiudadesDTO>>> getCiudades(@QuerydslPredicate(root = Ciudades.class) Predicate predicate) {
		List<CiudadesDTO> consulta = ciudadesService.getCiudades(predicate);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Servicio encargado de crear una nueva Ciudad.
	 *
	 * @param ciudadDTO DTO de la ciudad a crear.
	 * @return ResponseEntity<ApiResponseADE<CiudadesDTO>>
	 * @author Jules (AI Assistant)
	 */
	@PostMapping(value = "${endpoints.Ciudad.crear}")
	public ResponseEntity<ApiResponseADE<CiudadesDTO>> createCiudad(@RequestBody CiudadesDTO ciudadDTO) {
		CiudadesDTO nuevaCiudad = ciudadesService.createCiudad(ciudadDTO);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponseADE<>(nuevaCiudad, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description("Ciudad creada exitosamente.").build()));
	}

	/**
	 * Servicio encargado de consultar una Ciudad por su ID (Código DANE).
	 *
	 * @param id El Código DANE de la ciudad a consultar.
	 * @return ResponseEntity<ApiResponseADE<CiudadesDTO>>
	 * @author Jules (AI Assistant)
	 */
	@GetMapping(value = "${endpoints.Ciudad.consultarPorId}")
	public ResponseEntity<ApiResponseADE<CiudadesDTO>> getCiudadById(@PathVariable String id) {
		CiudadesDTO ciudad = ciudadesService.getCiudadById(id);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(ciudad, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Servicio encargado de actualizar una Ciudad existente.
	 *
	 * @param id El Código DANE de la ciudad a actualizar.
	 * @param ciudadDTO DTO con los datos actualizados de la ciudad.
	 * @return ResponseEntity<ApiResponseADE<CiudadesDTO>>
	 * @author Jules (AI Assistant)
	 */
	@PutMapping(value = "${endpoints.Ciudad.actualizar}")
	public ResponseEntity<ApiResponseADE<CiudadesDTO>> updateCiudad(@PathVariable String id, @RequestBody CiudadesDTO ciudadDTO) {
		CiudadesDTO ciudadActualizada = ciudadesService.updateCiudad(id, ciudadDTO);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(ciudadActualizada, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description("Ciudad actualizada exitosamente.").build()));
	}

	/**
	 * Servicio encargado de eliminar una Ciudad por su ID (Código DANE).
	 *
	 * @param id El Código DANE de la ciudad a eliminar.
	 * @return ResponseEntity<ApiResponseADE<Void>>
	 * @author Jules (AI Assistant)
	 */
	@DeleteMapping(value = "${endpoints.Ciudad.eliminar}")
	public ResponseEntity<ApiResponseADE<Void>> deleteCiudad(@PathVariable String id) {
		ciudadesService.deleteCiudad(id);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(null, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description("Ciudad eliminada exitosamente.").build()));
	}
}
