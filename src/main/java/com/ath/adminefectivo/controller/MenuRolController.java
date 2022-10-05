package com.ath.adminefectivo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.dto.MenuRolDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.MenuRol;
import com.ath.adminefectivo.service.IMenuRolService;
import com.querydsl.core.types.Predicate;

/**
 * Controlador responsable de exponer los metodos referentes a las menuRols 
 * @author bayron.perez
 */
@RestController
@RequestMapping("${endpoints.MenuRol}")
public class MenuRolController {

	@Autowired
	IMenuRolService menuRolService;

	/**
	 * Servicio encargado de retornar la consulta de todos los menuRols
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<MenuRolDTO>>>
	 * @author bayron.perez
	 */
	@GetMapping(value = "${endpoints.MenuRol.consultar}")
	public ResponseEntity<ApiResponseADE<List<MenuRolDTO>>> getMenuRols(@QuerydslPredicate
			(root = MenuRol.class) Predicate predicate) {
		List<MenuRol> consulta = menuRolService.getMenuRol(predicate);
		
		List<MenuRolDTO> listmenuRolsDto = new ArrayList<>();
		consulta.forEach(entity -> listmenuRolsDto.add(MenuRolDTO.CONVERTER_DTO.apply(entity)));
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(listmenuRolsDto, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Servicio encargado de retornar la consulta de un menuRol
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<MenuRolDTO>>>
	 * @author bayron.perez
	 */
	@GetMapping(value = "${endpoints.MenuRol.consultarById}")
	public ResponseEntity<ApiResponseADE<MenuRolDTO>> getMenuRolById(@RequestParam(required = true) Integer idMenuRol) {
		MenuRol consulta = menuRolService.getMenuRolById(idMenuRol);
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(MenuRolDTO.CONVERTER_DTO.apply(consulta), ResponseADE.builder().
						code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Servicio encargado de guardar menuRol
	 * 
	 * @return ResponseEntity<ApiResponseADE<MenuRolDTO>>
	 * @author Bayron Andres Perez M
	 */
	@PostMapping(value = "${endpoints.MenuRol.guardar}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<MenuRolDTO>> postMenuRol(@RequestBody MenuRolDTO menuRolDTO) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<MenuRolDTO>(MenuRolDTO.CONVERTER_DTO.apply(menuRolService.
						postMenuRol(MenuRolDTO.CONVERTER_ENTITY.apply(menuRolDTO))),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));

	}
	
	/**
	 * Servicio encargado de guardar cuentaPuc
	 * 
	 * @return ResponseEntity<ApiResponseADE<MenuRolDTO>>
	 * @author Bayron Andres Perez M
	 */
	@PostMapping(value = "${endpoints.MenuRol.actualizar}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<MenuRolDTO>> putCuentasPuc(@RequestBody MenuRolDTO menuRolDTO) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<MenuRolDTO>(MenuRolDTO.CONVERTER_DTO.apply(menuRolService.
						putMenuRol(MenuRolDTO.CONVERTER_ENTITY.apply(menuRolDTO))),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));

	}
}
