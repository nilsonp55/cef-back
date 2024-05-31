package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ath.adminefectivo.entities.Mensajes;


/**
 * Repository encargado de manejar la logica de la entidad mensaje
 *
 * @author duvan.naranjo
 */
public interface MensajeRepository extends JpaRepository<Mensajes, Integer > {	
	

}
