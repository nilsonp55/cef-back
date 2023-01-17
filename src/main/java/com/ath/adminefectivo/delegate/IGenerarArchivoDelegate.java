package com.ath.adminefectivo.delegate;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;

import com.ath.adminefectivo.dto.RespuestaContableDTO;
import com.ath.adminefectivo.dto.compuestos.RespuestaGenerarArchivoDTO;

public interface IGenerarArchivoDelegate {

	RespuestaGenerarArchivoDTO generarArchivo(Date fecha,String tipoContabilidad,int codBanco);
}
