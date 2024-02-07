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

import com.ath.adminefectivo.delegate.ICargueDefinitivoDelegate;
import com.ath.adminefectivo.dto.ArchivosCargadosDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;

/**
 * Controlador responsable de exponer los metodos referentes al proceso de carga
 * definitiva
 * 
 * @author CamiloBenavides
 */
@RestController
@RequestMapping("${endpoints.CargueDefinitivo}")
public class CargueDefinitivoController {

	@Autowired
	ICargueDefinitivoDelegate cargueDefinitivoDelegate;

	/**
	 * Servicio encargado de persistir un archvo cargado desde el archivo se
	 * persiste tanto en el repositorio, como en un registro en la tabla
	 * Archivo_Cargado
	 * 
	 * @param files
	 * @return ResponseEntity<ApiResponseADE<Boolean>>
	 * @author CamiloBenavides
	 */
	@PostMapping(value = "${endpoints.CargueDefinitivo.guardar}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<Boolean>> persistirArchvoCargado(@RequestPart("file") MultipartFile file) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<Boolean>(cargueDefinitivoDelegate.persistirArchvoCargado(file),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
								.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Servicio encargado de eliminar un archivo tanto de manera fisica del
	 * repositorio, como de manera lógica de la base de datos
	 * 
	 * @param idArchivo
	 * @return ResponseEntity<ApiResponseADE<Boolean>>
	 * @author CamiloBenavides
	 */
	@DeleteMapping(value = "${endpoints.CargueDefinitivo.eliminar}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<Boolean>> eliminarArchivo(@RequestParam String nombreArchivo, @RequestParam("idModeloArchivo") String idMaestroArchivo) {

		var archivoPersistido = cargueDefinitivoDelegate.eliminarArchivo(nombreArchivo, idMaestroArchivo);
		return ResponseEntity.status(HttpStatus.OK).body(
				new ApiResponseADE<>(archivoPersistido, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Metodo encargado de realizar la carga y validaciones del archivo definitivo
	 * desde el repositorio
	 * 
	 * @param idMaestroDefinicion
	 * @param nombreArchivo
	 * @return ResponseEntity<ApiResponseADE<ValidacionArchivoDTO>>
	 * @author CamiloBenavides
	 */
	@GetMapping(value = "${endpoints.CargueDefinitivo.validar}")
	public ResponseEntity<ApiResponseADE<ValidacionArchivoDTO>> validarArchivo(
			@RequestParam String idMaestroDefinicion,
			@RequestParam String nombreArchivo) {

		var respuesta = cargueDefinitivoDelegate.validarArchivo(idMaestroDefinicion, nombreArchivo);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(respuesta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Método encargado de procesar un archivo de carga definitiva ubicado en el
	 * repositorio, y realizar las validaciones y la respectivo procesamiento
	 * 
	 * @param idMaestroDefinicion
	 * @param nombreArchivo
	 * @return ResponseEntity<ApiResponseADE<ValidacionArchivoDTO>>
	 * @author CamiloBenavides
	 */
	@GetMapping(value = "${endpoints.CargueDefinitivo.procesar}")
	public ResponseEntity<ApiResponseADE<ValidacionArchivoDTO>> procesarArchivo(
			@RequestParam String idMaestroDefinicion,
			@RequestParam String nombreArchivo) {

		var respuesta = cargueDefinitivoDelegate.procesarArchivo(idMaestroDefinicion, nombreArchivo);

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
			@RequestParam Long idArchivoCargado) {
		var respuesta = cargueDefinitivoDelegate.consultarDetalleArchivo(idArchivoCargado);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(respuesta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Consulta los archivos de carga definitiva almacenados en repositorio, por
	 * estado del archivo
	 * 
	 * @param estado
	 * @return ResponseEntity<ApiResponseADE<List<ArchivosCargadosDTO>>>
	 * @author CamiloBenavides
	 */
	@GetMapping(value = "${endpoints.Archivos.consultar}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<List<ArchivosCargadosDTO>>> consultarArchivosCargaDefinitiva(
			@RequestParam String estado, @RequestParam("idModeloArchivo") String agrupador) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<List<ArchivosCargadosDTO>>(cargueDefinitivoDelegate.consultarArchivos(estado, agrupador),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
								.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

}
