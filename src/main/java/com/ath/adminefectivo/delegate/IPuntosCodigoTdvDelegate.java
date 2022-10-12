package com.ath.adminefectivo.delegate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ath.adminefectivo.dto.PuntosCodigoTdvDTO;
import com.querydsl.core.types.Predicate;

public interface IPuntosCodigoTdvDelegate {

	/**
	 * Interface encargada de retornar todos los PuntosCodigoTDV
	 * @param predicate
	 * @return List<PuntosCodigoTdvDTO>
	 * @author cesar.castano
	 */
	Page<PuntosCodigoTdvDTO> getPuntosCodigoTDV(Predicate predicate, Pageable page);

	/**
	 * Delegate encargado de contener la union con el servicio de guardar 
	 * un PuntosCodigoTdv
	 * 
	 * @param puntosCodigoTdvDTO
	 * @return PuntosCodigoTdvDTO
	 * @author duvan.naranjo
	 */
	PuntosCodigoTdvDTO guardarPuntosCodigoTdv(PuntosCodigoTdvDTO puntosCodigoTdvDTO);

	/**
	 * Delegate encargado de contener la union con el servicio para obtener 
	 * un PuntosCodigoTdv por id
	 * 
	 * @param idPuntoCodigoTdv
	 * @return PuntosCodigoTdvDTO
	 * @author duvan.naranjo
	 */
	PuntosCodigoTdvDTO getPuntosCodigoTdvById(Integer idPuntoCodigoTdv);

	/**
	 * Delegate encargado de contener la union con el servicio para actualizar 
	 * un PuntosCodigoTdv
	 * 
	 * @param puntosCodigoTdvDTO
	 * @return PuntosCodigoTdvDTO
	 * @author duvan.naranjo
	 */
	PuntosCodigoTdvDTO actualizarPuntosCodigoTdv(PuntosCodigoTdvDTO puntosCodigoTdvDTO);

	/**
	 * Delegate encargado de contener la union con el servicio para eliminar 
	 * un PuntosCodigoTdv
	 * 
	 * @param idPuntoCodigoTdv
	 * @return boolean
	 * @author duvan.naranjo
	 */
	boolean eliminarPuntosCodigoTdv(Integer idPuntoCodigoTdv);


}
