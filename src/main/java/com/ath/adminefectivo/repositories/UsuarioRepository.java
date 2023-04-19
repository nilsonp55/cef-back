package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import com.ath.adminefectivo.entities.Usuario;

/**
 * Repository encargado de manejar la logica de la entidad Rol
 * @author bayron.perez
 */

public interface UsuarioRepository extends JpaRepository<Usuario, String>, 
	QuerydslPredicateExecutor<Usuario> {

}
