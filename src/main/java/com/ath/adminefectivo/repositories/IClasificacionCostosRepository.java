package com.ath.adminefectivo.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.ath.adminefectivo.entities.ClasificacionCostos;
import com.ath.adminefectivo.entities.Escalas;


/**
 * Repository encargado de manejar la logica de la entidad CostosClasificacion
 *
 * @author duvan.naranjo
 */
@Repository
public interface IClasificacionCostosRepository extends JpaRepository<ClasificacionCostos, Integer>, QuerydslPredicateExecutor<ClasificacionCostos> {


}
