package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.ValoresLiquidados;

public interface IValoresLiquidadosRepository extends JpaRepository<ValoresLiquidados, Long>, 
QuerydslPredicateExecutor<ValoresLiquidados>{

	/**
	 * Metodo que se encarga de obtener la entidad Valores Liquidados segun el IdLiquidacion
	 * @param idLiquidacion
	 * @return ValoresLiquidados
	 * @author prv_ccastano
	 */
	ValoresLiquidados findByIdLiquidacion(Long idLiquidacion);

}
