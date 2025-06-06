package com.ath.adminefectivo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ath.adminefectivo.dto.EscalasDTO;
import com.querydsl.core.types.Predicate;

public interface IEscalasService {

	/**
	 * Servicio encargado de contener la logica para listar los
	 * las escalas
	 * 
	 * @param predicate
	 * @return List<EscalasDTO>
	 * @author duvan.naranjo
	 */
	Page<EscalasDTO> getEscalas(Predicate predicate, Pageable pageable);

	/**
	 * Servicio encargado de contener la logica para consultar
	 * una escala
	 * 
	 * @param idEscalas
	 * @return EscalasDTO
	 * @author duvan.naranjo
	 */
	EscalasDTO getEscalasById(Integer idEscalas);

	/**
	 * Servicio encargado de contener la logica guardar o persistir
	 * una escala
	 * 
	 * @param EscalasDTO
	 * @return EscalasDTO
	 * @author duvan.naranjo
	 */
	EscalasDTO guardarEscalas(EscalasDTO escalasDTO);

	/**
	 * Servicio encargado de contener la logica para actualizar una escala
	 * 
	 * @param EscalasDTO
	 * @return EscalasDTO
	 * @author duvan.naranjo
	 */
	EscalasDTO actualizarEscalas(EscalasDTO escalasDTO);

	/**
	 * Servicio encargado de contener la logica eliminar una escala
	 * 
	 * @param idEscalas
	 * @return boolean
	 * @author duvan.naranjo
	 */
	boolean eliminarEscalas(Integer idEscalas);

	

}
