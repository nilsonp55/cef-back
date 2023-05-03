package com.ath.adminefectivo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.ath.adminefectivo.dto.compuestos.ResultadoFuncionDinamicaDTO;
import com.ath.adminefectivo.entities.FuncionesDinamicas;

/**
 * Repository encargado de manejar la logica de la entidad funciones Dinamicas
 *
 * @author duvan.naranjo
 */
public interface IFuncionesDinamicasRepository extends JpaRepository<FuncionesDinamicas, Integer>, QuerydslPredicateExecutor<FuncionesDinamicas> {


	/**
	 * Retorna la entidad FuncionesDinamicas con base en el estado en que se encuentre
	 * 
	 * @return FuncionesDinamicas
	 * @author duvan.naranjo
	 */
	List<FuncionesDinamicas> findByEstado(Integer estado);
	
	/**
	 * Metodo encargado de ejecutar un procedimiento que se encuentra en la base de datos
	 * en el cual se ejecutara la funcion recibida como parametro con los parametros 
	 * recibidos, el procedimiento dinamico en la base de datos retorna un listado de strings
	 * 
	 * @param idFuncion
	 * @param parametros
	 * @return
	 */
	@Query(nativeQuery = true)
	List<ResultadoFuncionDinamicaDTO> ejecutar_procedimiento(@Param("idfuncion") int idfuncion,@Param("parametros") String parametros);

}
