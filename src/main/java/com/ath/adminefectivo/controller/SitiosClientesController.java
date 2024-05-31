package com.ath.adminefectivo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.dto.SitiosClientesDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.SitiosClientes;
import com.ath.adminefectivo.service.ISitiosClientesService;
import com.querydsl.core.types.Predicate;

/**
 * Controlador responsable de exponer los metodos referentes a los Sitios Clientes
 * @author cesar.castano
 */
@RestController
@RequestMapping("${endpoints.SitiosClientes}")
public class SitiosClientesController {

	@Autowired
	ISitiosClientesService sitiosClientesService;
	
	/**
	 * Servicio encargado de retornar la consulta de todos los Sitios Clientes
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<SitiosClientesDTO>>>
	 * @author cesar.castano
	 */
	@GetMapping(value = "${endpoints.SitiosClientes.consultar}")
	public ResponseEntity<ApiResponseADE<List<SitiosClientesDTO>>> getSitiosClientes(
			@QuerydslPredicate(root = SitiosClientes.class) Predicate predicate) {
		List<SitiosClientesDTO> consulta = sitiosClientesService.getSitiosClientes(predicate);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}
