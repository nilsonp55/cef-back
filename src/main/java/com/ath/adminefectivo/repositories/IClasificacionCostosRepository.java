package com.ath.adminefectivo.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.ath.adminefectivo.dto.ClasificacionCostosDTO;
import com.ath.adminefectivo.entities.ClasificacionCostos;
import com.ath.adminefectivo.entities.Escalas;


/**
 * Repository encargado de manejar la logica de la entidad CostosClasificacion
 *
 * @author duvan.naranjo
 */
@Repository
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
	List<ClasificacionCostosDTO> findByTransportadoraAndMesAnio(String transportadora, String mesAnio);


}
