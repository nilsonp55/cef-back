package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.Cajeros;

/**
 * Repository encargado de manejar la logica de la entidad Cajeros
 *
 * @author cesar.castano
 */
public interface ICajerosRepository extends JpaRepository<Cajeros, Integer>, QuerydslPredicateExecutor<Cajeros>{

	/**
	 * Metodo que se encarga de retirnar la entidad CAJEROS con base en el codigoATM
	 * @param codigoATM
	 * @return Cajeros
	 */
	Cajeros findByCodigoATM(String codigoATM);

	/**
	 * Metodo que se encarga de retirnar la entidad CAJEROS con base en el codigoPunto
	 * @param codigoPunto
	 * @return Cajeros
	 * @author cesar.castano
	 */
	Cajeros findByCodigoPunto(Integer codigoPunto);

}
