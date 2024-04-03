package com.ath.adminefectivo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.dto.ClientesCorporativosDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.ClientesCorporativos;
import com.ath.adminefectivo.service.IClientesCorporativosService;
import com.querydsl.core.types.Predicate;

/**
 * Controlador responsable de exponer los metodos referentes a las Clientes Corporativos 
 * @author cesar.castano
 */
@RestController
@RequestMapping("${endpoints.ClientesCorporativos}")
public class ClientesCorporativosController {

	private final IClientesCorporativosService clientesCorporativosService;
	
	public ClientesCorporativosController(@Autowired IClientesCorporativosService clientesCorporativosService) {
		this.clientesCorporativosService = clientesCorporativosService;
	}
	
	/**
	 * Servicio encargado de retornar la consulta de todos los Clientes Corporativos
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<ClientesCorporativosDTO>>>
	 * @author cesar.castano
	 */
	@GetMapping(value = "${endpoints.ClientesCorporativos.consultar}")
	public ResponseEntity<ApiResponseADE<List<ClientesCorporativosDTO>>> getClientesCorporativos(
			@QuerydslPredicate(root = ClientesCorporativos.class) Predicate predicate) {
		
		List<ClientesCorporativosDTO> consulta = clientesCorporativosService.getClientesCorporativos(predicate);
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}
