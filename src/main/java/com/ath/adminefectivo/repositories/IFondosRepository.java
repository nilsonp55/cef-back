package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.Fondos;

/**
 * Repository encargado de manejar la logica de la entidad Fondos
 *
 * @author cesar.castano
 */
public interface IFondosRepository extends JpaRepository<Fondos, Integer>, QuerydslPredicateExecutor<Fondos>{

	/**
	 * Retorna el objeto Fondos cpn base en el codigo de punto
	 * @param codigoPunto
	 * @return Fondos
	 * @author cesar.castano
	 */
	public Fondos findByCodigoPunto (Integer codigoPunto);
	
	/**
	 * Retorna el objeto Fondos con base en la transportadora
	 * @param tdv
	 * @return Fondos
	 * @author cesar.castano
	 */
	Fondos findByTdv(String tdv);
}
