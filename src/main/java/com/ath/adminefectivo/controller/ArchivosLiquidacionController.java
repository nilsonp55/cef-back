package com.ath.adminefectivo.controller;

import com.ath.adminefectivo.dto.ArchivosLiquidacionDTO;
import com.ath.adminefectivo.dto.compuestos.ArchivosLiquidacionListDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        var consulta = archivosLiquidacionDelegate.getAll(start, end, content, fileName);
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
                        .description(ApiResponseCode.SUCCESS.getDescription()).build()));
    }


    /**
     * Controlador que elimina un archivo por id
     *
     * @param idArchivo
     * @return ResponseEntity<ApiResponseADE<Boolean>>
     * @author juan.ortizt
     */
    @DeleteMapping(value = "${endpoints.ArchivosLiquidacion.eliminar}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseADE<Boolean>> eliminarArchivo(@RequestParam("id") Long idArchivo) {

        var archivoPersistido = archivosLiquidacionDelegate.eliminarArchivo(idArchivo);
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
}
