package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.entities.ParametrosRetencion;

public interface IGestionRetencionArchivosService {

	boolean eliminarArchivosPorRetencion();

	List<ParametrosRetencion> obtenerParametrosPorActivo(boolean activo);

}
