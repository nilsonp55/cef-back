package com.ath.adminefectivo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.ath.adminefectivo.entities.FuncionesDinamicas;
import com.ath.adminefectivo.entities.TarifasOperacion;

/**
 * Repository encargado de manejar la logica de la entidad tarifas operacion
 *
 * @author duvan.naranjo
 */
public interface ITarifasOperacionRepository extends JpaRepository<TarifasOperacion, Integer>, QuerydslPredicateExecutor<TarifasOperacion> {


}
