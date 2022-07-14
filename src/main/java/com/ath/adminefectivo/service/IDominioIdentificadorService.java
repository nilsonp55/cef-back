package com.ath.adminefectivo.service;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.ath.adminefectivo.dto.DominioIdentificadorDTO;

/**
 * Interfaz de los servicios relacionados con la creación de los dominios identificadores
 *
 * @author Bayron Andres erez M
 */
public interface IDominioIdentificadorService {
	
	/**
	 * Retorna la lista de dominios segun parametro de estado
	 * 
	 * @param estado
	 * @return DominioIdentificadorDTO
	 * @author Bayron Andres Perez Muñoz
	 */
	List<DominioIdentificadorDTO> obtenerDominiosIdentificador(@RequestParam("estado") String estado);
	
	/**
	 * Retorna un dominio segun su identificador unico
	 * 
	 * @param idDominioIdentificador
	 * @return DominioIdentificadorDTO
	 * @author Bayron Andres Perez Muñoz
	 */
	DominioIdentificadorDTO obtenerDominioIdentificadorById(@PathVariable("id") Integer id);
	
	/**
	 * Persiste un dominio en la base de datos
	 * 
	 * @param idDominioIdentificador
	 * @return DominioIdentificadorDTO
	 * @author Bayron Andres Perez Muñoz
	 */
	DominioIdentificadorDTO persistirDominioIdentificador(@RequestBody DominioIdentificadorDTO dominioIdentificadorDto);
	
	/**
	 * Actualiza un dominio en la base de datos
	 * 
	 * @param idDominioIdentificador
	 * @return DominioIdentificadorDTO
	 * @author Bayron Andres Perez Muñoz
	 */
	DominioIdentificadorDTO actualizarDominioIdentificador(@RequestBody DominioIdentificadorDTO dominioIdentificadorDto);
	
	/**
	 * Eliminar un dominio en la base de datos
	 * 
	 * @param idDominioIdentificador
	 * @return DominioIdentificadorDTO
	 * @author Bayron Andres Perez Muñoz
	 */
	Boolean eliminarDominioIdentificador(@RequestParam("id") Integer id);
}
