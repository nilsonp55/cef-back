package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.Parametro;

/**
 * Repository encargado de manejar la logica de la entidad Parametro
 *
 * @author CamiloBenavides
 */
public interface ParametroRepository
		extends JpaRepository<Parametro, String>, QuerydslPredicateExecutor<Parametro> {

}
