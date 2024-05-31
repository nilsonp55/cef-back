package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.ConciliacionServiciosHistorico;

/**
 * Repository encargado de manejar la logica de la entidad ConciliacionServiciosHistorico
 *
 * @author cesar.castano
 */
public interface IConciliacionServiciosHistoricoRepository
		extends JpaRepository<ConciliacionServiciosHistorico, Integer>,
		QuerydslPredicateExecutor<ConciliacionServiciosHistorico> {

}
