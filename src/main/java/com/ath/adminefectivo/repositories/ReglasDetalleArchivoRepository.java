package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ath.adminefectivo.entities.ReglasDetalleArchivo;


/**
 * Repository encargado de manejar la logica de la entidad ReglasDetalleArchivo
 *
 * @author duvan.naranjo
 */
public interface ReglasDetalleArchivoRepository extends JpaRepository<ReglasDetalleArchivo, Integer > {	
	

}
