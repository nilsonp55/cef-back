package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.dto.UpdateEstadoOperacionesDTO;

public interface IConciliacionOperacionesUpdateService {

	/**
	 * Actualiza operaciones programadas con el estado especificado en
	 * UpdateEstadoProgramadasDTO.estado, contenidas en el objeto List
	 * 
	 * @param updateEstadoProgramadasList
	 * @return
	 * @author nilsonparra
	 */
	List<UpdateEstadoOperacionesDTO> updateEstadoProgramadas(
			List<UpdateEstadoOperacionesDTO> updateEstadoProgramadasList);

	/**
	 * Actualiza operaciones certificadas con el estado especificado en
	 * UpdateEstadoCertificadasDTO.estado, contenidas en el objeto List
	 * 
	 * @param updateEstadoCertificadasList
	 * @return
	 * @author nilsonparra
	 */
	List<UpdateEstadoOperacionesDTO> updateEstadoCertificadas(
			List<UpdateEstadoOperacionesDTO> updateEstadoCertificadasList);
}
