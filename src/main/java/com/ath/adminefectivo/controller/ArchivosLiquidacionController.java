package com.ath.adminefectivo.controller;

import com.ath.adminefectivo.dto.ArchivosLiquidacionDTO;
import com.ath.adminefectivo.dto.compuestos.ArchivosLiquidacionListDTO;
import com.ath.adminefectivo.dto.RegistrosCargadosDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ath.adminefectivo.delegate.IArchivosLiquidacionDelegate;

import lombok.extern.log4j.Log4j2;

/**
 * Controlador responsable de exponer los metodos referentes al proceso de
 * manejo de archivos liquidación en S3
 *
 * @author juan.ortizt
 */
@RestController
@RequestMapping("${endpoints.ArchivosLiquidacion}")
@Log4j2
public class ArchivosLiquidacionController {

    @Autowired
    IArchivosLiquidacionDelegate archivosLiquidacionDelegate;

    /**
     * Metodo encargado de consultar los ArchivosLiquidacionDTO de bucket S3, por filtros y
     * paginador
     *
     * @param start = Define el punto de inicio para el paginado
     * @param end = Define el itema maximo del paginado
     * @param content = Booleando que determina si desea obtener el contenido del archivo
     * @param fileName = Nombre del archivo con extension para consultar solo por un archivo especifico
     * @return ResponseEntity<ApiResponseADE<Page<ArchivosLiquidacionDTO>>>
     * @author juan.ortizt
     */
    @GetMapping(value = "${endpoints.ArchivosLiquidacion.consultar}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseADE<Page<ArchivosLiquidacionDTO>>> getAllFiles(
    		@RequestParam(name = "start", defaultValue = "1") int start,
            @RequestParam(name = "end", defaultValue = "5") int end,
            @RequestParam(name = "content", defaultValue = "false") boolean content,
            @RequestParam(name = "fileName", required = false, defaultValue = "") String fileName) {
        var consulta = archivosLiquidacionDelegate.getAll(start, end, content, fileName, Optional.empty());
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
                        .description(ApiResponseCode.SUCCESS.getDescription()).build()));
    }


    /**
     * Controlador que elimina un archivo por id
     *
     * @param idArchivo
     * @return ResponseEntity<ApiResponseADE<ArchivosLiquidacionListDTO>>
     * @author johan.chaparro
     */
    @PostMapping(value = "${endpoints.ArchivosLiquidacion.eliminar}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseADE<ArchivosLiquidacionListDTO>> eliminarArchivo(
    		@RequestBody(required = true) ArchivosLiquidacionListDTO archivosLiquidacion) {

        var archivoPersistido = archivosLiquidacionDelegate.eliminarArchivo(archivosLiquidacion);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponseADE<>(archivoPersistido, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
                        .description(ApiResponseCode.SUCCESS.getDescription()).build()));
    }
    
	/**
	 * Método encargado de Validar y Procesar un archivo de carga de Liquidacion TDV ubicado en el
	 * repositorio, y realizar las validaciones y el respectivo procesamiento
	 * @param idMaestroDefinicion
	 * @param nombreArchivo
	 * @return ResponseEntity<ApiResponseADE<ValidacionArchivoDTO>>
	 * @author hector.mercado
	 */
	@PostMapping(value = "${endpoints.ArchivosLiquidacion.procesar}")
	public ResponseEntity<ApiResponseADE<Page<ArchivosLiquidacionDTO>>> procesarArchivos(
			@RequestBody ArchivosLiquidacionListDTO archivosProcesar) {

		var respuesta = archivosLiquidacionDelegate.procesarAchivos(archivosProcesar);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(respuesta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Método encargado de retornar el detalles de los errores ocurridos durante la carga de un archivo
	 * @param idArchivoCargado
	 * @return ResponseEntity<ApiResponseADE<ValidacionArchivoDTO>>
	 * @author johan.chaparro
	 */
	@GetMapping(value = "${endpoints.ArchivosLiquidacion.detalle-error}")
	public ResponseEntity<ApiResponseADE<ValidacionArchivoDTO>> consultarDetalleErrorArchivo(
			@RequestParam("idArchivoCargado") Long idArchivoCargado) {
		
		var respuesta = archivosLiquidacionDelegate.consultarDetalleError(idArchivoCargado);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(respuesta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Método encargado de retornar el detalles de los registros de un archivo
	 * @param idArchivoCargado
	 * @return ResponseEntity<ApiResponseADE<List<RegistrosCargadosDTO>>>
	 * @author johan.chaparro
	 */
	@GetMapping(value = "${endpoints.ArchivosLiquidacion.detalle}")
	public ResponseEntity<ApiResponseADE<List<RegistrosCargadosDTO>>> consultarDetalleArchivo(
			@RequestParam("idArchivoCargado") Long idArchivoCargado) {
		
		var respuesta = archivosLiquidacionDelegate.consultarDetalleArchivo(idArchivoCargado);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(respuesta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
		
}
