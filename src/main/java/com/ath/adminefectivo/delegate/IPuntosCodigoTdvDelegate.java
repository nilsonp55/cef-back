package com.ath.adminefectivo.delegate;

import java.util.List;

import com.ath.adminefectivo.dto.PuntosCodigoTdvDTO;
import com.querydsl.core.types.Predicate;

public interface IPuntosCodigoTdvDelegate {

	/**
	 * Interface encargada de retornar todos los PuntosCodigoTDV
	 * @param predicate
	 * @return List<PuntosCodigoTdvDTO>
	 * @author cesar.castano
	 */
	List<PuntosCodigoTdvDTO> getPuntosCodigoTDV(Predicate predicate);

}
