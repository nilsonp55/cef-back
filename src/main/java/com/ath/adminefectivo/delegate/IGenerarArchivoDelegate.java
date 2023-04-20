package com.ath.adminefectivo.delegate;

import com.ath.adminefectivo.dto.compuestos.RespuestaGenerarArchivoDTO;

public interface IGenerarArchivoDelegate {

	RespuestaGenerarArchivoDTO generarArchivo(String tipoContabilidad, int codBanco);
}
