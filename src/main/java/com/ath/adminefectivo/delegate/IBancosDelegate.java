package com.ath.adminefectivo.delegate;

import java.util.List;

import com.ath.adminefectivo.dto.BancosDTO;
import com.querydsl.core.types.Predicate;

/**
 * Delegate que expone los servicios referente a los bancos
 * @author cesar.castano
 */
public interface IBancosDelegate {

	/**
	 * Delegate encargado de retornar la lista de todas las Ciudades
	 * 
	 * @return List<BancosDTO>
	 * @author cesar.castano
	 */
	List<BancosDTO> getBancos(Predicate predicate);

}
