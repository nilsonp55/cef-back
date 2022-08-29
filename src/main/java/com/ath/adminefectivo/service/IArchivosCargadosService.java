package com.ath.adminefectivo.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ath.adminefectivo.dto.ArchivosCargadosDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;
import com.ath.adminefectivo.entities.ArchivosCargados;
import com.querydsl.core.types.Predicate;

/**
 * Interfaz de los servicios referentes a los archivosCargados
 *
 * @author CamiloBenavides
 */
public interface IArchivosCargadosService {

	/**
	 * Servicio encargado de consultar todos los registros de la entidad
	 * ArchivosCargados
	 * 
	 * @return List<ArchivosCargadosDTO>
	 * @author CamiloBenavides
	 */
	List<ArchivosCargadosDTO> getAll();

	/**
	 * Servicio encargado de consultar los archivos cargados por filtro y con
	 * paginación
	 * 
	 * @param predicate
	 * @param page
	 * @return List<ArchivosCargadosDTO>
	 * @author CamiloBenavides
	 */
	Page<ArchivosCargadosDTO> getAll(Predicate predicate, Pageable page);

	/**
	 * Consulta los archivos cargados y lo retorna en su respectivo DTO
	 * 
	 * @param idArchivo
	 * @return
	 * @return ArchivosCargadosDTO
	 * @author CamiloBenavides
	 */
	ArchivosCargadosDTO consultarArchivo(Long idArchivo);
	
	/**
	 * Metodo encargado de eliminar logicamente un archivo
	 * 
	 * @param idArchivo
	 * @return ArchivosCargadosDTO
	 * @author CamiloBenavides
	 */
	ArchivosCargadosDTO eliminarArchivoCargado(Long idArchivo);

	/**
	 * Servicio encargado persistir una lista de ArchivosCargados en base de datos
	 * 
	 * @param archivosCargados
	 * @return ArchivosCargados
	 * @author CamiloBenavides
	 */
	List<ArchivosCargados> guardarArchivos(List<ArchivosCargadosDTO> archivosCargados);

	/**
	 * Servicio encargado de consultar el detalle de los archivos cargados y
	 * organizarlos en el DTO de respuesta ValidacionArchivoDTO
	 * 
	 * @param idArchivo
	 * @return ArchivosCargadosDTO
	 * @author CamiloBenavides
	 */
	ValidacionArchivoDTO consultarDetalleArchivo(Long idArchivo);

	/**
	 * Metodo que persiste la información de los archivos cargados luego de obtener
	 * la data del proceso de validación
	 * 
	 * @param validacionArchivo
	 * @return Long
	 * @author CamiloBenavides
	 */
	Long persistirDetalleArchivoCargado(ValidacionArchivoDTO validacionArchivo,  boolean soloErrores);
	
	/**
	 * Metodo encagado de obtener los archivos cargados actuales que no se han
	 * procesado o generado el debido proceso de las operaciones programadas a traves
	 * del tipo de archivo cargado
	 * 
	 * @param idModeloArchivo
	 * @return List<ArchivosCargadosDTO>
	 * @author duvan.naranjo
	 */
	List<ArchivosCargadosDTO> getArchivosCargadosSinProcesar(String idModeloArchivo);
	
	/**
	 * Metodo encagado de actualizar el archivo
	 * 
	 * @param archivosCargadosDTO
	 * @return 
	 * @author duvan.naranjo
	 */
	void actualizarArchivosCargados(ArchivosCargadosDTO archivosCargadosDTO);
	
	/**
	 * Servicio encargado de consultar los archivos cargados por filtro y con
	 * paginación
	 * 
	 * @param agrupador
	 * @param page
	 * @return Page<ArchivosCargadosDTO>
	 * @author cesar.castano
	 */
	Page<ArchivosCargadosDTO> getAllByAgrupador(String agrupador, Pageable page);

	/**
	 * Metodo encagado de listar los archivos cargados sin procesar de definitiva
	 * 
	 * @param archivosCargadosDTO
	 * @return 
	 * @author duvan.naranjo
	 */

	List<ArchivosCargados> listadoArchivosCargadosSinProcesarDefinitiva(String agrupador,
			Date fecha, String estado);
	
	/**
	 * Metodo encagado de actualizar el archivo
	 * 
	 * @param archivosCargadosDTO
	 * @return 
	 * @author duvan.naranjo
	 */
	void actualizarArchivosCargados(ArchivosCargados archivosCargados);
}
