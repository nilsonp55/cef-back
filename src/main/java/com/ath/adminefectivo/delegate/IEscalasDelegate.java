package com.ath.adminefectivo.delegate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ath.adminefectivo.dto.EscalasDTO;
import com.querydsl.core.types.Predicate;

/**
 * Delegate que expone los servicios referente a las funciones dinamicas
 * @author duvan.naranjo
 */
public interface IEscalasDelegate {

	/**
	 * Delegate encargado de contener la union con el servicio para listar 
	 * las escalas
	 * 
	 * @param idTarifaOperacion
	 * @return PuntosCostosDTO
	 * @author duvan.naranjo
	 */
	Page<EscalasDTO> getEscalas(Predicate predicate, Pageable pageable);
	
	/**
	 * Delegate encargado de contener la union con el servicio para obtener 
	 * una escala por id
	 * 
	 * @param idTarifaOperacion
	 * @return EscalasDTO
	 * @author duvan.naranjo
	 */
	EscalasDTO getEscalasById(Integer idTarifaOperacion);
	

	/**
	 * Delegate encargado de contener la union con el servicio de guardar 
	 * una escala
	 * 
	 * @param idEscalas
	 * @return EscalasDTO
	 * @author duvan.naranjo
	 */
	EscalasDTO guardarEscalas(EscalasDTO escalasDTO);

	/**
	 * Delegate encargado de contener la union con el servicio para actualizar 
	 * una escala
	 * 
	 * @param idEscalas
	 * @return EscalasDTO
	 * @author duvan.naranjo
	 */
	EscalasDTO actualizarEscalas(EscalasDTO escalasDTO);

	/**
	 * Delegate encargado de contener la union con el servicio para eliminar 
	 * una escala
	 * 
	 * @param idEscalas
	 * @return EscalasDTO
	 * @author duvan.naranjo
	 */
	boolean eliminarEscalas(Integer idEscalas);



}
