package com.ath.adminefectivo.delegate;

import java.util.List;

import com.ath.adminefectivo.dto.PuntosDTO;
import com.querydsl.core.types.Predicate;

/**
 * Delegate que expone los servicios referente a los puntos
 * @author cesar.castano
 */
public interface IPuntosDelegate {

	/**
	 * Delegate encargado de retornar la lista de todos los Puntos
	 * 
	 * @return List<PuntosDTO>
	 * @author cesar.castano
	 */
	List<PuntosDTO> getPuntos(Predicate predicate);
}
