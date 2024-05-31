package com.ath.adminefectivo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.dto.CertificadasNoConciliadasDTO;
import com.ath.adminefectivo.dto.FechasConciliacionDTO;
import com.ath.adminefectivo.dto.ParametrosConciliacionDTO;
import com.ath.adminefectivo.dto.ProgramadasNoConciliadasDTO;
import com.ath.adminefectivo.dto.ResumenConciliacionesDTO;
import com.ath.adminefectivo.dto.UpdateCertificadasFallidasDTO;
import com.ath.adminefectivo.dto.UpdateProgramadasFallidasDTO;
import com.ath.adminefectivo.dto.compuestos.OperacionesProgramadasNombresDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.OperacionesCertificadas;
import com.ath.adminefectivo.entities.OperacionesProgramadas;
import com.ath.adminefectivo.service.IConciliacionOperacionesService;
import com.querydsl.core.types.Predicate;

import lombok.extern.log4j.Log4j2;

/**
 * Controlador responsable de exponer los metodos referentes al proceso de
 * conciliacion
 * 
 * @author cesar.castano
 */
@RestController
@RequestMapping("${endpoints.conciliacion}")
@Log4j2
public class ConciliacionOperacionesController {
	
	@Autowired
	IConciliacionOperacionesService conciliacionOperacionesService;

	/**
	 * Metodo encargado de retornar la lista de todas las operaciones conciliadas
	 * @return Page<OperacionesProgramadasConciliadasDTO>
	 * @param page
	 * @param predicate
	 * @author cesar.castano
	 */
	@GetMapping(value = "${endpoints.conciliacion.consultar-conciliadas}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<Page<OperacionesProgramadasNombresDTO>>> getConciliadas(
			@QuerydslPredicate(root = OperacionesProgramadas.class) Predicate predicate, Pageable page) {

		Page<OperacionesProgramadasNombresDTO> consulta = conciliacionOperacionesService.getOperacionesConciliadas(
				predicate, page);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Metodo encargado de consultar las operaciones programadas no conciliadas
	 * @param predicate
	 * @param page
	 * @return Page<ProgramadasNoConciliadasNombresDTO>
	 * @author cesar.castano
	 */
	@GetMapping(value = "${endpoints.conciliacion.consultar-programadas-no-conciliadas}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<Page<ProgramadasNoConciliadasDTO>>> getProgramadaNoConcilliada(
			@QuerydslPredicate(root = OperacionesProgramadas.class) Predicate predicate, Pageable page) {
		log.debug("programadas-no-conciliadas - predicate: {}, page: {}", predicate.toString(), page.toString());
		Page<ProgramadasNoConciliadasDTO> consulta = conciliacionOperacionesService.getProgramadaNoConcilliada(predicate, page);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	
	/**
	 * Metodo encargado de consultar los Operaciones Certificadas no conciliadas
	 * @param predicate
	 * @param page
	 * @return Page<CertificadasNoConciliadasDTO>
	 * @author cesar.castano
	 */
	@GetMapping(value = "${endpoints.conciliacion.consultar-certificadas-no-conciliadas}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<Page<CertificadasNoConciliadasDTO>>> getCertificadaNoConciliada(
			@QuerydslPredicate(root = OperacionesCertificadas.class) Predicate predicate, Pageable page) {
		log.debug("consultar-certificadas-no-conciliadas - Predicate: {} - Page: {}", predicate.toString(), page.toString());
		var consulta1 = conciliacionOperacionesService.getCertificadaNoConciliada(predicate, page);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta1, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Controlador que permite actualizar las operaciones programadas fallidas
	 * @param idOperacion
	 * @param estado
	 * @param valor
	 * @return Boolean
	 * @author cesar.castano
	 */
	@PostMapping(value = "${endpoints.conciliacion.update-programadas-fallidas}", 
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<Boolean>> updateOperacionesProgramadasFallidas(
			@RequestBody(required = true) UpdateProgramadasFallidasDTO updateProgramadasFallidasDTO) {

		Boolean respuesta = conciliacionOperacionesService
				.updateOperacionesProgramadasFallidas(updateProgramadasFallidasDTO);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(respuesta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Controlador que permite actualizar las operaciones certificadas fallidas
	 * 
	 * @param idOperacion
	 * @param estado
	 * @return Boolean
	 * @author cesar.castano
	 */
	@PostMapping(value = "${endpoints.conciliacion.update-certificadas-fallidas}", 
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<Boolean>> updateOperacionesCertificadasFallidas(
			@RequestBody(required = true) UpdateCertificadasFallidasDTO updateCertificadasFallidasDTO) {

		Boolean respuesta = conciliacionOperacionesService
				.updateOperacionesCertificadasFallidas(updateCertificadasFallidasDTO);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(respuesta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Metodo encargado de hacer la conciliacion manual de operaciones programadas y
	 * certificadas seleccionadas
	 * 
	 * @return Boolean
	 * @param ParametrosConciliacionDTO
	 * @param page
	 * @author cesar.castano
	 */
	@PostMapping(value = "${endpoints.conciliacion.manual}", 
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<Boolean>> conciliacionManual(
			@RequestBody(required = true) List<ParametrosConciliacionDTO> conciliacionManualDTO) {

		Boolean respuesta = conciliacionOperacionesService.conciliacionManual(conciliacionManualDTO);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(respuesta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Controlador que permite elaborar un resumen de las conciliaciones
	 * 
	 * @param fechaConciliacion
	 * @return ResponseEntity<ApiResponseADE<ResumenConciliacionesDTO>>
	 * @author cesar.castano
	 */
	@PostMapping(value = "${endpoints.conciliacion.consultar-resumen}", 
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<ResumenConciliacionesDTO>> consultaResumenConciliaciones(
			@RequestBody(required = true) FechasConciliacionDTO fechaConciliacion) {

		ResumenConciliacionesDTO consulta = conciliacionOperacionesService
				.consultaResumenConciliaciones(fechaConciliacion);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Controlador que permite hacer el cierre de conciliaciones
	 * 
	 * @return Boolean
	 * @author cesar.castano
	 */
	@PostMapping(value = "${endpoints.conciliacion.cierre}")
	public ResponseEntity<ApiResponseADE<Boolean>> cierreConciliaciones() {

		Boolean consulta = conciliacionOperacionesService.cierreConciliaciones();

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
}
