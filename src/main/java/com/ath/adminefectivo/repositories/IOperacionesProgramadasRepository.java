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

import com.ath.adminefectivo.dto.compuestos.OperacionIntradiaDTO;
import com.ath.adminefectivo.entities.OperacionesProgramadas;
import com.jayway.jsonpath.Predicate;

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
	public Page<OperacionesProgramadas> findAll(Predicate predicate, Pageable page);
	
	/**
	 * Retorna una lista de operaciones programadas segun el estado de conciliacion
	 * 
	 * @param estadoConciliacion
	 * @return List<OperacionesProgramadas>
	 * @author cesar.castano
	 */
	public Page<OperacionesProgramadas> findByEstadoConciliacion(String estadoConciliacion, Pageable page);

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
			+ "oc.codigoFondoTDV = op.codigoFondoTDV AND oc.entradaSalida = op.entradaSalida AND "
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
	/**
	 * Retorna una lista de operaciones programadas por idArchivoCargado
	 * @param idArchivoCargado
	 * @return List<OperacionesProgramadas>
	 * @author cesar.castano
	 */
	List<OperacionesProgramadas> findByIdArchivoCargado(Integer idArchivoCargado);
	
	/**
	 * Retorna una lista de operaciones programadas por idArchivoCargado
	 * @param tipoOperacion
	 * @param start
	 * @param end
	 * @return List<OperacionesProgramadas>
	 * @author duvan.naranjo
	 */
	List<OperacionesProgramadas> findByTipoOperacionAndFechaProgramacionBetween(String tipoOperacion, Date start, Date end);
	
	/**
	 * Retorna una lista de operaciones programadas por tipo operacion, fechas y si es cambio o no
	 * @param tipoOperacion
	 * @param start
	 * @param end
	 * @param esCambio
	 * @return List<OperacionesProgramadas>
	 * @author duvan.naranjo
	 */
	List<OperacionesProgramadas> findByTipoOperacionAndFechaOrigenBetweenAndEsCambio(String tipoOperacion, Date start, Date end, boolean esCambio);

	/**
	 * Retorna una lista de operaciones programadas por tipo operacion, fechas y si es cambio o no, ademas valida 
	 * el estado conciliacion
	 * @param tipoOperacion
	 * @param start
	 * @param end
	 * @param esCambio
	 * @return List<OperacionesProgramadas>
	 * @author duvan.naranjo
	 */
	List<OperacionesProgramadas> findByTipoOperacionAndFechaOrigenBetweenAndEsCambioAndEstadoConciliacion(String tipoOperacion, Date start, Date end, boolean esCambio, String estadoConciliacion );

	
	/**
	 * Retorna el objeto OperacionesProgramadas con las operaciones candidatas a conciliacion automatica
	 * @param estadoConciliacion
	 * @param idOperacion
	 * @param idCertificacion
	 * @return OperacionesProgramadas
	 * @author cesar.castano
	 */
	@Query("SELECT distinct(op) FROM OperacionesProgramadas op JOIN OperacionesCertificadas oc ON "
			+ "(oc.fechaEjecucion = op.fechaOrigen OR oc.fechaEjecucion = op.fechaDestino) AND "
			+ "oc.codigoFondoTDV = op.codigoFondoTDV AND oc.entradaSalida = op.entradaSalida AND "
			+ "(oc.valorTotal + oc.valorFaltante - oc.valorSobrante) = op.valorTotal AND "
			+ "oc.codigoPuntoOrigen = op.codigoPuntoOrigen AND oc.codigoPuntoDestino = op.codigoPuntoDestino AND "
			+ "oc.estadoConciliacion = op.estadoConciliacion "
			+ "WHERE op.estadoConciliacion = ?1")
	List<OperacionesProgramadas> conciliacionAutomatica(String estadoConciliacion);

	/**
	 * Retorna el objeto OperacionesProgramadas segun el IdServicio
	 * @param orderId
	 * @param idArchivo
	 * @return OperacionesProgramadas
	 * @author cesar.castano
	 */
	OperacionesProgramadas findByIdServicioAndIdArchivoCargado(String orderId, int idArchivo);
	
	/**
	 * Retorna el id de un bancoAval yla operacion programada perteneciente al banco
	 * en la fecha recibida para el tipo operacion recibido y tipo de entrada o salida
	 * 
	 * @param fechaInicio
	 * @param fechaFin
	 * @param entradaSalida
	 * @param tipoOperacion
	 * @return OperacionIntradiaDTO
	 * @author duvan.naranjo
	 */
