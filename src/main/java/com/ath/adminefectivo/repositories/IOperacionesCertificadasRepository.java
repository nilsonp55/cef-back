package com.ath.adminefectivo.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.OperacionesCertificadas;

/**
 * Repository encargado de manejar la logica de la entidad
 * OperacionesCertificadas
 *
 * @author cesar.castano
 */
public interface IOperacionesCertificadasRepository
		extends JpaRepository<OperacionesCertificadas, Integer>, QuerydslPredicateExecutor<OperacionesCertificadas> {

	/**
	 * Retorna una lista de Operaciones Certificadas con base en el estado de
	 * conciliacion y si es conciliable o no
	 * 
	 * @param estadoConciliacion
	 * @param conciliable
	 * @return List<OperacionesCertificadas>
	 * @author cesar.castano
	 */
	public List<OperacionesCertificadas> findByEstadoConciliacionNotAndConciliable(String estadoConciliacion,
			String conciliable);

	/**
	 * Retorna el numero de operaciones certificadas segun un estado de conciliacion
	 * y un rango de fechas
	 * 
	 * @param estadoConciliacion
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return Integer
	 * @author cesar.castano
	 */
	Integer countByEstadoConciliacionAndFechaEjecucionBetween(String estadoConciliacion, Date fechaInicial,
			Date fechaFinal);

	/**
	 * Retorna una lista de operaciones certificadas segun el codigo del fondo y el estado de conciliacion
	 * @param codigoFondoTDV
	 * @param estadoConciliacion
	 * @return List<OperacionesCertificadas>
	 * @author cesar.castano
	 */
	List<OperacionesCertificadas> findByCodigoFondoTDVAndEstadoConciliacion(Integer codigoFondoTDV,
			String estadoConciliacion);
}
