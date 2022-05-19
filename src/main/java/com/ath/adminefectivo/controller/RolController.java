package com.ath.adminefectivo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.delegate.IRolDelegate;
import com.ath.adminefectivo.dto.RolDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;

/**
 * Controlador responsable de exponer los metodos referentes a los Roles 
 * @author CamiloBenavides
 */
@RestController
@RequestMapping("${endpoints.Rol}")
public class RolController {
	
	@Autowired
	IRolDelegate rolDelegate;
	

	/**
	 * Servicio encargado de retornar la consulta de todos los roles del aplicativo
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<RolDTO>>>
	 * @author CamiloBenavides
	 */
	@GetMapping(value = "${endpoints.Rol.consultar}")
	public ResponseEntity<ApiResponseADE<List<RolDTO>>> getRoles() {
		var consulta = rolDelegate.getRoles() ;
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

}
