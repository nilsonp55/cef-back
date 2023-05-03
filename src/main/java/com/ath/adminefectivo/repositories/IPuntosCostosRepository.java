package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.PuntosCostos;

/**
 * Repository encargado de manejar la logica de la entidad puntos costos 
 *
 * @author duvan.naranjo
 */
public interface IPuntosCostosRepository extends JpaRepository<PuntosCostos, Integer>, QuerydslPredicateExecutor<PuntosCostos> {


}
