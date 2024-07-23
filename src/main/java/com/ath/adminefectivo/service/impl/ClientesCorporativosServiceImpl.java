package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ath.adminefectivo.dto.ClientesCorporativosDTO;
import com.ath.adminefectivo.entities.ClientesCorporativos;
import com.ath.adminefectivo.entities.QClientesCorporativos;
import com.ath.adminefectivo.exception.NotFoundException;
import com.ath.adminefectivo.repositories.IClientesCorporativosRepository;
import com.ath.adminefectivo.service.IClientesCorporativosService;
import com.ath.adminefectivo.service.ISitiosClientesService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ClientesCorporativosServiceImpl implements IClientesCorporativosService {

	private final IClientesCorporativosRepository clientesCorporativosRepository;	
	private final ISitiosClientesService sitiosClientesService;
	
	public ClientesCorporativosServiceImpl(@Autowired IClientesCorporativosRepository clientesCorporativosRepository,
			@Autowired ISitiosClientesService sitiosClientesService) {
		this.clientesCorporativosRepository = clientesCorporativosRepository;
		this.sitiosClientesService = sitiosClientesService;
	}

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
	public Page<ClientesCorporativosDTO> listarClientesCorporativos(Predicate predicate, Pageable page,
			String busqueda) {
		log.debug("listarClientesCorporativos - predicate: {} - busqueda: {}", predicate.toString(), busqueda);

		BooleanBuilder builder = new BooleanBuilder().and(predicate);
		if (StringUtils.hasText(busqueda)) {
			builder.and(QClientesCorporativos.clientesCorporativos.nombreCliente.containsIgnoreCase(busqueda))
					.or(QClientesCorporativos.clientesCorporativos.identificacion.containsIgnoreCase(busqueda))
					.or(QClientesCorporativos.clientesCorporativos.codigoCliente.like(busqueda));
		}
		Page<ClientesCorporativos> clientes = clientesCorporativosRepository.findAll(builder, page);
		if(ObjectUtils.isNotEmpty(clientes))
			log.debug("listarClientesCorporativos - totalElements: {}", clientes.getTotalElements());
		return new PageImpl<>(clientes.stream().map(ClientesCorporativosDTO.CONVERTER_DTO).toList(),
				clientes.getPageable(), clientes.getTotalElements());
	}
    
    /**
     * {@inheritDoc}
     */
	@Override
	public ClientesCorporativosDTO getClientesCorporativos(Integer codigoCliente) throws NotFoundException {
		log.debug("getClientesCorporativos - codigoCliente: {}", codigoCliente);
		Optional<ClientesCorporativos> clienteFind = clientesCorporativosRepository.findById(codigoCliente);
		log.debug("getClientesCorporativos - isEmpty: {}", clienteFind.isEmpty());
		return ClientesCorporativosDTO.CONVERTER_DTO.apply(
				clienteFind.orElseThrow(() -> new NotFoundException(ClientesCorporativosServiceImpl.class.getName(),
						codigoCliente.toString())));
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public ClientesCorporativosDTO guardarClientesCorporativos(ClientesCorporativosDTO clientesCorporativos)
			throws NotFoundException {
		log.debug("guardarClientesCorporativos - ClientesCorporativosDTO: {}", clientesCorporativos);
		ClientesCorporativos clienteCorporativoEntity = ClientesCorporativosDTO.CONVERTER_ENTITY
				.apply(clientesCorporativos);
		clienteCorporativoEntity = clientesCorporativosRepository.save(clienteCorporativoEntity);
		log.debug("guardarClientesCorporativos - id: {}", clienteCorporativoEntity.getCodigoCliente());
		return ClientesCorporativosDTO.CONVERTER_DTO.apply(clienteCorporativoEntity);
	}
	
    /**
     * {@inheritDoc}
     */
	@Override
	public ClientesCorporativosDTO actualizarClientesCorporativos(ClientesCorporativosDTO clientesCorporativos)
			throws NotFoundException {
		log.debug("actualizarClientesCorporativos - clientesCorporativos: {}", clientesCorporativos.getCodigoCliente());
		Optional<ClientesCorporativos> clienteFind = clientesCorporativosRepository
				.findById(clientesCorporativos.getCodigoCliente());
		log.debug("actualizarClientesCorporativos - isEmpty: {}", clienteFind.isEmpty());
		clienteFind.ifPresentOrElse(t -> {
			t = ClientesCorporativosDTO.CONVERTER_ENTITY.apply(clientesCorporativos);
			t = clientesCorporativosRepository.save(t);
			log.debug("actualizarClientesCorporativos - save: {}", t.getCodigoCliente());
		}, () -> {
			log.debug("actualizarClientesCorporativos - save error: {}", clientesCorporativos.getCodigoCliente());
			throw new NotFoundException(ClientesCorporativosServiceImpl.class.getName(), clientesCorporativos.getCodigoCliente().toString());
		});
		return clientesCorporativos;
	}
    
    /**
     * {@inheritDoc}
     */
	@Override
	public void eliminarClientesCorporativos(Integer codigoCliente) throws NotFoundException {
		log.debug("eliminarClientesCorporativos - id: {}", codigoCliente);
		Optional<ClientesCorporativos> clienteFind = clientesCorporativosRepository.findById(codigoCliente);
		log.debug("eliminarClientesCorporativos - isEmpty: {}", clienteFind.isEmpty());
		clienteFind.ifPresentOrElse(t -> clientesCorporativosRepository.delete(t), () -> {
			log.debug("eliminarClientesCorporativos - delete error: {}", codigoCliente);
			throw new NotFoundException(ClientesCorporativosServiceImpl.class.getName(), codigoCliente.toString());
		});
	}

}
