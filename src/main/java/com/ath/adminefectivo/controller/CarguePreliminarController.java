package com.ath.adminefectivo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ath.adminefectivo.delegate.ICarguePreliminarDelegate;
import com.ath.adminefectivo.dto.ArchivosCargadosDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;

import lombok.extern.log4j.Log4j2;

/**
 * Controlador responsable de exponer los metodos referentes al proceso de carga
 * preliminar
 * 
 * @author CamiloBenavides
 */
@RestController
@RequestMapping("${endpoints.CarguePreliminar}")
@Log4j2
public class CarguePreliminarController {
	
	@Autowired
	ICarguePreliminarDelegate carguePreliminarDelegate;

	/**
	 * Servicio encargado de persistir un archvo cargado desde el archivo se
	 * persiste tanto en el repositorio, como en un registro en la tabla
	 * Archivo_Cargado
	 * 
	 * @param files
	 * @return ResponseEntity<ApiResponseADE<Boolean>>
	 * @author CamiloBenavides
	 */
	@PostMapping(value = "${endpoints.CarguePreliminar.guardar}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<Boolean>> persistirArchvoCargado(@RequestPart("file") MultipartFile file) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<Boolean>(carguePreliminarDelegate.persistirArchvoCargado(file),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
								.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Servicio encargado de eliminar un archivo tanto de manera fisica del
	 * repositorio, como de manera lógica de la base de datos
	 * 
	 * @param nombreArchivo
	 * @param idMaestroArchivo
	 * @return ResponseEntity<ApiResponseADE<Boolean>>
	 * @author CamiloBenavides
	 */
	@DeleteMapping(value = "${endpoints.CarguePreliminar.eliminar}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<Boolean>> eliminarArchivo(
			@RequestParam("nombreArchivo") String nombreArchivo, 
			@RequestParam("idMaestroArchivo") String idMaestroArchivo) {

		var archivoPersistido = carguePreliminarDelegate.eliminarArchivo(nombreArchivo, idMaestroArchivo);
		return ResponseEntity.status(HttpStatus.OK).body(
				new ApiResponseADE<>(archivoPersistido, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Metodo encargado de realizar la carga del archivo preliminar desde el
	 * repositorio
	 * 
	 * @param idArchivo
	 * @return <ResponseEntity<ApiResponseADE<Boolean>>
	 * @author duvan.naranjo
	 */
	@GetMapping(value = "${endpoints.CarguePreliminar.validar}")
	public ResponseEntity<ApiResponseADE<ValidacionArchivoDTO>> validarArchivo(
			@RequestParam("idMaestroDefinicion") String idMaestroDefinicion,
			@RequestParam("nombreArchivo") String nombreArchivo) {
			
		var respuesta = carguePreliminarDelegate.validarArchivo(idMaestroDefinicion, nombreArchivo);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(respuesta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Método encargado de procesar un archivo cargado en repositorio, y realizar
	 * las validaciones y la respectiva persitencia
	 * 
	 * @param idMaestroDefinicion
	 * @param nombreArchivo
	 * @return ResponseEntity<ApiResponseADE<ValidacionArchivoDTO>>
	 * @author CamiloBenavides
	 */
	@GetMapping(value = "${endpoints.CarguePreliminar.procesar}")
	public ResponseEntity<ApiResponseADE<ValidacionArchivoDTO>> procesarArchivo(
			@RequestParam("idMaestroDefinicion") String idMaestroDefinicion,
			@RequestParam("nombreArchivo") String nombreArchivo) {

		var respuesta = carguePreliminarDelegate.procesarArchivo(idMaestroDefinicion, nombreArchivo);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(respuesta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Método encargado de retornar el detalle de carga de un archivo
	 * 
	 * @param idArchivoCargado
	 * @return ResponseEntity<ApiResponseADE<ValidacionArchivoDTO>>
	 * @author CamiloBenavides
	 */
	@GetMapping(value = "${endpoints.CarguePreliminar.detalle}")
	public ResponseEntity<ApiResponseADE<ValidacionArchivoDTO>> consultarDetalleArchivo(
			@RequestParam("idArchivoCargado") Long idArchivoCargado) {
		log.debug("Entro al controlador de Cargue prelinminar");
		var respuesta = carguePreliminarDelegate.consultarDetalleArchivo(idArchivoCargado);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(respuesta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Consulta los archivos de carga preliminar almacenados en repositorio, por
	 * estado del archivo
	 * 
	 * @param estado
	 * @parm idModeloArchivo
	 * @return ResponseEntity<ApiResponseADE<List<ArchivosCargadosDTO>>>
	 * @author CamiloBenavides
	 */
	@GetMapping(value = "${endpoints.Archivos.consultar}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<List<ArchivosCargadosDTO>>> consultarArchivosCargaPreliminar(
			@RequestParam("estado") String estado, 
			@RequestParam("idModeloArchivo") String agrupador) {
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<List<ArchivosCargadosDTO>>(
						carguePreliminarDelegate.consultarArchivos(estado, agrupador),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
								.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}
