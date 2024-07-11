package com.ath.adminefectivo.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;


import com.ath.adminefectivo.entities.ParametrosLiquidacionCostoFlat;


/**
 * Repository encargado de manejar la logica de la entidad Parametros Liquidacion Costo
 *
 * @author jose.pabon
 */

public interface IParametrosLiquidacionCostosRepositoryFlat extends JpaRepository<ParametrosLiquidacionCostoFlat, Long>, QuerydslPredicateExecutor<ParametrosLiquidacionCostoFlat> {


	
}
