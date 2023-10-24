package com.ath.adminefectivo.controller;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.delegate.IContabilidadDelegate;
import com.ath.adminefectivo.dto.compuestos.ContabilidadDTO;
import com.ath.adminefectivo.dto.compuestos.ProcesoErroresContablesDTO;
import com.ath.adminefectivo.dto.compuestos.RespuestaGenerarArchivoDTO;
import com.ath.adminefectivo.dto.compuestos.ResultadoErroresContablesDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;

import lombok.extern.log4j.Log4j2;

/**
 * Controlador responsable de exponer los metodos correspondientes al 
 * modulo de contabilidad
 * 
 * @author duvan.naranjo
 */
@RestController
@RequestMapping("${endpoints.Contabilidad}")
@Log4j2
public class ContabilidadController {

	@Autowired
	IContabilidadDelegate contabilidadDelegate;

	/**
	 * Metodo encargado de realizar la contabilidad según el tipo de contabilidad
	 * recibida
	 * 
	 * @param tipoContabilidad
	 * @return ResponseEntity<ApiResponseADE<ContabilidadDTO>>
	 * @author duvan.naranjo
	 */
	@GetMapping(value = "${endpoints.Contabilidad.generar}")
	public ResponseEntity<ApiResponseADE<ContabilidadDTO>> getGenerarContabilidad(@RequestParam(required = true) String tipoContabilidad) {
		log.info("generar contabilidad tipo: {}", tipoContabilidad);
		ContabilidadDTO consulta = contabilidadDelegate.generarContabilidad(tipoContabilidad);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Metodo encargado de realizar obtener el listado de errores contables 
	 * que se encuentran en el registro de errores contables y no se han 
	 * solucionado
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<ResultadoErroresContablesDTO>>>
	 * @author duvan.naranjo
	 */
	@GetMapping(value = "${endpoints.Contabilidad.consultarErrores}")
	public ResponseEntity<ApiResponseADE<List<ResultadoErroresContablesDTO>>> consultarErroresContables() {

		List<ResultadoErroresContablesDTO> consulta = contabilidadDelegate.consultarErroresContables();

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	/**
	 * Metodo encargado de volver a generar el proceso de contabilidad
	 * basado en los errores contables actuales que no hayan sido resueltos
	 * 
	 * @return ResponseEntity<ApiResponseADEResultadoErroresContablesDTO>>
	 * @author duvan.naranjo
	 */
	@GetMapping(value = "${endpoints.Contabilidad.procesarErrores}")
	@Transactional(readOnly = false)
	public ResponseEntity<ApiResponseADE<ProcesoErroresContablesDTO>> procesarErroresContables() {

		ProcesoErroresContablesDTO consulta = contabilidadDelegate.procesarErroresContables();

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	@GetMapping(value = "${endpoints.Contabilidad.archivoCierre}")
	public ResponseEntity<InputStreamResource> generarContabilidadCierre(@RequestParam(value = "fecha") Date fecha,
			@RequestParam(value = "tipoContabilidad") @Pattern(regexp = "^(AM|PM)$", message = "El valor del parámetro tipoContabilidad debe ser AM o PM") String tipoContabilidad,
			@RequestParam(value = "codBanco") int codBanco) {

		RespuestaGenerarArchivoDTO archivoDTO = contabilidadDelegate.generarArchivo(tipoContabilidad, codBanco);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Expose-Headers", "Content-Disposition");
		headers.add("Content-Disposition", "attachment; filename=" + archivoDTO.getNombreArchivo());
		ByteArrayInputStream bais = new ByteArrayInputStream(archivoDTO.getArchivoBytes().toByteArray());
		return ResponseEntity.ok().headers(headers).body(new InputStreamResource(bais));
				
	}

}
