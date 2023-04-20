package com.ath.adminefectivo.controller;

import java.io.ByteArrayInputStream;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.delegate.impl.GenerarArchivoDelegate;
import com.ath.adminefectivo.dto.compuestos.RespuestaGenerarArchivoDTO;

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
	
	@GetMapping(value = "${endpoints.GenerarArchivo.generar}")
	  public ResponseEntity<InputStreamResource> GenerarContabilidadCierre(
			@RequestParam(value = "fecha") Date fecha,
			@RequestParam(value = "tipoContabilidad") String tipoContabilidad,
			@RequestParam(value = "codBanco") int codBanco
			) {
		RespuestaGenerarArchivoDTO archivo =generarArchivoDelegate.generarArchivo(tipoContabilidad, codBanco);
 		 HttpHeaders headers = new HttpHeaders();
 		 headers.add("Content-Disposition", "attachment; filename="+archivo.getNombreArchivo());
 		ByteArrayInputStream bais =	new ByteArrayInputStream(archivo.getArchivoBytes().toByteArray());
		 return ResponseEntity.ok().headers(headers).body(new InputStreamResource(bais));
		 
	}
}




