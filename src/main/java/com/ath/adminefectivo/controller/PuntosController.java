package com.ath.adminefectivo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.delegate.IPuntosDelegate;
import com.ath.adminefectivo.dto.CreatePuntosDTO;
import com.ath.adminefectivo.dto.PuntosDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.Puntos;
import com.querydsl.core.types.Predicate;

/**
 * Controlador responsable de exponer los metodos referentes a los Puntos
 * 
 * @author cesar.castano
 */
@RestController
@RequestMapping("${endpoints.Puntos}")
public class PuntosController {

  @Autowired
  IPuntosDelegate puntosDelegate;

  /**
   * Servicio encargado de retornar la consulta de todos los Puntos
   * 
   * @return ResponseEntity<ApiResponseADE<List<PuntosDTO>>>
   * @author cesar.castano
   * @author prv_nparra
   */
  @GetMapping(value = "${endpoints.Puntos.consultar}")
  public ResponseEntity<ApiResponseADE<Page<PuntosDTO>>> getPuntos(
      @QuerydslPredicate(root = Puntos.class) Predicate predicate, Pageable page, String busqueda) {
    Page<PuntosDTO> consulta = puntosDelegate.getPuntos(predicate, page, busqueda);
    return ResponseEntity.status(HttpStatus.OK).body(
        new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
            .description(ApiResponseCode.SUCCESS.getDescription()).build()));
  }

  /**
   * Servicio encargado de guardar los Puntos
   * 
   * @return ResponseEntity<ApiResponseADE<PuntosDTO>>
   * @author Bayron Andres Perez M
   */
  @PostMapping(value = "${endpoints.Puntos.guardar}", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ApiResponseADE<PuntosDTO>> persistirPuntos(
      @RequestBody CreatePuntosDTO createPuntosDTO) {

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponseADE<PuntosDTO>(puntosDelegate.guardarPunto(createPuntosDTO),
            ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
                .description(ApiResponseCode.SUCCESS.getDescription()).build()));

  }

  /**
   * Servicio encargado de retornar la consulta de todos los Puntos
   * 
   * @return ResponseEntity<ApiResponseADE<List<PuntosDTO>>>
   * @author cesar.castano
   */
  @GetMapping(value = "${endpoints.Puntos.consultar}/{id}")
  public ResponseEntity<ApiResponseADE<PuntosDTO>> getPuntos(@PathVariable Integer id) {
    PuntosDTO consulta = puntosDelegate.getPuntoById(id);
    return ResponseEntity.status(HttpStatus.OK).body(
        new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
            .description(ApiResponseCode.SUCCESS.getDescription()).build()));
  }
}
