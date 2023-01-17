package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.FallasRegistro;
import com.ath.adminefectivo.entities.id.FallasRegistroPK;

/**
 * Repository encargado de manejar la logica de la entidad FallasRegistro
 *
 * @author CamiloBenavides
 */
public interface FallasRegistroRepository
		extends JpaRepository<FallasRegistro, FallasRegistroPK>, QuerydslPredicateExecutor<FallasRegistro> {

}
