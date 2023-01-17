package com.ath.adminefectivo.service;

import java.util.Date;
import java.util.List;

import com.ath.adminefectivo.dto.MaestrosDefinicionArchivoDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionLineasDTO;

/**
 * Servicio encargado de la logica de las validaciones de los archivos cargados
 *
 * @author CamiloBenavides
 */
public interface IValidacionArchivoService {

	/**
	 * Metodo encargado de realizar las validaciones correspondientres al archivo
	 * cargado
	 * 
	 * @param maestroDefinicion
	 * @param contenido
	 * @param validacionArchivo
	 * @return ValidacionLineasDTO
	 * @author duvan.naranjo
	 */
	ValidacionArchivoDTO validar(MaestrosDefinicionArchivoDTO maestroDefinicion, List<String[]> contenido,
			ValidacionArchivoDTO validacionArchivo);

	/**
	 * Metodo encargado de realizar el recorrido del listado de registros y realiza
	 * la validación de cada registro
	 * 
	 * @param maestroDefinicion
	 * @param contenido
	 * @param validacionArchivo
	 * @return ValidacionLineasDTO
	 * @author duvan.naranjo
	 */
	ValidacionArchivoDTO validarEstructura(MaestrosDefinicionArchivoDTO maestroDefinicion, List<String[]> contenido,
			ValidacionArchivoDTO validacionArchivo, List<ValidacionLineasDTO> respuesta);

	/**
	 * Metodo encargado de validar que el nombre del archivo es correcto
	 * comparandolo la mascara y extencion que tiene su definición maestro
	 * 
	 * @param maestroDefinicion
	 * @param nombreArchivo
	 * @return boolean
	 * @author CamiloBenavides
	 */
	boolean validarNombreArchivo(MaestrosDefinicionArchivoDTO maestroDefinicion, String nombreArchivo);

	/**
	 * Método que retona la fecha contenida en el nombre del archivo y si se envia
	 * la fecha comparacion valida que sea la misma fecha sin tener en cuenta horas
	 * ni minutos
	 * 
	 * @param nombreArchivo
	 * @param fechaComparacion
	 * @return Date
	 * @author CamiloBenavides
	 */
	Date validarFechaArchivo(String nombreArchivo, String mascaraArchivo, Date fechaComparacion);
	
	/**
	 * Método que retona la fecha contenida en el nombre del archivo y si se envia
	 * la fecha comparacion valida que sea la misma fecha sin tener en cuenta horas
	 * ni minutos
	 * 
	 * @param nombreArchivo
	 * @param fechaComparacion
	 * @param fechaAnteriorHabil
	 * @return Date
	 * @author CamiloBenavides
	 */
	Date validarFechaArchivoBetween(String nombreArchivo, String mascaraArchivo, Date fechaComparacion, Date fechaAnteriorHabil);

	/**
	 * Retorna la fecha de archivo cargado, en caso de no poderla obtener retorna
	 * null
	 * 
	 * @param nombreArchivo
	 * @return Date
	 * @author CamiloBenavides
	 */
	Date obtenerFechaArchivo(String nombreArchivo, String mascaraArchivo);

}
