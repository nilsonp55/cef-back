package com.ath.adminefectivo.delegate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import com.ath.adminefectivo.dto.TarifasOperacionDTO;
import com.querydsl.core.types.Predicate;

/**
 * Delegate que expone los servicios referente a las funciones dinamicas
 * @author duvan.naranjo
 */
public interface ITarifasOperacionDelegate {

	/**
	 * Delegate encargado de contener la union con el servicio para listar 
	 * las tarifas operacion
	 * 
	 * @param idTarifaOperacion
	 * @return TarifasOperacionDTO
	 * @author duvan.naranjo
	 */
	Page<TarifasOperacionDTO> getTarifasOperacion(Predicate predicate, Pageable pageable);
	
	/**
	 * Delegate encargado de contener la union con el servicio para obtener 
	 * una tarifa operacion por id
	 * 
	 * @param idTarifaOperacion
	 * @return TarifasOperacionDTO
	 * @author duvan.naranjo
	 */
	TarifasOperacionDTO getTarifasOperacionById(Integer idTarifaOperacion);
	

	/**
	 * Delegate encargado de contener la union con el servicio de guardar 
	 * una tarifa operacion
	 * 
	 * @param idTarifaOperacion
	 * @return TarifasOperacionDTO
	 * @author duvan.naranjo
	 */
	TarifasOperacionDTO guardarTarifasOperacion(TarifasOperacionDTO tarifasOperacionDTO);

	/**
	 * Delegate encargado de contener la union con el servicio para actualizar 
	 * una tarifa operacion
	 * 
	 * @param idTarifaOperacion
	 * @return TarifasOperacionDTO
	 * @author duvan.naranjo
	 */
	TarifasOperacionDTO actualizarTarifasOperacion(TarifasOperacionDTO tarifasOperacionDTO);

	/**
	 * Delegate encargado de contener la union con el servicio para eliminar 
	 * una tarifa operacion
	 * 
	 * @param idTarifaOperacion
	 * @return TarifasOperacionDTO
	 * @author duvan.naranjo
	 */
	boolean eliminarTarifasOperacion(Integer idTarifaOperacion);



}
