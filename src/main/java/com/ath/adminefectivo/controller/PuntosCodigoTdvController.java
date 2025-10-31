package com.ath.adminefectivo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ath.adminefectivo.dto.PuntosCodigoTdvDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.PuntosCodigoTDV;
import com.ath.adminefectivo.service.IPuntosCodigoTdvService;
import com.querydsl.core.types.Predicate;
import lombok.extern.log4j.Log4j2;

/**
 * Controlador responsable de exponer los metodos referentes a los Puntos Codigo TDV
 * @author cesar.castano
 */
@RestController
@RequestMapping("${endpoints.PuntosCodigoTdv}")
@Log4j2
public class PuntosCodigoTdvController {

	@Autowired
	IPuntosCodigoTdvService puntosCodigoTdvService;
	
	/**
	 * Servicio encargado de retornar la consulta de todos los Puntos Codigo Tdv
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<PuntosCodigoTdvDTO>>>
	 * @author cesar.castano
	 */
	@GetMapping(value = "${endpoints.PuntosCodigoTdv.consultar}")
	public ResponseEntity<ApiResponseADE<Page<PuntosCodigoTdvDTO>>> getPuntosCodigoTDV(
			@QuerydslPredicate(root = PuntosCodigoTDV.class) Predicate predicate, Pageable page, String busqueda) {
		
		var consulta = puntosCodigoTdvService.getPuntosCodigoTDV(predicate, page, busqueda);
		
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
	@PostMapping(value = "${endpoints.PuntosCodigoTdv.guardar}", consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<PuntosCodigoTdvDTO>> guardarPuntosCodigoTdv(@RequestBody PuntosCodigoTdvDTO puntosCodigoTdvDTO) {
		log.info("Crear punto TDV: {}", puntosCodigoTdvDTO.toString());
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<PuntosCodigoTdvDTO>(puntosCodigoTdvService.guardarPuntosCodigoTdv(puntosCodigoTdvDTO),
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
		PuntosCodigoTdvDTO consulta = puntosCodigoTdvService.getPuntosCodigoTdvById(idPuntoCodigoTdv);
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
		PuntosCodigoTdvDTO consulta = puntosCodigoTdvService.actualizarPuntosCodigoTdv(puntosCodigoTdvDTO);
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
	@DeleteMapping(value = "${endpoints.PuntosCodigoTdv.eliminar}")
	public ResponseEntity<ApiResponseADE<Boolean>> eliminar(@PathVariable("id") Integer idPuntoCodigoTdv) {
		boolean consulta = puntosCodigoTdvService.eliminarPuntosCodigoTdv(idPuntoCodigoTdv);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Consulta CodigosTDV por codigoPunto, listar los codigodPropiosTDV de un punto.
	 * @param idCodigoPunto
	 * @return Lista de CodigosPropioTDV de un punto dado.
	 */
	@GetMapping(value = "${endpoints.PuntosCodigoTdv.codigopunto}")
	public ResponseEntity<ApiResponseADE<List<PuntosCodigoTdvDTO>>> consultarporCodigPunto(@PathVariable("idcodigopunto") Integer idCodigoPunto) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(puntosCodigoTdvService.getByCodigoPunto(idCodigoPunto), ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
				.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}
