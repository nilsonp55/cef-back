package com.ath.adminefectivo.service;

import java.util.Date;

import com.ath.adminefectivo.dto.compuestos.RespuestaGenerarArchivoDTO;

/**
 * Interfaz de los servicios referentes a la generacion
 * de archvivos contables
 *
 * @author Miller  Caro
 */
public interface IgenerarArchivoService {

	RespuestaGenerarArchivoDTO generarArchivo(Date fecha,String tipoContabilidad,int codBanco );
	
	/**
	 * Metodo encargado de realizar la extraccion y formación del archivo contable para Banco de Bogota
	 * 
	 * @param fecha
	 * @param tipoContabilidad
	 * @param codBanco
	 * @return
	 */
	RespuestaGenerarArchivoDTO generarArchivoBBOG(Date fecha, String tipoContabilidad,int codBanco);
	
	/**
	 * Metodo encargado de realizar la extraccion y formación del archivo contable para Banco de Avvillas
	 * 
	 * @param fecha
	 * @param tipoContabilidad
	 * @param codBanco
	 * @return
	 */
	RespuestaGenerarArchivoDTO generarArchivoBAVV(Date fecha, String tipoContabilidad,int codBanco);

	/**
	 * Metodo encargado de generar los archivos de cierre de contabilidad
	 * para todos los bancos y subirlos al S3
	 * 
	 * @param fecha
	 * @param tipoContabilidad
	 * @author duvan.naranjo
	 */
	void generarArchivosCierreContable(Date fecha, String tipoContabilidad);
}
