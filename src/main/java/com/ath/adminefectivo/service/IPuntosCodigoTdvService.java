package com.ath.adminefectivo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
	Page<PuntosCodigoTdvDTO> getPuntosCodigoTDV(Predicate predicate, Pageable page, String busqueda);
	
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
	Integer getCodigoPunto(String codigoPuntoTdv, String codigoTdv, Integer codigoAval, String codigoDane);
	
	/**
	 * Servicio encargado de consultar la lista de todos los Puntos Codigos TDV filtrados
	 * con el predicado
	 * 
	 * @param predicate
	 * @return List<PuntosCodigoTdvDTO>
	 * @author cesar.castano
	 */
	List<PuntosCodigoTdvDTO> getPuntosCodigoTdvAll();

	/**
	 * Servicio encargado de contener la logica para consultar un
	 * Puntos Codigos TDV
	 * 
	 * @param idPuntoCodigoTdv
	 * @return PuntosCodigoTdvDTO
	 * @author duvan.naranjo
	 */
	PuntosCodigoTdvDTO getPuntosCodigoTdvById(Integer idPuntoCodigoTdv);

	/**
	 * Servicio encargado de contener la logica guardar o persistir un
	 * Puntos Codigos TDV
	 * 
	 * @param puntosCodigoTdvDTO
	 * @return PuntosCodigoTdvDTO
	 * @author duvan.naranjo
	 */
	PuntosCodigoTdvDTO guardarPuntosCodigoTdv(PuntosCodigoTdvDTO puntosCodigoTdvDTO);

	/**
	 * Servicio encargado de contener la logica para actualizar un Puntos Codigos TDV
	 * 
	 * @param puntosCodigoTdvDTO
	 * @return PuntosCodigoTdvDTO
	 * @author duvan.naranjo
	 */
	PuntosCodigoTdvDTO actualizarPuntosCodigoTdv(PuntosCodigoTdvDTO puntosCodigoTdvDTO);

	/**
	 * Servicio encargado de contener la logica eliminar un Puntos Codigos TDV
	 * 
	 * @param idPuntoCodigoTdv
	 * @return boolean
	 * @author duvan.naranjo
	 */
	boolean eliminarPuntosCodigoTdv(Integer idPuntoCodigoTdv);
	
}
