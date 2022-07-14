package com.ath.adminefectivo.delegate;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ath.adminefectivo.dto.ArchivosCargadosDTO;
import com.ath.adminefectivo.entities.ArchivosCargados;
import com.querydsl.core.types.Predicate;

public interface IContabilidadDelegate {	
	
	/**
	 * Delegate encargado de generar la logica para generar la contabilidad
	 * 
	 * @param tipoContabilidad
	 * @return String
	 * @author duvan.naranjo
	 */
	String generarContabilidad(String tipoContabilidad);
	
		
}
