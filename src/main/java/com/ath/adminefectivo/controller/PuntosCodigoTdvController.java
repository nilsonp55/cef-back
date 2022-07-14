package com.ath.adminefectivo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.delegate.IPuntosCodigoTdvDelegate;
import com.ath.adminefectivo.dto.PuntosCodigoTdvDTO;
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
}
