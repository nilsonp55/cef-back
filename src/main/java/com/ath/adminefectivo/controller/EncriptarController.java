package com.ath.adminefectivo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.delegate.IBancosDelegate;
import com.ath.adminefectivo.delegate.IEncriptarDelegate;
import com.ath.adminefectivo.dto.BancosDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.encript.AES256;
import com.ath.adminefectivo.encript.RSA;
import com.ath.adminefectivo.entities.Bancos;
import com.ath.adminefectivo.service.IParametroService;
import com.ath.adminefectivo.service.impl.ParametroServiceImpl;
import com.querydsl.core.types.Predicate;

/**
 * Controlador responsable de exponer los servicios de encriptacion de archivos
 * @author duvan.naranjo
 */
@RestController
@RequestMapping("${endpoints.Encriptar}")
public class EncriptarController {

	@Autowired
	IEncriptarDelegate encriptarDelegate;
	
	@Autowired
	IParametroService parametros;
	
	/**
	 * Servicio encargado de encriptar un archivo 
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<BancosDTO>>>
	 * @author duvan.naranjo
	 */
	@PostMapping(value = "${endpoints.Encriptar.encriptar}")
	public ResponseEntity<ApiResponseADE<String>> encriptarArchivo(@RequestParam(name = "path") String path, @RequestParam(name = "nombreArchivo") String nombreArchivo) {
		String consulta = encriptarDelegate.encriptarArchivo(path, nombreArchivo);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	@PostMapping(value = "${endpoints.Encriptar.desencriptar}")
	public ResponseEntity<ApiResponseADE<String>> desencriptarArchivo(@RequestParam(name = "path") String path, @RequestParam(name = "nombreArchivo") String nombreArchivo) {
		String consulta = encriptarDelegate.desencriptarArchivo(path, nombreArchivo);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	
	@GetMapping(value = "${endpoints.Encriptar.generarLlaves}")
	public ResponseEntity<ApiResponseADE<String>> generarLlaves() {
		String consulta = encriptarDelegate.generarLlaves();

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}
