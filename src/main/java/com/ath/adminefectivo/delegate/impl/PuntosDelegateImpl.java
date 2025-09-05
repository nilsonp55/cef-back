package com.ath.adminefectivo.delegate.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.delegate.IPuntosDelegate;
import com.ath.adminefectivo.dto.CreatePuntosDTO;
import com.ath.adminefectivo.dto.PuntosDTO;
import com.ath.adminefectivo.entities.Bancos;
import com.ath.adminefectivo.entities.CajerosATM;
import com.ath.adminefectivo.entities.ClientesCorporativos;
import com.ath.adminefectivo.entities.Fondos;
import com.ath.adminefectivo.entities.Oficinas;
import com.ath.adminefectivo.entities.Puntos;
import com.ath.adminefectivo.entities.SitiosClientes;
import com.ath.adminefectivo.repositories.IOficinasRepository;
import com.ath.adminefectivo.service.IClientesCorporativosService;
import com.ath.adminefectivo.service.IPuntosService;
import com.querydsl.core.types.Predicate;

@Service
public class PuntosDelegateImpl implements IPuntosDelegate {

  @Autowired
  IPuntosService puntosService;
  
  @Autowired
  IClientesCorporativosService clientesServices;
  
  @Autowired
  IOficinasRepository oficinasRepository;

