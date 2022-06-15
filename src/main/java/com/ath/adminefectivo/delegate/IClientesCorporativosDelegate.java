package com.ath.adminefectivo.delegate;

import java.util.List;

import com.ath.adminefectivo.dto.ClientesCorporativosDTO;
import com.querydsl.core.types.Predicate;

/**
 * Delegate que expone los servicios referente a los clientes corporativos
 * @author cesar.castano
 */
public interface IClientesCorporativosDelegate {

	/**
	 * Delegate encargado de retornar la lista de todos los clientes corporativos
	 * 
	 * @return List<ClientesCorporativosDTO>
	 * @author cesar.castano
	 */
	List<ClientesCorporativosDTO> getClientesCorporativos(Predicate predicate);
	
}

