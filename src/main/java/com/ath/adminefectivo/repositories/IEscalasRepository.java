package com.ath.adminefectivo.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.Escalas;


/**
 * Repository encargado de manejar la logica de la entidad escalas
 *
 * @author duvan.naranjo
 */
public interface IEscalasRepository extends JpaRepository<Escalas, Integer>, QuerydslPredicateExecutor<Escalas> {


}
