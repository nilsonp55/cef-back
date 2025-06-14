package com.ath.adminefectivo.delegate.impl;

import java.util.Objects;
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
import com.ath.adminefectivo.entities.Fondos;
import com.ath.adminefectivo.entities.Oficinas;
import com.ath.adminefectivo.entities.Puntos;
import com.ath.adminefectivo.entities.SitiosClientes;
import com.ath.adminefectivo.service.IClientesCorporativosService;
import com.ath.adminefectivo.service.IPuntosService;
import com.querydsl.core.types.Predicate;

@Service
public class PuntosDelegateImpl implements IPuntosDelegate {

  @Autowired
  IPuntosService puntosService;
  
  @Autowired
  IClientesCorporativosService clientesServices;

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
  public PuntosDTO guardarPunto(CreatePuntosDTO createPuntosDTO) {

    Puntos punto = null;
    Puntos puntoResponse = null;
    if (Objects.isNull(createPuntosDTO.getCodigoPunto())) {
      
      // Debe ser igual el bancoAval del ClienteCorporativo con el bancoAval del Punto a crear
      // en caso de no cumplir la validacion, se retorna error
      if (createPuntosDTO.getTipoPunto().equals(Constantes.PUNTO_SITIO_CLIENTE))
        clientesServices.validateClienteBancoAval(createPuntosDTO.getBancoAVAL(), createPuntosDTO.getCodigoCliente());
      
      punto = new Puntos();
      punto.setTipoPunto(createPuntosDTO.getTipoPunto());
      punto.setNombrePunto(createPuntosDTO.getNombrePunto());
      punto.setCodigoCiudad(createPuntosDTO.getCodigoDANE());
      punto.setEstado(createPuntosDTO.getEstado());

      puntoResponse = puntosService.crearPunto(punto);

      // Se genera logica para decidir que tipo de punto se crea
      if (createPuntosDTO.getTipoPunto().equals(Constantes.PUNTO_BANCO)) {
        Bancos banco = new Bancos();
        banco.setNombreBanco(createPuntosDTO.getNombrePunto());
        banco.setCodigoCompensacion(createPuntosDTO.getCodigoCompensacion());
        banco.setNumeroNit(createPuntosDTO.getNumeroNit());
        banco.setAbreviatura(createPuntosDTO.getAbreviatura());
        banco.setEsAVAL(createPuntosDTO.getEsAVAL());

        puntoResponse = puntosService.guardarPuntoBanco(punto, banco);
      }

      if (createPuntosDTO.getTipoPunto().equals(Constantes.PUNTO_CAJERO)) {
        CajerosATM cajero = new CajerosATM();
        cajero.setCodigoATM(createPuntosDTO.getCodigoATM());
        cajero.setTarifaRuteo(createPuntosDTO.getTarifaRuteo());
        cajero.setTarifaVerificacion(createPuntosDTO.getTarifaVerificacion());
        cajero.setBancoAval(createPuntosDTO.getBancoAVAL());

        puntoResponse = puntosService.guardarPuntoCajeroATM(punto, cajero);
      }

      if (createPuntosDTO.getTipoPunto().equals(Constantes.PUNTO_SITIO_CLIENTE)) {
        SitiosClientes sitiosClientes = new SitiosClientes();
        sitiosClientes.setCodigoCliente(createPuntosDTO.getCodigoCliente());
        sitiosClientes.setCodigoPuntoCliente(createPuntosDTO.getCodigoPuntoCliente());
        sitiosClientes.setFajado(createPuntosDTO.getFajado());
        sitiosClientes.setPuntos(puntoResponse);
        
        puntoResponse = puntosService.guardarPuntoSitioCliente(punto, sitiosClientes);
      }

      if (createPuntosDTO.getTipoPunto().equals(Constantes.PUNTO_FONDO)) {
        Fondos fondos = new Fondos();
        fondos.setTdv(createPuntosDTO.getCodigoTDV());
        fondos.setBancoAVAL(createPuntosDTO.getBancoAVAL());
        fondos.setNombreFondo(createPuntosDTO.getNombreFondo());
        fondos.setPuntos(puntoResponse);

        puntoResponse = puntosService.guardarPuntoFondo(puntoResponse, fondos);
      }

      if (createPuntosDTO.getTipoPunto().equals(Constantes.PUNTO_OFICINA)) {
        Oficinas oficina = new Oficinas();
        oficina.setCodigoOficina(createPuntosDTO.getCodigoOficina());
        oficina.setBancoAVAL(createPuntosDTO.getBancoAVAL());
        oficina.setFajado(createPuntosDTO.getFajado());
        oficina.setRefajillado(createPuntosDTO.getRefagillado());
        oficina.setTarifaRuteo(createPuntosDTO.getTarifaRuteo());
        oficina.setTarifaVerificacion(createPuntosDTO.getTarifaVerificacion());

        puntoResponse = puntosService.guardarPuntoOficina(punto, oficina);
      }
    } else { // actualizar datos
      // se valida que el punto exista y mantenga el mismo tipo de punto
      punto = puntosService.validarPuntoActualizar(createPuntosDTO.getCodigoPunto(),
          createPuntosDTO.getTipoPunto());

      punto.setNombrePunto(createPuntosDTO.getNombrePunto());
      punto.setCodigoCiudad(createPuntosDTO.getCodigoDANE());
      punto.setEstado(createPuntosDTO.getEstado());

      // lógica para decidir qué tipo de punto se actualiza
      if (punto.getTipoPunto().equals(Constantes.PUNTO_BANCO)) {
        punto.getBancos().setCodigoCompensacion(createPuntosDTO.getCodigoCompensacion());
        punto.getBancos().setNombreBanco(createPuntosDTO.getNombrePunto());
        punto.getBancos().setNumeroNit(createPuntosDTO.getNumeroNit());
        punto.getBancos().setAbreviatura(createPuntosDTO.getAbreviatura());
        punto.getBancos().setEsAVAL(createPuntosDTO.getEsAVAL());
      }

      if (punto.getTipoPunto().equals(Constantes.PUNTO_CAJERO)) {
        punto.getCajeroATM().setCodigoATM(createPuntosDTO.getCodigoATM());
        punto.getCajeroATM().setTarifaRuteo(createPuntosDTO.getTarifaRuteo());
        punto.getCajeroATM().setTarifaVerificacion(createPuntosDTO.getTarifaVerificacion());
        punto.getCajeroATM().setBancoAval(createPuntosDTO.getBancoAVAL());
      }

      if (createPuntosDTO.getTipoPunto().equals(Constantes.PUNTO_SITIO_CLIENTE)) {
        punto.getSitiosClientes().setCodigoCliente(createPuntosDTO.getCodigoCliente());
        punto.getSitiosClientes().setFajado(createPuntosDTO.getFajado());
      }

      if (punto.getTipoPunto().equals(Constantes.PUNTO_FONDO)) {
        punto.getFondos().setTdv(createPuntosDTO.getCodigoTDV());
        punto.getFondos().setBancoAVAL(createPuntosDTO.getBancoAVAL());
        punto.getFondos().setNombreFondo(createPuntosDTO.getNombreFondo());
      }

      if (punto.getTipoPunto().equals(Constantes.PUNTO_OFICINA)) {
        punto.getOficinas().setCodigoOficina(createPuntosDTO.getCodigoOficina());
        punto.getOficinas().setBancoAVAL(createPuntosDTO.getBancoAVAL());
        punto.getOficinas().setFajado(createPuntosDTO.getFajado());
        punto.getOficinas().setRefajillado(createPuntosDTO.getRefagillado());
        punto.getOficinas().setTarifaRuteo(createPuntosDTO.getTarifaRuteo());
        punto.getOficinas().setTarifaVerificacion(createPuntosDTO.getTarifaVerificacion());
      }
      // se actualizan datos del punto
      puntoResponse = puntosService.crearPunto(punto);
    }
    return PuntosDTO.CONVERTER_DTO.apply(puntoResponse);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PuntosDTO actualizarPunto(CreatePuntosDTO createPuntosDTO) {
    return null;
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
