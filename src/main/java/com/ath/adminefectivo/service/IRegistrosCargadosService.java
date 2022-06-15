package com.ath.adminefectivo.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ath.adminefectivo.dto.DownloadDTO;
import com.ath.adminefectivo.dto.RegistrosCargadosDTO;

/**
 * Interfaz de los servicios referentes a los registros cargados de un archivo
 *
 * @author duvan.naranjo
 */
public interface IRegistrosCargadosService {

	/**
	 * Servicio encargado de la consulta de registros cargados por id archivo
	 * 
	 * @param idArchivo
	 * @return List<RegistrosCargadosDTO>
	 * @author duvan.naranjo
	 */
	List<RegistrosCargadosDTO> consultarRegistrosCargadosPorIdArchivo(Long idArchivo);

}
