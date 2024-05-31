package com.ath.adminefectivo.controller;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.FestivosNacionales;
import com.ath.adminefectivo.service.IFestivosNacionalesService;
import com.querydsl.core.types.Predicate;

/**
 * Controlador responsable de exponer los metodos referentes a los festivos nacionales
 * @author bayron.perez
 */
@RestController
@RequestMapping("${endpoints.FestivosNacionales}")
public class FestivosNacionalesController {

	@Autowired
	IFestivosNacionalesService festivosNacionalesService;
	
	
	/**
	 * Metodo encargado de persisitir los dias festivos nacionales
	 * @param festivosNacionales
	 * @return
	 */
	@PostMapping(value = "${endpoints.FestivosNacionales.guardar}", consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<FestivosNacionales>> saveFestivosNacionales(@RequestBody FestivosNacionales festivosNacionales) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<FestivosNacionales>(festivosNacionalesService.saveFestivosNacionales(festivosNacionales),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	
	/**
	 * Metodo encargado de eliminar un dia festivo nacional
	 * @param idFecha
	 * @return
	 * @throws ParseException 
	 */
	@DeleteMapping(value = "${endpoints.FestivosNacionales.eliminar}")
	public ResponseEntity<ApiResponseADE<Boolean>> eliminar(@RequestParam("idFecha") String idFestivoNacional) {
		festivosNacionalesService.eliminarFestivosNacionales(idFestivoNacional);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(Boolean.TRUE, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	@GetMapping(value = "${endpoints.FestivosNacionales.consultar}")
	public ResponseEntity<ApiResponseADE<List<FestivosNacionales>>> getFestivosNacionales(@QuerydslPredicate(root = FestivosNacionales.class) Predicate predicate, Pageable page) {
		List<FestivosNacionales> consulta = festivosNacionalesService.consultarFestivosNacionales();
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}
