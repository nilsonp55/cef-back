package com.ath.adminefectivo.delegate;

import java.util.List;

import com.ath.adminefectivo.dto.CajerosDTO;
import com.querydsl.core.types.Predicate;

public interface ICajerosDelegate {

	/**
	 * Interface encargada de obtener toda la lista de cajeros de la tabla Cajeros
	 * @param predicate
	 * @return List<CajerosDTO>
	 * @author cesar.castano
	 */
	List<CajerosDTO> getCajeros(Predicate predicate);

}
