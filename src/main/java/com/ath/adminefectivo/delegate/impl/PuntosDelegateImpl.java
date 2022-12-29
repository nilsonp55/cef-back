package com.ath.adminefectivo.delegate.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.delegate.IPuntosDelegate;
import com.ath.adminefectivo.dto.CreatePuntosDTO;
import com.ath.adminefectivo.dto.PuntosDTO;
import com.ath.adminefectivo.entities.Bancos;
import com.ath.adminefectivo.entities.CajerosATM;
import com.ath.adminefectivo.entities.Fondos;
import com.ath.adminefectivo.entities.Oficinas;
import com.ath.adminefectivo.entities.Puntos;
import com.ath.adminefectivo.entities.SitiosClientes;
import com.ath.adminefectivo.service.IDominioService;
import com.ath.adminefectivo.service.IPuntosService;
import com.querydsl.core.types.Predicate;

@Service
public class PuntosDelegateImpl implements IPuntosDelegate{

	@Autowired
	IPuntosService puntosService;
	
	@Autowired
	IDominioService dominioService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<PuntosDTO> getPuntos(Predicate predicate, Pageable page) {
		return puntosService.getPuntos(predicate, page);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PuntosDTO guardarPunto(CreatePuntosDTO createPuntosDTO) {
		//Se crea instanca para persistir el punto
		Puntos punto = new Puntos();
		punto.setTipoPunto(createPuntosDTO.getTipoPunto());
		punto.setNombrePunto(createPuntosDTO.getNombrePunto());
		punto.setCodigoCiudad(createPuntosDTO.getCodigoCiudad());
		
		Puntos puntoResponse = puntosService.crearPunto(punto);
		
		
		//Se genera logica para decidir que tipo de punto se crea
		if(createPuntosDTO.getTipoPunto().equals(Constantes.PUNTO_BANCO)) {
			Bancos banco = new Bancos();
			banco.setCodigoCompensacion(createPuntosDTO.getCodigoCompensacion());
			banco.setNumeroNit(createPuntosDTO.getNumeroNit());
			banco.setAbreviatura(createPuntosDTO.getAbreviatura());
			banco.setEsAVAL(createPuntosDTO.getEsAVAL());
			
			puntoResponse = puntosService.guardarPuntoBanco(punto, banco);
		}
		
		if(createPuntosDTO.getTipoPunto().equals(Constantes.PUNTO_CAJERO)) {
			CajerosATM cajero = new CajerosATM();
			cajero.setCodigoATM(createPuntosDTO.getCodigoATM());
			cajero.setTarifaRuteo(createPuntosDTO.getTarifaRuteo());
			cajero.setTarifaVerificacion(createPuntosDTO.getTarifaVerificacion());
			Bancos bancoAval = new Bancos();
			bancoAval.setCodigoPunto(createPuntosDTO.getBancoAVAL());
			//cajero.setBancoAval(bancoAval);
			
			puntoResponse = puntosService.guardarPuntoCajeroATM(punto, cajero);
		}
		
		if(createPuntosDTO.getTipoPunto().equals(Constantes.PUNTO_SITIO_CLIENTE)) {
			SitiosClientes sitiosClientes = new SitiosClientes();
			sitiosClientes.setCodigoPunto(puntoResponse.getCodigoPunto());
			sitiosClientes.setCodigoCliente(createPuntosDTO.getCodigoCliente());
			sitiosClientes.setFajado(createPuntosDTO.getFajado());
			sitiosClientes.setPuntos(puntoResponse);
			
			puntoResponse = puntosService.guardarPuntoSitioCliente(punto, sitiosClientes);
		}
		
		if(createPuntosDTO.getTipoPunto().equals(Constantes.PUNTO_FONDO)) {
			Fondos fondos = new Fondos();
			fondos.setTdv(createPuntosDTO.getTdv());
			fondos.setBancoAVAL(createPuntosDTO.getBancoAVAL());
			fondos.setNombreFondo(createPuntosDTO.getNombreFondo());
			
			puntoResponse = puntosService.guardarPuntoFondo(punto, fondos);
		}
			
		if(createPuntosDTO.getTipoPunto().equals(Constantes.PUNTO_OFICINA)) {
			Oficinas oficina = new Oficinas();
			oficina.setCodigoOficina(createPuntosDTO.getCodigoOficina());
			Bancos bancoAval = new Bancos();
			bancoAval.setCodigoPunto(createPuntosDTO.getBancoAVAL());
			//oficina.setBancoAval(bancoAval);
			oficina.setFajado(createPuntosDTO.getFajado());
			oficina.setRefagillado(createPuntosDTO.getRefagillado());
			oficina.setTarifaRuteo(createPuntosDTO.getTarifaRuteo());
			oficina.setTarifaVerificacion(createPuntosDTO.getTarifaVerificacion());
			
			puntoResponse = puntosService.guardarPuntoOficina(punto, oficina);
		}
		
		return PuntosDTO.CONVERTER_DTO.apply(puntoResponse);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PuntosDTO actualizarPunto(CreatePuntosDTO rreatePuntosDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PuntosDTO getPuntoById(Integer idPunto) {
		return PuntosDTO.CONVERTER_DTO.apply(puntosService.getPuntoById(idPunto));
	}
}
