package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.Transportadoras;

/**
 * Repository encargado de manejar la logica de la entidad Transportadoras
 *
 * @author cesar.castano
 */
public interface ITransportadorasRepository
		extends JpaRepository<Transportadoras, String>, QuerydslPredicateExecutor<Transportadoras> {
	
	/**
	 * Retorna la entidad Transportadoras con base en el nombre de la transportadora
	 * @param nombreTransportadora
	 * @return Transportadoras
	 * @author cesar.castano
	 */
	Transportadoras findByNombreTransportadora(String nombreTransportadora);
	
	/**
	 * Metodo encargado de realizar la consulta por codigo de la transportadora
	 * 
	 * @param codigo
	 * @return Transportadoras
	 * @author duvan.naranjo
	 */
	Transportadoras findByCodigo(String codigo);
}
