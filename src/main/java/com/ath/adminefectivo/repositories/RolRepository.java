package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ath.adminefectivo.entities.Rol;


/**
 * Repository encargado de manejar la logica de la entidad Rol
 *
 * @author CamiloBenavides
 */
public interface RolRepository extends JpaRepository<Rol, String > {	
	

}
