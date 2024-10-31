package com.ath.adminefectivo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.dto.UpdateEstadoOperacionesDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.service.IConciliacionOperacionesUpdateService;

import lombok.extern.log4j.Log4j2;

/**
 * Exponer los metodos referentes al proceso de conciliacion
 * 
 * @author prv_nparra
 */
@RestController
@RequestMapping("${endpoints.conciliacion}")
@Log4j2
public class ConciliacionOperacionesUpdateEstadoController {

	@Autowired
	private IConciliacionOperacionesUpdateService conciliacionOperacionesUpdateService;

	/**
	 * Permite actualizar el estado de un listado de operaciones programadas
	 * 
	 * @param updateEstadoProgramadasDTOList
	 * @return Un List de operaciones porgramadas indicando si se actualizo
	 *         correctamente por cada operacion programada
	 */
	@PostMapping(value = "${endpoints.conciliacion.update-estado-programadas}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<List<UpdateEstadoOperacionesDTO>>> updateEstadoProgramadas(
			@RequestBody(required = true) List<UpdateEstadoOperacionesDTO> updateEstadoProgramadasDTOList) {
		log.info("Operaciones programadas para actualizar estado: {}", updateEstadoProgramadasDTOList.size());

		List<UpdateEstadoOperacionesDTO> respuesta = conciliacionOperacionesUpdateService
				.updateEstadoProgramadas(updateEstadoProgramadasDTOList);
		log.info("Operaciones programadas actualizadas para estado: {}", respuesta.size());
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(respuesta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Permite actualizar el estado de un listado de operaciones certificadas
	 * 
	 * @param updateEstadoOperacionesDTOList
	 * @return Un List de operaciones certificadas indicando si se actualizo
	 *         correctamente por cada operacion certificada
	 */
	@PostMapping(value = "${endpoints.conciliacion.update-estado-certificadas}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<List<UpdateEstadoOperacionesDTO>>> updateEstadoCertificadas(
			@RequestBody(required = true) List<UpdateEstadoOperacionesDTO> updateEstadoCertificadasDTOList) {
		log.info("Operaciones certificadas para actualizar estado: {}", updateEstadoCertificadasDTOList.size());

		List<UpdateEstadoOperacionesDTO> respuesta = conciliacionOperacionesUpdateService
				.updateEstadoCertificadas(updateEstadoCertificadasDTOList);
		log.info("Operaciones programadas actualizadas para estado: {}", respuesta.size());
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(respuesta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}
