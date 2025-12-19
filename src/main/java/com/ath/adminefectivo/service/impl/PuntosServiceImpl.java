package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.dto.PuntosDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.Bancos;
import com.ath.adminefectivo.entities.CajerosATM;
import com.ath.adminefectivo.entities.ClientesCorporativos;
import com.ath.adminefectivo.entities.Fondos;
import com.ath.adminefectivo.entities.Oficinas;
import com.ath.adminefectivo.entities.Puntos;
import com.ath.adminefectivo.entities.QPuntos;
import com.ath.adminefectivo.entities.SitiosClientes;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.exception.NotFoundException;
import com.ath.adminefectivo.repositories.CajerosATMRepository;
import com.ath.adminefectivo.repositories.IBancosRepository;
import com.ath.adminefectivo.repositories.IClientesCorporativosRepository;
import com.ath.adminefectivo.repositories.IFondosRepository;
import com.ath.adminefectivo.repositories.IOficinasRepository;
import com.ath.adminefectivo.repositories.IPuntosRepository;
import com.ath.adminefectivo.repositories.ISitiosClientesRepository;
import com.ath.adminefectivo.repositories.jdbc.IPuntosJdbcRepository;
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
  
  private static final String TIPO_PUNTO = "tipoPunto: ";
  private static final String CODIGO_PUNTO = "codigoPunto: ";
  private static final String NOMBRE_PUNTO = "nombrePunto: ";

  @Autowired
  IPuntosJdbcRepository puntosJdbcRepository;
  
  @Autowired
  IClientesCorporativosRepository clientesCorporativosRepository;

  /**
   * {@inheritDoc}
   */
  @Override
  public Page<PuntosDTO> getPuntos(Predicate predicate, Pageable page, String busqueda) {

    BooleanBuilder builder = new BooleanBuilder();
    builder.and(predicate);
    if ( StringUtils.isNotEmpty(busqueda) ) {
      builder.andAnyOf(QPuntos.puntos.nombrePunto.containsIgnoreCase(busqueda));
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
          ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getDescription() + TIPO_PUNTO + tipoPunto
              + CODIGO_PUNTO + codigoPunto,
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
          ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getDescription() + TIPO_PUNTO + tipoPunto
              + CODIGO_PUNTO + codigoPunto,
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
          ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getDescription() + CODIGO_PUNTO
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
          ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getDescription() + TIPO_PUNTO + tipoPunto
              + NOMBRE_PUNTO + nombrePunto,
          ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getHttpStatus());
    }
    return puntosOpt.getCodigoPunto();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Puntos getCodigoPuntoJdbc(Integer codigoBancoAval) {
    Puntos puntos = puntosJdbcRepository.findPuntoByCodigoAval(codigoBancoAval);
    if (Objects.isNull(puntos)) {
      throw new NegocioException(ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getCode(),
          ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getDescription(), ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getHttpStatus());
    }
    return puntos;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Puntos actualizarPunto(Puntos punto) {
    if( ObjectUtils.isEmpty( punto.getCodigoPunto() ) ) {
      log.error("No se permite actualizar Punto sin valor en codigo_punto, punto: {}", punto.toString());
      throw new NegocioException(ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getCode(),
          ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getDescription(),
          ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getHttpStatus());
    }
    return puntosRepository.save(punto);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Puntos crearPunto(Puntos punto) {
    if( ObjectUtils.isNotEmpty( punto.getCodigoPunto() ) ) {
      log.error("No se permite crear Punto con valor en codigo_punto, punto: {}", punto.toString());
      throw new NegocioException(ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getCode(),
          ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getDescription(),
          ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getHttpStatus());
    }
    
    return puntosRepository.save(punto);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Puntos crearPuntoBanco(Puntos punto, Bancos banco) {

    punto = puntosRepository.save(punto);

    banco.setCodigoPunto(punto.getCodigoPunto());
    bancosRepository.save(banco);

    return punto;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Puntos crearPuntoOficina(Puntos punto, Oficinas oficina) {

    punto = puntosRepository.save(punto);

    oficina.setCodigoPunto(punto.getCodigoPunto());
    oficinasRepository.save(oficina);

    return punto;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Puntos crearPuntoCajeroATM(Puntos punto, CajerosATM cajerosATM) {

    punto = puntosRepository.save(punto);

    cajerosATM.setCodigoPunto(punto.getCodigoPunto());
    cajerosATMRepository.save(cajerosATM);

    return punto;
  }

  @Override
  public Puntos crearPuntoSitioCliente(Puntos punto, SitiosClientes sitiosClientes) {

    punto = puntosRepository.save(punto);
    
    sitiosClientes.setCodigoPunto(punto.getCodigoPunto());
    ClientesCorporativos cliente = this.clientesCorporativosRepository.findByCodigoCliente(sitiosClientes.getCodigoCliente().getCodigoCliente());
    sitiosClientes.setCodigoCliente(cliente);
    sitiosClientes = sitiosClienteRepository.saveAndFlush(sitiosClientes);
    punto.setSitiosClientes(sitiosClientes);
    return punto;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Puntos crearPuntoFondo(Puntos punto, Fondos fondo) {

    punto = puntosRepository.save(punto);

    fondo.setCodigoPunto(punto.getCodigoPunto());
    fondosRepository.save(fondo);

    return punto;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Puntos crearPuntoBanrep(Puntos punto) {

    punto = puntosRepository.save(punto);

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
  public PuntosDTO getPuntoBancoByNombrePuntoTipoPunto(String nombrePunto, String tipoPunto) {
	    var punto = puntosRepository.findByNombrePuntoAndTipoPunto(nombrePunto, tipoPunto);
	    if (!Objects.isNull(punto)) {
	      return PuntosDTO.CONVERTER_DTO.apply(punto);
	    }
	    return null;
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
  public Puntos getPuntoByIdJdbc(Integer idPunto) {
    try {
    	return puntosJdbcRepository.findByCodigoPunto(idPunto);
    } catch (Exception e) {
      throw new AplicationException(ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getCode(),
          ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getDescription(), ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getHttpStatus());
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
  public Boolean getEntidadPuntoBanrepJdbc(String tipoPunto, Integer codigoPunto) {
    return puntosJdbcRepository.existsByTipoPuntoAndCodigoPunto(tipoPunto, codigoPunto);
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

    // Verificar que codigo_punto existe en tabla, en caso contrario lanzar exception
    Puntos punto = puntosRepository.findById(codigoPunto).orElseThrow(() -> {
      throw new NegocioException(ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getCode(),
          ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getDescription(),
          ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getHttpStatus());
    });
    
    // Verificar que TIPO_PUNTO no cambia con respecto al registro existente, en caso
    // contrario se lanza exception
    if( punto.getTipoPunto().equalsIgnoreCase(tipoPunto) ) {
      log.debug("Se valida punto con codigo_punto: {} y tipo punto: {}", codigoPunto, tipoPunto);
    } else {
      throw new NegocioException(ApiResponseCode.ERROR_TIPO_PUNTO_DIFERENTE.getCode(),
          ApiResponseCode.ERROR_TIPO_PUNTO_DIFERENTE.getDescription(),
          ApiResponseCode.ERROR_TIPO_PUNTO_DIFERENTE.getHttpStatus());
    }
        
    return punto;
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

  @Override
  public void eliminarPunto(Integer codigoPunto) throws NegocioException {
    log.debug("Eliminar punto: {}", codigoPunto);
    puntosRepository.findById(codigoPunto).ifPresentOrElse(punto -> {
      puntosRepository.deleteById(codigoPunto);
      log.debug("Punto Eliminado: {}", codigoPunto);
    }, () -> {
      throw new NotFoundException(PuntosServiceImpl.class.getName(), codigoPunto.toString());
    });
    
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void validarPuntoFondoUnique(Puntos p, Fondos f) throws NegocioException {
    List<Puntos> puntosFondo = puntosRepository.findFondoUnique(p.getNombrePunto(),
        p.getCodigoCiudad(), f.getBancoAVAL(), f.getNombreFondo());
    Optional.ofNullable(puntosFondo).ifPresent(puntos -> {
      List<Puntos> listaValidar =
          puntos.stream().filter(t -> t.getCodigoPunto() != p.getCodigoPunto()).toList();
      if (listaValidar.size() > 0)
        throw new NegocioException(ApiResponseCode.ERROR_FONDO_EXIST.getCode(),
            ApiResponseCode.ERROR_FONDO_EXIST.getHttpStatus(),
            ApiResponseCode.ERROR_FONDO_EXIST.getDescription(),
            puntos.stream().map(punto -> new String("Punto fondo existe: ")
                .concat(punto.getCodigoPunto().toString())).toList());
    });
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void validarPuntoBancoUnique(Puntos p, Bancos b) throws NegocioException {

    List<Puntos> PuntosBanco = puntosRepository.findBancoUnique(p.getNombrePunto(),
        p.getCodigoCiudad(), b.getNumeroNit(), b.getAbreviatura(), b.getNombreBanco());
    Optional.ofNullable(PuntosBanco).ifPresent(puntos -> {
      List<Puntos> listaValidar =
          puntos.stream().filter(t -> t.getCodigoPunto() != p.getCodigoPunto()).toList();
      if (listaValidar.size() > 0)
        throw new NegocioException(ApiResponseCode.ERROR_BANCO_EXIST.getCode(),
            ApiResponseCode.ERROR_BANCO_EXIST.getHttpStatus(),
            ApiResponseCode.ERROR_BANCO_EXIST.getDescription(),
            puntos.stream().map(punto -> new String("Punto banco existe: ")
                .concat(punto.getCodigoPunto().toString())).toList());
    });

  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void validarPuntoCajeroUnique(Puntos p, CajerosATM c) throws NegocioException {

    List<Puntos> puntosCajero = puntosRepository.findCajeroUnique(p.getNombrePunto(),
        p.getCodigoCiudad(), c.getBancoAval(), c.getCodigoATM());
    Optional.ofNullable(puntosCajero).ifPresent(puntos -> {
      List<Puntos> listaValidar =
          puntos.stream().filter(t -> t.getCodigoPunto() != p.getCodigoPunto()).toList();
      if (listaValidar.size() > 0)
        throw new NegocioException(ApiResponseCode.ERROR_CAJERO_EXIST.getCode(),
            ApiResponseCode.ERROR_CAJERO_EXIST.getHttpStatus(),
            ApiResponseCode.ERROR_CAJERO_EXIST.getDescription(),
            puntos.stream().map(punto -> new String("Punto cajero existe: ")
                .concat(punto.getCodigoPunto().toString())).toList());
    });
  } 
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void validarPuntoOficinaUnique(Puntos p, Oficinas o) throws NegocioException {
    List<Puntos> puntosOficina = puntosRepository.findOficinaUnique(p.getNombrePunto(),
        p.getCodigoCiudad(), o.getBancoAVAL(), o.getCodigoOficina());
    Optional.ofNullable(puntosOficina).ifPresent(puntos -> {
      List<Puntos> listaValidar =
          puntos.stream().filter(t -> t.getCodigoPunto() != p.getCodigoPunto()).toList();
      if (listaValidar.size() > 0)
        throw new NegocioException(ApiResponseCode.ERROR_OFICINA_EXIST.getCode(),
            ApiResponseCode.ERROR_OFICINA_EXIST.getHttpStatus(),
            ApiResponseCode.ERROR_OFICINA_EXIST.getDescription(),
            puntos.stream().map(punto -> new String("Punto cajero existe: ")
                .concat(punto.getCodigoPunto().toString())).toList());
    });
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void validarPuntoBanrepUnique(Puntos p) throws NegocioException { 
    List<Puntos> puntosBanrep = puntosRepository.findBanrepUnique(p.getNombrePunto(),
        p.getCodigoCiudad());
    Optional.ofNullable(puntosBanrep).ifPresent(puntos -> {
      List<Puntos> listaValidar =
          puntos.stream().filter(t -> t.getCodigoPunto() != p.getCodigoPunto()).toList();
      if (listaValidar.size() > 0)
        throw new NegocioException(ApiResponseCode.ERROR_EXIST_BANREP.getCode(),
            ApiResponseCode.ERROR_EXIST_BANREP.getHttpStatus(),
            ApiResponseCode.ERROR_EXIST_BANREP.getDescription(),
            puntos.stream().map(punto -> new String("Punto banrep existe: ")
                .concat(punto.getCodigoPunto().toString())).toList());
    });
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void validarPuntoClienteUnique(Puntos p, SitiosClientes sc) throws NegocioException {
    List<Puntos> puntosCliente =
        puntosRepository.findSitioClienteUnique(p.getNombrePunto(), p.getCodigoCiudad(),
            sc.getIdentificadorCliente(), sc.getCodigoCliente().getCodigoCliente());
    Optional.ofNullable(puntosCliente).ifPresent(puntos -> {
      List<Puntos> listaValidar = puntos.stream()
          .filter(t -> t.getSitiosClientes().getCodigoCliente().getCodigoBancoAval()
              .equals(sc.getCodigoCliente().getCodigoBancoAval())
              && t.getCodigoPunto() != p.getCodigoPunto())
          .toList();
      if (listaValidar.size() > 0)
        throw new NegocioException(ApiResponseCode.ERROR_SITIO_CLIENTE_EXIST.getCode(),
            ApiResponseCode.ERROR_SITIO_CLIENTE_EXIST.getHttpStatus(),
            ApiResponseCode.ERROR_SITIO_CLIENTE_EXIST.getDescription(),
            puntos.stream().map(punto -> new String("Punto sitio_cliente existe: ")
                .concat(punto.getCodigoPunto().toString())).toList());
    });
  }
  
}
