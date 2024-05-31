package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.ValoresLiquidados;
import com.ath.adminefectivo.entities.ValoresLiquidadosFlatEntity;

public interface IValoresLiquidadosFlatRepository extends JpaRepository<ValoresLiquidadosFlatEntity, Long>, 
QuerydslPredicateExecutor<ValoresLiquidadosFlatEntity>{

	/**
	 * Metodo que se encarga de obtener la entidad Valores Liquidados segun el IdLiquidacion
	 * @param idLiquidacion
	 * @return ValoresLiquidados
	 * @author hector.mercado
	 */
	@Query(value = "SELECT * FROM valores_liquidados WHERE id_liquidacion = ?1" , nativeQuery = true)
	ValoresLiquidadosFlatEntity consultarPorIdLiquidacion(Long idLiquidacion);

}
