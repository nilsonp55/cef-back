package com.ath.adminefectivo.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
	@Query(value = "SELECT * FROM valores_liquidados WHERE id_liquidacion = ?1" , nativeQuery = true)
	ValoresLiquidados consultarPorIdLiquidacion(Long idLiquidacion);

	/**
	 * Metodo encargado de ejecutar la funcion de la base de datos para 
	 * realizar el proceso de costos de armar parametros 
	 * @return bayron.perez
	 */
	@Procedure(name = "armar_parametros_liquida")
	String armarParametrosLiquida(@Param("fecha") Date fecha);
	
	/**
	 * Metodo encargado de ejecutar la funcion de la base de datos para 
	 * realizar el proceso de liquidar costos
	 * @param parametro
	 * @return bayron.perez
	 */
	@Procedure(name = "liquidar_costos")
	String liquidarCostos(@Param("parametro") Integer parametro);
	
	/**
	 * Metodo que se encarga de obtener los valores liquidados por el procedimiento almacenado
	 * @return ValoresLiquidados
	 * @author bayron.perez
	 */
	List<ValoresLiquidados> findByIdSeqGrupo(Integer idSeqGrupo);

	@Query(value = "SELECT MAX(id_seq_grupo)  FROM valores_liquidados", nativeQuery = true)
	int obtenerUltimoIdSeq();
	
	@Query(value = "SELECT COUNT(*) FROM valores_liquidados WHERE id_seq_grupo = ?1", nativeQuery = true)
	int consultarCantidadValoresLiquidadosByIdSeqGrupo(Integer idSeqGroup);
	
	@Query(value = "SELECT COUNT(*) FROM errores_costos WHERE ID_SEQ_GRUPO = ?1", nativeQuery = true)
	int consultarCantidadErroresValoresLiquidadosByIdSeqGrupo(Integer idSeqGroup);
}
