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

import com.ath.adminefectivo.dto.UsuarioDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.Usuario;
import com.ath.adminefectivo.service.IUsuarioService;
import com.querydsl.core.types.Predicate;

/**
 * Controlador responsable de exponer los metodos referentes a las usuarios 
 * @author bayron.perez
 */
@RestController
@RequestMapping("${endpoints.Usuario}")
public class UsuarioController {

	@Autowired
	IUsuarioService usuarioService;

	/**
	 * Servicio encargado de retornar la consulta de todos los usuarios
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<UsuarioDTO>>>
	 * @author bayron.perez
	 */
	@GetMapping(value = "${endpoints.Usuario.consultar}")
	public ResponseEntity<ApiResponseADE<List<UsuarioDTO>>> getUsuarios(@QuerydslPredicate
			(root = Usuario.class) Predicate predicate) {
		List<Usuario> consulta = usuarioService.getUsuarios(predicate);
		
		List<UsuarioDTO> listusuariosDto = new ArrayList<>();
		consulta.forEach(entity -> listusuariosDto.add(UsuarioDTO.CONVERTER_DTO.apply(entity)));
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(listusuariosDto, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Servicio encargado de retornar la consulta de un usuario
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<UsuarioDTO>>>
	 * @author bayron.perez
	 */
	@GetMapping(value = "${endpoints.Usuario.consultarById}")
	public ResponseEntity<ApiResponseADE<UsuarioDTO>> getUsuarioById(@RequestParam(required = true) String idUsuario) {
		Usuario consulta = usuarioService.getUsuarioById(idUsuario);
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(UsuarioDTO.CONVERTER_DTO.apply(consulta), ResponseADE.builder().
						code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Servicio encargado de guardar usuario
	 * 
	 * @return ResponseEntity<ApiResponseADE<UsuarioDTO>>
	 * @author Bayron Andres Perez M
	 */
	@PostMapping(value = "${endpoints.Usuario.guardar}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<UsuarioDTO>> postUsuario(@RequestBody UsuarioDTO usuarioDTO) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<UsuarioDTO>(UsuarioDTO.CONVERTER_DTO.apply(usuarioService.
						postUsuario(UsuarioDTO.CONVERTER_ENTITY.apply(usuarioDTO))),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));

	}
	
	/**
	 * Servicio encargado de guardar cuentaPuc
	 * 
	 * @return ResponseEntity<ApiResponseADE<UsuarioDTO>>
	 * @author Bayron Andres Perez M
	 */
	@PostMapping(value = "${endpoints.Usuario.actualizar}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<UsuarioDTO>> putCuentasPuc(@RequestBody UsuarioDTO usuarioDTO) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<UsuarioDTO>(UsuarioDTO.CONVERTER_DTO.apply(usuarioService.
						putUsuario(UsuarioDTO.CONVERTER_ENTITY.apply(usuarioDTO))),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));

	}
	
}
