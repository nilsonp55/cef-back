package com.ath.adminefectivo.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.ath.adminefectivo.entities.ParametrosLiquidacionCosto;

@Repository
public interface ICostosFletesCharterRepository extends JpaRepository<ParametrosLiquidacionCosto, Long>, 
QuerydslPredicateExecutor<ParametrosLiquidacionCosto>{

	/**
	 * Metodo que se encarga de obtener la lista de los parametros liquidacion de costos
	 * con base en un rango de fechas y para Charter
	 * @param escala
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return List<ParametrosLiquidacionCosto>
	 * @author prv_ccastano
	 */
	List<ParametrosLiquidacionCosto> findByEscalaAndFechaEjecucionBetween(
							String escala, Date fechaInicial, Date fechaFinal);

}
