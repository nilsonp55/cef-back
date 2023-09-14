package com.ath.adminefectivo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.delegate.IClasificacionCostosDelegate;
import com.ath.adminefectivo.dto.compuestos.CostosMensualesClasificacionDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;

/**
 * Controlador responsable de exponer los metodos referentes a los puntos costos
 * @author duvan.naranjo
 */
@RestController
@RequestMapping("${endpoints.ClasificacionMensual}")
public class ClasificacionCostosController {

	@Autowired
	IClasificacionCostosDelegate clasificacionCostosDelegate;

	/**
	 * Servicio encargado de retornar la liquidacion de los costos mensuales por clasificacion
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<CostosMensualesClasificacionDTO>>>
	 * @author duvan.naranjo
	 */
	@GetMapping(value = "${endpoints.ClasificacionMensual.consultar}")
	public ResponseEntity<ApiResponseADE<List<CostosMensualesClasificacionDTO>>> getClasificacionMensualPorBanco(@RequestParam("transportadora") String transportadora) {
		List<CostosMensualesClasificacionDTO> consulta = clasificacionCostosDelegate.getClasificacionMensualCostos(transportadora);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Servicio encargado de realizar la liquidacion de las clasificacion de costos mensuales
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<CostosMensualesClasificacionDTO>>>
	 * @author duvan.naranjo
	 */
	@PostMapping(value = "${endpoints.ClasificacionMensual.liquidar}", consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<List<CostosMensualesClasificacionDTO>>> liquidarClasificacionCostos(@RequestBody List<CostosMensualesClasificacionDTO> listadoCostosMensuales) {

		List<CostosMensualesClasificacionDTO> consulta = clasificacionCostosDelegate.liquidarClasificacionCostos(listadoCostosMensuales);
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<List<CostosMensualesClasificacionDTO>>(consulta,
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));

	}
	

	/**
	 * Servicio encargado de guardar la clasificacion de costos mensual
	 * 
	 * @return ResponseEntity<ApiResponseADE<String>>
	 * @author duvan.naranjo
	 */
	@PostMapping(value = "${endpoints.ClasificacionMensual.guardar}", consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<String>> guardarClasificacionCostosMensuales(@RequestBody List<CostosMensualesClasificacionDTO> listadoCostosMensuales) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<String>(clasificacionCostosDelegate.guardarClasificacionCostosMensuales(listadoCostosMensuales),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));

	}
	
}
