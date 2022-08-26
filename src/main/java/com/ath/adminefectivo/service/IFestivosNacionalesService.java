package com.ath.adminefectivo.service;

import java.util.Date;
import java.util.List;

/**
 * Interfaz de los servicios referentes a los Festivos Nacionales
 *
 * @author CamiloBenavides
 */
public interface IFestivosNacionalesService {

	/**
	 * Retorna true si la fecha consultada corresponde a un dia festivo
	 * 
	 * @param fecha
	 * @return boolean
	 * @author CamiloBenavides
	 */
	boolean esFestivo(Date fecha);

	/**
	 * Consulta la lista de festivos vigentes en el sistema, si se envia la fecha
	 * retorna los festivos mayores o iguales de lo contrario realiza la comparacion
	 * con el dia actual
	 * 
	 * @param fecha
	 * @return
	 * @return List<Date>
	 * @author CamiloBenavides
	 */
	List<Date> consultarFestivosVigentes(Date fecha);

	/**
	 * Retorna el siguiente dia habil dada una fecha de referencia
	 * 
	 * @param fechaReferencia
	 * @return Date
	 * @author CamiloBenavides
	 */
	Date consultarSiguienteHabil(Date fechaReferencia);

	Date consultarAnteriorHabil(Date fechaArchivo);

}
