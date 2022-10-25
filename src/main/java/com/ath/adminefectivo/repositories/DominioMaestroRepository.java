package com.ath.adminefectivo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.DominioMaestro;

/**
 * Repository encargado de manejar la logica de la entidad DominioMaestro
 *
 * @author Bayron Andres Perez Muñoz
 */
public interface DominioMaestroRepository extends JpaRepository<DominioMaestro, String>, 
		QuerydslPredicateExecutor<DominioMaestro> {

	/**
	 * Consulta la lista de dominios maestro por su estado
	 * 
	 * @param estado
	 * @return List<DominioMaestro>
	 * @author Bayron Andres Perez
	 */
	public List<DominioMaestro> findByEstado(String estado);
	
}
