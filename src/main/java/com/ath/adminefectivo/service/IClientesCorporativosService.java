package com.ath.adminefectivo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ath.adminefectivo.dto.ClientesCorporativosDTO;
import com.ath.adminefectivo.exception.NotFoundException;
import com.querydsl.core.types.Predicate;

public interface IClientesCorporativosService {

	/**
	 * Servicio encargado de consultar la lista de todos los clientes corporativos filtrados
	 * con el predicado
	 * 
	 * @param predicate
	 * @return List<ClientesCorporativosDTO>
	 * @author cesar.castano
	 */
	List<ClientesCorporativosDTO> getClientesCorporativos(Predicate predicate);
	
	/**
	 * Servicio encargado de consultar el codigo de cliente corporativos
	 * @param Nit
	 * @return Integer
	 * @author cesar.castano
	 */
	Integer getCodigoCliente(Integer codigoBancoAval, String identificacion);
	
	/**
	 * Servicio encargado de consultar si existe el codigo punto del cliente
	 * @param codigoPunto
	 * @return Boolean
	 * @author cesar.castano
	 */
	Boolean getCodigoPuntoCliente(Integer codigoPunto);
	
    /**
     * Servicio encargado de consultar la lista de todos los clientes corporativos filtrados con el
     * predicado y pageable
     * 
     * @param predicate
     * @param pageable
     * @return List<ClientesCorporativosDTO>
     * @author prv_nparra
     */
    Page<ClientesCorporativosDTO> listarClientesCorporativos(Predicate predicate, Pageable page);

    /**
     * Servicio encargado de crear un nuevo clientes corporativos
     * 
     * @param DTO del nuevo clienteCorporativo
     * @return ClientesCorporativosDTO
     * @author prv_nparra
     */
    ClientesCorporativosDTO guardarClientesCorporativos(
        ClientesCorporativosDTO clientesCorporativosDTO);

    /**
     * Servicio encargado de actualizar clientes corporativos existente
     * 
     * @param DTO del nuevo clienteCorporativo
     * @return ClientesCorporativosDTO
     * @author prv_nparra
     */
    ClientesCorporativosDTO actualizarClientesCorporativos(
        ClientesCorporativosDTO clientesCorporativosDTO) throws NotFoundException;

    /**
     * Servicio encargado de eliminar clientes corporativos existente
     * 
     * @param identificador del cliente corporativo
     * @return ClientesCorporativosDTO
     * @author prv_nparra
     */
    void eliminarClientesCorporativos(Integer codigoCliente) throws NotFoundException;
}
