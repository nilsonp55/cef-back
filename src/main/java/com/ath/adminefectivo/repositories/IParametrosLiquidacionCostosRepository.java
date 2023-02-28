package com.ath.adminefectivo.repositories;


import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.ath.adminefectivo.dto.ParametrosLiquidacionCostoDTO;
import com.ath.adminefectivo.dto.compuestos.EstimadoClasificacionCostosDTO;
import com.ath.adminefectivo.entities.Escalas;
import com.ath.adminefectivo.entities.ParametrosLiquidacionCosto;


/**
 * Repository encargado de manejar la logica de la entidad Parametros Liquidacion Costo
 *
 * @author duvan.naranjo
 */

@Repository
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

	/**
	 * Metodo encargado de realizar la consulta de todos los parametrosLiquidacionCosto 
	 * por fecha Conciliacion
	 * 
	 * @param fechaSistema
	 * @return List<ParametrosLiquidacionCosto>
	 * @author duvan.naranjo
	 */
	List<ParametrosLiquidacionCosto> findByFechaConcilia(Date fechaSistema);
	
	
}
