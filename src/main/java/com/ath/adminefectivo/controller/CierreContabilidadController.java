package com.ath.adminefectivo.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.delegate.ICierreContabilidadDelegate;
import com.ath.adminefectivo.dto.RespuestaContableDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;

/**
 * Controlador responsable de exponer los metodos correspondientes al 
 * modulo de contabilidad
 * 
 * @author duvan.naranjo
 */
@RestController
@RequestMapping("${endpoints.CierreContabilidad}")
//@RequestMapping("CierreContabilidad")
public class CierreContabilidadController {

	@Autowired
	ICierreContabilidadDelegate cierreContabilidadDelegate;

	/**
	 * Metodo encargado de realizar el cierre contabilidad AM pm
	 * recibida
	 * 
	 * @param fecha,tipoContabilidad,numeroBancos,codBanco,fase
	 * @return ResponseEntity<ApiResponseADE<String>>
	 * @author Miller.Caro
	 */	
	@GetMapping(value = "${endpoints.CierreContabilidad.cerrar}")
	public ResponseEntity<ApiResponseADE<List<RespuestaContableDTO>>> generarContabilidadCierre(
			@RequestParam Date fechaSistema,
			@RequestParam String tipoContabilidad,
			@RequestParam int codBanco,
			@RequestParam String fase		
			) 
	
	{
		List<RespuestaContableDTO> consultas = cierreContabilidadDelegate.cerrarContabilidad(tipoContabilidad, codBanco, fase);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consultas, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}



