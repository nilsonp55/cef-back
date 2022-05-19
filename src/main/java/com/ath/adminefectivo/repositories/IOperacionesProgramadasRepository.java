package com.ath.adminefectivo.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.OperacionesProgramadas;

/**
 * Repository encargado de manejar la logica de la entidad
 * OperacionesProgramadas
 *
 * @author cesar.castano
 */
public interface IOperacionesProgramadasRepository
		extends JpaRepository<OperacionesProgramadas, Integer>, QuerydslPredicateExecutor<OperacionesProgramadas> {

	/**
	 * Retorna una lista de operaciones programadas segun el estado de conciliacion
	 * 
	 * @param estadoConciliacion
	 * @return List<OperacionesProgramadas>
	 * @author cesar.castano
	 */
	public List<OperacionesProgramadas> findByEstadoConciliacion(String estadoConciliacion);

	/**
	 * Retorna una lista de operaciones programadas donde el estado de conciliacion
	 * sea diferente a (estadoConciliacion)
	 * @param estadoConciliacion
	 * @return List<OperacionesProgramadas>
	 * @author cesar.castano
	 */
	public List<OperacionesProgramadas> findByEstadoConciliacionNot(String estadoConciliacion);

	/**
	 * Retorna el objeto OperacionesProgramadas con las operaciones candidatas a conciliacion manual
	 * @param estadoConciliacion
	 * @param idOperacion
	 * @param idCertificacion
	 * @return OperacionesProgramadas
	 * @author cesar.castano
	 */
	@Query("SELECT op FROM OperacionesProgramadas op JOIN OperacionesCertificadas oc ON "
			+ "(oc.fechaEjecucion = op.fechaOrigen OR oc.fechaEjecucion = op.fechaDestino) AND "
			+ "oc.codigoFondoTDV = op.codigoFondoTDV AND oc.tipoOperacion = op.tipoOperacion AND "
			+ "(oc.valorTotal + oc.valorFaltante - oc.valorSobrante) = op.valorTotal AND "
			+ "oc.tipoPuntoOrigen = op.tipoPuntoOrigen AND oc.tipoPuntoDestino = op.tipoPuntoDestino AND "
			+ "oc.codigoPuntoOrigen = op.codigoPuntoOrigen AND oc.codigoPuntoDestino = op.codigoPuntoDestino AND "
			+ "oc.estadoConciliacion = op.estadoConciliacion "
			+ "WHERE op.estadoConciliacion = ?1 AND op.idOperacion = ?2 AND oc.idCertificacion = ?3")
	OperacionesProgramadas conciliacionManual(String estadoConciliacion, Integer idOperacion, Integer idCertificacion);

	/**
	 * Retorna el numero de operaciones programadas por estado de conciliacion y rango de fechas
	 * @param estadoConciliacion
	 * @param fechaOrigenInicial
	 * @param fechaOrigenFinal
	 * @return Integer
	 * @author cesar.castano
	 */
	Integer countByEstadoConciliacionAndFechaOrigenBetween(String estadoConciliacion, Date fechaOrigenInicial,
			Date fechaOrigenFinal);

	/**
	 * Retorna una lista de operaciones programadas por codigo del fondo y estado de conciliacion
	 * @param codigoFondoTDV
	 * @param estadoConciliacion
	 * @return List<OperacionesProgramadas>
	 * @author cesar.castano
	 */
	List<OperacionesProgramadas> findByCodigoFondoTDVAndEstadoConciliacion(Integer codigoFondoTDV,
			String estadoConciliacion);

}
