package com.ath.adminefectivo.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ath.adminefectivo.entities.FestivosNacionales;

/**
 * Repository encargado de manejar la logica de la entidad FestivosNacionales
 *
 * @author CamiloBenavides
 */
public interface FestivosNacionalesRepository extends JpaRepository<FestivosNacionales, Date> {

	/**
	 * Retorna la lista de festivos vigentes, si no se envia la fecha se toma por
	 * defecto el dia actual de lo contrario la fecha enviada
	 * 
	 * @param fecha
	 * @return boolean
	 * @author CamiloBenavides
	 */
	@Query("select fn.fecha from FestivosNacionales fn where fn.fecha >= :fecha ")
	List<Date> consultarFestivosVigentes(@Param("fecha") Date fecha);


}
