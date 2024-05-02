package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.TdvDenominCantidad;

/**
 * Repository encargado de manejar la logica de la entidad escalas
 *
 * @author duvan.naranjo
 */
public interface ITdvDenominCantidadRepository
		extends JpaRepository<TdvDenominCantidad, Integer>, QuerydslPredicateExecutor<TdvDenominCantidad> {

}
