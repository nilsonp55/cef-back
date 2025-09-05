package com.ath.adminefectivo.controller;

import com.ath.adminefectivo.dto.TarifasEspecialesClienteDTO;
import com.ath.adminefectivo.dto.VTarifasEspecialesClienteDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.service.ITarifasEspecialesClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("${endpoints.TarifasEspeciales}")
public class TarifasEspecialesClienteController {

	@Autowired
	private ITarifasEspecialesClienteService tarifasService;

	@GetMapping("${endpoints.TarifasEspeciales.consultar}")
	public ResponseEntity<ApiResponseADE<List<TarifasEspecialesClienteDTO>>> consultar() {
		List<TarifasEspecialesClienteDTO> lista = tarifasService.consultar();
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(lista, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
	@GetMapping("${endpoints.TarifasEspeciales.consultar.cliente}")
	public ResponseEntity<ApiResponseADE<Page<VTarifasEspecialesClienteDTO>>> consultarPorCodigoPunto(
	        @RequestParam(name = "codigoCliente") Integer codigoCliente,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(name = "vigencias", required = false) String vigencia) {

	    Sort sortOrder = Sort.by(
	        Sort.Order.asc("tipoOperacion"),
	        Sort.Order.asc("tipoComision"),
	        Sort.Order.asc("nombreCiudad"),
	        Sort.Order.desc("fechaFinVigencia")
	    );

	    Pageable pageable = PageRequest.of(page, size, sortOrder);

	    Page<VTarifasEspecialesClienteDTO> resultado = tarifasService.consultarPorCodigoCliente(codigoCliente, vigencia, pageable);

	    return ResponseEntity.ok(new ApiResponseADE<>(
	        resultado,
	        ResponseADE.builder()
	            .code(ApiResponseCode.SUCCESS.getCode())
	            .description(ApiResponseCode.SUCCESS.getDescription())
	            .build()
	    ));
	}

	@PostMapping("${endpoints.TarifasEspeciales.guardar}")
	public ResponseEntity<ApiResponseADE<TarifasEspecialesClienteDTO>> guardar(
			@Validated @RequestBody TarifasEspecialesClienteDTO dto) {
		TarifasEspecialesClienteDTO creado = tarifasService.guardar(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseADE<>(creado, ResponseADE.builder()
				.code(ApiResponseCode.SUCCESS.getCode()).description("Registro creado correctamente.").build()));
	}

	@PutMapping("${endpoints.TarifasEspeciales.actualizar}")
	public ResponseEntity<ApiResponseADE<TarifasEspecialesClienteDTO>> actualizar(
			@Validated @RequestBody TarifasEspecialesClienteDTO dto) {
		TarifasEspecialesClienteDTO actualizado = tarifasService.actualizar(dto);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseADE<>(actualizado, ResponseADE.builder()
				.code(ApiResponseCode.SUCCESS.getCode()).description("Registro actualizado correctamente.").build()));
	}

	@DeleteMapping("${endpoints.TarifasEspeciales.eliminar}")
	public ResponseEntity<ApiResponseADE<Void>> eliminar(@RequestParam Long idTarifaEspecial) {
		tarifasService.eliminar(idTarifaEspecial);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseADE<>(null, ResponseADE.builder()
				.code(ApiResponseCode.SUCCESS.getCode()).description("Registro eliminado correctamente.").build()));
	}
}