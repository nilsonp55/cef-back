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

import com.ath.adminefectivo.delegate.ICargueCertificacionDelegate;
import com.ath.adminefectivo.dto.ArchivosCargadosDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;

/**
 * Controlador responsable de exponer los metodos referentes al proceso de carga
 * de certificaciones
 * @author cesar.castano
 */
@RestController
@RequestMapping("${endpoints.CargueCertificacion}")
public class CargueCertificacionController {

	@Autowired
	ICargueCertificacionDelegate cargueCertificacionDelegate;
	
	/**
	 * Servicio encargado de persistir un archvo cargado. Desde el archivo se
	 * persiste tanto en el repositorio, como en un registro en la tabla
	 * Archivo_Cargado
	 * @param file
	 * @return ResponseEntity<ApiResponseADE<Boolean>>
	 * @author cesar.castano
	 */
	@PostMapping(value = "${endpoints.CargueCertificacion.guardar}", 
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<Boolean>> persistirArchvoCargado(@RequestPart("file") MultipartFile file) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<Boolean>(cargueCertificacionDelegate.persistirArchvoCargado(file),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
								.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Servicio encargado de eliminar un archivo tanto de manera fisica del
	 * repositorio, como de manera lógica de la base de datos
	 * @param idArchivo
	 * @return ResponseEntity<ApiResponseADE<Boolean>>
	 * @author cesar.castano
	 */
	@DeleteMapping(value = "${endpoints.CargueCertificacion.eliminar}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<Boolean>> eliminarArchivo(@RequestParam("nombreArchivo") String nombreArchivo, @RequestParam("idModeloArchivo") String idMaestroArchivo) {

		var archivoPersistido = cargueCertificacionDelegate.eliminarArchivo(nombreArchivo, idMaestroArchivo);
		return ResponseEntity.status(HttpStatus.OK).body(
				new ApiResponseADE<>(archivoPersistido, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Metodo encargado de realizar la carga y validaciones del archivo de certificacion
	 * desde el repositorio
	 * @param idMaestroDefinicion
	 * @param nombreArchivo
	 * @return ResponseEntity<ApiResponseADE<ValidacionArchivoDTO>>
	 * @author cesar.castano
	 */
	@GetMapping(value = "${endpoints.CargueCertificacion.validar}")
	public ResponseEntity<ApiResponseADE<ValidacionArchivoDTO>> validarArchivo(
			@RequestParam("idMaestroDefinicion") String idMaestroDefinicion,
			@RequestParam("nombreArchivo") String nombreArchivo) {

		var respuesta = cargueCertificacionDelegate.validarArchivo(idMaestroDefinicion, nombreArchivo);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(respuesta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Método encargado de procesar un archivo de carga de certificacion ubicado en el
	 * repositorio, y realizar las validaciones y el respectivo procesamiento
	 * @param idMaestroDefinicion
	 * @param nombreArchivo
	 * @return ResponseEntity<ApiResponseADE<ValidacionArchivoDTO>>
	 * @author cesar.castano
	 */
	@GetMapping(value = "${endpoints.CargueCertificacion.procesar}")
	public ResponseEntity<ApiResponseADE<ValidacionArchivoDTO>> procesarArchivo(
			@RequestParam("idMaestroDefinicion") String idMaestroDefinicion,
			@RequestParam("nombreArchivo") String nombreArchivo) {

		var respuesta = cargueCertificacionDelegate.procesarArchivo(idMaestroDefinicion, nombreArchivo);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(respuesta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Método encargado de retornar el detalle de carga de un archivo
	 * @param idArchivoCargado
	 * @return ResponseEntity<ApiResponseADE<ValidacionArchivoDTO>>
	 * @author cesar.castano
	 */
	@GetMapping(value = "${endpoints.CargueCertificacion.detalle}")
	public ResponseEntity<ApiResponseADE<ValidacionArchivoDTO>> consultarDetalleArchivo(
			@RequestParam("idArchivoCargado") Long idArchivoCargado) {
		
		var respuesta = cargueCertificacionDelegate.consultarDetalleArchivo(idArchivoCargado);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(respuesta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Consulta los archivos de carga de certificacion almacenados en repositorio, por
	 * estado del archivo
	 * @param estado
	 * @return ResponseEntity<ApiResponseADE<List<ArchivosCargadosDTO>>>
	 * @author cesar.castano
	 */
	@GetMapping(value = "${endpoints.Archivos.consultar}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<List<ArchivosCargadosDTO>>> consultarArchivosCargaCertificacion(
					@RequestParam("estado") String estado, 
					@RequestParam("idMaestroDefinicion") String agrupador) {
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<List<ArchivosCargadosDTO>>(cargueCertificacionDelegate.consultarArchivos(estado, agrupador),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
								.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

}
