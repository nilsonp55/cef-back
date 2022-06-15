package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.Bancos;

/**
 * Repository encargado de manejar la logica de la entidad Bancos
 *
 * @author cesar.castano
 */
public interface IBancosRepository extends JpaRepository<Bancos, Integer>, QuerydslPredicateExecutor<Bancos> {

	Bancos findBancoByCodigoPunto(int codigoPunto);

	Bancos findBancoByAbreviatura(String abreviatura);

	/**
	 * Retorna la entidad Bancos con base en el codigo de compensacion
	 * @param codigoCompensacion
	 * @return Bancos
	 * @author cesar.castano
	 */
	Bancos findByCodigoCompensacion(Integer codigoCompensacion);

	/**
	 * Retorna la entidad Bancos con base en el codigo punto
	 * @param codigoCompensacion
	 * @return Bancos
	 * @author cesar.castano
	 */
	Bancos findByCodigoPunto(Integer codigoPunto);

}
