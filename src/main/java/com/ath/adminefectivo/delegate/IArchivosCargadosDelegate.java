package com.ath.adminefectivo.delegate;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ath.adminefectivo.dto.ArchivosCargadosDTO;
import com.ath.adminefectivo.entities.ArchivosCargados;
import com.querydsl.core.types.Predicate;

public interface IArchivosCargadosDelegate {	
	
	/**
	 * Delegate encargado de persistir un ArchivoCargado
	 * @param archivo
	 * @return List<ArchivosCargados>
	 * @author CamiloBenavides
	 */
	List<ArchivosCargados> guardarArchivo(ArchivosCargadosDTO archivo);
	
	/**
	 * Delegate responsable de consultar los ArchivosCargados del sistema, por filtros y paginador
	 * @param predicate
	 * @param page
	 * @return List<ArchivosCargadosDTO>
	 * @author CamiloBenavides
	 */
	Page<ArchivosCargadosDTO> getAll(Predicate predicate, Pageable page);
	
	/**
	 * Delegate responsable de consultar todos los ArchivosCargados
	 * @return List<ArchivosCargadosDTO>
	 * @author CamiloBenavides
	 */
	List<ArchivosCargadosDTO> getAll();
	
	/**
	 * Método encargado de eliminar un archvivo cargado tanto su registro lógico como físico
	 * @param idArchivo
	 * @return
	 * @return Boolean
	 * @author CamiloBenavides
	 */
	Boolean eliminarArchivo(Long idArchivo);
		
}
