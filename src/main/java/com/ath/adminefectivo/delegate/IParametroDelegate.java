package com.ath.adminefectivo.delegate;

import java.util.List;

import com.ath.adminefectivo.dto.ParametroDTO;
import com.querydsl.core.types.Predicate;

/**
 * Delegate que contiene la logica de negocio referente a la tabla Parametro
 *
 * @author CamiloBenavides
 */
public interface IParametroDelegate {

	/**
	 * Delegate encargado de retornar la lista de todos los Parametros
	 * 
	 * @return List<ParametroDTO>
	 * @author CamiloBenavides
	 */
	List<ParametroDTO> getParametros(Predicate predicate);
}
