package com.ath.adminefectivo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.DominioIdentificador;


/**
 * Repository encargado de manejar la logica de la entidad DominioIdentificador
 *
 * @author Bayron Andres Perez Muñoz
 */
public interface DominioIdentificadorReporitory extends JpaRepository<DominioIdentificador, Integer>, 
QuerydslPredicateExecutor<DominioIdentificador> {

	/**
	 * Consulta la lista de dominios identificador por su estado
	 * 
	 * @param estado
	 * @return List<DominioIdentificador>
	 * @author Bayron Andres Perez
	 */
	public List<DominioIdentificador> findByEstado(String estado);
}
