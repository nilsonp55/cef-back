package com.ath.adminefectivo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.service.IEncriptarService;
import com.ath.adminefectivo.service.IParametroService;

/**
 * Controlador responsable de exponer los servicios de encriptacion de archivos
 * @author duvan.naranjo
 */
@RestController
@RequestMapping("${endpoints.Encriptar}")
public class EncriptarController {

	@Autowired
	IEncriptarService encriptarService;
	
	@Autowired
	IParametroService parametros;
	
	/**
	 * Servicio encargado de encriptar un archivo 
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<BancosDTO>>>
	 * @author duvan.naranjo
	 */
	@PostMapping(value = "${endpoints.Encriptar.encriptar}")
	public ResponseEntity<ApiResponseADE<String>> encriptarArchivo(@RequestParam String path, @RequestParam String nombreArchivo) {
		String consulta = encriptarService.encriptarArchivo(path, nombreArchivo);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	@PostMapping(value = "${endpoints.Encriptar.desencriptar}")
	public ResponseEntity<ApiResponseADE<String>> desencriptarArchivo(@RequestParam String path, @RequestParam String nombreArchivo) {
		String consulta = encriptarService.desencriptarArchivo(path, nombreArchivo);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	
	@GetMapping(value = "${endpoints.Encriptar.generarLlaves}")
	public ResponseEntity<ApiResponseADE<String>> generarLlaves() {
		String consulta = encriptarService.generarLlaves();

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
}
