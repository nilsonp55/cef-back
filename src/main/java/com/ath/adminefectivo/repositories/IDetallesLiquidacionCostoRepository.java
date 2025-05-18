package com.ath.adminefectivo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.DetallesLiquidacionCosto;
import com.ath.adminefectivo.entities.ValoresLiquidados;
import com.ath.adminefectivo.entities.ValoresLiquidadosFlatEntity;

public interface IDetallesLiquidacionCostoRepository extends JpaRepository<DetallesLiquidacionCosto, Long>, 
QuerydslPredicateExecutor<DetallesLiquidacionCosto>{

	/**
	 * Metodo que se encarga de obtener la entidad Valores Liquidados segun el IdLiquidacion
	 * @param idLiquidacion
	 * @return DetallesLiquidacionCosto
	 * @author hector.mercado
	 */
	@Query(value = "SELECT * FROM detalles_liquidacion_costo WHERE ID_LIQUIDACION = ?1" , nativeQuery = true)
	List<DetallesLiquidacionCosto> consultarPorIdLiquidacion(Long idLiquidacion);

}
