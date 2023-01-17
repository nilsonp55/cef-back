package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.BitacoraAutomaticos;
import com.ath.adminefectivo.entities.CajerosATM;

/**
 * Repositorio de la tabla BitacoraAutomaticos
 * 
 * @author duvan.naranjo
 *
 */
public interface IBitacoraAutomaticosRepository extends JpaRepository<BitacoraAutomaticos, Long>, QuerydslPredicateExecutor<BitacoraAutomaticos> {

}
