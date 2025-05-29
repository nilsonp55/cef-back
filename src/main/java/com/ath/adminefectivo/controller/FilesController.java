package com.ath.adminefectivo.controller;

import java.util.List;

import javax.transaction.RollbackException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Parametros;
import com.ath.adminefectivo.constantes.SwaggerConstants;
import com.ath.adminefectivo.controller.endpoints.FilesEndpoint;
import com.ath.adminefectivo.delegate.IFilesDelegate;
import com.ath.adminefectivo.dto.ArchivosCargadosDTO;
import com.ath.adminefectivo.dto.DownloadDTO;
import com.ath.adminefectivo.dto.DownloadGestionArchivosDTO;
import com.ath.adminefectivo.dto.GestionArchivosDTO;
import com.ath.adminefectivo.dto.MaestrosDefinicionArchivoDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.service.IMaestroDefinicionArchivoService;
import com.ath.adminefectivo.service.IParametroService;
import com.ath.adminefectivo.utils.S3Utils;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;

/**
 * Controlador responsable de exponer los metodos referentes a la carga y
 * persistencia de archivos
 * 
 * @author CamiloBenavides
 */
@RestController
@RequestMapping("${endpoints.Archivos}")
@Log4j2
public class FilesController {

	@Autowired
	IFilesDelegate filesDelegate;
	
	@Autowired
	IMaestroDefinicionArchivoService maestroDefinicionArchivoService;

