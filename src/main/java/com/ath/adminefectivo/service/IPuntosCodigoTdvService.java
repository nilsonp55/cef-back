package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.dto.PuntosCodigoTdvDTO;
import com.ath.adminefectivo.entities.PuntosCodigoTDV;
import com.querydsl.core.types.Predicate;

public interface IPuntosCodigoTdvService {

	/**
	 * Servicio encargado de consultar la lista de todos los Puntos Codigos TDV filtrados
	 * con el predicado
	 * @param predicate
	 * @return List<PuntosCodigoTdvDTO>
	 * @author cesar.castano
	 */
	List<PuntosCodigoTdvDTO> getPuntosCodigoTDV(Predicate predicate);

	/**
	 * Servicio encargado de consultar el objeto PuntosCodigoTdv por codigo. Este
	 * servicio lanza un error en caso de que el codigo punto no exista
	 * @param codigo
	 * @return PuntosCodigoTDV
	 * @author cesar.castano
	 */
	PuntosCodigoTDV getEntidadPuntoCodigoTDV(String codigo);

	/**
	 * Servicio encargado de consultar el codigoPunto. Este
	 * servicio lanza un error en caso de que el codigo punto no exista
	 * @param codigoPuntoTdv
	 * @return Integer
	 * @author cesar.castano
	 */
	Integer getCodigoPunto(String codigoPuntoTdv, String codigoTdv);
	
	/**
	 * Servicio encargado de consultar la lista de todos los Puntos Codigos TDV filtrados
	 * con el predicado
	 * @param predicate
	 * @return List<PuntosCodigoTdvDTO>
	 * @author cesar.castano
	 */
	List<PuntosCodigoTdvDTO> getPuntosCodigoTdvAll();
	
}
