package com.ath.adminefectivo.delegate;

import java.util.List;

import com.ath.adminefectivo.dto.CiudadesDTO;
import com.querydsl.core.types.Predicate;

/**
 * Delegate que expone los servicios referente a las ciudades
 * @author cesar.castano
 */
public interface ICiudadesDelegate {

	/**
	 * Delegate encargado de retornar la lista de todas las Ciudades
	 * 
	 * @return List<CiudadesDTO>
	 * @author cesar.castano
	 */
	List<CiudadesDTO> getCiudades(Predicate predicate);

}
