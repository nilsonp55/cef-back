package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.ClientesCorporativosDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.ClientesCorporativos;
import com.ath.adminefectivo.entities.SitiosClientes;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.repositories.IClientesCorporativosRepository;
import com.ath.adminefectivo.service.IClientesCorporativosService;
import com.ath.adminefectivo.service.ISitiosClientesService;
import com.querydsl.core.types.Predicate;

@Service
public class ClientesCorporativosServiceImpl implements IClientesCorporativosService {

	@Autowired
	IClientesCorporativosRepository clientesCorporativosRepository;
	
	@Autowired
	ISitiosClientesService sitiosClientesService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ClientesCorporativosDTO> getClientesCorporativos(Predicate predicate) {
		var clientes = clientesCorporativosRepository.findAll(predicate);
		List<ClientesCorporativosDTO> listClientesDto = new ArrayList<>();
		clientes.forEach(entity -> listClientesDto.add(ClientesCorporativosDTO.CONVERTER_DTO.apply(entity)));
		return listClientesDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getCodigoCliente(Integer codigoBanco, String nit) {
		var clientesCorporativos = clientesCorporativosRepository.findByCodigoBancoAvalAndIdentificacion(codigoBanco, nit);
		if (clientesCorporativos == null) {
			throw new AplicationException(ApiResponseCode.ERROR_CLIENTES_CORPORATIVOS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_CLIENTES_CORPORATIVOS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_CLIENTES_CORPORATIVOS_NO_ENCONTRADO.getHttpStatus());
		} else {
			return clientesCorporativos.getCodigoCliente();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean getCodigoPuntoCliente(Integer codigoPunto) {
		Boolean estado = true;
		var sitiosCliente = sitiosClientesService.getCodigoPuntoSitio(codigoPunto);
		if(sitiosCliente != null) {
			var cliente = clientesCorporativosRepository.findByCodigoCliente(sitiosCliente.getCodigoCliente());
			if(cliente == null) {
				estado = false;
			}
		}else {
			estado = false;
		}
		return estado;
	}
}
