package com.ath.adminefectivo.delegate;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;

import com.ath.adminefectivo.dto.RespuestaContableDTO;

public interface IGenerarArchivoDelegate {

	ByteArrayInputStream generarArchivo(Date fecha,String tipoContabilidad,String codBanco);
}
