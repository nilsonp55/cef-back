package com.ath.adminefectivo.delegate;

import java.util.List;

import com.ath.adminefectivo.dto.FondosDTO;
import com.querydsl.core.types.Predicate;

/**
 * Delegate que expone los servicios referente a los fondos
 * @author cesar.castano
 */
public interface IFondosDelegate {

	/**
	 * Delegate encargado de retornar la lista de todos los Fondos
	 * 
	 * @return List<FondosDTO>
	 * @author cesar.castano
	 */
	List<FondosDTO> getFondos(Predicate predicate);
}
