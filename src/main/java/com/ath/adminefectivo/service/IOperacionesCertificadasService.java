package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.dto.FechasConciliacionDTO;
import com.ath.adminefectivo.entities.ArchivosCargados;

/**
 * @author cesar.castano
 */
public interface IOperacionesCertificadasService {

	/**
	 * Servicio encargado de actualizar el estado en la tabla de Operaciones
	 * Certificadas por id
	 * 
	 * @param idCertificacion
	 * @return Boolean
	 * @author cesar.castano
	 */
	Boolean actualizarEstadoEnCertificadas(Integer idCertificacion, String estado);

	/**
	 * Servicio encargado de generar el numero de operaciones certificadas no
	 * conciliadas por rango de fechas
	 * @return Integer
	 * @author cesar.castano
	 * @param fechaConciliacion
	 * @param estado
	 */
	Integer numeroOperacionesPorEstadoyFecha(FechasConciliacionDTO fechaConciliacion, String estado);

	/**
	 * Servicio encargado de procesar los archivos cargados a la entidad OperacionesCertificadas
	 * @param archivosCargados
	 * @return Boolean
	 * @author cesar.castano
	 */
	Boolean procesarArchivosCertificaciones(List<ArchivosCargados> archivosCargados);
}
