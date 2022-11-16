package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.dto.BancosDTO;
import com.ath.adminefectivo.dto.PuntosDTO;
import com.querydsl.core.types.Predicate;

public interface IEncriptarService {

	public String encriptarArchivo(String path, String nombreArchivo);
	
	public String desencriptarArchivo(String path, String nombreArchivo);

	

}
