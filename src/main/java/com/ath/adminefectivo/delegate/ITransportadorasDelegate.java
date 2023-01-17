package com.ath.adminefectivo.delegate;

import java.util.List;

import com.ath.adminefectivo.dto.TransportadorasDTO;
import com.querydsl.core.types.Predicate;

/**
 * Delegate que expone los servicios referente a las transportadoras
 * @author cesar.castano
 */
public interface ITransportadorasDelegate {

	/**
	 * Delegate encargado de retornar la lista de todas las Transportadoras
	 * @return List<TransportadorasDTO>
	 * @param predicate
	 * @author cesar.castano
	 */
	List<TransportadorasDTO> getTransportadoras(Predicate predicate);

}
