package com.ath.adminefectivo.delegate;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.ath.adminefectivo.dto.DominioMaestroDto;

public interface IDominioMaestroDelegate {

	/**
	 * Retorna la lista de dominios segun parametro de estado
	 * 
	 * @param estado
	 * @return DominioDTO
	 * @author Bayron Andres Perez Muñoz
	 */
	List<DominioMaestroDto> obtenerDominiosMaestro(@RequestParam("estado") String estado);

	List<DominioMaestroDto> obtenerTodosDominiosMaestro();
	
	/**
	 * Retorna un dominio segun su identificador unico
	 * 
	 * @param idDOminio
	 * @return DominioDTO
	 * @author Bayron Andres Perez Muñoz
	 */
	DominioMaestroDto obtenerDominioMaestroById(@PathVariable("id") String id);
	
	/**
	 * Persiste un dominio en la base de datos
	 * 
	 * @param dominioDto
	 * @return DominioDTO
	 * @author Bayron Andres Perez Muñoz
	 */
	DominioMaestroDto persistirDominioMaestro(@RequestBody DominioMaestroDto dominioMaestroDto);
	
	/**
	 * Actualiza un dominio en la base de datos
	 * 
	 * @param dominioDto
	 * @return DominioDTO
	 * @author Bayron Andres Perez Muñoz
	 */
	DominioMaestroDto actualizarDominioMaestro(@RequestBody DominioMaestroDto dominioMaestroDto);
	
	/**
	 * Eliminar un dominio en la base de datos
	 * 
	 * @param idDominio
	 * @return DominioDTO
	 * @author Bayron Andres Perez Muñoz
	 */
	Boolean eliminarDominioMaestro(@RequestParam("id") String id);
}

