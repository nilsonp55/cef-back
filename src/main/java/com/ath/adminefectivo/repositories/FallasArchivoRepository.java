package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.FallasArchivo;

/**
 * Repository encargado de manejar la logica de la entidad FallasArchivo
 *
 * @author CamiloBenavides
 */
public interface FallasArchivoRepository
		extends JpaRepository<FallasArchivo, Long>, QuerydslPredicateExecutor<FallasArchivo> {

}
