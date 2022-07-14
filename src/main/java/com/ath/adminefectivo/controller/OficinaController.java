package com.ath.adminefectivo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.delegate.IOficinasDelegate;
import com.ath.adminefectivo.dto.OficinasDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.Oficinas;
import com.querydsl.core.types.Predicate;

/**
 * Controlador responsable de exponer los metodos referentes a las Oficinas
 * @author cesar.castano
 */
@RestController
@RequestMapping("${endpoints.Oficinas}")
public class OficinaController {

	@Autowired
	IOficinasDelegate oficinasDelegate;
	
	/**
	 * Servicio encargado de retornar la consulta de todas las Oficinas
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<OficinasDTO>>>
	 * @author cesar.castano
	 */
	@GetMapping(value = "${endpoints.Oficinas.consultar}")
	public ResponseEntity<ApiResponseADE<List<OficinasDTO>>> getOficinas(
			@QuerydslPredicate(root = Oficinas.class) Predicate predicate) {
		List<OficinasDTO> consulta = oficinasDelegate.getOficinas(predicate);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}
