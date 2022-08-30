package com.ath.adminefectivo.controller;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.delegate.IGenerarArchivoDelegate;
import com.ath.adminefectivo.delegate.impl.GenerarArchivoDelegate;
import com.ath.adminefectivo.dto.RespuestaContableDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;

@RestController
@RequestMapping("${endpoints.GenerarArchivo}")
public class GenerarArchivoController {
	/**
	 * Metodo encargado de generar la contabilidad
	 * 
	 * @param fecha,tipoContabilidad,codBanco, o numero de bancos
	 * @return ResponseEntity<ApiResponseADE<String>>
	 * @author Miller.Caro
	 */
	
	@Autowired
	GenerarArchivoDelegate generarArchivoDelegate;
	
	@PostMapping(value = "${endpoints.GenerarArchivo.generar}"
)
	//public ResponseEntity<ApiResponseADE<InputStreamResource>> GenerarContabilidadCierre(
	  public ResponseEntity<InputStreamResource> GenerarContabilidadCierre(
			@RequestParam(value = "fecha") Date fecha,
			@RequestParam(value = "tipoContabilidad") String tipoContabilidad,
			@RequestParam(value = "codBanco") int codBanco
			) {
		ByteArrayInputStream archivo =generarArchivoDelegate.generarArchivo(fecha, tipoContabilidad, codBanco);
 		 HttpHeaders headers = new HttpHeaders();
 		 headers.add("Content-Disposition", "attachment; filename=cierreContable.xls");
 		 
 		 
		// return ResponseEntity.status(HttpStatus.OK)
			//		.body(new ApiResponseADE<>(archivo, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
				//			.description(ApiResponseCode.SUCCESS.getDescription()).build()));
		 
		 return ResponseEntity.ok().headers(headers).body(new InputStreamResource(archivo));
		 
	}
}




