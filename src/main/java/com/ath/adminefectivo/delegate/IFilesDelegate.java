package com.ath.adminefectivo.delegate;

import java.util.List;

import javax.transaction.RollbackException;

import org.springframework.web.multipart.MultipartFile;

import com.ath.adminefectivo.dto.ArchivosCargadosDTO;
import com.ath.adminefectivo.dto.DownloadDTO;

public interface IFilesDelegate {

	/**
	 * Metodo encargado de orquestar el servicio de persistencia de archivos
	 * 
	 * @param files
	 * @return Boolean
	 * @author CamiloBenavides
	 */
	public Boolean persistirArchvos(MultipartFile[] files);

	/**
	 * Delegate encargado de seleccionar y propagar las excepciones en la prueba de
	 * concepto
	 * 
	 * @param numeroExcepcion
	 * @return Boolean
	 * @author CamiloBenavides
	 * @throws RollbackException
	 */
	public Boolean manejoExcepciones(Integer numeroExcepcion);

	/**
	 * Metodo encargado de descargar un documento del repositorio
	 * 
	 * @param idArchivo
	 * @return
	 * @return DownloadDTO
	 * @author CamiloBenavides
	 */
	DownloadDTO downloadFile(Long idArchivo);

	/**
	 * Metodo encargado de descargar un documento del repositorio
	 * 
	 * @param idArchivo
	 * @return
	 * @return DownloadDTO
	 * @author CamiloBenavides
	 */
	DownloadDTO descargarArchivo(String nombreArchivo, String idMaestroArchivo);
	
	/**
	 * Metodo encargado de descargar un archivo procesado del repositorio
	 * 
	 * @param idArchivo
	 * @return
	 * @return DownloadDTO
	 * @author RParra
	 */
	DownloadDTO descargarArchivoProcesado(Long idArchivo);

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
	 * Consulta los archivos cargados en repositorio, por tipo de archivo
	 * (idMaestroDetalle) y estado del archivo
	 * 
	 * @param idMaestroDefinicion
	 * @param estado
	 * @return List<ArchivosCargadosDTO>
	 * @author CamiloBenavides
	 */
	List<ArchivosCargadosDTO> consultarArchivos(String idMaestroDefinicion, String estado);

}
