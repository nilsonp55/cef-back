package com.ath.adminefectivo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;


import com.ath.adminefectivo.entities.DetallesLiquidacionCostoFlatEntity;


public interface IDetallesLiquidacionCostoRepositoryFlat extends JpaRepository<DetallesLiquidacionCostoFlatEntity, Long>, 
QuerydslPredicateExecutor<DetallesLiquidacionCostoFlatEntity>{

	/**
	 * Metodo que se encarga de obtener la entidad Valores Liquidados segun el IdLiquidacion
	 * @param idLiquidacion
	 * @return DetallesLiquidacionCosto
	 * @author jose.pabon
	 */
	@Query(value = "SELECT * FROM detalles_liquidacion_costo WHERE ID_LIQUIDACION = ?1" , nativeQuery = true)
	List<DetallesLiquidacionCostoFlatEntity> consultarPorIdLiquidacion(Long idLiquidacion);

}
