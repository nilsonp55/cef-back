package com.ath.adminefectivo.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.ath.adminefectivo.entities.ArchivosCargados;
import com.ath.adminefectivo.entities.OperacionesProgramadas;
import com.ath.adminefectivo.entities.Puntos;

/**
 * Repository encargado de manejar la logica de la entidad ArchivosCargados
 *
 * @author CamiloBenavides
 */
@Repository
public interface ArchivosCargadosRepository
		extends JpaRepository<ArchivosCargados, Long>, QuerydslPredicateExecutor<ArchivosCargados> {

	/**
	 * Metodo encargado de realizar la consulta de los archivos cargados  y no han sido procesados
	 * @return List<ArchivosCargados>
	 * @author cesar.castano
	 */
//	List<ArchivosCargados> findByIdModeloArchivo(String idModeloArchivo);
	
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

	List<ArchivosCargados> findByIdModeloArchivoOrIdModeloArchivo(String tipoArchivoItvcs, String tipoArchivoIstrc);

}
