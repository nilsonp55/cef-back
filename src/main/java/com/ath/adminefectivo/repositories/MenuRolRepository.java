package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.ath.adminefectivo.entities.MenuRol;

/**
 * Repository encargado de manejar la logica de la entidad MenuRol
 * @author bayron.perez
 */

@Repository
public interface MenuRolRepository extends JpaRepository<MenuRol, Integer>, 
		QuerydslPredicateExecutor<MenuRol> {

}
