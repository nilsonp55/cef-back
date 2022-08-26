package com.ath.adminefectivo.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.ath.adminefectivo.entities.ArchivosCargados;

/**
 * Repository encargado de manejar la logica de la entidad ArchivosCargados
 *
 * @author CamiloBenavides
 */
@Repository
public interface ArchivosCargadosRepository
		extends JpaRepository<ArchivosCargados, Long>, 
				QuerydslPredicateExecutor<ArchivosCargados>, 
				PagingAndSortingRepository<ArchivosCargados, Long> 
{
	
	/**
	 * Metodo encargado de realizar la consulta de los archivos cargados por modeloarchivo y 	
	 * idArchivo
	 * @param idModeloArchivo
	 * @param idArchivo
	 * @return List<ArchivosCargados>
	 * @author duvan.naranjo
	 */
	List<ArchivosCargados> findByIdModeloArchivoAndIdArchivo(String idModeloArchivo, Long idArchivo);
	
	/**
	 * Metodo encargado de realizar la consulta de los archivos cargados que fueron cargados 
	 * exitosamente y no han sido procesados

	 * @return List<ArchivosCargados>
	 * @author duvan.naranjo
	 */
	public List<ArchivosCargados> findByEstadoCargueAndFechaArchivoBetween(String estadoCargue, Date start, Date end);
	
	/**
	 * Metodo encargado de realizar la consulta de los archivos cargados que fueron cargados 
	 * exitosamente y no han sido procesados por id modelo archivo
	 * 
	 * @param estadoCargue
	 * @return List<ArchivosCargados>
	 * @author duvan.naranjo
	 */
	public List<ArchivosCargados> findByEstadoCargue(String estadoCargue);
	
	/**
	 * Metodo encargado de realizar la consulta de los archivos cargados que fueron cargados 
	 * exitosamente y no han sido procesados por id modelo archivo, además de ser filtrado por
	 * el tipo de archivo
	 * 
	 * @param estadoCargue
	 * @param idModeloArchivo
	 * @return List<ArchivosCargados>
	 * @author duvan.naranjo
	 */
	public List<ArchivosCargados> findByEstadoCargueAndIdModeloArchivo(String estadoCargue, String idModeloArchivo);

	/**
	 * Metodo encargado de realizar la consulta de los registros cargados sin procesar de hoy
	 * @param agrupador
	 * @return Page<ArchivosCargados>
	 * @author duvan.naranjo
	 */
	@Query("select ac from ArchivosCargados ac "
            + "where idModeloArchivo IN ("
            + "select idMaestroDefinicionArchivo from MaestroDefinicionArchivo "
            + "where agrupador = ?1)") 
	Page<ArchivosCargados> getArchivosByAgrupador(String agrupador, Pageable page);
	
	/**
	 * Metodo encargado de realizar la consulta de los registros cargados sin procesar de hoy
	 * @param agrupador
	 * @return List<ArchivosCargados>
	 * @author duvan.naranjo
	 */
	@Query("select ac from ArchivosCargados ac "
		 + "where estadoCargue = ?3 and cast(fechaArchivo as date) = cast(?2 as date) and "
		 + "idModeloArchivo IN (select idMaestroDefinicionArchivo from MaestroDefinicionArchivo "
		 + "where agrupador = ?1)")
	List<ArchivosCargados> getRegistrosCargadosSinProcesarDeHoy(String agrupador, Date fecha, String estado);

	/**
	 * Consulta de un archivo cargado en un estado , con un nombre determinado 
	 * y que pertenece a un idModelo
	 * @param estadoCargue
	 * @param nombreArchivo
	 * @param idModeloArchivo
	 * @return List<ArchivosCargados>
	 * @author rparra
	 */
	@Query("select ac from ArchivosCargados ac "
		 + "where estadoCargue = ?1 and nombreArchivo = ?2 and "
		 + "idModeloArchivo = ?3")
	List<ArchivosCargados> getRegistrosCargadosPorNombreyEstado(String estadoCargue, String nombreArchivo, String idModeloArchivo);

}
