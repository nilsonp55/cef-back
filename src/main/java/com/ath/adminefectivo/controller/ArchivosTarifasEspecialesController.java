package com.ath.adminefectivo.controller;

import com.ath.adminefectivo.dto.ArchivosLiquidacionDTO;
import com.ath.adminefectivo.dto.ArchivosTarifasEspecialesDTO;
import com.ath.adminefectivo.dto.compuestos.ArchivosLiquidacionListDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import com.ath.adminefectivo.delegate.IArchivosCargadosDelegate;
import com.ath.adminefectivo.delegate.IArchivosTarifasEspecialesDelegate;

import lombok.extern.log4j.Log4j2;

/**
 * Controlador responsable de exponer los metodos referentes al proceso de
 * manejo de archivos tarifas especiales en S3
 *
 * @author jorge.trespalacios
 */
@RestController
@RequestMapping("${endpoints.ArchivosTarifasEspeciales}")
@Log4j2
public class ArchivosTarifasEspecialesController {

    @Autowired
    IArchivosTarifasEspecialesDelegate archivosTarifasEspecialesDelegate;
    
    @Autowired
	IArchivosCargadosDelegate archivosCargadosDelegate;

    /**
     * Metodo encargado de consultar los ArchivosTarifasEspecialesDTO de bucket S3, por filtros y
     * paginador
     *
     * @param start = Define el punto de inicio para el paginado
     * @param end = Define el itema maximo del paginado
     * @param content = Booleando que determina si desea obtener el contenido del archivo
     * @param fileName = Nombre del archivo con extension para consultar solo por un archivo especifico
     * @return ResponseEntity<ApiResponseADE<Page<ArchivosTarifasEspecialesDTO>>>
     * @author juan.ortizt
     */
    @GetMapping(value = "${endpoints.ArchivosTarifasEspeciales.consultar}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseADE<Page<ArchivosTarifasEspecialesDTO>>> getAllFiles(
    		@RequestParam(name = "start", defaultValue = "1") int start,
            @RequestParam(name = "end", defaultValue = "5") int end,
            @RequestParam(name = "content", defaultValue = "false") boolean content,
            @RequestParam(name = "fileName", required = false, defaultValue = "") String fileName,
            @RequestParam(name = "idOption", defaultValue = "1") int idOption) {
        var consulta = archivosTarifasEspecialesDelegate.getAll(start, end, content, fileName, Optional.empty(), idOption);
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
                        .description(ApiResponseCode.SUCCESS.getDescription()).build()));
    }
    
    /**
	 * Método encargado de Validar y Procesar un archivo de carga de Liquidacion TDV ubicado en el
	 * repositorio, y realizar las validaciones y el respectivo procesamiento
	 * @param idMaestroDefinicion
	 * @param nombreArchivo
	 * @return ResponseEntity<ApiResponseADE<ValidacionArchivoDTO>>
	 * @author johan.caparro
	 */
	@PostMapping(value = "${endpoints.ArchivosTarifasEspeciales.procesar}")
	public ResponseEntity<ApiResponseADE<Page<ArchivosLiquidacionDTO>>> procesarArchivos(
			@RequestBody ArchivosLiquidacionListDTO archivosProcesar) {

		var respuesta = archivosTarifasEspecialesDelegate.procesarAchivosTarifasEspeciales(archivosProcesar);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(respuesta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	
	@PostMapping(value = "${endpoints.ArchivosTarifasEspeciales.consultar-contenido}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseADE<Page<ArchivosLiquidacionDTO>>> getContentFiles(
    		@RequestBody ArchivosLiquidacionListDTO archivosProcesar) {
        var consulta = archivosTarifasEspecialesDelegate.getContentFile(archivosProcesar);
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
                        .description(ApiResponseCode.SUCCESS.getDescription()).build()));
    }
	
	
	@DeleteMapping(value = "${endpoints.ArchivosTarifasEspeciales.eliminar}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<Boolean>> eliminarArchivo(
			@RequestBody ArchivosLiquidacionListDTO archivosProcesar) {

		var archivoPersistido = archivosTarifasEspecialesDelegate.eliminarArchivoTarifaEspecial(archivosProcesar);
		return ResponseEntity.status(HttpStatus.OK).body(
				new ApiResponseADE<>(archivoPersistido, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}


		
}
