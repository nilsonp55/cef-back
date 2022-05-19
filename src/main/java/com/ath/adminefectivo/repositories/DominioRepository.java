package com.ath.adminefectivo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.Dominio;
import com.ath.adminefectivo.entities.id.DominioPK;

/**
 * Repository encargado de manejar la logica de la entidad Dominio
 *
 * @author CamiloBenavides
 */
public interface DominioRepository extends JpaRepository<Dominio, DominioPK>, QuerydslPredicateExecutor<Dominio> {

	/**
	 * Consulta la lista de dominios por su dominio
	 * 
	 * @param dominio
	 * @return List<Dominio>
	 * @author CamiloBenavides
	 */
	public List<Dominio> findByDominioPKDominio(String dominio);

}
