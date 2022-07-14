package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.dto.DetallesDefinicionArchivoDTO;
import com.ath.adminefectivo.entities.id.DetallesDefinicionArchivoPK;

/**
 * Interfaz de los servicios relacionados con la entidad DetallesDefincionArchivo
 *
 * @author duvan.naranjo
 */
public interface IDetalleDefinicionArchivoService {

	/**
	 * Metodo encargado de consultar el detalle de los archivos id
	 * el metodo retorna un error en caso de no encontrar resultados
	 * @param detallesDefinicionArchivoPK
	 * @return DetallesDefinicionArchivoDTO
	 * @author duvan.naranjo
	 */
	DetallesDefinicionArchivoDTO consultarDetalleDefinicionArchivoById(DetallesDefinicionArchivoPK detallesDefinicionArchivoPK);

	/**
	 * Metodo encargado de consultar archivo detalle por 
	 * id maestro archivo
	 * 
	 * @param idMaestro
	 * @return List<DetallesDefinicionArchivoDTO>
	 * @author duvan.naranjo
	 */
	List<DetallesDefinicionArchivoDTO> consultarDetalleDefinicionArchivoByIdMaestro(String idMaestro);
	
	/**
	 * Metodo encargado de consultar archivo detalle por 
	 * idArchivo y numero de campo
	 * 
	 * @param idArchivo
	 * @param numeroCampo
	 * @return List<DetallesDefinicionArchivoDTO>
	 * @author duvan.naranjo
	 */
	List<DetallesDefinicionArchivoDTO> consultarDetalleDefinicionArchivoByIdArchivoNumeroCampo(String idArchivo, Integer numeroCampo);



}