	@Autowired
	IParametroService parametroService;
	/**
	 * Servicio encargado de ejecutar la prueba de concepto de la lectura y
	 * persistencia de archivos En este servicio no se ve reflejado el manejo de
	 * excepciones ya que es una prueba de concepto y se utiliza como base de la
	 * carga y validacion de integridad de los documentos enviados desde front
	 * 
	 * @param files
	 * @return ResponseEntity<ApiResponseADE<Boolean>>
	 * @author CamiloBenavides
	 */
	@PostMapping(value = FilesEndpoint.SAVE_FILES, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<Boolean>> persistirArchvos(@RequestPart("files") MultipartFile[] files) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<Boolean>(filesDelegate.persistirArchvos(files),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
								.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Servicio encargado de ejecutar la lectura del archivo
	 * @param nombreArchivo
	 * @param idMaestroArchivo
	 * @return ResponseEntity<InputStreamResource>
	 * @author CamiloBenavides
	 */
	@GetMapping(FilesEndpoint.DOWNLOAD_FILE_BY_ID)
	@ApiOperation(value = "Download files ", notes = "")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = SwaggerConstants.RESPONSE_MESSAGE_200) })
	public ResponseEntity<InputStreamResource> downloadFile(
			@RequestParam String nombreArchivo, 
			@RequestParam String idMaestroArchivo) {

		DownloadDTO file = filesDelegate.descargarArchivo(nombreArchivo, idMaestroArchivo);

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, Constantes.PARAMETER_HEADER + file.getName())
				.body(new InputStreamResource(file.getFile()));
	}

	/**
	 * Servicio encargado de ejecutar la lectura del archivo
	 * @param idArchivo
	 * @return ResponseEntity<InputStreamResource>
	 * @author rparra
	 */
	@GetMapping("/descargar-idArchivo")
	@ApiOperation(value = "Download files ", notes = "")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = SwaggerConstants.RESPONSE_MESSAGE_200) })
	public ResponseEntity<InputStreamResource> downloadFileId(
			@RequestParam Long idArchivo)  {

		DownloadDTO file = filesDelegate.descargarArchivoProcesado(idArchivo);

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, Constantes.PARAMETER_HEADER + file.getName())
				.body(new InputStreamResource(file.getFile()));
	}

	/**
	 * Metodo empleado para visualizar las pruebas de concepto del manejador de
	 * excepciones
	 * 
	 * @param exceptionNumber
	 * @return ResponseEntity<ApiResponseADE<Boolean>>
	 * @author CamiloBenavides
	 * @throws RollbackException
	 */
	@GetMapping(value = FilesEndpoint.EXCEPTION_MANAGER, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<Boolean>> manejoExcepciones(@RequestParam("number") Integer exceptionNumber) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<Boolean>(filesDelegate.manejoExcepciones(exceptionNumber),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
								.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Servicio encargado de persistir un archvo cargado desde el archivo se
	 * persiste tanto en el repositorio, como en un registro en la tabla
	 * Archivo_Cargado
	 * 
	 * @param files
	 * @return ResponseEntity<ApiResponseADE<Boolean>>
	 * @author CamiloBenavides
	 */
	@PostMapping(value = "/guardar")
	public ResponseEntity<ApiResponseADE<Boolean>> persistirArchvoCargado(@RequestPart("file") MultipartFile file,
			@RequestPart("tipoCargue") String tipoCargue) {
		log.debug("tipocargue: {}", tipoCargue);
		try {
			S3Utils utils = new S3Utils();
			List<MaestrosDefinicionArchivoDTO> agrup = maestroDefinicionArchivoService
					.consultarDefinicionArchivoByAgrupador(Constantes.ESTADO_MAESTRO_DEFINICION_ACTIVO, tipoCargue);
			String param = parametroService.valorParametro(Parametros.RUTA_ARCHIVOS_PENDIENTES);
			String ubicacion = agrup.get(0).getUbicacion() + param;
			utils.convertAndSaveArchivoEnBytes(file, ubicacion, file.getOriginalFilename());
		} catch (Exception e) {
			log.error("tipoCargue: {} - Error: {}", tipoCargue, e);
			throw new NegocioException(ApiResponseCode.GENERIC_ERROR.getCode(),
					ApiResponseCode.GENERIC_ERROR.getDescription(), ApiResponseCode.GENERIC_ERROR.getHttpStatus());
		}
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<Boolean>(true, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Consulta los archivos cargados en repositorio, por tipo de archivo
	 * (idMaestroDetalle) y estado del archivo
	 * 
	 * @param idMaestroDefinicion
	 * @param estado
	 * @return ResponseEntity<ApiResponseADE<List<ArchivosCargadosDTO>>>
	 * @author CamiloBenavides
	 */
	@GetMapping(value = "${endpoints.Archivos.consultar}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<List<ArchivosCargadosDTO>>> consultarArchivos(
			@RequestParam String idMaestroDefinicion, @RequestParam String estado) {
				log.debug("Entro controller");
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<List<ArchivosCargadosDTO>>(filesDelegate.consultarArchivos(idMaestroDefinicion, estado),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
								.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Servicio para la descarga de los archivos de liquidacion posterior al procesamiento 
	 * @param idArchivo
	 * @return ResponseEntity<InputStreamResource>
	 * @author johan.chaparro
	 */
	@GetMapping("/descargar-idArchivo-liquidacion")
	@ApiOperation(value = "Download files settlement", notes = "")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = SwaggerConstants.RESPONSE_MESSAGE_200) })
	public ResponseEntity<InputStreamResource> downloadLiqFileId(
			@RequestParam Long idArchivo)  {

		DownloadDTO file = filesDelegate.descargarArchivoLiqProcesado(idArchivo);

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, Constantes.PARAMETER_HEADER + file.getName())
				.body(new InputStreamResource(file.getFile()));
	}
	
	/**
	 * Servicio para la descarga de los archivos de liquidacion posterior al procesamiento 
	 * @param idArchivo
	 * @return ResponseEntity<InputStreamResource>
	 * @author johan.chaparro
	 */
	@PostMapping("/descargar-gestion-archivos-liquidacion")
	@ApiOperation(value = "Download files settlement", notes = "")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = SwaggerConstants.RESPONSE_MESSAGE_200) })
	public ResponseEntity<DownloadGestionArchivosDTO> downloadGestionFiles(
	        @RequestBody GestionArchivosDTO archivos)  {

		DownloadGestionArchivosDTO files = filesDelegate.descargarGestionArchivosLiq(archivos);

	    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, Constantes.PARAMETER_HEADER + files.getName())
	            .body(files);
	}
		
}
