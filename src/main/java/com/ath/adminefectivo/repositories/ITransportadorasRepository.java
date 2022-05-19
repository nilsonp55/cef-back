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

}
