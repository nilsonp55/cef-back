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

import com.ath.adminefectivo.dto.OperacionesCertificadasDTO;
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
	 * Retorna el numero de operaciones certificadas segun un estado de conciliacion
	 * y un rango de fechas
	 * 
	 * @param estadoConciliacion
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param conciliable
	 * @return Integer
	 * @author cesar.castano
	 */
	Integer countByEstadoConciliacionAndFechaEjecucionBetweenAndConciliable(String estadoConciliacion, Date fechaInicial,
			Date fechaFinal, String conciliable);

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
	findByCodigoFondoTDVAndCodigoPuntoOrigenAndCodigoPuntoDestinoAndCodigoServicioTdvAndEntradaSalidaAndFechaEjecucionAndCodigoPropioTDVAndIdArchivoCargado
			(Integer codigoFondoTDV, Integer codigoPuntoOrigen,Integer codigoPuntoDestino, String codigoServicio, String entradaSalida, 
					Date fechaEjecucion, String codigoPropioTDV, Long idArchivoCargado);
	
	/**
     * Retorna la entidad operaciones certificadas segun el codigo del servicio tdv, codigo propio tdv y codigoOperacion
     * @param codigoServicio
     * @return OperacionesCertificadas
     * @author prv_nparra
     */
	OperacionesCertificadas 
    findByCodigoFondoTDVAndCodigoPuntoOrigenAndCodigoPuntoDestinoAndCodigoServicioTdvAndEntradaSalidaAndFechaEjecucionAndCodigoPropioTDVAndIdArchivoCargadoAndCodigoOperacion
            (Integer codigoFondoTDV, Integer codigoPuntoOrigen,Integer codigoPuntoDestino, String codigoServicio, String entradaSalida, 
                    Date fechaEjecucion, String codigoPropioTDV, Long idArchivoCargado, String codigoOperacion);

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
			+ "WHERE op.estadoConciliacion = ?1 and oc.conciliable = 'SI'")
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

	@Procedure(procedureName = "validarnoconciliables")
	public boolean validarnoconciliables();

	@Procedure(procedureName = "compara_archivos_alcance_certi")
	public String procesarArchivosAlcance();
	
	@Query("SELECT new com.ath.adminefectivo.dto.OperacionesCertificadasDTO(" +
		       "    oc.codigoFondoTDV, " +
		       "    oc.codigoPuntoOrigen, " +
		       "    oc.codigoPuntoDestino, " +
		       "    oc.fechaEjecucion, " +
		       "    oc.tipoOperacion, " +
		       "    oc.tipoServicio, " +
		       "    oc.estadoConciliacion, " +
		       "    oc.conciliable, " +
		       "    oc.valorTotal, " +
		       "    oc.valorFaltante, " +
		       "    oc.valorSobrante, " +
		       "    oc.fallidaOficina, " +
		       "    oc.usuarioCreacion, " +
		       "    oc.usuarioModificacion, " +
		       "    oc.fechaCreacion, " +
		       "    oc.fechaModificacion, " +
		       "    oc.codigoServicioTdv, " +
		       "    oc.entradaSalida, " +
		       "    oc.idArchivoCargado, " +
		       "    oc.tdv, " +
		       "    oc.bancoAVAL, " +
		       "    oc.tipoPuntoOrigen, " +
		       "    oc.tipoPuntoDestino, " +
		       "    oc.codigoPropioTDV, " +
		       "    oc.moneda, " +
		       "    oc.codigoOperacion, " +
		       "    oc.consecutivoRegistro, " +
		       "    oc.codigoPuntoCodigotdv, " +
		       "    oc.descripcionPuntoCodigotdv) " +
		       "FROM OperacionesCertificadas oc " +
		       "LEFT JOIN OperacionesProgramadas op ON " +
		       "    op.codigoFondoTDV = oc.codigoFondoTDV " +
		       "    AND op.codigoPuntoDestino = oc.codigoPuntoDestino " +
		       "    AND op.codigoPuntoOrigen = oc.codigoPuntoOrigen " +
		       "    AND op.entradaSalida = oc.entradaSalida " +
		       "    AND op.estadoConciliacion = oc.estadoConciliacion " +
		       "    AND CAST(op.fechaCreacion AS date) = CAST(oc.fechaCreacion AS date) " +
		       "    AND CAST(op.fechaDestino AS date) = CAST(oc.fechaEjecucion AS date) " +
		       "    AND CAST(op.fechaModificacion AS date) = CAST(oc.fechaModificacion AS date) " +
		       "    AND op.tipoOperacion = oc.tipoOperacion " +
		       "    AND op.tipoServicio = oc.tipoServicio " +
		       "    AND op.valorTotal = oc.valorTotal " +
		       "    AND op.codigoServicioTdv = oc.codigoServicioTdv " +
		       "    AND op.tipoPuntoDestino = oc.tipoPuntoDestino " +
		       "    AND op.tipoPuntoOrigen = oc.tipoPuntoOrigen " +
		       "    AND op.codigoMoneda = oc.moneda " +
		       "    AND op.bancoAVAL = oc.bancoAVAL " +
		       "    AND op.tdv = oc.tdv " +
		       "    AND op.esCambio = false " +
		       "    AND op.estadoOperacion = 'EJECUTADA' " +
		       "WHERE op.idOperacion IS NULL " +
		       "    AND CAST(oc.fechaCreacion AS date) = :fechaCreacion " +
		       "    AND EXISTS (SELECT 1 FROM Oficinas o " +
		       "               WHERE o.programaTransporte = false " +
		       "               AND (o.codigoPunto = oc.codigoPuntoDestino " +
		       "                    OR o.codigoPunto = oc.codigoPuntoOrigen))")
		List<OperacionesCertificadasDTO> findOpCertificadasNotInOpProgramadas(
		    @Param("fechaCreacion") Date fechaCreacion);

}
