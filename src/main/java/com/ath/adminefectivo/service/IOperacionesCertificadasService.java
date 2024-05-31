package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.dto.DetallesDefinicionArchivoDTO;
import com.ath.adminefectivo.dto.FechasConciliacionDTO;
import com.ath.adminefectivo.entities.ArchivosCargados;
import com.ath.adminefectivo.entities.OperacionesCertificadas;

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
	 * 
	 * @return Integer
	 * @author cesar.castano
	 * @param fechaConciliacion
	 * @param estado
	 */
	Integer numeroOperacionesPorEstadoyFecha(FechasConciliacionDTO fechaConciliacion, String estado);


	/**
	 * Servicio encargado de obtener los registros de OperacionesCertirficadas que
	 * estan dentro de la conciliacion
	 * 
	 * @return List<OperacionesCertificadas>
	 * @author cesar.castano
	 */
	List<OperacionesCertificadas> obtenerOperacionesCertificaciones();

	/**
	 * Servicio encargado de obtener la entidad de OperacionesCertirficadas por Id
	 * 
	 * @return OperacionesCertificadas
	 * @author cesar.castano
	 */
	OperacionesCertificadas obtenerEntidadOperacionesCertificacionesporId(Integer idCertificacion);

	/**
	 * Metodo encargado de realizar la validacion de las operaciones certificadas
	 * que no pueden ser conciliadas
	 * 
	 * @param archivosCargados
	 * @author duvan.naranjo
	 */
	void validarNoConciliables();

	/**
	 * Servicio encargado de generar el numero de operaciones certificadas no
	 * conciliadas por rango de fechas y que el campo conciliable sea SI
	 * 
	 * @return Integer
	 * @author cesar.castano
	 * @param fechaConciliacion
	 * @param estado
	 */
	Integer numeroOperacionesPorEstadoFechaYConciliable(FechasConciliacionDTO fechaConciliacion, String estado,
			String conciliable);

	/**
	 * Servicio encargado de procesar los datos de los archivos de alcance a
	 * certificaciones
	 *
	 * @return String
	 * @author rafael.parra
	 */
	String procesarArchivosAlcance();

	void procesarArchivoBrinks(ArchivosCargados elemento, List<DetallesDefinicionArchivoDTO> detalleArchivo);

	void procesarArchivoOtrosFondos(ArchivosCargados elemento, List<DetallesDefinicionArchivoDTO> detalleArchivo);

}
