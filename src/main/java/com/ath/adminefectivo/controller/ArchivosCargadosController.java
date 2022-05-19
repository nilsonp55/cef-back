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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.controller.endpoints.ArchivosCargadosEndpoint;
import com.ath.adminefectivo.delegate.IArchivosCargadosDelegate;
import com.ath.adminefectivo.dto.ArchivosCargadosDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.ArchivosCargados;
import com.querydsl.core.types.Predicate;

/**
 * Controlador responsable de exponer los metodos referentes a los archivos de
 * carga
 * 
 * @author CamiloBenavides
 */
@RestController
@RequestMapping(ArchivosCargadosEndpoint.V1_0_1)
public class ArchivosCargadosController {

	@Autowired
	IArchivosCargadosDelegate archivosCargadosDelegate;

	/**
	 * Metodo encargado de retornar la lista de todos los archivos cargados
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<ArchivosCargadosDTO>>>
	 * @author CamiloBenavides
	 */
	@GetMapping(value = ArchivosCargadosEndpoint.FINDALL)
	public ResponseEntity<ApiResponseADE<List<ArchivosCargadosDTO>>> getAll() {

		var consulta = archivosCargadosDelegate.getAll();

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Metodo encargado de consultar los ArchivosCargados del sistema, por filtros y
	 * paginador
	 * 
	 * @param predicate
	 * @param page
	 * @return ResponseEntity<ApiResponseADE<Page<ArchivosCargadosDTO>>>
	 * @author CamiloBenavides
	 */
	@GetMapping(value = ArchivosCargadosEndpoint.FINDALL_PAGE)
	public ResponseEntity<ApiResponseADE<Page<ArchivosCargadosDTO>>> getAll(
			@QuerydslPredicate(root = ArchivosCargados.class) Predicate predicate, Pageable page) {

		var consulta = archivosCargadosDelegate.getAll(predicate, page);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Controlador que permite persistir un nuevo registro de ArchivosCargados
	 * 
	 * @param archivo
	 * @return ResponseEntity<ApiResponseADE<ArchivosCargados>>: retorna la entidad
	 *         creada con el id autoincremental
	 * @author CamiloBenavides
	 */
	@PostMapping(value = ArchivosCargadosEndpoint.SAVE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<List<ArchivosCargados>>> guardarArchivo(
			@RequestBody(required = true) ArchivosCargadosDTO archivo) {

		var archivoPersistido = archivosCargadosDelegate.guardarArchivo(archivo);
		return ResponseEntity.status(HttpStatus.OK).body(
				new ApiResponseADE<>(archivoPersistido, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Controlador que elimina un archvo por id
	 * 
	 * @param idArchivo
	 * @return ResponseEntity<ApiResponseADE<Boolean>>
	 * @author CamiloBenavides
	 */
	@DeleteMapping(value = ArchivosCargadosEndpoint.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<Boolean>> eliminarArchivo(@RequestParam("id") Long idArchivo) {

		var archivoPersistido = archivosCargadosDelegate.eliminarArchivo(idArchivo);
		return ResponseEntity.status(HttpStatus.OK).body(
				new ApiResponseADE<>(archivoPersistido, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

}
