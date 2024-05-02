package com.ath.adminefectivo.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ath.adminefectivo.dto.TarifasOperacionDTO;
import com.querydsl.core.types.Predicate;

public interface ITarifasOperacionService {

	/**
	 * Servicio encargado de contener la logica para listar las
	 * tarifas operacion
	 * 
	 * @param predicate
	 * @return List<TarifasOperacionDTO>
	 * @author duvan.naranjo
	 */
	Page<TarifasOperacionDTO> getTarifasOperacion(Predicate predicate, Pageable pageable);

	/**
	 * Servicio encargado de contener la logica para consultar una
	 * tarifa operacion
	 * 
	 * @param idTarifaOperacion
	 * @return TarifasOperacionDTO
	 * @author duvan.naranjo
	 */
	TarifasOperacionDTO getTarifasOperacionById(Integer idTarifaOperacion);

	/**
	 * Servicio encargado de contener la logica guardar o persistir una 
	 * tarifa operacion
	 * 
	 * @param tarifasOperacionDTO
	 * @return TarifasOperacionDTO
	 * @author duvan.naranjo
	 */
	TarifasOperacionDTO guardarTarifasOperacion(TarifasOperacionDTO tarifasOperacionDTO);

	/**
	 * Servicio encargado de contener la logica para actualizar una tarifa operacion
	 * 
	 * @param tarifasOperacionDTO
	 * @return TarifasOperacionDTO
	 * @author duvan.naranjo
	 */
	TarifasOperacionDTO actualizarTarifasOperacion(TarifasOperacionDTO tarifasOperacionDTO);

	/**
	 * Servicio encargado de contener la logica eliminar una tarifa operacion
	 * 
	 * @param idTarifaOperacion
	 * @return boolean
	 * @author duvan.naranjo
	 */
	boolean eliminarTarifasOperacion(Integer idTarifaOperacion);

	/**
	 * Metodo encargado de consultar las tarifas de las operaciones 
	 * de clasificacion de costos 
	 * 
	 * @param codigoBanco
	 * @param codigoTdv
	 * @param fechaSistema
	 * @return List<TarifasOperacionDTO>
	 * @author duvan.naranjo
	 */
	List<TarifasOperacionDTO> getTarifasOperacionByCodigoBancoAndCodigoTdv(int codigoBanco, String codigoTdv,
			Date fechaSistema);

	

}