  /**
   * {@inheritDoc}
   */
  @Override
  public Page<PuntosDTO> getPuntos(Predicate predicate, Pageable page, String busqueda) {
    return puntosService.getPuntos(predicate, page, busqueda);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public PuntosDTO crearPunto(CreatePuntosDTO createPuntosDTO) {

    Puntos puntoResponse = null;

    Puntos punto = new Puntos();
    punto.setTipoPunto(createPuntosDTO.getTipoPunto());
    punto.setNombrePunto(createPuntosDTO.getNombrePunto());
    punto.setCodigoCiudad(createPuntosDTO.getCodigoDANE());
    punto.setEstado(createPuntosDTO.getEstado());

    // puntoResponse = puntosService.crearPunto(punto);

    // Se genera logica para decidir que tipo de punto se crea
    if (createPuntosDTO.getTipoPunto().equals(Constantes.PUNTO_BANCO)) {
      Bancos banco = new Bancos();
      banco.setNombreBanco(createPuntosDTO.getNombrePunto());
      banco.setCodigoCompensacion(createPuntosDTO.getCodigoCompensacion());
      banco.setNumeroNit(createPuntosDTO.getNumeroNit());
      banco.setAbreviatura(createPuntosDTO.getAbreviatura());
      banco.setEsAVAL(createPuntosDTO.getEsAVAL());

      // validar si Banco Existe, se lazan exception si existe
      puntosService.validarPuntoBancoUnique(punto, banco);
      
      puntoResponse = puntosService.crearPuntoBanco(punto, banco);
    }

    if (createPuntosDTO.getTipoPunto().equals(Constantes.PUNTO_CAJERO)) {
      CajerosATM cajero = new CajerosATM();
      cajero.setCodigoATM(createPuntosDTO.getCodigoATM());
      cajero.setTarifaRuteo(createPuntosDTO.getTarifaRuteo());
      cajero.setTarifaVerificacion(createPuntosDTO.getTarifaVerificacion());
      cajero.setBancoAval(createPuntosDTO.getBancoAVAL());

      // validar si Cajero Existe, se lazan exception si existe
      puntosService.validarPuntoCajeroUnique(punto, cajero);
      
      puntoResponse = puntosService.crearPuntoCajeroATM(punto, cajero);
    }

    if (createPuntosDTO.getTipoPunto().equals(Constantes.PUNTO_SITIO_CLIENTE)) {
      // Debe ser igual el bancoAval del ClienteCorporativo con el bancoAval del Punto a crear
      // en caso de no cumplir la validacion, se retorna error
      clientesServices.validateClienteBancoAval(createPuntosDTO.getBancoAVAL(),
            createPuntosDTO.getCodigoCliente());
      
            
      SitiosClientes sitiosClientes = new SitiosClientes();
      ClientesCorporativos cliente = new ClientesCorporativos();
      cliente.setCodigoCliente(createPuntosDTO.getCodigoCliente());
      cliente.setCodigoBancoAval(createPuntosDTO.getBancoAVAL());
      sitiosClientes.setCodigoCliente(cliente);
      sitiosClientes.setIdentificadorCliente(createPuntosDTO.getIdentificadorCliente());
      sitiosClientes.setFajado(createPuntosDTO.getFajado());

      // Verificar si existe un sitio_cliente/cliente igual, se lazan exception si existe
      puntosService.validarPuntoClienteUnique(punto, sitiosClientes);
      
      puntoResponse = puntosService.crearPuntoSitioCliente(punto, sitiosClientes);
    }

    if (createPuntosDTO.getTipoPunto().equals(Constantes.PUNTO_FONDO)) {
      Fondos fondos = new Fondos();
      fondos.setTdv(createPuntosDTO.getCodigoTDV());
      fondos.setBancoAVAL(createPuntosDTO.getBancoAVAL());
      fondos.setNombreFondo(createPuntosDTO.getNombrePunto());

      // Verificar si existe un fondo igual, se lazan exception si existe
      puntosService.validarPuntoFondoUnique(punto, fondos);
      
      puntoResponse = puntosService.crearPuntoFondo(punto, fondos);
    }

    if (createPuntosDTO.getTipoPunto().equals(Constantes.PUNTO_OFICINA)) {
      Oficinas oficina = new Oficinas();
      oficina.setCodigoOficina(createPuntosDTO.getCodigoOficina());
      oficina.setBancoAVAL(createPuntosDTO.getBancoAVAL());
      oficina.setFajado(createPuntosDTO.getFajado());
      oficina.setRefajillado(createPuntosDTO.getRefagillado());
      oficina.setTarifaRuteo(createPuntosDTO.getTarifaRuteo());
      oficina.setTarifaVerificacion(createPuntosDTO.getTarifaVerificacion());
      oficina.setProgramaTransporte(createPuntosDTO.getProgramaTransporte() != null 
              ? createPuntosDTO.getProgramaTransporte()
              : true);

      // validar si Oficina Existe, se lazan exception si existe
      puntosService.validarPuntoOficinaUnique(punto, oficina);
      
      puntoResponse = puntosService.crearPuntoOficina(punto, oficina);
    }
    
    if (createPuntosDTO.getTipoPunto().equals(Constantes.PUNTO_BANC_REP)) {
      // validar si Oficina Existe, se lazan exception si existe
      puntosService.validarPuntoBanrepUnique(punto);
      puntoResponse = puntosService.crearPuntoBanrep(punto);
    }

    return PuntosDTO.CONVERTER_DTO.apply(puntoResponse);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public PuntosDTO actualizarPunto(CreatePuntosDTO createPuntosDTO) {
    
    // se valida que el punto exista y mantenga el mismo tipo de punto
    puntosService.validarPuntoActualizar(createPuntosDTO.getCodigoPunto(),
        createPuntosDTO.getTipoPunto());
    
    // se actualizan datos del punto
    Puntos punto = Puntos.builder()
        .tipoPunto(createPuntosDTO.getTipoPunto())
        .codigoPunto(createPuntosDTO.getCodigoPunto())
        .nombrePunto(createPuntosDTO.getNombrePunto())
        .codigoCiudad(createPuntosDTO.getCodigoDANE())
        .estado(createPuntosDTO.getEstado())
        .build();
        
    // lógica para decidir qué tipo de punto se actualiza
    if (punto.getTipoPunto().equals(Constantes.PUNTO_BANCO)) {
      Bancos banco = Bancos.builder()
          .codigoPunto(createPuntosDTO.getCodigoPunto())
          .codigoCompensacion(createPuntosDTO.getCodigoCompensacion())
          .nombreBanco(createPuntosDTO.getNombrePunto())
          .numeroNit(createPuntosDTO.getNumeroNit())
          .abreviatura(createPuntosDTO.getAbreviatura())
          .esAVAL(createPuntosDTO.getEsAVAL())
          .build();
      punto.setBancos(banco);
      // validar si Banco Existe, se lazan exception si existe
      puntosService.validarPuntoBancoUnique(punto, banco);
    }

    if (punto.getTipoPunto().equals(Constantes.PUNTO_CAJERO)) {
      CajerosATM cajero = CajerosATM.builder()
          .codigoPunto(createPuntosDTO.getCodigoPunto())
          .codigoATM(createPuntosDTO.getCodigoATM())
          .tarifaRuteo(createPuntosDTO.getTarifaRuteo())
          .tarifaVerificacion(createPuntosDTO.getTarifaVerificacion())
          .bancoAval(createPuntosDTO.getBancoAVAL())
          .build();
      punto.setCajeroATM(cajero);
      // validar si Cajero Existe, se lazan exception si existe
      puntosService.validarPuntoCajeroUnique(punto, cajero);
    }

    if (createPuntosDTO.getTipoPunto().equals(Constantes.PUNTO_SITIO_CLIENTE)) {

      SitiosClientes sitiosCliente =
          SitiosClientes.builder().codigoPunto(createPuntosDTO.getCodigoPunto())
              .identificadorCliente(createPuntosDTO.getIdentificadorCliente())
              .fajado(createPuntosDTO.getFajado())
              .codigoCliente(
                  ClientesCorporativos.builder().codigoCliente(createPuntosDTO.getCodigoCliente())
                      .codigoBancoAval(createPuntosDTO.getBancoAVAL()).build())
              .build();

      punto.setSitiosClientes(sitiosCliente);

      // Verificar si existe un sitio_cliente/cliente igual, se lazan exception si existe
      puntosService.validarPuntoClienteUnique(punto, sitiosCliente);
    }

    if (punto.getTipoPunto().equals(Constantes.PUNTO_FONDO)) {
      Fondos fondo = Fondos.builder()
          .codigoPunto(createPuntosDTO.getCodigoPunto())
          .tdv(createPuntosDTO.getCodigoTDV())
          .bancoAVAL(createPuntosDTO.getBancoAVAL())
          .nombreFondo(createPuntosDTO.getNombrePunto())
          .build();      
      punto.setFondos(fondo);
      // Verificar si existe un fondo igual, se lazan exception si existe
      puntosService.validarPuntoFondoUnique(punto, fondo);
    }

    if (punto.getTipoPunto().equals(Constantes.PUNTO_OFICINA)) {
      Oficinas oficina = Oficinas.builder()
          .codigoPunto(createPuntosDTO.getCodigoPunto())
          .codigoOficina(createPuntosDTO.getCodigoOficina())
          .bancoAVAL(createPuntosDTO.getBancoAVAL())
          .fajado(createPuntosDTO.getFajado())
          .refajillado(createPuntosDTO.getRefagillado())
          .tarifaRuteo(createPuntosDTO.getTarifaRuteo())
          .tarifaVerificacion(createPuntosDTO.getTarifaVerificacion())
          .build();
      punto.setOficinas(oficina);
      // validar si Oficina Existe, se lazan exception si existe
      puntosService.validarPuntoOficinaUnique(punto, oficina);
    }
    
    if (punto.getTipoPunto().equals(Constantes.PUNTO_BANC_REP)) {
      // validar si Oficina Existe, se lazan exception si existe
      puntosService.validarPuntoBanrepUnique(punto);
    }
    
    Puntos puntoResponse = puntosService.actualizarPunto(punto);
    
    Oficinas oficinaTrasnporte = oficinasRepository.findByCodigoPunto(createPuntosDTO.getCodigoPunto());
    oficinaTrasnporte.setProgramaTransporte(createPuntosDTO.getProgramaTransporte());
    oficinasRepository.save(oficinaTrasnporte);
    
    return PuntosDTO.CONVERTER_DTO.apply(puntoResponse);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PuntosDTO getPuntoById(Integer idPunto) {
    return PuntosDTO.CONVERTER_DTO.apply(puntosService.getPuntoById(idPunto));
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public void eliminarPunto(Integer idPunto) {
    puntosService.eliminarPunto(idPunto);
  }
}
