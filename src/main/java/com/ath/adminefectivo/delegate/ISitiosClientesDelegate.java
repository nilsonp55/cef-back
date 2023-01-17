package com.ath.adminefectivo.delegate;

import java.util.List;

import com.ath.adminefectivo.dto.SitiosClientesDTO;
import com.querydsl.core.types.Predicate;

/**
 * Delegate que expone los servicios referente a los sitios clientes
 * @author cesar.castano
 */
public interface ISitiosClientesDelegate {

	/**
	 * Delegate encargado de retornar la lista de todos los Sitios Clientes
	 * 
	 * @return List<SitiosClientesDTO>
	 * @author cesar.castano
	 */
	List<SitiosClientesDTO> getSitiosClientes(Predicate predicate);

}
