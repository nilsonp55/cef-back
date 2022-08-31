package com.ath.adminefectivo.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.controller.endpoints.ArchivosCargadosEndpoint;
import com.ath.adminefectivo.delegate.IArchivosCargadosDelegate;
import com.ath.adminefectivo.delegate.ICierreContabilidadDelegate;
import com.ath.adminefectivo.delegate.IContabilidadDelegate;
import com.ath.adminefectivo.dto.ArchivosCargadosDTO;
import com.ath.adminefectivo.dto.BancosDTO;
import com.ath.adminefectivo.dto.RespuestaContableDTO;
import com.ath.adminefectivo.dto.TransaccionesContablesDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.ArchivosCargados;
import com.querydsl.core.types.Predicate;

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
	ICierreContabilidadDelegate CierreContabilidadDelegate;

	/**
	 * Metodo encargado de realizar el cierre contabilidad AM pm
	 * recibida
	 * 
	 * @param fecha,tipoContabilidad,numeroBancos,codBanco,fase
	 * @return ResponseEntity<ApiResponseADE<String>>
	 * @author Miller.Caro
	 */	
	@PostMapping(value = "${endpoints.CierreContabilidad.cerrar}")
	public ResponseEntity<ApiResponseADE<List<RespuestaContableDTO>>> GenerarContabilidadCierre(
			@RequestParam(value = "fechaSistema") Date fechaSistema,
			@RequestParam(value = "tipoContabilidad") String tipoContabilidad,
			@RequestParam(value = "codBanco") int codBanco,
			@RequestParam(value = "fase") String fase		
			) 
	
	{
		System.out.println("ENTRO 69 controller");
		List<RespuestaContableDTO> consultas = CierreContabilidadDelegate.cerrarContabilidad(fechaSistema, tipoContabilidad, codBanco, fase);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consultas, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}



