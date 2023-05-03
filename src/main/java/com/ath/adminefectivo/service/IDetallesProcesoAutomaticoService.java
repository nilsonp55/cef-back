package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.dto.DetallesProcesoAutomaticoDTO;
import com.querydsl.core.types.Predicate;

public interface IDetallesProcesoAutomaticoService {

	/**
	 * Servicio encargado de consultar la lista de todas las bitacoras automaticas filtrados
	 * con el predicado
	 * 
	 * @param predicate
	 * @return List<DetallesProcesoAutomaticoDTO>
	 * @author duvan.naranjo
	 */
	List<DetallesProcesoAutomaticoDTO> getDetallesProcesoAutomatico(Predicate predicate);
	

}
