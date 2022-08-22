package com.ath.adminefectivo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.controller.endpoints.ArchivosCargadosEndpoint;
import com.ath.adminefectivo.delegate.IArchivosCargadosDelegate;
import com.ath.adminefectivo.delegate.IContabilidadDelegate;
import com.ath.adminefectivo.dto.ArchivosCargadosDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.ArchivosCargados;
import com.querydsl.core.types.Predicate;

/**
 * Controlador responsable de exponer los metodos correspondientes al 
 * modulo de contabilidad
 * 
 * @author duvan.naranjo
 */
@RestController
@RequestMapping("${endpoints.Contabilidad}")
public class ContabilidadController {

	@Autowired
	IContabilidadDelegate contabilidadDelegate;

	/**
	 * Metodo encargado de realizar la contabilidad según el tipo de contabilidad
	 * recibida
	 * 
	 * @param tipoContabilidad
	 * @return ResponseEntity<ApiResponseADE<String>>
	 * @author duvan.naranjo
	 */
	@GetMapping(value = "${endpoints.Contabilidad.generar}/{tipoContabilidad}")
	public ResponseEntity<ApiResponseADE<String>> getGenerarContabilidad(@PathVariable("tipoContabilidad") String tipoContabilidad) {

		String consulta = contabilidadDelegate.generarContabilidad(tipoContabilidad);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}


}
