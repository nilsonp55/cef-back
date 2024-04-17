package com.ath.adminefectivo.delegate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ath.adminefectivo.dto.CreatePuntosDTO;
import com.ath.adminefectivo.dto.PuntosDTO;
import com.querydsl.core.types.Predicate;

/**
 * Delegate que expone los servicios referente a los puntos
 * 
 * @author cesar.castano
 */
public interface IPuntosDelegate {

  /**
   * Delegate encargado de retornar la lista de todos los Puntos
   * 
   * @return List<PuntosDTO>
   * @author cesar.castano
   */
  Page<PuntosDTO> getPuntos(Predicate predicate, Pageable page, String busqueda);

  /**
   * Delegate encargado de la persistencia de los puntos
   * 
   * @return PuntosDTO
   * @author Bayron Andres Perez M.
   */
  PuntosDTO guardarPunto(CreatePuntosDTO rreatePuntosDTO);

  /**
   * Delegate encargado de la actualizacion de los puntos
   * 
   * @return PuntosDTO
   * @author Bayron Andres Perez M.
   */
  PuntosDTO actualizarPunto(CreatePuntosDTO rreatePuntosDTO);

  /**
   * Delegate encargado de retornar un punto por
   * 
   * @return List<PuntosDTO>
   * @author cesar.castano
   */
  PuntosDTO getPuntoById(Integer idPunto);

}
