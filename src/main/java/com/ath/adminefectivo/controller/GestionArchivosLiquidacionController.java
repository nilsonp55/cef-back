package com.ath.adminefectivo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import com.ath.adminefectivo.delegate.IGestionArchivosLiquidacionDelegate;
import com.ath.adminefectivo.dto.ArchivosCargadosDTO;
import com.ath.adminefectivo.dto.ArchivosLiquidacionDTO;
import com.ath.adminefectivo.dto.compuestos.ArchivosLiquidacionListDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;

import lombok.extern.log4j.Log4j2;

/**
 * Controlador responsable de exponer los metodos referentes a la gestion de los
 * archivos de costos transporte y procesamiento para los procesos de carga
 * y conciliacion
 * 
 * @author johan.chaparro
 */
@RestController
@RequestMapping("${endpoints.GestionArchivosLiquidacion}")
public class GestionArchivosLiquidacionController {
	
	@Autowired
    IGestionArchivosLiquidacionDelegate gestionArchivosLiquidacion;
	
	/**
	 * Metodo encargado de consultar los ArchivosCargados del sistema, por filtros y
	 * paginador
	 * 
	 * @param agrupador
	 * @param page
	 * @return ResponseEntity<ApiResponseADE<Page<ArchivosCargadosDTO>>>
	 * @author johan.chaparro
	 */
	
	@GetMapping(value = "${endpoints.GestionArchivosLiquidacion.consultarPorAgrupador}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseADE<Page<ArchivosLiquidacionDTO>>> getAllFiles(
    		@RequestParam String agrupador,
    		@RequestParam int page,
			@RequestParam int size) {
		
		Pageable pr = PageRequest.of(page, size);
        var consulta = gestionArchivosLiquidacion.getAll(agrupador,pr);
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
                        .description(ApiResponseCode.SUCCESS.getDescription()).build()));
    }
	
	/**
	 * Método encargado de Validar y realizar el cierre de la conciliacion de cada archivo 
	 * @param ArchivosLiquidacionListDTO
	 * @return ResponseEntity<ApiResponseADE<Page<ArchivosLiquidacionDTO>>>
	 * @author hector.mercado
	 */
	@PostMapping(value = "${endpoints.GestionArchivosLiquidacion.aceptarArchivos}")
	public ResponseEntity<ApiResponseADE<Page<ArchivosLiquidacionDTO>>> aceptarArchivos(
			@RequestBody ArchivosLiquidacionListDTO archivosAceptar) {

		var respuesta = gestionArchivosLiquidacion.aceptarArchivos(archivosAceptar);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(respuesta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

}
