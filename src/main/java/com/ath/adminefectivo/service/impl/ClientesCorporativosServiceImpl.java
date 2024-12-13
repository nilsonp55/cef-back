package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.ClientesCorporativosDTO;
import com.ath.adminefectivo.repositories.IClientesCorporativosRepository;
import com.ath.adminefectivo.repositories.jdbc.IClientesCorporativosJdbcRepository;
import com.ath.adminefectivo.service.IClientesCorporativosService;
import com.ath.adminefectivo.service.ISitiosClientesService;
import com.querydsl.core.types.Predicate;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ClientesCorporativosServiceImpl implements IClientesCorporativosService {

	@Autowired
	IClientesCorporativosRepository clientesCorporativosRepository;
	
	@Autowired
	IClientesCorporativosJdbcRepository clientesCorporativosJdbcRepository;
	
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
		log.debug("codigoBanco, nit " +codigoBanco+ " -- nit " + nit);
		var clientesCorporativos = clientesCorporativosRepository.findByCodigoBancoAvalAndIdentificacion(codigoBanco, nit);
		if (Objects.isNull(clientesCorporativos)) {
			return clientesCorporativosRepository.findByCodigoBancoAvalAndIdentificacion(codigoBanco, "9999999999").getCodigoCliente();
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
		if(!Objects.isNull(sitiosCliente)) {
			var cliente = clientesCorporativosRepository.findByCodigoCliente(sitiosCliente.getCodigoCliente());
			if(cliente == null) {
				estado = false;
			}
		}else {
			estado = false;
		}
		return estado;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean getCodigoPuntoClienteJdbc(Integer codigoPunto) {
		var sitiosCliente = sitiosClientesService.getCodigoPuntoSitioJdbc(codigoPunto);
		if(Objects.isNull(sitiosCliente)) {
			return false;
		}
		return clientesCorporativosJdbcRepository.existsByCodigoCliente(sitiosCliente.getCodigoCliente());
	}
}
