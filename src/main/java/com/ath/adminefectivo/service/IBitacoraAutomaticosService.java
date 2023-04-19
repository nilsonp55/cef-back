package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.dto.BitacoraAutomaticosDTO;
import com.querydsl.core.types.Predicate;

public interface IBitacoraAutomaticosService {

	/**
	 * Servicio encargado de consultar la lista de todas las bitacoras automaticas filtrados
	 * con el predicado
	 * 
	 * @param predicate
	 * @return List<BitacoraAutomaticosDTO>
	 * @author duvan.naranjo
	 */
	List<BitacoraAutomaticosDTO> getBitacoraAutomaticos(Predicate predicate);

	/**
	 * Servicio encargado de guardar el resultado del proceso de los procesos automaticos del 
	 * aplicativo
	 * 
	 * @param bitacoraDTO
	 * @author duvan.naranjo
	 */
	BitacoraAutomaticosDTO guardarBitacoraAutomaticos(BitacoraAutomaticosDTO bitacoraDTO);
	

}
