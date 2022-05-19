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

}
