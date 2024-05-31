package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.DetallesProcesoAutomatico;

/**
 * Repositorio de la tabla DetallesProcesoAutomatico
 * 
 * @author duvan.naranjo
 *
 */
public interface DetallesProcesoAutomaticoRepository extends JpaRepository<DetallesProcesoAutomatico, Long>, QuerydslPredicateExecutor<DetallesProcesoAutomatico> {

}
