package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.entities.Usuario;
import com.ath.adminefectivo.exception.NotFoundException;
import com.querydsl.core.types.Predicate;

public interface IUsuarioService {

	/**
	 * Servicio encargado de consultar la lista de todos los usuario 
	 * @param predicate
	 * @return List<ParametroDTO>
	 * @author bayron.perez
	 */
	List<Usuario> getUsuarios(Predicate predicate);
	
	/**
	 * Metodo encargado de obtener un usaurio por su ID
	 * @param idUsuario
	 * @return
	 * @author bayron.perez
	 */
	Usuario getUsuarioById(String idUsuario);
	
	/**
	 * Metodo encargado de guardar un usaurio 
	 * @param idUsuario
	 * @return
	 * @author bayron.perez
	 */
	Usuario postUsuario(Usuario usuario) throws NotFoundException;
	
	/**
	 * Metodo encargado de actualizar un usaurio 
	 * @param idUsuario
	 * @return
	 * @author bayron.perez
	 */
	Usuario putUsuario(Usuario usuario) throws NotFoundException;
	
	/**
	 * Metodo encargado de eliminar un usaurio 
	 * @param idUsuario
	 * @return
	 * @author bayron.perez
	 */
	void deleteUsuario(String idUsuario);

}
