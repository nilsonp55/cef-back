package com.ath.adminefectivo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.dto.CostoBolsaMonedaAdicionalDTO;
import com.ath.adminefectivo.dto.ListCostoBolsaMonedaAdicionalDTO;
import com.ath.adminefectivo.dto.compuestos.RegistrosConciliacionListDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.CostoBolsaMonedaAdicional;
import com.ath.adminefectivo.exception.ConflictException;
import com.ath.adminefectivo.service.ICostoBolsaMonedaAdicionalService;
import com.ath.adminefectivo.service.ITarifasOperacionService;
import com.querydsl.core.types.Predicate;

/**
 * Controlador responsable de exponer los metodos referentes a las tarifas especiales cliente
 * @author hector.mercado
 */
@RestController
@RequestMapping("${endpoints.CostoBolsaMonedaAdicional}")
public class CostoBolsaMonedaAdicionalController {

	@Autowired
	ICostoBolsaMonedaAdicionalService costoBolsaMonedaAdicionalService;

	/**
	 * Servicio encargado de retornar la consulta de todas las COSTO BOLSA MONEDA ADICIONAL (
	 * 
	 * @return ResponseEntity<ApiResponseADE<Page<CostoBolsaMonedaAdicionalDTO>>>
	 * @author hector.mercado
	 */
	@GetMapping(value = "${endpoints.CostoBolsaMonedaAdicional.consultar}")
	public ResponseEntity<ApiResponseADE<Page<CostoBolsaMonedaAdicionalDTO>>> getCostoBolsaMonedaAdicional(
			@QuerydslPredicate(root = CostoBolsaMonedaAdicional.class) Predicate predicate, 
			Pageable page) 
	{
		Page<CostoBolsaMonedaAdicionalDTO> consulta = costoBolsaMonedaAdicionalService.getCostoBolsaMonedaAdicional(predicate, page);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	
	/**
	 * Servicio encargado de retornar la consulta de un COSTO BOLSA MONEDA ADICIONAL por transportadora
	 * 
	 * @return ResponseEntity<ApiResponseADE<Page<CostoBolsaMonedaAdicionalDTO>>>
	 * @author hector.mercado
	 */
	@GetMapping(value = "${endpoints.CostoBolsaMonedaAdicional.consultar.tdv}")
	public ResponseEntity<ApiResponseADE<Page<CostoBolsaMonedaAdicionalDTO>>> getByTransportadora(@RequestParam String codigoTdv) {
		Page<CostoBolsaMonedaAdicionalDTO> consulta = costoBolsaMonedaAdicionalService.getCostoBolsaMonedaAdicionalByTransportadora(codigoTdv);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	
	/**
	 * Servicio encargado de guardar un COSTO BOLSA MONEDA ADICIONAL
	 * 
	 * @return ResponseEntity<ApiResponseADE<CostoBolsaMonedaAdicionalDTO>>
	 * @author hector.mercado
	 */
	@PostMapping(value = "${endpoints.CostoBolsaMonedaAdicional.guardar}", consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<CostoBolsaMonedaAdicionalDTO>> guardarCostoBolsaMonedaAdicional(@RequestBody CostoBolsaMonedaAdicionalDTO costoBolsaMonedaAdicionalDTO) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(new ApiResponseADE<CostoBolsaMonedaAdicionalDTO>(costoBolsaMonedaAdicionalService.saveCostoBolsaMonedaAdicional(costoBolsaMonedaAdicionalDTO),
					ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
					.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	

	/**
	 * Servicio encargado de eliminar un COSTO BOLSA MONEDA ADICIONAL por id
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<CostoBolsaMonedaAdicionalDTO>>>
	 * @author hector.mercado
	 */
	@DeleteMapping(value = "${endpoints.CostoBolsaMonedaAdicional.eliminar}")
	public ResponseEntity<ApiResponseADE<Boolean>> eliminar(@RequestBody ListCostoBolsaMonedaAdicionalDTO registrosEliminar) {
		boolean consulta = costoBolsaMonedaAdicionalService.eliminarCostoBolsaMonedaAdicional(registrosEliminar);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

}
