package com.ath.adminefectivo.delegate;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ath.adminefectivo.dto.ArchivosCargadosDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;

/**
 * Delegate que expone los servicios referente al carge preliminar 
 *
 * @author CamiloBenavides
 */
public interface ICarguePreliminarDelegate {

	/**
	 * Metodo encargado de persistir en el repositorio como en la base de datos el
	 * archivo cargado
	 * 
	 * @param files
	 * @return Boolean
	 * @author CamiloBenavides
	 */
	Boolean persistirArchvoCargado(MultipartFile files);

	/**
	 * Método encargado de eliminar un archvivo cargado tanto su registro lógico
	 * como físico
	 * 
	 * @param idArchivo
	 * @return
	 * @return Boolean
	 * @author CamiloBenavides
	 */
	Boolean eliminarArchivo(String nombreArchivo, String idMaestroArchivo);


	/**
	 * Metodo que realiza el cargue y las validaciones del un archivo, consultado por
	 * nombre y definicion maestro
	 * 
	 * @param idMaestroDefinicion
	 * @param nombreArchivo
	 * @return ValidacionArchivoDTO
	 * @author duvan.naranjo
	 */
	ValidacionArchivoDTO validarArchivo(String idMaestroDefinicion, String nombreArchivo);

	/**
	 * Retorna el detalla del archivo cargado con la informacion de fallos.
	 * 
	 * @param idArchivoCargado
	 * @return ValidacionArchivoDTO
	 * @author CamiloBenavides
	 */
	ValidacionArchivoDTO consultarDetalleArchivo(Long idArchivoCargado);

	/**
	 * Procesa el archivo cargado y lo almacena en las tablas definitivas
	 * 
	 * @param idArchivoCargado
	 * @return ValidacionArchivoDTO
	 * @author CamiloBenavides
	 */
	ValidacionArchivoDTO procesarArchivo(String idMaestroDefinicion, String nombreArchivo);

	/**
	 * Consulta los archivos de carga definitiva almacenados en repositorio, por
	 * estado del archivo
	 * 
	 * @param estado
	 * @return List<ArchivosCargadosDTO>
	 * @author CamiloBenavides
	 */
	List<ArchivosCargadosDTO> consultarArchivos(String estado, String agrupador);
}
