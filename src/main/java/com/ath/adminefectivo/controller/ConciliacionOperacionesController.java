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

import com.ath.adminefectivo.delegate.IConciliacionOperacionesDelegate;
import com.ath.adminefectivo.dto.CertificadasNoConciliadasDTO;
import com.ath.adminefectivo.dto.ParametrosConciliacionManualDTO;
import com.ath.adminefectivo.dto.FechasConciliacionDTO;
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
import com.querydsl.core.types.Predicate;

/**
 * Controlador responsable de exponer los metodos referentes al proceso de
 * conciliacion
 * 
 * @author cesar.castano
 */
@RestController
@RequestMapping("${endpoints.conciliacion}")
public class ConciliacionOperacionesController {

	@Autowired
	IConciliacionOperacionesDelegate conciliacionOperacionesDelegate;

	/**
	 * Metodo encargado de retornar la lista de todas las operaciones conciliadas
	 * @return Page<OperacionesProgramadasConciliadasDTO>
	 * @param page
	 * @param predicate
	 * @author cesar.castano
	 */
	@PostMapping(value = "${endpoints.conciliacion.consultar-conciliadas}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<Page<OperacionesProgramadasNombresDTO>>> getConciliadas(
			@QuerydslPredicate(root = OperacionesProgramadas.class) Predicate predicate, Pageable page) {

		Page<OperacionesProgramadasNombresDTO> consulta = conciliacionOperacionesDelegate.getOperacionesConciliadas(
				predicate, page);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Metodo encargado de consultar las operaciones programadas no conciliadas
	 * @param predicate
	 * @param page
	 * @return Page<ProgramadasNoConciliadasDTO>
	 * @author cesar.castano
	 */
	@PostMapping(value = "${endpoints.conciliacion.consultar-programadas-no-conciliadas}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<Page<ProgramadasNoConciliadasDTO>>> getProgramadaNoConcilliada(
			@QuerydslPredicate(root = OperacionesProgramadas.class) Predicate predicate, Pageable page) {

		var consulta = conciliacionOperacionesDelegate.getProgramadaNoConcilliada(predicate, page);

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
	@PostMapping(value = "${endpoints.conciliacion.consultar-certificadas-no-conciliadas}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<Page<CertificadasNoConciliadasDTO>>> getCertificadaNoConciliada(
			@QuerydslPredicate(root = OperacionesCertificadas.class) Predicate predicate, Pageable page) {

		var consulta1 = conciliacionOperacionesDelegate.getCertificadaNoConciliada(predicate, page);

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

		Boolean respuesta = conciliacionOperacionesDelegate
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

		Boolean respuesta = conciliacionOperacionesDelegate
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
	 * @param ParametrosConciliacionManualDTO
	 * @param page
	 * @author cesar.castano
	 */
	@PostMapping(value = "${endpoints.conciliacion.manual}", 
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<Boolean>> conciliacionManual(
			@RequestBody(required = true) List<ParametrosConciliacionManualDTO> conciliacionManualDTO) {

		Boolean respuesta = conciliacionOperacionesDelegate.conciliacionManual(conciliacionManualDTO);

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

		ResumenConciliacionesDTO consulta = conciliacionOperacionesDelegate
				.consultaResumenConciliaciones(fechaConciliacion);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Metodo encargado de hacer la conciliacion automatica de operaciones
	 * @param fecha
	 * @return Boolean
	 * @author cesar.castano
	 */
	@GetMapping(value = "${endpoints.conciliacion.automatica}")
	public ResponseEntity<ApiResponseADE<Boolean>> conciliacionAutomatica(){

		Boolean respuesta = conciliacionOperacionesDelegate.conciliacionAutomatica();

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(respuesta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
}
