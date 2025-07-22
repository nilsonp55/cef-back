package com.ath.adminefectivo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.dto.TasasCambioDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.TasasCambio;
import com.ath.adminefectivo.entities.TasasCambioPK;
import com.ath.adminefectivo.service.TasasCambioService;
import com.querydsl.core.types.Predicate;

/**
 * Controlador responsable de exponer los metodos referentes a las tasas cambio 
 * @author bayron.perez
 */
@RestController
@RequestMapping("${endpoints.TasasCambio}")
public class TasasCambioController {

	@Autowired
	TasasCambioService tasasCambioService;

	/**
	 * Servicio encargado de retornar la consulta de todos los tasasCambios
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<TasasCambioDTO>>>
	 * @author bayron.perez
	 */
    @GetMapping(value = "${endpoints.TasasCambio.consultar}")
    public ResponseEntity<ApiResponseADE<Page<TasasCambioDTO>>> getTasasCambios(
        @QuerydslPredicate(root = TasasCambio.class) Predicate predicate, Pageable page) {
      Page<TasasCambio> consulta = tasasCambioService.getTasasCambios(predicate, page);

      Page<TasasCambioDTO> listtasasCambiosDto =
          new PageImpl<>(consulta.stream().map(TasasCambioDTO.CONVERTER_DTO).toList(),
              consulta.getPageable(), consulta.getTotalElements());

      return ResponseEntity.status(HttpStatus.OK)
          .body(new ApiResponseADE<>(listtasasCambiosDto,
              ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
                  .description(ApiResponseCode.SUCCESS.getDescription()).build()));
    }
	
	/**
	 * Servicio encargado de retornar la consulta de un tasasCambio
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<TasasCambioDTO>>>
	 * @author bayron.perez
	 */
	@GetMapping(value = "${endpoints.TasasCambio.consultar-id}")
	public ResponseEntity<ApiResponseADE<TasasCambioDTO>> getTasasCambioById(@RequestBody TasasCambioPK tasasCambioPK) {
		TasasCambio consulta = tasasCambioService.getTasasCambioById(tasasCambioPK);
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(TasasCambioDTO.CONVERTER_DTO.apply(consulta), ResponseADE.builder().
						code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}

	/**
	 * Servicio encargado de guardar tasasCambio
	 * 
	 * @return ResponseEntity<ApiResponseADE<TasasCambioDTO>>
	 * @author Bayron Andres Perez M
	 */
	@PostMapping(value = "${endpoints.TasasCambio.guardar}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<TasasCambioDTO>> postTasasCambio(@RequestBody TasasCambioDTO tasasCambioDTO) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<TasasCambioDTO>(TasasCambioDTO.CONVERTER_DTO.apply(tasasCambioService.
						postTasasCambio(TasasCambioDTO.CONVERTER_ENTITY.apply(tasasCambioDTO))),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));

	}
	
	/**
	 * Servicio encargado de guardar tasasCambio
	 * 
	 * @return ResponseEntity<ApiResponseADE<TasasCambioDTO>>
	 * @author Bayron Andres Perez M
	 */
	@PutMapping(value = "${endpoints.TasasCambio.actualizar}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseADE<TasasCambioDTO>> putCuentasPuc(@RequestBody TasasCambioDTO tasasCambioDTO) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<TasasCambioDTO>(TasasCambioDTO.CONVERTER_DTO.apply(tasasCambioService.
						putTasasCambio(TasasCambioDTO.CONVERTER_ENTITY.apply(tasasCambioDTO))),
						ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));

	}
	
	/**
	 * Servicio encargado eliminar una tasasCambio
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<boolean>>>
	 * @author bayronperez
	 */
	@DeleteMapping(value = "${endpoints.TasasCambio.eliminar}")
	public ResponseEntity<ApiResponseADE<Boolean>> eliminarTasasCambio(@RequestBody TasasCambioDTO tasasCambioDTO) {
		tasasCambioService.deleteTasasCambio(TasasCambioDTO.CONVERTER_ENTITY.apply(tasasCambioDTO));
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(Boolean.TRUE, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
}
