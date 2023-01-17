package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ath.adminefectivo.entities.Rol;

/**
 * Repository encargado de manejar la logica de la entidad Rol
 * @author bayron.perez
 */

@Repository
public interface RolRepository extends JpaRepository<Rol, String > {	
	

}
