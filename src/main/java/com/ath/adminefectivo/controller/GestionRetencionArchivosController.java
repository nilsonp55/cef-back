package com.ath.adminefectivo.controller;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.delegate.impl.GestionRetencionArchivosDelegateImpl;
import com.ath.adminefectivo.dto.BancosDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.Bancos;
import com.ath.adminefectivo.service.IBancosService;
import com.ath.adminefectivo.service.IGestionRetencionArchivosService;
import com.querydsl.core.types.Predicate;

@RestController
@RequestMapping("${endpoints.GestionRetencionArchivos}")
public class GestionRetencionArchivosController {
	
	@Autowired
	IGestionRetencionArchivosService gestionRetencionArchivos;
	
	@GetMapping(value = "${endpoints.GestionRetencionArchivos.eliminar}")
	public ResponseEntity<ApiResponseADE<Boolean>> eliminarRetencionArchivo() {
		var archivosEliminados = gestionRetencionArchivos.EliminarArchivosPorRetencion();
		return ResponseEntity.status(HttpStatus.OK).body(
				new ApiResponseADE<>(archivosEliminados, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}	

}
