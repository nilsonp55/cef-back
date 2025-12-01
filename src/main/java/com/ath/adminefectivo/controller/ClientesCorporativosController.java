package com.ath.adminefectivo.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.dto.ClientesCorporativosDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.ClientesCorporativos;
import com.ath.adminefectivo.service.IClientesCorporativosService;
import com.querydsl.core.types.Predicate;

import lombok.extern.log4j.Log4j2;

/**
 * Controlador responsable de exponer los metodos referentes a las Clientes Corporativos 
 * @author cesar.castano
 */
@Log4j2
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
	
	/**
	 * Servicio crud listar Clientes Corporativos
	 * 
	 * @return HttpStatus 200 -
	 *         ResponseEntity<ApiResponseADE<List<ClientesCorporativosDTO>>>
	 * @author prv_nparra
	 */
	@GetMapping
	public ResponseEntity<ApiResponseADE<Page<ClientesCorporativosDTO>>> listarClientesCorporativos(
			@QuerydslPredicate(root = ClientesCorporativos.class) Predicate predicate, Pageable page, String busqueda) {
		log.debug("listarClientesCorporativos - predicate: {} - page: {}", predicate.toString(), page.getPageNumber());
		Page<ClientesCorporativosDTO> clientesReturn = clientesCorporativosService.listarClientesCorporativos(predicate,
				page, busqueda); 
		log.debug("listarClientesCorporativos - clientes: {}", clientesReturn.getTotalElements());
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(clientesReturn, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Servicio crud para obtener Clientes Corporativos por codigoCliente
	 * 
	 * @return HttpStatus 200 -
	 *         ResponseEntity<ApiResponseADE<ClientesCorporativosDTO>>
	 * @author prv_nparra
	 */
	@GetMapping(value = "${endpoints.ClientesCorporativos.crud.get}")
	public ResponseEntity<ApiResponseADE<ClientesCorporativosDTO>> obtenerClientesCorporativo(
			@PathVariable @NotNull @Valid Integer codigoCliente) {
		log.debug("obtenerClientesCorporativo - codigoCliente: {}", codigoCliente);
		ClientesCorporativosDTO cliente = clientesCorporativosService.getClientesCorporativos(codigoCliente);
		log.debug("obtenerClientesCorporativo - identificacion: {}", cliente.getIdentificacion());
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(cliente, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Servicio crud guardar nuevo Clientes Corporativos
	 * 
	 * @return HttpStatus 200 -
	 *         ResponseEntity<ApiResponseADE<ClientesCorporativosDTO>>
	 * @author prv_nparra
	 */
	@PostMapping
	public ResponseEntity<ApiResponseADE<ClientesCorporativosDTO>> guardarClientesCorporativos(
			@RequestBody @Valid ClientesCorporativosDTO clientesCorporativosDTO, BindingResult bindingResult) {

		if (bindingResult.hasFieldErrors()) {
			log.debug("guardarClientesCorporativos -  failed validation: {}", bindingResult.getErrorCount());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseADE<>(null, ResponseADE.builder()
					.code(Constantes.CAMPO_REQUERIDO).description(Constantes.CAMPO_REQUERIDO)
					.errors(bindingResult.getFieldErrors().stream()
							.map(error -> error.getField().concat(": ").concat(error.getDefaultMessage())).toList())
					.build()));

		}
		log.debug("guardarClientesCorporativos - dto: {}", clientesCorporativosDTO.toString());
		ClientesCorporativosDTO clienteSaved = clientesCorporativosService
				.guardarClientesCorporativos(clientesCorporativosDTO);
		log.debug("guardarClientesCorporativos - codigoCliente: {}", clienteSaved.getCodigoCliente());
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(clienteSaved, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Servicio crud actualizar Clientes Corporativos existente
	 * 
	 * @return HttpStatus 200 - ResponseEntity<ApiResponseADE<ClientesCorporativosDTO>>
	 * @author prv_nparra
	 */
	@PutMapping
	public ResponseEntity<ApiResponseADE<ClientesCorporativosDTO>> actualizarClientesCorporativos(
			@RequestBody @Valid ClientesCorporativosDTO clientesCorporativosDTO, BindingResult bindingResult) {
		// Validacion campos obligatorios del DTO
		if (bindingResult.hasFieldErrors()) {
			log.debug("actualizarClientesCorporativos - failed validation: {}", bindingResult.getErrorCount());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseADE<>(null, ResponseADE.builder()
					.code(Constantes.CAMPO_REQUERIDO).description(Constantes.CAMPO_REQUERIDO)
					.errors(bindingResult.getFieldErrors().stream()
							.map(error -> error.getField().concat(": ").concat(error.getDefaultMessage())).toList())
					.build()));

		}
		// Codigo cliente obligatorio
		if (ObjectUtils.isEmpty(clientesCorporativosDTO.getCodigoCliente())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ApiResponseADE<>(null, ResponseADE.builder().code(Constantes.CAMPO_REQUERIDO)
							.description(Constantes.CAMPO_REQUERIDO.concat(": CodigoCliente")).build()));
		}
		ClientesCorporativosDTO clienteUpdated = clientesCorporativosService
				.actualizarClientesCorporativos(clientesCorporativosDTO);
		log.debug("actualizarClientesCorporativos - dto: {}", clientesCorporativosDTO.toString());
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(clienteUpdated, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Servicio crud eliminar Clientes Corporativos
	 * 
	 * @return HttpStatus 200 - ResponseEntity<ApiResposeADE<ClientesCorporativosDTO>>
	 * @author prv_nparra
	 */
	@DeleteMapping(value = "${endpoints.ClientesCorporativos.crud.get}")
	public ResponseEntity<ApiResponseADE<ClientesCorporativosDTO>> eliminarClientesCorporativos(
			@PathVariable @NotNull @Valid Integer codigoCliente) {
		log.debug("eliminarClientesCorporativos - codigoCliente: {}", codigoCliente);
		clientesCorporativosService.eliminarClientesCorporativos(codigoCliente);
		log.debug("eliminarClientesCorporativos - deleted: {}", codigoCliente);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
	}
}