//	@Query("select fo.bancoAVAL, op.codigoPuntoOrigen, op.entradaSalida "
//			+ "from OperacionesProgramadas op, Fondos fo, Bancos ba "
//			+ "where fo.codigoPunto = op.codigoFondoTDV "
//			+ "and ba.codigoPunto = op.codigoPuntoOrigen "
//			+ "and ba.esAVAL = false "	
//			+ "and op.tipoOperacion in (:tipoOperacion) "
//			+ "and op.entradaSalida = :entradaSalida "
//			+ "and op.fechaProgramacion between  :fechaInicio and :fechaFin "
//			+ "group by fo.bancoAVAL, op.codigoPuntoOrigen, op.entradaSalida ")
	@Query(nativeQuery = true)
	List<OperacionIntradiaDTO> consultaOperacionesIntradia_Entrada(@Param("fechaInicio") Date fechaInicio, @Param("fechaFin") Date fechaFin, @Param("entradaSalida") String entradaSalida, @Param("tipoOperacion") String tipoOperacion);
//	List<intradiaPruebaDTO> consultarOperacionesIntradiaEntrada(@Param("fechaInicio") Date fechaInicio, @Param("fechaFin") Date fechaFin, @Param("entradaSalida") String entradaSalida, @Param("tipoOperacion") String tipoOperacion);

	/**
	 * Retorna el id de un bancoAval yla operacion programada perteneciente al banco
	 * en la fecha recibida para el tipo operacion recibido y tipo de entrada o salida
	 * 
	 * @param fechaInicio
	 * @param fechaFin
	 * @param entradaSalida
	 * @param tipoOperacion
	 * @return OperacionIntradiaDTO
	 * @author duvan.naranjo
	 */
//	@Query("select fo.bancoAVAL, op.codigoPuntoDestino, op.entradaSalida  "
//			+ "from OperacionesProgramadas op, Fondos fo, Bancos ba "
//			+ "where fo.codigoPunto = op.codigoFondoTDV "
//			+ "and ba.codigoPunto = op.codigoPuntoDestino "
//			+ "and ba.esAVAL = false "
//			+ "and op.tipoOperacion in (:tipoOperacion) "
//			+ "and op.entradaSalida = :entradaSalida "
//			+ "and op.fechaProgramacion between  :fechaInicio and :fechaFin "
//			+ "group by fo.bancoAVAL, op.codigoPuntoDestino, op.entradaSalida  ")
	@Query(nativeQuery = true)
	List<OperacionIntradiaDTO> consultaOperacionesIntradia_Salida(@Param("fechaInicio") Date fechaInicio, @Param("fechaFin") Date fechaFin, @Param("entradaSalida") String entradaSalida, @Param("tipoOperacion") String tipoOperacion);

	@Query("SELECT op FROM OperacionesProgramadas op where idOperacion in (SELECT DISTINCT "
			+" ti.idOperacion "
			+ "	FROM ErroresContables ec, "
			+ "		 TransaccionesInternas ti "
			+ "	WHERE "
			+ "		 ec.estado = 1 AND "
			+ "		 ec.transaccionInterna = ti.idTransaccionesInternas AND "
			+ "		 ti.tipoProceso = ?1) ")
	public List<OperacionesProgramadas> obtenerConErroresContables(String tipoContabilidad);

	/**
	 * Procedimiento encargado de ejecutar la repaertura del cierre certificaciones
	 * @return String
	 * @author duvan.naranjo
	 */
	@Procedure(name = "reabrir_certificaciones")
	public String reabrir_certificaciones();
	
	/**
	 * Procedimiento encargado de ejecutar la repaertura del cierre definitiva
	 * @return String
	 * @author duvan.naranjo
	 */
	@Procedure(name = "reabrir_definitiva")
	public String reabrir_definitiva();
	
	/**
	 * Procedimiento encargado de ejecutar la repaertura del cierre preliminar
	 * @return String
	 * @author duvan.naranjo
	 */
	@Procedure(name = "reabrir_preliminar")
	public String reabrir_preliminar();
	
	/**
	 * Procedimiento encargado de ejecutar la repaertura del cierre preliminar
	 * @return String
	 * @author duvan.naranjo
	 */
	@Procedure(name = "reabrir_conciliaciones")
	public String reabrir_conciliaciones();
	
//	List<intradiaPruebaDTO> consultarOperacionesIntradiaSalida(@Param("fechaInicio") Date fechaInicio, @Param("fechaFin") Date fechaFin, @Param("entradaSalida") String entradaSalida, @Param("tipoOperacion") String tipoOperacion);
	

}
