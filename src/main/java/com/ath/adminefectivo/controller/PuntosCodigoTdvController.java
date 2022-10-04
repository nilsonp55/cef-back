package com.ath.adminefectivo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.ath.adminefectivo.delegate.IPuntosCodigoTdvDelegate;
import com.ath.adminefectivo.dto.PuntosCodigoTdvDTO;
import com.ath.adminefectivo.dto.TarifasOperacionDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.PuntosCodigoTDV;
import com.querydsl.core.types.Predicate;

/**
 * Controlador responsable de exponer los metodos referentes a los Puntos Codigo TDV
 * @author cesar.castano
 */
@RestController
@RequestMapping("${endpoints.PuntosCodigoTdv}")
public class PuntosCodigoTdvController {

	@Autowired
	IPuntosCodigoTdvDelegate puntosCodigoTdvDelegate;
	
	/**
	 * Servicio encargado de retornar la consulta de todos los Puntos Codigo Tdv
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<PuntosCodigoTdvDTO>>>
	 * @author cesar.castano
	 */
	@GetMapping(value = "${endpoints.PuntosCodigoTdv.consultar}")
	public ResponseEntity<ApiResponseADE<List<PuntosCodigoTdvDTO>>> getPuntosCodigoTDV(
			@QuerydslPredicate(root = PuntosCodigoTDV.class) Predicate predicate) {
		
		List<PuntosCodigoTdvDTO> consulta = puntosCodigoTdvDelegate.getPuntosCodigoTDV(predicate);
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Servicio encargado de guardar los PuntosCodigoTdv
	 * 
	 * @return ResponseEntity<ApiResponseADE<PuntosCodigoTdvDTO>>
	 * @author duvan.naranjo
	 */
	@PostMapping(value = "${endpoints.PuntosCodigoTdv.guardar}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<PuntosCodigoTdvDTO>> guardarPuntosCodigoTdv(@RequestBody PuntosCodigoTdvDTO puntosCodigoTdvDTO) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<PuntosCodigoTdvDTO>(puntosCodigoTdvDelegate.guardarPuntosCodigoTdv(puntosCodigoTdvDTO),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));

	}
	
	/**
	 * Servicio encargado de retornar la consulta de un PuntosCodigoTdv por id
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<PuntosCodigoTdvDTO>>>
	 * @author duvan.naranjo
	 */
	@GetMapping(value = "${endpoints.PuntosCodigoTdv.consultar}/{id}")
	public ResponseEntity<ApiResponseADE<PuntosCodigoTdvDTO>> getPunto(@RequestParam("id") Integer idPuntoCodigoTdv) {
		PuntosCodigoTdvDTO consulta = puntosCodigoTdvDelegate.getPuntosCodigoTdvById(idPuntoCodigoTdv);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Servicio encargado de actualizar una PuntosCodigoTdv
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<PuntosCodigoTdvDTO>>>
	 * @author duvan.naranjo
	 */
	@PutMapping(value = "${endpoints.PuntosCodigoTdv.actualizar}")
	public ResponseEntity<ApiResponseADE<PuntosCodigoTdvDTO>> actualizar(@RequestBody PuntosCodigoTdvDTO puntosCodigoTdvDTO) {
		PuntosCodigoTdvDTO consulta = puntosCodigoTdvDelegate.actualizarPuntosCodigoTdv(puntosCodigoTdvDTO);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Servicio encargado de retornar la consulta de  PuntosCodigoTdv por id
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<boolean>>>
	 * @author duvan.naranjo
	 */
	@DeleteMapping(value = "${endpoints.PuntosCodigoTdv.eliminar}/{id}")
	public ResponseEntity<ApiResponseADE<Boolean>> eliminar(@RequestParam("id") Integer idPuntoCodigoTdv) {
		boolean consulta = puntosCodigoTdvDelegate.eliminarPuntosCodigoTdv(idPuntoCodigoTdv);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}
