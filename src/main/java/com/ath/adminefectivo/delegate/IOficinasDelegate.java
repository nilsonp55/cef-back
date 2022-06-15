package com.ath.adminefectivo.delegate;

import java.util.List;

import com.ath.adminefectivo.dto.OficinasDTO;
import com.querydsl.core.types.Predicate;

public interface IOficinasDelegate {

	/**
	 * Delegate encargado de retornar la lista de todas las Oficinas
	 * 
	 * @return List<OficinasDTO>
	 * @author cesar.castano
	 */
	List<OficinasDTO> getOficinas(Predicate predicate);

}
