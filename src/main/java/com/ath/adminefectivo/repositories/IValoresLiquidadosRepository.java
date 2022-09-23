package com.ath.adminefectivo.repositories;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

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

	/**
	 * Metodo encargado de ejecutar la funcion de la base de datos para 
	 * realizar el proceso de costos de armar parametros 
	 * @return bayron.perez
	 */
	@Procedure(name = "public.armar_parametros_liquita")
	Integer armar_parametros_liquida();
	
	/**
	 * Metodo encargado de ejecutar la funcion de la base de datos para 
	 * realizar el proceso de liquidar costos
	 * @param parametro
	 * @return bayron.perez
	 */
	@Procedure(name = "public.liquidar_costos")
	boolean liquidar_costos(@Param("parametro") Integer parametro);
}
