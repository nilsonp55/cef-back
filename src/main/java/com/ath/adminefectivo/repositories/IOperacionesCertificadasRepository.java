package com.ath.adminefectivo.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

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
			
	/**
	 * Retorna una lista de operaciones certificadas segun el codigo del servicio tdv
	 * @param codigoServicio
	 * @return List<OperacionesCertificadas>
	 * @author cesar.castano
	 */
	List<OperacionesCertificadas> findByCodigoServicioTdv(String codigoServicio);
	
	/**
	 * Retorna una lista de operaciones certificadas segun el codigo del servicio tdv
	 * @param codigoServicio
	 * @return List<OperacionesCertificadas>
	 * @author cesar.castano
	 */

	Page<OperacionesCertificadas> findByEstadoConciliacion(String estadoConciliacion, Pageable page);

	/**
	 * Retorna la entidad operaciones certificadas segun el codigo del servicio tdv y codigo propio tdv
	 * @param codigoServicio
	 * @return OperacionesCertificadas
	 * @author cesar.castano
	 */
	OperacionesCertificadas 
	findByCodigoPuntoOrigenAndCodigoServicioTdvAndEntradaSalidaAndFechaEjecucion(Integer codigoPuntoOrigen,
			String codigoServicio, String entradaSalida, Date fechaEjecucion);

	/**
	 * Retorna la entidad operaciones certificadas segun el codigo del servicio tdv y codigo propio tdv
	 * @param codigoServicio
	 * @return OperacionesCertificadas
	 * @author cesar.castano
	 */
	OperacionesCertificadas 
	findByCodigoPuntoDestinoAndCodigoServicioTdvAndEntradaSalidaAndFechaEjecucion(Integer codigoPuntoDestino,
			String codigoServicio, String entradaSalida, Date fechaEjecucion);

	/**
	 * Retorna el objeto OperacionesCertificadas con las operaciones candidatas a conciliacion automatica
	 * @param estadoConciliacion
	 * @param idOperacion
	 * @param idCertificacion
	 * @return OperacionesCertificadas
	 * @author cesar.castano
	 */
	@Query("SELECT distinct(oc) FROM OperacionesProgramadas op JOIN OperacionesCertificadas oc ON "
			+ "(oc.fechaEjecucion = op.fechaOrigen OR oc.fechaEjecucion = op.fechaDestino) AND "
			+ "oc.codigoFondoTDV = op.codigoFondoTDV AND oc.entradaSalida = op.entradaSalida AND "
			+ "(oc.valorTotal + oc.valorFaltante - oc.valorSobrante) = op.valorTotal AND "
			+ "oc.codigoPuntoOrigen = op.codigoPuntoOrigen AND oc.codigoPuntoDestino = op.codigoPuntoDestino AND "
			+ "oc.estadoConciliacion = op.estadoConciliacion "
			+ "WHERE op.estadoConciliacion = ?1")
	List<OperacionesCertificadas> conciliacionAutomatica(String estadoConciliacion);
	
	/**
	 * Retorna una lista de operaciones certificadas segun el codigo del servicio tdv
	 * @param codigoServicio
	 * @return List<OperacionesCertificadas>
	 * @author cesar.castano
	 */
	List<OperacionesCertificadas> findByCodigoPuntoDestinoAndEntradaSalidaAndFechaEjecucion(Integer codigoPuntoDestino, String entradaSalida, Date fechaEjecucion);

	/**
	 * Retorna una lista de operaciones certificadas segun el codigo del servicio tdv
	 * @param codigoServicio
	 * @return List<OperacionesCertificadas>
	 * @author cesar.castano
	 */
	List<OperacionesCertificadas> findByCodigoPuntoOrigenAndEntradaSalidaAndFechaEjecucion(Integer codigoPuntoOrigen, String entradaSalida, Date fechaEjecucion);

	@Procedure(name = "validarnoconciliables")
	public boolean validarnoconciliables(@Param("p_id_archivo") long idArchivo);

	
}
