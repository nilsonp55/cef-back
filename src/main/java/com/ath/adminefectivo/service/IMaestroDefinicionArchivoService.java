package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.dto.MaestrosDefinicionArchivoDTO;
import com.ath.adminefectivo.entities.MaestroDefinicionArchivo;

/**
 * Interfaz de los servicios relacionados con la entidad maestroDefincionArchivo
 *
 * @author CamiloBenavides
 */
public interface IMaestroDefinicionArchivoService {

	/**
	 * Metodo encargado de consultar el detalle de los maestros definicion por id
	 * el metodo retornar un error en caso de no encontrar resultados
	 * @param idMaestroDefinicion
	 * @return MaestrosDefinicionArchivoDTO
	 * @author CamiloBenavides
	 */
	MaestrosDefinicionArchivoDTO consultarDefinicionArchivoById(String idMaestroDefinicion);

	/**
	 * Consulta los maestros definicion por estado y agrupador
	 * 
	 * @param agrupador
	 * @return List<MaestrosDefinicionArchivoDTO>
	 * @author CamiloBenavides
	 */
	List<MaestrosDefinicionArchivoDTO> consultarDefinicionArchivoByAgrupador(String estado, String agrupador);

	/**
	 * Metodo encargado de consultar el detalle de los maestros definicion por id
	 * las iniciales de la mascara
	 * @param inicialMarcara
	 * @return MaestrosDefinicionArchivoDTO
	 * @author cesar.castano
	 */
	MaestroDefinicionArchivo consultarInicialMascara(String inicialMarcara);

}
