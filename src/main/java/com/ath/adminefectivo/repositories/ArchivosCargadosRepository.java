package com.ath.adminefectivo.repositories;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.ath.adminefectivo.entities.ArchivosCargados;

/**
 * Repository encargado de manejar la logica de la entidad ArchivosCargados
 *
 * @author CamiloBenavides
 */
public interface ArchivosCargadosRepository extends JpaRepository<ArchivosCargados, Long>,
		QuerydslPredicateExecutor<ArchivosCargados>, PagingAndSortingRepository<ArchivosCargados, Long> {

	/**
	 * Metodo encargado de realizar la consulta de los archivos cargados por
	 * modeloarchivo y idArchivo
	 * 
	 * @param idModeloArchivo
	 * @param idArchivo
	 * @return List<ArchivosCargados>
	 * @author duvan.naranjo
	 */
	List<ArchivosCargados> findByIdModeloArchivoAndIdArchivo(String idModeloArchivo, Long idArchivo);

	/**
	 * Metodo encargado de realizar la consulta de los archivos cargados que fueron
	 * cargados exitosamente y no han sido procesados
	 * 
	 * @return List<ArchivosCargados>
	 * @author duvan.naranjo
	 */
	public List<ArchivosCargados> findByEstadoCargueAndFechaArchivoBetween(String estadoCargue, Date start, Date end);

	/**
	 * Metodo encargado de realizar la consulta de los archivos cargados que fueron
	 * cargados exitosamente y no han sido procesados por id modelo archivo
	 * 
	 * @param estadoCargue
	 * @return List<ArchivosCargados>
	 * @author duvan.naranjo
	 */
	public List<ArchivosCargados> findByEstadoCargue(String estadoCargue);

	/**
	 * Metodo encargado de realizar la consulta de los archivos cargados que fueron
	 * cargados exitosamente y no han sido procesados por id modelo archivo, además
	 * de ser filtrado por el tipo de archivo
	 * 
	 * @param estadoCargue
	 * @param idModeloArchivo
	 * @return List<ArchivosCargados>
	 * @author duvan.naranjo
	 */
	public List<ArchivosCargados> findByEstadoCargueAndIdModeloArchivo(String estadoCargue, String idModeloArchivo);

	/**
	 * Metodo encargado de realizar la consulta de los registros cargados sin
	 * procesar de hoy
	 * 
	 * @param agrupador
	 * @return Page<ArchivosCargados>
	 * @author duvan.naranjo
	 */
	@Query("select ac from ArchivosCargados ac " + "where idModeloArchivo IN ("
			+ "select idMaestroDefinicionArchivo from MaestroDefinicionArchivo "
			+ "where agrupador = ?1) and estado = ?2 order by fechaArchivo desc, estadoCargue asc")
	Page<ArchivosCargados> getArchivosByAgrupador(String agrupador, String estado, Pageable page);
	
	/**
	 * Metodo encargado de realizar la consulta de los registros cargados sin
	 * procesar de hoy
	 * 
	 * @param agrupador
	 * @param estado
	 * @param fechaProceso
	 * @return Page<ArchivosCargados>
	 * @author prv_nparra
	 */
	@Query("select ac from ArchivosCargados ac " + "where idModeloArchivo IN ("
			+ "select idMaestroDefinicionArchivo from MaestroDefinicionArchivo "
			+ "where agrupador = ?1) and estado = ?2 and fechaArchivo = ?3 order by fechaArchivo desc, estadoCargue asc")
	Page<ArchivosCargados> getArchivosByAgrupadorAndFechaArchivo(String agrupador, String estado, Date fechaProceso, Pageable page);

	/**
	 * Metodo encargado de realizar la consulta de los registros cargados sin
	 * procesar de hoy
	 * 
	 * @param agrupador
	 * @return List<ArchivosCargados>
	 * @author duvan.naranjo
	 */
	@Query("select ac from ArchivosCargados ac " + "where estadoCargue = ?3 and fechaArchivo = ?2 and "
			+ "idModeloArchivo IN (select idMaestroDefinicionArchivo from MaestroDefinicionArchivo "
			+ "where agrupador = ?1)")
	List<ArchivosCargados> getRegistrosCargadosSinProcesarDeHoy(String agrupador, Date fecha, String estado);

	/**
	 * Metodo encargado de realizar la consulta de los ids de los archivos cargados
	 * sin procesar de una fecha, en un estado de cargue
	 * 
	 * @param agrupador
	 * @return List<Long>
	 * @author rafael.parra
	 */
	@Query("select ac.idArchivo from ArchivosCargados ac where estadoCargue = ?3 and fechaArchivo = ?2 and "
			+ "idModeloArchivo IN (select idMaestroDefinicionArchivo from MaestroDefinicionArchivo "
			+ "where agrupador = ?1) "
			+ "order by ac.idArchivo " )
	List<Long> getIdArchivosCargadosPorAgrupadorFechaEstado(String agrupador, Date fecha, String estado);

	/**
	 * Consulta de un archivo cargado en un estado , con un nombre determinado y que
	 * pertenece a un idModelo
	 * 
	 * @param estadoCargue
	 * @param nombreArchivo
	 * @param idModeloArchivo
	 * @return List<ArchivosCargados>
	 * @author rparra
	 */
	@Query("select ac from ArchivosCargados ac " + "where estadoCargue = ?1 and nombreArchivo = ?2 and "
			+ "idModeloArchivo = ?3")
	List<ArchivosCargados> getRegistrosCargadosPorNombreyEstado(String estadoCargue, String nombreArchivo,
			String idModeloArchivo);

	/**
	 * Consulta de un archivo cargado en un estado , con un nombre determinado y que
	 * pertenece a un idModelo
	 * 
	 * @param estadoCargue
	 * @param nombreArchivo
	 * @param idModeloArchivo
	 * @return List<ArchivosCargados>
	 * @author rparra
	 */
	@Query("select ac from ArchivosCargados ac " + "where estadoCargue = ?1 and nombreArchivoUpper = ?2 and "
			+ "idModeloArchivo = ?3")
	List<ArchivosCargados> getRegistrosCargadosPorEstadoCargueyNombreUpperyModelo(String estadoCargue,
			String nombreArchivo, String idModeloArchivo);

	/**
	 * Consulta encargada de filtrar los archivos cargados por una fecha de archivo
	 * 
	 * @param fechaArchivo
	 * @return List<ArchivosCargados>
	 * @author duvan.naranjo
	 */
	List<ArchivosCargados> findByFechaArchivo(Date fechaArchivo);
	
	/**
	 * Metodo encargado de realizar la consulta de los registros cargados cierre de conciliacion
	 * para el proceso de liquidacion de costos
	 * 
	 * @param agrupador
	 * @param estadosCargue
	 * @return Page<ArchivosCargados>
	 * @author johan.chaparro
	 */
//	@Query("select ac from ArchivosCargados ac " +
//		       "where idModeloArchivo IN (" +
//		       "select idMaestroDefinicionArchivo from MaestroDefinicionArchivo " +
//		       "where agrupador = ?1) " +
//		       "and estado = ?2 " +
//		       "and estadoCargue IN ?3 " +
//		       "order by fechaArchivo desc, estadoCargue asc")
	
	@Query(
		    value = """
		        WITH ULTIMOS AS (
		            SELECT ac.ID_ARCHIVO,
		                   ac.FECHA_INICIO_CARGUE,
		                   ac.FECHA_FIN_CARGUE,
		                   ac.ESTADO_CARGUE,
		                   ac.NOMBRE_ARCHIVO,
		                   ac.NOMBRE_ARCHIVO_UPPER,
		                   ac.FECHA_ARCHIVO,
		                   ac.NUMERO_REGISTROS,
		                   ac.NUMERO_ERRORES,
		                   ac.VIGENCIA,
		                   ac.ID_MODELO_ARCHIVO,
		                   ac.URL,
		                   ac.CONTENT_TYPE,
		                   ac.ESTADO,
		                   ac.USUARIO_CREACION,
		                   ac.FECHA_CREACION,
		                   ac.USUARIO_MODIFICACION,
		                   ac.OBSERVACION,
		                   ac.FECHA_MODIFICACION,
		                   ROW_NUMBER() OVER (
		                       PARTITION BY ac.NOMBRE_ARCHIVO
		                       ORDER BY ac.FECHA_ARCHIVO DESC, ac.ESTADO_CARGUE ASC
		                   ) AS rn
		            FROM CONTROLEFECT.ARCHIVOS_CARGADOS ac
		            WHERE ac.ID_MODELO_ARCHIVO IN (
		                SELECT mda.ID_MAESTRO_DEFINICION_ARCHIVO
		                FROM CONTROLEFECT.MAESTRO_DEFINICION_ARCHIVO mda
		                WHERE mda.AGRUPADOR = :agrupador
		            )
		            AND ac.ESTADO = :estado
		            AND ac.ESTADO_CARGUE IN (:estadoCargue)
		        )
		        SELECT ID_ARCHIVO,
		               FECHA_INICIO_CARGUE,
		               FECHA_FIN_CARGUE,
		               ESTADO_CARGUE,
		               NOMBRE_ARCHIVO,
		               NOMBRE_ARCHIVO_UPPER,
		               FECHA_ARCHIVO,
		               NUMERO_REGISTROS,
		               NUMERO_ERRORES,
		               VIGENCIA,
		               ID_MODELO_ARCHIVO,
		               URL,
		               CONTENT_TYPE,
		               ESTADO,
		               USUARIO_CREACION,
		               FECHA_CREACION,
		               USUARIO_MODIFICACION,
		               OBSERVACION,
		               FECHA_MODIFICACION
		        FROM ULTIMOS
		        WHERE rn = 1
		        ORDER BY FECHA_ARCHIVO DESC, ESTADO_CARGUE ASC
		        """,
		    countQuery = """
		        SELECT COUNT(*) 
		        FROM (
		            WITH ULTIMOS AS (
		                SELECT ac.ID_ARCHIVO,
		                       ROW_NUMBER() OVER (
		                           PARTITION BY ac.NOMBRE_ARCHIVO
		                           ORDER BY ac.FECHA_ARCHIVO DESC, ac.ESTADO_CARGUE ASC
		                       ) AS rn
		                FROM CONTROLEFECT.ARCHIVOS_CARGADOS ac
		                WHERE ac.ID_MODELO_ARCHIVO IN (
		                    SELECT mda.ID_MAESTRO_DEFINICION_ARCHIVO
		                    FROM CONTROLEFECT.MAESTRO_DEFINICION_ARCHIVO mda
		                    WHERE mda.AGRUPADOR = :agrupador
		                )
		                AND ac.ESTADO = :estado
		                AND ac.ESTADO_CARGUE IN (:estadoCargue)
		            )
		            SELECT 1 FROM ULTIMOS WHERE rn = 1
		        ) AS conteo
		        """,
		    nativeQuery = true
		)
		Page<ArchivosCargados> getArchivosByAgrupadorAndEstadoCargue(
		        @Param("agrupador") String agrupador,
		        @Param("estado") String estado,
		        @Param("estadoCargue") Set<String> estadoCargue,
		        Pageable pageable
		);


}
