package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.dto.PuntosDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.Bancos;
import com.ath.adminefectivo.entities.CajerosATM;
import com.ath.adminefectivo.entities.Fondos;
import com.ath.adminefectivo.entities.Oficinas;
import com.ath.adminefectivo.entities.Puntos;
import com.ath.adminefectivo.entities.QPuntos;
import com.ath.adminefectivo.entities.SitiosClientes;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.exception.ConflictException;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.CajerosATMRepository;
import com.ath.adminefectivo.repositories.IBancosRepository;
import com.ath.adminefectivo.repositories.IFondosRepository;
import com.ath.adminefectivo.repositories.IOficinasRepository;
import com.ath.adminefectivo.repositories.IPuntosRepository;
import com.ath.adminefectivo.repositories.ISitiosClientesRepository;
import com.ath.adminefectivo.service.IPuntosService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class PuntosServiceImpl implements IPuntosService {

  @Autowired
  IPuntosRepository puntosRepository;

  @Autowired
  IBancosRepository bancosRepository;

  @Autowired
  IOficinasRepository oficinasRepository;

  @Autowired
  CajerosATMRepository cajerosATMRepository;

  @Autowired
  ISitiosClientesRepository sitiosClienteRepository;

  @Autowired
  IFondosRepository fondosRepository;

  /**
   * {@inheritDoc}
   */
  @Override
  public Page<PuntosDTO> getPuntos(Predicate predicate, Pageable page, String busqueda) {

    BooleanBuilder builder = new BooleanBuilder();
    builder.and(predicate);
    if (StringUtils.hasText(busqueda)) {
      builder.and(QPuntos.puntos.nombrePunto.containsIgnoreCase(busqueda));
    }
    Pageable paginaOrdenada =
        PageRequest.of(page.getPageNumber(), page.getPageSize(), Sort.by("nombrePunto"));
    Page<Puntos> puntos = puntosRepository.findAll(builder, paginaOrdenada);

    return new PageImpl<>(puntos.getContent().stream().map(PuntosDTO.CONVERTER_DTO).toList(),
        puntos.getPageable(), puntos.getTotalElements());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<PuntosDTO> getPuntos(Predicate predicate) {
    log.info("predicate: {}", predicate.toString());
    var puntos = puntosRepository.findAll(predicate, Sort.by("nombrePunto"));
    List<PuntosDTO> listPuntosDto = new ArrayList<>();
    puntos.forEach(entity -> listPuntosDto.add(PuntosDTO.CONVERTER_DTO.apply(entity)));
    return listPuntosDto;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Puntos getEntidadPunto(String tipoPunto, Integer codigoPunto) {
    var puntosOpt = puntosRepository.findByCodigoPuntoAndTipoPunto(codigoPunto, tipoPunto);
    if (Objects.isNull(puntosOpt)) {
      throw new AplicationException(ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getCode(),
          ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getDescription() + " tipoPunto = " + tipoPunto
              + " codigoPunto = " + codigoPunto,
          ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getHttpStatus());
    }
    return puntosOpt;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getNombrePunto(String tipoPunto, Integer codigoPunto) {
    var puntosOpt = puntosRepository.findByCodigoPuntoAndTipoPunto(codigoPunto, tipoPunto);
    if (Objects.isNull(puntosOpt)) {
      throw new AplicationException(ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getCode(),
          ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getDescription() + " tipoPunto = " + tipoPunto
              + " codigoPunto = " + codigoPunto,
          ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getHttpStatus());
    }
    return puntosOpt.getNombrePunto();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getNombrePunto(Integer codigoPunto) {
    var puntosOpt = puntosRepository.findByCodigoPunto(codigoPunto);
    if (Objects.isNull(puntosOpt)) {
      throw new AplicationException(ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getCode(),
          ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getDescription() + " codigoPunto = "
              + codigoPunto,
          ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getHttpStatus());
    }
    return puntosOpt.getNombrePunto();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getcodigoPunto(String tipoPunto, String nombrePunto) {
    var puntosOpt = puntosRepository.findByTipoPuntoAndNombrePunto(tipoPunto, nombrePunto);
    if (Objects.isNull(puntosOpt)) {
      throw new AplicationException(ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getCode(),
          ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getDescription() + " tipoPunto = " + tipoPunto
              + " nombrePunto = " + nombrePunto,
          ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getHttpStatus());
    }
    return puntosOpt.getCodigoPunto();
  }

  @Override
  public Puntos crearPunto(Puntos punto) {
    return puntosRepository.save(punto);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Puntos guardarPuntoBanco(Puntos punto, Bancos banco) {

    if (banco.getCodigoPunto() != null && bancosRepository.existsById(banco.getCodigoPunto())) {
      throw new ConflictException(ApiResponseCode.ERROR_BANCO_EXIST.getDescription());
    }

    banco.setCodigoPunto(punto.getCodigoPunto());
    banco.setPuntos(punto);
    bancosRepository.save(banco);

    return punto;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Puntos guardarPuntoOficina(Puntos punto, Oficinas oficina) {

    if (oficina.getCodigoPunto() != null
        && oficinasRepository.existsById(oficina.getCodigoPunto())) {
      throw new ConflictException(ApiResponseCode.ERROR_OFICINA_EXIST.getDescription());
    }

    oficina.setCodigoPunto(punto.getCodigoPunto());
    oficinasRepository.save(oficina);

    return punto;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Puntos guardarPuntoCajeroATM(Puntos punto, CajerosATM cajerosATM) {

    if (cajerosATM.getCodigoPunto() != null
        && cajerosATMRepository.existsById(cajerosATM.getCodigoPunto())) {
      throw new ConflictException(ApiResponseCode.ERROR_CAJERO_EXIST.getDescription());
    }

    cajerosATM.setCodigoPunto(punto.getCodigoPunto());
    cajerosATMRepository.save(cajerosATM);

    return punto;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PuntosDTO getPuntoByNombrePunto(String nombrePunto) {
    var punto = puntosRepository.findByNombrePunto(nombrePunto);
    if (!Objects.isNull(punto)) {
      return PuntosDTO.CONVERTER_DTO.apply(punto);
    }
    return null;
  }


  @Override
  public Puntos guardarPuntoSitioCliente(Puntos punto, SitiosClientes sitiosClientes) {

    sitiosClientes.setPuntos(punto);
    sitiosClientes.setCodigoPunto(punto.getCodigoPunto());
    if (sitiosClientes.getCodigoPunto() != null
        && sitiosClienteRepository.existsById(sitiosClientes.getCodigoPunto())) {
      throw new ConflictException(ApiResponseCode.ERROR_SITIO_CLIENTE_EXIST.getDescription());
    }

    sitiosClienteRepository.save(sitiosClientes);

    return punto;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Puntos guardarPuntoFondo(Puntos punto, Fondos fondo) {

    if (fondo.getCodigoPunto() != null && fondosRepository.existsById(fondo.getCodigoPunto())) {
      throw new ConflictException(ApiResponseCode.ERROR_FONDO_EXIST.getDescription());
    }

    fondo.setCodigoPunto(punto.getCodigoPunto());
    fondosRepository.save(fondo);

    return punto;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PuntosDTO getPuntoByTipoPuntoAndCodigoCiudad(String tipoPunto, String codigoCiudad) {
    var punto = puntosRepository.findByTipoPuntoAndCodigoCiudad(tipoPunto, codigoCiudad);
    if (!Objects.isNull(punto)) {
      return PuntosDTO.CONVERTER_DTO.apply(punto);
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Puntos getPuntoById(Integer idPunto) {
    try {
      return puntosRepository.findById(idPunto).get();
    } catch (Exception e) {
      throw new AplicationException(ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getCode(),
          ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getDescription()
              + " no encontrado para idPunto = " + idPunto,
          ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getHttpStatus());
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Boolean getEntidadPuntoBanrep(String tipoPunto, Integer codigoPunto) {
    var puntosOpt = puntosRepository.findByCodigoPuntoAndTipoPunto(codigoPunto, tipoPunto);
    return !Objects.isNull(puntosOpt);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Puntos getEntidadPunto(Integer codigoBancoAval) {
    List<Puntos> puntos = puntosRepository.obtenerCodigoPunto(codigoBancoAval);
    if (Objects.isNull(puntos) || puntos.size() == 0) {
      throw new NegocioException(ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getCode(),
          ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getDescription()
              + " no encontrado para codigoBancoAval = " + codigoBancoAval,
          ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getHttpStatus());
    }
    if( puntos.size() > 1) {
      throw new NegocioException(ApiResponseCode.ERROR_PUNTOS_ENCONTRADOS_REPETIDOS.getCode(),
          ApiResponseCode.ERROR_PUNTOS_ENCONTRADOS_REPETIDOS.getDescription()
              + " varios registros para codigoBancoAval = " + codigoBancoAval,
          ApiResponseCode.ERROR_PUNTOS_ENCONTRADOS_REPETIDOS.getHttpStatus());
    }
    return puntos.get(0);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Puntos validarPuntoActualizar(Integer codigoPunto, String tipoPunto) {

    Optional<Puntos> punto =
        Optional.ofNullable(puntosRepository.findByCodigoPuntoAndTipoPunto(codigoPunto, tipoPunto));
    if (punto.isPresent()) {
      throw new NegocioException(ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getCode(),
          ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getDescription(),
          ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getHttpStatus());
    }

    return punto.get();
  }

  @Override
  public HashMap<Integer, String> getPuntosTipoOficina() {
    HashMap<Integer, String> mapaPuntosTipoOficina = new HashMap<>();
    puntosRepository.findByTipoPunto(Constantes.PUNTO_OFICINA).stream()
        .map(punto -> mapaPuntosTipoOficina.put(punto.getCodigoPunto(), punto.getNombrePunto()));
    return mapaPuntosTipoOficina;
  }

  @Override
  public HashMap<Integer, Puntos> getAllPuntos() {
    HashMap<Integer, Puntos> mapaPuntos = new HashMap<>();
    puntosRepository.findAll().stream().map(p -> mapaPuntos.put(p.getCodigoPunto(), p));
    return mapaPuntos;
  }

}
