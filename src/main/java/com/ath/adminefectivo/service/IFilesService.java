package com.ath.adminefectivo.service;

import java.util.List;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.springframework.web.multipart.MultipartFile;

import com.ath.adminefectivo.dto.DownloadDTO;
import com.ath.adminefectivo.dto.compuestos.SummaryArchivoLiquidacionDTO;

/**
 * Interfaz de los servicios referentes a lo carga y persistencia de archivos
 *
 * @author CamiloBenavides
 */
public interface IFilesService {

	/**
	 * Servicio encargado de la persitencia de documentos en el servidor
	 * 
	 * @return Boolean
	 * @author CamiloBenavides
	 */
	Boolean persistirArchvos(List<MultipartFile> files);

	/**
	 * Servicio encargado de retornar un documento alojado en el servidor
	 * 
	 * @param download
	 * @return
	 * @return DownloadDTO
	 * @author CamiloBenavides
	 */
	DownloadDTO downloadFile(DownloadDTO download);
	
	/**
	 * Metodo encargado de eliminar logicamente un archivo, arrojando error en caso de que el id no exista
	 * @param url
	 * @return
	 * @return ArchivosCargadosDTO
	 * @author CamiloBenavides
	 */
	Boolean eliminarArchivo(String url);



	/**
	 * Persiste un documento en el repositorio y retornar la url donde lo almacenó
	 * 
	 * @param file
	 * @return String
	 * @author CamiloBenavides
	 */
	String persistirArchvo(MultipartFile file);


	/**
	 * Consulta la ruta url y retorna el contenido de los archivos contenidos en el directorio
	 * 
	 * @param url
	 * @return List<String>
	 * @author CamiloBenavides
	 */
	List<String> obtenerContenidoCarpeta(String url);


	/**
	 * Consulta la ruta url y retorna el contenido de los archivos contenidos en el directorio
	 *
	 * @param url
	 * @return List<String>
	 * @author CamiloBenavides
	 */
	public List<SummaryArchivoLiquidacionDTO> obtenerContenidoCarpetaSummaryS3Object(String url, int start, int end, boolean content, String fileName);

	/**
	 * Copia un archivo de una carpeta de origen a una carpeta final
	 * @param urlSource
	 * @param urlDestino
	 * @param nombreArchivo
	 * @param postfijo
	 * @return
	 * @return boolean
	 * @author CamiloBenavides
	 */
	boolean moverArchivos(String urlSource, String urlDestino, String nombreArchivo, String postfijo);
	
	/**
	 * Retorna el parametro del los documentos almacenados por estado
	 * 
	 * @param estado
	 * @return String
	 * @author CamiloBenavides
	 */
	String consultarPathArchivos(String estado);

}
