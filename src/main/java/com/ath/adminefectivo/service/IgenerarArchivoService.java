package com.ath.adminefectivo.service;

import java.io.ByteArrayInputStream;
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

	ByteArrayInputStream generarArchivo(Date fecha,String tipoContabilidad,int codBanco );
}
