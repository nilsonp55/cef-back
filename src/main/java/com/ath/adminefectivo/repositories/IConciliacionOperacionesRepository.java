package com.ath.adminefectivo.repositories;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

/**
 * Repository encargado de manejar la logica de la entidad ConciliacionServicios
 *
 * @author cesar.castano
 */
import com.ath.adminefectivo.entities.ConciliacionServicios;

/**
 * Repository encargado de manejar la logica de la entidad ConciliacionServicios
 *
 * @author cesar.castano
 */
public interface IConciliacionOperacionesRepository
		extends JpaRepository<ConciliacionServicios, Integer>, QuerydslPredicateExecutor<ConciliacionServicios> {

	/**
	 * Retorna el numero de conciliaciones para un rango de fechas
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return Integer
	 * @author cesar.castano
	 */
	Integer countByFechaConciliacionBetween(Date fechaInicial, Date fechaFinal);
	
}
