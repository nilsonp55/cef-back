package com.ath.adminefectivo.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.dto.ParametrosLiquidacionCostoDTO;
import com.ath.adminefectivo.dto.compuestos.EstimadoClasificacionCostosDTO;
import com.ath.adminefectivo.entities.Escalas;
import com.ath.adminefectivo.entities.ParametrosLiquidacionCosto;


/**
 * Repository encargado de manejar la logica de la entidad Parametros Liquidacion Costo
 *
 * @author duvan.naranjo
 */
public interface IParametrosLiquidacionCostosRepository extends JpaRepository<ParametrosLiquidacionCosto, Long>, QuerydslPredicateExecutor<ParametrosLiquidacionCosto> {


	/**
	 * Metodo encargado de calcular el estimado de fajos, bolsas por banco, 
	 * transportadora y mes año
	 * 
	 * @param transportadora
	 * @param bancoAval
	 * @param mes
	 * @param anio
	 * @return EstimadoClasificacionCostosDTO
	 * @author duvan.naranjo
	 */
	@Query(nativeQuery = true)
	EstimadoClasificacionCostosDTO consultaEstimadosCostos(String transportadora, int bancoAval, int mes,
			int anio);
	
}
