package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.Puntos;

/**
 * Repository encargado de manejar la logica de la entidad Puntos
 *
 * @author cesar.castano
 */
public interface IPuntosRepository extends JpaRepository<Puntos, Integer>, QuerydslPredicateExecutor<Puntos>{

	/**
	 * Retorna el objeto Puntos para tipo de punto y codigo de punto
	 * @param codigoPunto
	 * @param tipoPunto
	 * @return Puntos
	 * @author cesar.castano
	 */
	public Puntos findByCodigoPuntoAndTipoPunto(Integer codigoPunto, String tipoPunto);

}
