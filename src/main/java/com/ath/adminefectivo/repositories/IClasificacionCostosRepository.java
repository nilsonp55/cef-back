package com.ath.adminefectivo.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.ClasificacionCostos;


/**
 * Repository encargado de manejar la logica de la entidad CostosClasificacion
 *
 * @author duvan.naranjo
 */
public interface IClasificacionCostosRepository extends JpaRepository<ClasificacionCostos, Integer>, QuerydslPredicateExecutor<ClasificacionCostos> {

	/**
	 * Metodo encargado de consultar los costos Clasificacion 
	 * que se encuentren para el mes en curso y mes año
	 * 
	 * @param transportadora
	 * @param mesAnio
	 * @return List<ClasificacionCostosDTO>
	 * @author duvan.naranjo
	 */
	List<ClasificacionCostos> findByTransportadoraAndMesAnio(String transportadora, String mesAnio);

	


}
