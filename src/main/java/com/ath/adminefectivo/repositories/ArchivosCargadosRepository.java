package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.ArchivosCargados;

/**
 * Repository encargado de manejar la logica de la entidad ArchivosCargados
 *
 * @author CamiloBenavides
 */
public interface ArchivosCargadosRepository
		extends JpaRepository<ArchivosCargados, Long>, QuerydslPredicateExecutor<ArchivosCargados> {

}
