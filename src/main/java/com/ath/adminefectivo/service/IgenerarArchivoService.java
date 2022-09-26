package com.ath.adminefectivo.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;

import com.ath.adminefectivo.dto.RespuestaContableDTO;

/**
 * Interfaz de los servicios referentes a la generacion
 * de archvivos contables
 *
 * @author Miller  Caro
 */
public interface IgenerarArchivoService {

	ByteArrayOutputStream generarArchivo(Date fecha,String tipoContabilidad,int codBanco );

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
