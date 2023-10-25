package com.ath.adminefectivo.service;

import java.util.Date;

import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;

public interface ICargueCertificacionService {
	
	ValidacionArchivoDTO procesarArchivo2(String idMaestroDefinicion, String nombreArchivo, boolean alcance,
			 Date fechaActual, Date fechaAnteriorHabil, Date fechaAnteriorHabil2);

}
