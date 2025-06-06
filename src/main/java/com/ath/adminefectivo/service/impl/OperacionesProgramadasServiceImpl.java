package com.ath.adminefectivo.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.ArchivosCargadosDTO;
import com.ath.adminefectivo.dto.DetallesDefinicionArchivoDTO;
import com.ath.adminefectivo.dto.FechasConciliacionDTO;
import com.ath.adminefectivo.dto.LogProcesoDiarioDTO;
import com.ath.adminefectivo.dto.MaestrosDefinicionArchivoDTO;
import com.ath.adminefectivo.dto.OperacionesProgramadasDTO;
import com.ath.adminefectivo.dto.PuntosDTO;
import com.ath.adminefectivo.dto.RegistrosCargadosDTO;
import com.ath.adminefectivo.dto.compuestos.DetalleOperacionesDTO;
import com.ath.adminefectivo.dto.compuestos.OperacionIntradiaDTO;
import com.ath.adminefectivo.dto.compuestos.OperacionesProgramadasNombresDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.Fondos;
import com.ath.adminefectivo.entities.OperacionesProgramadas;
import com.ath.adminefectivo.entities.Puntos;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.ICiudadesRepository;
import com.ath.adminefectivo.repositories.IDetalleOperacionesProgramadasRepository;
import com.ath.adminefectivo.repositories.IOperacionesProgramadasRepository;
import com.ath.adminefectivo.service.IArchivosCargadosService;
import com.ath.adminefectivo.service.IBancosService;
import com.ath.adminefectivo.service.ICajerosService;
import com.ath.adminefectivo.service.ICiudadesService;
import com.ath.adminefectivo.service.IClientesCorporativosService;
import com.ath.adminefectivo.service.IDetalleDefinicionArchivoService;
import com.ath.adminefectivo.service.IDominioService;
import com.ath.adminefectivo.service.IFestivosNacionalesService;
import com.ath.adminefectivo.service.IFondosService;
import com.ath.adminefectivo.service.ILecturaArchivoService;
import com.ath.adminefectivo.service.ILogProcesoDiarioService;
import com.ath.adminefectivo.service.IMaestroDefinicionArchivoService;
import com.ath.adminefectivo.service.IOficinasService;
import com.ath.adminefectivo.service.IOperacionesProgramadasService;
import com.ath.adminefectivo.service.IParametroService;
import com.ath.adminefectivo.service.IPuntosService;
import com.ath.adminefectivo.service.IRegistrosCargadosService;
import com.ath.adminefectivo.service.ITransportadorasService;
import com.ath.adminefectivo.utils.UtilsString;
import com.querydsl.core.types.Predicate;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class OperacionesProgramadasServiceImpl implements IOperacionesProgramadasService {

  @Autowired
  IOperacionesProgramadasRepository operacionesProgramadasRepository;

  @Autowired
  IFondosService fondosService;

  @Autowired
  ITransportadorasService transportadorasService;

  @Autowired
  IPuntosService puntosService;

  @Autowired
  ICiudadesService ciudadService;

  @Autowired
  IOficinasService oficinaService;

  @Autowired
  IDetalleDefinicionArchivoService detalleDefinicionArchivoService;

  @Autowired
  IDetalleOperacionesProgramadasRepository detalleOperacionesProgramadasRepository;

  @Autowired
  IMaestroDefinicionArchivoService maestroDefinicionArchivoService;

  @Autowired
  IRegistrosCargadosService registrosCargadosService;

  @Autowired
  ILecturaArchivoService lecturaArchivoService;

  @Autowired
  IBancosService bancosService;

  @Autowired
  ICajerosService cajerosService;

  @Autowired
  IArchivosCargadosService archivosCargadosService;

  @Autowired
  IClientesCorporativosService clientesCorporativosService;

  @Autowired
  IParametroService parametroService;

  @Autowired
  ILogProcesoDiarioService logProcesoDiarioService;

  @Autowired
  ICiudadesRepository ciudadesRepository;
  
  @Autowired
  IFestivosNacionalesService festivosNacionalesService;

  private IDominioService dominioService;
  private OperacionesProgramadasDTO operaciones;
  private static final String USER1 = "user1";
  private static final String S_SALIDA = "SALIDA";
  private static final String S_ENTRADA = "ENTRADA";
  private static final String S_VENTA = "VENTA";

  public OperacionesProgramadasServiceImpl(IDominioService dominioService) {
    super();
    this.dominioService = dominioService;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Boolean actualizarEstadoEnProgramadas(Integer idOperacion, String estado) {

    Optional<OperacionesProgramadas> operacionesP =
        operacionesProgramadasRepository.findById(idOperacion);
    if (!operacionesP.isPresent()) {
      throw new NegocioException(
          ApiResponseCode.ERROR_OPERACIONES_PROGRAMADAS_NO_ENCONTRADO.getCode(),
          ApiResponseCode.ERROR_OPERACIONES_PROGRAMADAS_NO_ENCONTRADO.getDescription(),
          ApiResponseCode.ERROR_OPERACIONES_PROGRAMADAS_NO_ENCONTRADO.getHttpStatus());
    }
    try {
      operacionesP.get().setEstadoConciliacion(estado);
      operacionesP.get().setFechaModificacion(new Date());
      operacionesP.get().setUsuarioModificacion("user1");
      operacionesProgramadasRepository.save(operacionesP.get());
    } catch (Exception e) {
      e.getMessage();
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Page<OperacionesProgramadasNombresDTO> getNombresProgramadasConciliadas(
      Page<OperacionesProgramadas> operacionesProgramadasList, Predicate predicate, Pageable page) {


    for (OperacionesProgramadas programadas : operacionesProgramadasList) {

      try {
        programadas.getConciliacionServicios().get(0);
        // Obtiene nombres de transportadora y Banco dueño del fondo
        programadas.setNombreTransportadora(programadas.getTdv());
        programadas.setNombreBanco(programadas.getBancoAVAL());
        // Obtiene nombres de tipo origen y nombre ciudad origen
        Puntos punto = puntosService.getPuntoById(programadas.getCodigoPuntoOrigen());
        programadas.setNombrePuntoOrigen(punto.getNombrePunto());
        programadas.setNombreCiudadOrigen(
            ciudadService.getCiudadPorCodigoDane(punto.getCodigoCiudad()).getNombreCiudad());
        // Obtiene nombres de tipo destino y nombre ciudad destino
        punto = puntosService.getPuntoById(programadas.getCodigoPuntoDestino());
        programadas.setNombrePuntoDestino(punto.getNombrePunto());
        programadas.setNombreCiudadDestino(
            ciudadService.getCiudadPorCodigoDane(punto.getCodigoCiudad()).getNombreCiudad());
        programadas.setFechaEjecucion(programadas.getFechaOrigen());
        // Obtiene datos de la tabla de Conciliacion Servicios
        programadas.setTipoConciliacion(
            programadas.getConciliacionServicios().get(0).getTipoConciliacion());
        programadas
            .setIdConciliacion(programadas.getConciliacionServicios().get(0).getIdConciliacion());
        punto = puntosService.getPuntoById(programadas.getCodigoFondoTDV());
        programadas.setNombreFondoTDV(punto.getNombrePunto());
      } catch (Exception e) {
        log.error("Failed getConciliacionServicios(): {} - {}", programadas.getIdOperacion(), e);
      }
    }
    return new PageImpl<>(
        operacionesProgramadasList.getContent().stream()
            .map(OperacionesProgramadasNombresDTO.CONVERTER_DTO)
            .collect(Collectors.<OperacionesProgramadasNombresDTO>toList()),
        page, operacionesProgramadasList.getTotalElements());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer numeroOperacionesPorEstadoyFecha(FechasConciliacionDTO fechaConciliacion,
      String estado) {
    Integer cuentaProgramadas =
        operacionesProgramadasRepository.countByEstadoConciliacionAndFechaOrigenBetween(estado,
            fechaConciliacion.getFechaConciliacionInicial(),
            fechaConciliacion.getFechaConciliacionFinal());
    if (Objects.isNull(cuentaProgramadas)) {
      throw new NegocioException(
          ApiResponseCode.ERROR_OPERACIONES_PROGRAMADAS_NO_ENCONTRADO.getCode(),
          ApiResponseCode.ERROR_OPERACIONES_PROGRAMADAS_NO_ENCONTRADO.getDescription(),
          ApiResponseCode.ERROR_OPERACIONES_PROGRAMADAS_NO_ENCONTRADO.getHttpStatus());
    }
    return cuentaProgramadas;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<OperacionesProgramadasDTO> generarOperacionesProgramadas(
      List<ArchivosCargadosDTO> archivos) {
    List<OperacionesProgramadasDTO> listadoOperacionesProgramadas = new ArrayList<>();
    archivos.forEach(archivo -> {
    	
      List<OperacionesProgramadasDTO> listaOperacionesProgramadas =
          this.procesarRegistrosCargadosArchivo(archivo);

      listadoOperacionesProgramadas.addAll(listaOperacionesProgramadas);

      if (listaOperacionesProgramadas.isEmpty()) {
        throw new NegocioException(ApiResponseCode.ERROR_CREACION_OPERACION_PROGRAMADA.getCode(),
            ApiResponseCode.ERROR_CREACION_OPERACION_PROGRAMADA.getDescription(),
            ApiResponseCode.ERROR_CREACION_OPERACION_PROGRAMADA.getHttpStatus());
      }
    });
    return listadoOperacionesProgramadas;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<OperacionesProgramadas> obtenerOperacionesProgramadas() {
    List<OperacionesProgramadas> operacionesp =
        operacionesProgramadasRepository.conciliacionAutomatica(dominioService.valorTextoDominio(
            Constantes.DOMINIO_ESTADO_CONCILIACION, Dominios.ESTADO_CONCILIACION_NO_CONCILIADO));
    if (Objects.isNull(operacionesp)) {
      throw new NegocioException(
          ApiResponseCode.ERROR_OPERACIONES_A_CONCILIAR_NO_ENCONTRADO.getCode(),
          ApiResponseCode.ERROR_OPERACIONES_A_CONCILIAR_NO_ENCONTRADO.getDescription(),
          ApiResponseCode.ERROR_OPERACIONES_A_CONCILIAR_NO_ENCONTRADO.getHttpStatus());
    }
    return operacionesp;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public OperacionesProgramadas obtenerEntidadOperacionesProgramadasporId(Integer idOperacion) {
    OperacionesProgramadas operacionesP =
        operacionesProgramadasRepository.findById(idOperacion).get();
    if (Objects.isNull(operacionesP)) {
      throw new NegocioException(
          ApiResponseCode.ERROR_OPERACIONES_PROGRAMADAS_NO_ENCONTRADO.getCode(),
          ApiResponseCode.ERROR_OPERACIONES_PROGRAMADAS_NO_ENCONTRADO.getDescription(),
          ApiResponseCode.ERROR_OPERACIONES_PROGRAMADAS_NO_ENCONTRADO.getHttpStatus());
    }
    return operacionesP;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<OperacionesProgramadasDTO> getOperacionesProgramadasPorFechas(String tipoContabilidad,
      Date fechaInicio, Date fechaFin) {
    List<OperacionesProgramadas> listaOperacionesProgramadas = new ArrayList<>();
    if (tipoContabilidad.equals("PM")) {
      listaOperacionesProgramadas.addAll(
          operacionesProgramadasRepository.findByTipoOperacionAndFechaOrigenBetweenAndEsCambio(
              Dominios.TIPO_OPERA_CONSIGNACION, fechaInicio, fechaFin, false));
      listaOperacionesProgramadas.addAll(
          operacionesProgramadasRepository.findByTipoOperacionAndFechaOrigenBetweenAndEsCambio(
              Dominios.TIPO_OPERA_RETIRO, fechaInicio, fechaFin, false));
      listaOperacionesProgramadas.addAll(
          operacionesProgramadasRepository.findByTipoOperacionAndFechaOrigenBetweenAndEsCambio(
              Dominios.TIPO_OPERA_VENTA, fechaInicio, fechaFin, false));
    } else if (tipoContabilidad.equals("AM")) {
      Date fechaProceso = parametroService.valorParametroDate(Constantes.FECHA_DIA_PROCESO);
      LogProcesoDiarioDTO logProcesoDiarioDTO =
          logProcesoDiarioService.obtenerEntidadLogProcesoDiarioByCodigoAndFecha(
              Dominios.CODIGO_PROCESO_LOG_CONCILIACION, fechaProceso);
      if (Objects.isNull(logProcesoDiarioDTO)) {
        throw new NegocioException(ApiResponseCode.ERROR_LOGPROCESODIARIO_NO_ENCONTRADO.getCode(),
            ApiResponseCode.ERROR_LOGPROCESODIARIO_NO_ENCONTRADO.getDescription(),
            ApiResponseCode.ERROR_LOGPROCESODIARIO_NO_ENCONTRADO.getHttpStatus());
      }
      // CONFORMAN UN CAMBIO (RETIRO Y CONSIGNACION ESCAMBIO=TRUE) conciliada
      listaOperacionesProgramadas.addAll(operacionesProgramadasRepository
          .findByTipoOperacionAndFechaOrigenBetweenAndEsCambioAndEstadoConciliacion(
              Dominios.TIPO_OPERA_CONSIGNACION, fechaInicio, fechaFin, true,
              Dominios.ESTADO_CONCILIACION_CONCILIADO));
      listaOperacionesProgramadas.addAll(operacionesProgramadasRepository
          .findByTipoOperacionAndFechaOrigenBetweenAndEsCambioAndEstadoConciliacion(
              Dominios.TIPO_OPERA_RETIRO, fechaInicio, fechaFin, true,
              Dominios.ESTADO_CONCILIACION_CONCILIADO));

      // CONFORMAN UN CAMBIO (RETIRO Y CONSIGNACION ESCAMBIO=TRUE) pospuesta
      listaOperacionesProgramadas.addAll(operacionesProgramadasRepository
          .findByTipoOperacionAndFechaOrigenBetweenAndEsCambioAndEstadoConciliacion(
              Dominios.TIPO_OPERA_CONSIGNACION, fechaInicio, fechaFin, true,
              Dominios.ESTADO_CONCILIACION_POSPUESTA));
      listaOperacionesProgramadas.addAll(operacionesProgramadasRepository
          .findByTipoOperacionAndFechaOrigenBetweenAndEsCambioAndEstadoConciliacion(
              Dominios.TIPO_OPERA_RETIRO, fechaInicio, fechaFin, true,
              Dominios.ESTADO_CONCILIACION_POSPUESTA));

      listaOperacionesProgramadas.addAll(operacionesProgramadasRepository
          .findByTipoOperacionAndFechaOrigenBetweenAndEsCambioAndEstadoConciliacion(
              Dominios.TIPO_OPERA_TRASLADO, fechaInicio, fechaFin, false,
              Dominios.ESTADO_CONCILIACION_CONCILIADO));
      listaOperacionesProgramadas.addAll(operacionesProgramadasRepository
          .findByTipoOperacionAndFechaOrigenBetweenAndEsCambioAndEstadoConciliacion(
              Dominios.TIPO_OPERA_TRASLADO, fechaInicio, fechaFin, false,
              Dominios.ESTADO_CONCILIACION_POSPUESTA));

      listaOperacionesProgramadas.addAll(operacionesProgramadasRepository
          .findByTipoOperacionAndFechaOrigenBetweenAndEsCambioAndEstadoConciliacion(
              Dominios.TIPO_OPERA_INTERCAMBIO, fechaInicio, fechaFin, false,
              Dominios.ESTADO_CONCILIACION_CONCILIADO));
      listaOperacionesProgramadas.addAll(operacionesProgramadasRepository
          .findByTipoOperacionAndFechaOrigenBetweenAndEsCambioAndEstadoConciliacion(
              Dominios.TIPO_OPERA_INTERCAMBIO, fechaInicio, fechaFin, false,
              Dominios.ESTADO_CONCILIACION_POSPUESTA));
    }
    List<OperacionesProgramadasDTO> listadoOperacionesProgramadasDTO = new ArrayList<>();
    if (!listaOperacionesProgramadas.isEmpty()) {
      listaOperacionesProgramadas.forEach(operacionProgramada -> listadoOperacionesProgramadasDTO
          .add(OperacionesProgramadasDTO.CONVERTER_DTO.apply(operacionProgramada)));
    }
    return listadoOperacionesProgramadasDTO;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<OperacionIntradiaDTO> consultarOperacionesIntradia(Date fechaInicio, Date fechaFin) {
    List<OperacionIntradiaDTO> operacionesProgramadas = operacionesProgramadasRepository
        .consultaOperacionesIntradiaSalida(fechaInicio, fechaFin, S_SALIDA, S_VENTA);
    operacionesProgramadas.addAll(operacionesProgramadasRepository
        .consultaOperacionesIntradiaEntrada(fechaInicio, fechaFin, S_ENTRADA, S_VENTA));
    return operacionesProgramadas;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<OperacionesProgramadasDTO> obtenerOperacionesProgramadasConErroresContables(
      String tipoContabilidad) {
    List<OperacionesProgramadas> listadoOperacionesProgramadas =
        operacionesProgramadasRepository.obtenerConErroresContables(tipoContabilidad);
    List<OperacionesProgramadasDTO> listadoOperacionesProgramadasDTO = new ArrayList<>();

    listadoOperacionesProgramadas.forEach(operacion -> listadoOperacionesProgramadasDTO
        .add(OperacionesProgramadasDTO.CONVERTER_DTO.apply(operacion)));
    return listadoOperacionesProgramadasDTO;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public String reabrirCierrePorAgrupador(String agrupador) {
    if (agrupador.equals(Dominios.AGRUPADOR_DEFINICION_ARCHIVOS_CERTIFICACION)) {
      return operacionesProgramadasRepository.reabrirCertificaciones();
    }
    if (agrupador.equals(Dominios.AGRUPADOR_DEFINICION_ARCHIVOS_DEFINITIVO)) {
      return operacionesProgramadasRepository.reabrirDefinitiva();
    }
    if (agrupador.equals(Dominios.AGRUPADOR_DEFINICION_ARCHIVOS_PRELIMINARES)) {
      return operacionesProgramadasRepository.reabrirPreliminar();
    }
    if (agrupador.equals(Dominios.AGRUPADOR_DEFINICION_ARCHIVOS_CONCILIACION)) {
      return operacionesProgramadasRepository.reabrirConciliaciones();
    }
    return "Agrupador no existente. ";
  }

  /**
   * ------------------------------------------------- INICIO METODOS PRIVADOS
   * --------------------------------------------------------------------
   */

  /**
   * Metodo encargado de realizar las consultas necesarias del archivo recibido para iniciar con el
   * proceso de generacion de las operaciones programadas
   * 
   * @param archivo
   * @return List<OperacionesProgramadasDTO>
   * @author duvan.naranjo
   */
  @Transactional
  private List<OperacionesProgramadasDTO> procesarRegistrosCargadosArchivo(
      ArchivosCargadosDTO archivo) {
    List<OperacionesProgramadasDTO> listadoOperacionesProgramadas = new ArrayList<>();

    MaestrosDefinicionArchivoDTO maestrosDefinicionArchivoDTO = maestroDefinicionArchivoService
        .consultarDefinicionArchivoById(archivo.getIdModeloArchivo());

    String delimitador =
        lecturaArchivoService.obtenerDelimitadorArchivo(maestrosDefinicionArchivoDTO);

    List<RegistrosCargadosDTO> listadoRegistrosCargados =
        registrosCargadosService.consultarRegistrosCargadosPorIdArchivo(archivo.getIdArchivo());

    List<DetallesDefinicionArchivoDTO> listadoDetalleArchivo = detalleDefinicionArchivoService
        .consultarDetalleDefinicionArchivoByIdMaestro(archivo.getIdModeloArchivo());

    if (!listadoRegistrosCargados.isEmpty() && !Objects.isNull(listadoDetalleArchivo)) {
      listadoRegistrosCargados.forEach(
          registroCargado -> listadoOperacionesProgramadas.add(this.procesarRegistroCargado(
              registroCargado.getContenido().split(delimitador), listadoDetalleArchivo, archivo)));
      archivo.setEstadoCargue(Dominios.ESTADO_VALIDACION_ACEPTADO);
      archivosCargadosService.actualizarArchivosCargados(archivo);
    }
    return listadoOperacionesProgramadas;
  }

  /**
   * Funcion encargada de obtener el tipo de operacion y llamar la función encargada de realizar
   * cada operacion
   * 2024-09-11 Actualizacion HU006 No liquida costos para banco de la republica (consignacion, retiro,cambio)
   * @param contenido
   * @param detalleArchivo
   * @param archivo
   * @return OperacionesProgramadasDTO
   * @author duvan.naranjo
   */
  private OperacionesProgramadasDTO procesarRegistroCargado(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detalleArchivo, ArchivosCargadosDTO archivo) {

    OperacionesProgramadasDTO operacionProgramada = null;
    String tipoServicio = contenido[this.obtenerNumeroCampoTipoServ(detalleArchivo)];
    
    if (archivo.getIdModeloArchivo().equals(Dominios.TIPO_ARCHIVO_IPPSV)
        && tipoServicio.toUpperCase().trim().contains(Dominios.TIPO_OPERA_CONSIGNACION)) {
      operacionProgramada =
          this.generarOperacionConsignacion(contenido, detalleArchivo, archivo, false);
    } else if (archivo.getIdModeloArchivo().equals(Dominios.TIPO_ARCHIVO_IPPSV)
        && tipoServicio.toUpperCase().trim().contains(Dominios.TIPO_OPERA_RETIRO)) {
      operacionProgramada = this.generarOperacionRetiro(contenido, detalleArchivo, archivo, false);
    } else if (archivo.getIdModeloArchivo().equals(Dominios.TIPO_ARCHIVO_IPPSV)
        && tipoServicio.toUpperCase().trim().contains(Dominios.TIPO_OPERA_VENTA)) {
      operacionProgramada = this.generarOperacionVenta(contenido, detalleArchivo, archivo);
    } else if (archivo.getIdModeloArchivo().equals(Dominios.TIPO_ARCHIVO_ISTRC)
        && tipoServicio.toUpperCase().trim().contains(Dominios.TIPO_OPERA_INTERCAMBIO)) {
      operacionProgramada = this.generarOperacionIntercambio(contenido, detalleArchivo, archivo);
    } else if (archivo.getIdModeloArchivo().equals(Dominios.TIPO_ARCHIVO_ISTRC)
        && tipoServicio.toUpperCase().trim().contains(Dominios.TIPO_OPERA_TRASLADO)) {
      operacionProgramada = this.generarOperacionTraslado(contenido, detalleArchivo, archivo);
    } else if (archivo.getIdModeloArchivo().equals(Dominios.TIPO_ARCHIVO_ISTRC)
        && tipoServicio.toUpperCase().trim().startsWith(Dominios.TIPO_OPERA_CAMBIO)) {
      operacionProgramada = this.generarOperacionCambio(contenido, detalleArchivo, archivo);
    } else if (archivo.getIdModeloArchivo().equals(Dominios.TIPO_ARCHIVO_ISRPO)) {
      operacionProgramada = this.procesarArchivoOficinas(contenido, detalleArchivo, archivo);
    } else if (archivo.getIdModeloArchivo().equals(Dominios.TIPO_ARCHIVO_ISRPC)) {
      operacionProgramada = this.procesarArchivoCajeros(contenido, detalleArchivo, archivo);
    }
    return operacionProgramada;
  }

  /**
   * Funcion encargada de obtener el numero de campo del tipo registro del detalle del archivo
   * 
   * @param detallesArchivo
   * @return int
   * @author duvan.naranjo
   */
  private int obtenerNumeroCampoTipoServ(List<DetallesDefinicionArchivoDTO> detallesArchivo) {
    DetallesDefinicionArchivoDTO detalle =
        detallesArchivo.stream().filter(deta -> deta.getNombreCampo().toUpperCase().trim()
            .equals(Constantes.CAMPO_ARCHIVO_TIPO_SERVICIO)).findFirst().orElse(null);
    if (!Objects.isNull(detalle)) {
      return detalle.getId().getNumeroCampo() - 1;
    }
    return 1;
  }

  /**
   * Funcion encargada de realizar la logica de la operacion consignacion
   * 
   * @param contenido
   * @param detalleArchivo
   * @param archivo
   * @param esCambio
   * @return OperacionesProgramadasDTO
   * @author duvan.naranjo
   */
  private OperacionesProgramadasDTO generarOperacionConsignacion(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detalleArchivo, ArchivosCargadosDTO archivo,
      boolean esCambio) {

    OperacionesProgramadasDTO operacionesProgramadasDTO = new OperacionesProgramadasDTO();
    PuntosDTO puntoFondoOrigen = this.consultarPuntoPorDetalle(contenido, detalleArchivo,
        Constantes.CAMPO_DETALLE_ARCHIVO_FONDO_ORIGEN);
    PuntosDTO puntoBancoDestino = this.consultarPuntoBanRepPorDetalle(contenido,
        detalleArchivo, Constantes.CAMPO_DETALLE_ARCHIVO_FONDO_DESTINO);
    
    String tipoOperacion = contenido[this.obtenerNumeroCampoTipoServ(detalleArchivo)];
  
	if (!Objects.isNull(puntoFondoOrigen)
			&& !puntoFondoOrigen.getTipoPunto().toUpperCase().trim().equals(Constantes.PUNTO_FONDO)) {
		throw new NegocioException(ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getCode(),
				ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getDescription() + " no encontrado para fondo origen = "
						+ contenido,
				ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getHttpStatus());
	}
	
	if (!esCambio && !Objects.isNull(puntoBancoDestino)
			&& !puntoBancoDestino.getTipoPunto().toUpperCase().trim().equals(Constantes.PUNTO_BANC_REP)) {
		throw new NegocioException(
				ApiResponseCode.ERROR_NO_ES_BANREP.getCode(), ApiResponseCode.ERROR_NO_ES_BANREP.getDescription()
						+ " no encontrado para fondo destino = " + contenido,
				ApiResponseCode.ERROR_NO_ES_BANREP.getHttpStatus());
	}

    if (Objects.isNull(puntoBancoDestino) && esCambio) {
      puntoBancoDestino = puntoFondoOrigen;
    }

	if (!Objects.isNull(puntoFondoOrigen) && !Objects.isNull(puntoBancoDestino)) {
		operacionesProgramadasDTO = OperacionesProgramadasDTO.builder()
				.codigoFondoTDV(puntoFondoOrigen.getCodigoPunto()).entradaSalida(Constantes.VALOR_SALIDA)
				.codigoPuntoOrigen(puntoFondoOrigen.getCodigoPunto())
				.codigoPuntoDestino(puntoBancoDestino.getCodigoPunto())
				.idArchivoCargado(Math.toIntExact(archivo.getIdArchivo()))
				.liquidaCosto(Dominios.VERDADERO)
				.build();

		operacionesProgramadasDTO.setTipoOperacion(Dominios.TIPO_OPERA_CONSIGNACION);
		if (tipoOperacion.trim().endsWith(Dominios.TIPO_OPERACION_TERMINA_CCE))
			operacionesProgramadasDTO.setLiquidaCosto(Dominios.FALSO);
	}
    
    var operacionProgramadaEnt = operacionesProgramadasRepository
        .save(OperacionesProgramadasDTO.CONVERTER_ENTITY.apply(this.completarOperacionesProgramadas(
            operacionesProgramadasDTO, contenido, detalleArchivo)));

    return OperacionesProgramadasDTO.CONVERTER_DTO.apply(operacionProgramadaEnt);
  }

  /**
   * Funcion encargada de realizar la logica de la operacion retiro
   * 
   * @param contenido
   * @param detalleArchivo
   * @param archivo
   * @param esCambio
   * @return OperacionesProgramadasDTO
   * @author duvan.naranjo
   */
  private OperacionesProgramadasDTO generarOperacionRetiro(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detalleArchivo, ArchivosCargadosDTO archivo,
      boolean esCambio) {
    log.info("operacion retiro archivo: {}, esCambio: {}", archivo.getNombreArchivo(), esCambio);
    OperacionesProgramadasDTO operacionesProgramadasDTO = new OperacionesProgramadasDTO();

    String tipoOperacion = contenido[this.obtenerNumeroCampoTipoServ(detalleArchivo)];
    
    PuntosDTO puntoFondoDestino = this.consultarPuntoPorDetalle(contenido,
        detalleArchivo, Constantes.CAMPO_DETALLE_ARCHIVO_FONDO_DESTINO);
    PuntosDTO puntoBancoOrigen = this.consultarPuntoBanRepPorDetalle(contenido,
        detalleArchivo, Constantes.CAMPO_DETALLE_ARCHIVO_FONDO_ORIGEN);

	if (!esCambio && !Objects.isNull(puntoBancoOrigen)
			&& !puntoBancoOrigen.getTipoPunto().toUpperCase().trim().equals(Constantes.PUNTO_BANC_REP)) {
		throw new NegocioException(ApiResponseCode.ERROR_NO_ES_BANREP.getCode(),
				ApiResponseCode.ERROR_NO_ES_BANREP.getDescription(),
				ApiResponseCode.ERROR_NO_ES_BANREP.getHttpStatus());
	}
    
	if (!Objects.isNull(puntoFondoDestino)
			&& !puntoFondoDestino.getTipoPunto().toUpperCase().trim().equals(Constantes.PUNTO_FONDO)) {
		throw new NegocioException(ApiResponseCode.ERROR_NO_ES_FONDO.getCode(),
				ApiResponseCode.ERROR_NO_ES_FONDO.getDescription(), ApiResponseCode.ERROR_NO_ES_FONDO.getHttpStatus());
	}

    if (Objects.isNull(puntoBancoOrigen) && esCambio) {
      puntoBancoOrigen = puntoFondoDestino;
    }

    Integer valorComisionBR = 0;
    String cobroBR =
        this.consultarValorCobroBR(contenido, detalleArchivo, Constantes.CAMPO_DETALLE_COBROBR);
    if (!Objects.isNull(cobroBR) && "NO".equals(cobroBR)) {
      // consultar parametro que tiene valor de la comisi�n
      valorComisionBR = dominioService
          .valorNumericoDominio(Constantes.DOMINIO_COMISIONES, Dominios.COMISION_1).intValue();
    }

	if (!Objects.isNull(puntoBancoOrigen) && !Objects.isNull(puntoFondoDestino)) {
		operacionesProgramadasDTO = OperacionesProgramadasDTO.builder()
				.codigoFondoTDV(puntoFondoDestino.getCodigoPunto()).entradaSalida(Constantes.VALOR_ENTRADA)
				.codigoPuntoOrigen(puntoBancoOrigen.getCodigoPunto())
				.codigoPuntoDestino(puntoFondoDestino.getCodigoPunto())
				.idArchivoCargado(Math.toIntExact(archivo.getIdArchivo())).comisionBR(valorComisionBR)
				.liquidaCosto(Dominios.VERDADERO)
				.build();

		operacionesProgramadasDTO.setTipoOperacion(Dominios.TIPO_OPERA_RETIRO);
		if (tipoOperacion.trim().endsWith(Dominios.TIPO_OPERACION_TERMINA_CCE))
			operacionesProgramadasDTO.setLiquidaCosto(Dominios.FALSO);
	}
    
    var operacionProgramadaEnt = operacionesProgramadasRepository
        .save(OperacionesProgramadasDTO.CONVERTER_ENTITY.apply(this.completarOperacionesProgramadas(
            operacionesProgramadasDTO, contenido, detalleArchivo)));
    return OperacionesProgramadasDTO.CONVERTER_DTO.apply(operacionProgramadaEnt);
  }

  /**
   * Metodo encargado de realizar leer del detalle si se cobra o no comicion BanRep.
   * 
   * @param contenido
   * @param detalle
   * @return PuntosDTO
   * @author rparra
   */
  private String consultarValorCobroBR(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detallesArchivo, String nombreCampo) {
    DetallesDefinicionArchivoDTO detalle = detallesArchivo.stream()
        .filter(deta -> deta.getNombreCampo().toUpperCase().equals(nombreCampo)).findFirst()
        .orElse(null);
    if (!Objects.isNull(detalle)) {
      return contenido[detalle.getId().getNumeroCampo() - 1].trim();
    }
    return null;
  }

  /**
   * Funcion encargada de realizar la logica de la operacion venta
   * 
   * @param contenido
   * @param detalleArchivo
   * @param archivo
   * @return OperacionesProgramadasDTO
   * @author duvan.naranjo
   */
  private OperacionesProgramadasDTO generarOperacionVenta(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detalleArchivo, ArchivosCargadosDTO archivo) {

    OperacionesProgramadasDTO operacionesProgramadasDTO = null;

    PuntosDTO puntoFondoOrigen = this.consultarPuntoPorDetalle(contenido,
        detalleArchivo, Constantes.CAMPO_DETALLE_ARCHIVO_FONDO_ORIGEN);
    PuntosDTO bancoOrigen = new PuntosDTO();
    PuntosDTO bancoDestino = null;
    var operacionProgramadaEnt = new OperacionesProgramadas();
	if (Objects.isNull(puntoFondoOrigen)) {
		bancoOrigen = this.consultarPuntoPorDetalle(contenido, detalleArchivo,
				Constantes.CAMPO_DETALLE_ARCHIVO_ENTIDAD_ORIGEN);
	}

    PuntosDTO puntoFondoDestino = this.consultarPuntoPorDetalle(contenido, detalleArchivo,
        Constantes.CAMPO_DETALLE_ARCHIVO_FONDO_DESTINO);

    if (Objects.isNull(puntoFondoDestino)) {
      bancoDestino = this.consultarPuntoPorDetalle(contenido, detalleArchivo,
          Constantes.CAMPO_DETALLE_ARCHIVO_ENTIDAD_DESTINO);
    }


    if (!Objects.isNull(puntoFondoOrigen)
        && !puntoFondoOrigen.getTipoPunto().toUpperCase().trim().equals(Constantes.PUNTO_FONDO)) {
      throw new NegocioException(ApiResponseCode.ERROR_NO_ES_FONDO.getCode(),
          ApiResponseCode.ERROR_NO_ES_FONDO.getDescription(),
          ApiResponseCode.ERROR_NO_ES_FONDO.getHttpStatus());
    }

    if (Objects.isNull(puntoFondoOrigen) && !Objects.isNull(puntoFondoDestino) && !Objects.isNull(bancoOrigen)) {
        operacionesProgramadasDTO = OperacionesProgramadasDTO.builder()
            .codigoFondoTDV(puntoFondoDestino.getCodigoPunto())
            .entradaSalida(Constantes.VALOR_ENTRADA).codigoPuntoOrigen(bancoOrigen.getCodigoPunto())
            .codigoPuntoDestino(puntoFondoDestino.getCodigoPunto())
            .idArchivoCargado(Math.toIntExact(archivo.getIdArchivo()))
            .liquidaCosto(Dominios.VERDADERO)
            .build();
        operacionProgramadaEnt = operacionesProgramadasRepository.save(
            OperacionesProgramadasDTO.CONVERTER_ENTITY.apply(this.completarOperacionesProgramadas(
                operacionesProgramadasDTO, contenido, detalleArchivo)));

    } else if (!Objects.isNull(puntoFondoOrigen) && !Objects.isNull(bancoDestino)) {
      if (ObjectUtils.isEmpty(puntoFondoOrigen.getCodigoPunto())) {
        throw new NegocioException(ApiResponseCode.ERROR_NO_ES_FONDO.getCode(),
            ApiResponseCode.ERROR_NO_ES_FONDO.getDescription(),
            ApiResponseCode.ERROR_NO_ES_FONDO.getHttpStatus());
      }
      operacionesProgramadasDTO = OperacionesProgramadasDTO.builder()
          .codigoFondoTDV(puntoFondoOrigen.getCodigoPunto()).entradaSalida(Constantes.VALOR_SALIDA)
          .codigoPuntoOrigen(puntoFondoOrigen.getCodigoPunto())
          .codigoPuntoDestino(bancoDestino.getCodigoPunto())
          .idArchivoCargado(Math.toIntExact(archivo.getIdArchivo()))
          .liquidaCosto(Dominios.VERDADERO)
          .build();
      operacionProgramadaEnt = operacionesProgramadasRepository.save(
          OperacionesProgramadasDTO.CONVERTER_ENTITY.apply(this.completarOperacionesProgramadas(
              operacionesProgramadasDTO, contenido, detalleArchivo)));
    } else if(!Objects.isNull(puntoFondoOrigen) && !Objects.isNull(puntoFondoDestino)) {
      // EN CASO DE QUE LOS DOS PUNTOS FONDOS NO SEAN NULL ES PORQUE AMBOS SON AVAL
      operacionesProgramadasDTO = OperacionesProgramadasDTO.builder()
          .codigoFondoTDV(puntoFondoOrigen.getCodigoPunto()).entradaSalida(Constantes.VALOR_SALIDA)
          .codigoPuntoOrigen(puntoFondoOrigen.getCodigoPunto())
          .codigoPuntoDestino(puntoFondoDestino.getCodigoPunto())
          .idArchivoCargado(Math.toIntExact(archivo.getIdArchivo()))
          .liquidaCosto(Dominios.VERDADERO)
          .build();

      OperacionesProgramadasDTO operacionesProgramadasEntradaDTO =
          OperacionesProgramadasDTO.builder().codigoFondoTDV(puntoFondoDestino.getCodigoPunto())
              .entradaSalida(Constantes.VALOR_ENTRADA)
              .codigoPuntoOrigen(puntoFondoOrigen.getCodigoPunto())
              .codigoPuntoDestino(puntoFondoDestino.getCodigoPunto())
              .idArchivoCargado(Math.toIntExact(archivo.getIdArchivo())).build();

      OperacionesProgramadas operacionProgramadaEntradaEnt = operacionesProgramadasRepository.save(
          OperacionesProgramadasDTO.CONVERTER_ENTITY.apply(this.completarOperacionesProgramadas(
              operacionesProgramadasEntradaDTO, contenido, detalleArchivo)));
      operacionesProgramadasDTO.setIdOperacionRelac(operacionProgramadaEntradaEnt.getIdOperacion());

      operacionProgramadaEnt = operacionesProgramadasRepository.save(
          OperacionesProgramadasDTO.CONVERTER_ENTITY.apply(this.completarOperacionesProgramadas(
              operacionesProgramadasDTO, contenido, detalleArchivo)));

    }

    return OperacionesProgramadasDTO.CONVERTER_DTO.apply(operacionProgramadaEnt);

  }


  /**
   * Funcion encargada de realizar la logica de la operacion cambio
   * 
   * @param contenido
   * @param detallesArchivo
   * @param archivo
   * @return OperacionesProgramadasDTO
   * @author duvan.naranjo
   */
  private OperacionesProgramadasDTO generarOperacionCambio(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detallesArchivo, ArchivosCargadosDTO archivo) {

    OperacionesProgramadasDTO operacionesProgramadaConsignacion = new OperacionesProgramadasDTO();
    try {
      operacionesProgramadaConsignacion =
          this.generarOperacionConsignacion(contenido, detallesArchivo, archivo, true);
      var codigoPuntoOrigen = this.consultarBancoPorCiudad(contenido, detallesArchivo,
          Constantes.CAMPO_DETALLE_ARCHIVO_FONDO_ORIGEN);
      operacionesProgramadaConsignacion.setCodigoPuntoDestino(codigoPuntoOrigen);
      OperacionesProgramadasDTO operacionesProgramadaRetiro =
          this.generarOperacionRetiro(contenido, detallesArchivo, archivo, true);
      var codigoPuntoDestino = this.consultarBancoPorCiudad(contenido, detallesArchivo,
          Constantes.CAMPO_DETALLE_ARCHIVO_FONDO_DESTINO);
      operacionesProgramadaRetiro.setCodigoPuntoOrigen(codigoPuntoDestino);
      operacionesProgramadaRetiro.setEsCambio(true);
      operacionesProgramadaConsignacion.setEsCambio(true);
      operacionesProgramadaRetiro.setTipoOperacion(Dominios.TIPO_OPERA_RETIRO);
      operacionesProgramadaConsignacion.setTipoOperacion(Dominios.TIPO_OPERA_CONSIGNACION);
      operacionesProgramadaConsignacion
          .setIdOperacionRelac(operacionesProgramadaRetiro.getIdOperacion());

      operacionesProgramadasRepository
          .save(OperacionesProgramadasDTO.CONVERTER_ENTITY.apply(operacionesProgramadaRetiro));
      log.info("paso operacionesProgramadaRetiro: {}", operacionesProgramadaRetiro.toString());
      operacionesProgramadasRepository.save(
          OperacionesProgramadasDTO.CONVERTER_ENTITY.apply(operacionesProgramadaConsignacion));
      log.info("paso operacionesProgramadaConsignacion: {}", operacionesProgramadaConsignacion);

    } catch (Exception e) {
      log.info("generarOperacionCambio: {}", e.getMessage());
      throw e;
    }

    return operacionesProgramadaConsignacion;
  }

  /**
   * Funcion encargada de realizar la logica de la operacion intercambio
   * 
   * @param contenido
   * @param detallesArchivo
   * @param archivo
   * @return OperacionesProgramadasDTO
   * @author duvan.naranjo
   */
  private OperacionesProgramadasDTO generarOperacionIntercambio(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detallesArchivo, ArchivosCargadosDTO archivo) {

    PuntosDTO bancoOrigen = this.consultarPuntoPorDetalle(contenido, detallesArchivo,
        Constantes.CAMPO_DETALLE_ARCHIVO_ENTIDAD_ORIGEN);

    PuntosDTO bancoDestino = this.consultarPuntoPorDetalle(contenido, detallesArchivo,
        Constantes.CAMPO_DETALLE_ARCHIVO_ENTIDAD_DESTINO);

    OperacionesProgramadasDTO operacionesProgramadasIntercambio1 = null;
    // SI LOS DOS BANCOS SON AVAL
    if (Objects.isNull(bancoOrigen) && Objects.isNull(bancoDestino)) {
      operacionesProgramadasIntercambio1 =
          this.generarOperacionIntercambioSalida(contenido, detallesArchivo, archivo, true);

      OperacionesProgramadasDTO operacionesProgramadasIntercambio2 =
          this.generarOperacionIntercambioEntrada(contenido, detallesArchivo, archivo, true);

      operacionesProgramadasIntercambio2 = OperacionesProgramadasDTO.CONVERTER_DTO
          .apply(operacionesProgramadasRepository.save(OperacionesProgramadasDTO.CONVERTER_ENTITY
              .apply(operacionesProgramadasIntercambio2)));

      operacionesProgramadasIntercambio1
          .setIdOperacionRelac(operacionesProgramadasIntercambio2.getIdOperacion());

      operacionesProgramadasRepository.save(
          OperacionesProgramadasDTO.CONVERTER_ENTITY.apply(operacionesProgramadasIntercambio1));
    } else // SI EL BANCO ORIGEN ES AVAL
    if (Objects.isNull(bancoOrigen)) {
      if (Objects.isNull(bancoDestino)) {
        throw new NegocioException(ApiResponseCode.ERROR_BANCO_EXTERNO_NO_ENCONTRADO.getCode(),
            ApiResponseCode.ERROR_BANCO_EXTERNO_NO_ENCONTRADO.getDescription(),
            ApiResponseCode.ERROR_BANCO_EXTERNO_NO_ENCONTRADO.getHttpStatus());
      }
      operacionesProgramadasIntercambio1 =
          this.generarOperacionIntercambioSalida(contenido, detallesArchivo, archivo, false);
      operacionesProgramadasRepository.save(
          OperacionesProgramadasDTO.CONVERTER_ENTITY.apply(operacionesProgramadasIntercambio1));
    } else {
      // SI EL BANCO DESTINO ES AVAL Y EL ORIGEN NO ES AVAL
      operacionesProgramadasIntercambio1 =
          this.generarOperacionIntercambioEntrada(contenido, detallesArchivo, archivo, false);
      operacionesProgramadasRepository.save(
          OperacionesProgramadasDTO.CONVERTER_ENTITY.apply(operacionesProgramadasIntercambio1));
    }



    return operacionesProgramadasIntercambio1;
  }

  /**
   * Funcion encargada de realizar la logica de la operacion traslado
   * 
   * @param contenido
   * @param detallesArchivo
   * @param archivo
   * @return OperacionesProgramadasDTO
   * @author duvan.naranjo
   */
  private OperacionesProgramadasDTO generarOperacionTraslado(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detallesArchivo, ArchivosCargadosDTO archivo) {

    if (!this.esTrasladoMismoBanco(contenido, detallesArchivo)) {
      throw new NegocioException(ApiResponseCode.ERROR_NO_ES_IGUAL_BANCO.getCode(),
          ApiResponseCode.ERROR_NO_ES_IGUAL_BANCO.getDescription(),
          ApiResponseCode.ERROR_NO_ES_IGUAL_BANCO.getHttpStatus());
    }

    OperacionesProgramadasDTO operacionesProgramadasTraslado1 =
        this.generarOperacionTraslado1(contenido, detallesArchivo, archivo);

    OperacionesProgramadasDTO operacionesProgramadasTraslado2 =
        this.generarOperacionTraslado2(contenido, detallesArchivo, archivo);

    operacionesProgramadasTraslado1
        .setIdOperacionRelac(operacionesProgramadasTraslado2.getIdOperacion());

    operacionesProgramadasRepository
        .save(OperacionesProgramadasDTO.CONVERTER_ENTITY.apply(operacionesProgramadasTraslado1));

    return operacionesProgramadasTraslado1;
  }

  /**
   * Metodo encargado de realizar la logica del registro de operaciones programadas para la salida
   * por parte de un traslado
   * 
   * @param contenido
   * @param detallesArchivo
   * @param archivo
   * @return OperacionesProgramadasDTO
   * @author duvan.naranjo
   */
  private OperacionesProgramadasDTO generarOperacionTraslado1(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detallesArchivo, ArchivosCargadosDTO archivo) {

    OperacionesProgramadasDTO operacionesProgramadasDTO = null;
    PuntosDTO puntoFondoOrigen = this.consultarPuntoPorDetalle(contenido, detallesArchivo,
        Constantes.CAMPO_DETALLE_ARCHIVO_FONDO_ORIGEN);

    PuntosDTO puntoFondoDestino = this.consultarPuntoPorDetalle(contenido, detallesArchivo,
        Constantes.CAMPO_DETALLE_ARCHIVO_FONDO_DESTINO);

    if (!Objects.isNull(puntoFondoOrigen)
        && !puntoFondoOrigen.getTipoPunto().toUpperCase().trim().equals(Constantes.PUNTO_FONDO)) {
      throw new AplicationException(ApiResponseCode.ERROR_NO_ES_FONDO.getCode(),
          ApiResponseCode.ERROR_NO_ES_FONDO.getDescription(),
          ApiResponseCode.ERROR_NO_ES_FONDO.getHttpStatus());
    }
    if (!Objects.isNull(puntoFondoDestino)
        && !puntoFondoDestino.getTipoPunto().toUpperCase().trim().equals(Constantes.PUNTO_FONDO)) {
      throw new AplicationException(ApiResponseCode.ERROR_NO_ES_FONDO.getCode(),
          ApiResponseCode.ERROR_NO_ES_FONDO.getDescription(),
          ApiResponseCode.ERROR_NO_ES_FONDO.getHttpStatus());
    }

    operacionesProgramadasDTO = OperacionesProgramadasDTO.builder()
        .codigoFondoTDV(puntoFondoOrigen.getCodigoPunto()).entradaSalida(Constantes.VALOR_SALIDA)
        .codigoPuntoOrigen(puntoFondoOrigen.getCodigoPunto())
        .codigoPuntoDestino(puntoFondoDestino.getCodigoPunto())
        .idArchivoCargado(Math.toIntExact(archivo.getIdArchivo()))
        .liquidaCosto(Dominios.VERDADERO)
        .build();

    return this.completarOperacionesProgramadas(operacionesProgramadasDTO, contenido,
        detallesArchivo);
  }

  /**
   * Metodo encargado de realizar la logica del registro de operaciones programadas para la entrada
   * por parte de un traslado
   * 
   * @param contenido
   * @param detallesArchivo
   * @param archivo
   * @return OperacionesProgramadasDTO
   * @author duvan.naranjo
   */
  private OperacionesProgramadasDTO generarOperacionTraslado2(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detallesArchivo, ArchivosCargadosDTO archivo) {

    OperacionesProgramadasDTO operacionesProgramadasDTO = null;
    PuntosDTO puntoFondoOrigen = this.consultarPuntoPorDetalle(contenido, detallesArchivo,
        Constantes.CAMPO_DETALLE_ARCHIVO_FONDO_ORIGEN);

    PuntosDTO puntoFondoDestino = this.consultarPuntoPorDetalle(contenido, detallesArchivo,
        Constantes.CAMPO_DETALLE_ARCHIVO_FONDO_DESTINO);

    operacionesProgramadasDTO = OperacionesProgramadasDTO.builder()
        .codigoFondoTDV(puntoFondoDestino.getCodigoPunto()).entradaSalida(Constantes.VALOR_ENTRADA)
        .codigoPuntoOrigen(puntoFondoOrigen.getCodigoPunto())
        .codigoPuntoDestino(puntoFondoDestino.getCodigoPunto())
        .idArchivoCargado(Math.toIntExact(archivo.getIdArchivo()))
        .liquidaCosto(Dominios.VERDADERO)
        .build();

    OperacionesProgramadas traslado = operacionesProgramadasRepository
        .save(OperacionesProgramadasDTO.CONVERTER_ENTITY.apply(this.completarOperacionesProgramadas(
            operacionesProgramadasDTO, contenido, detallesArchivo)));
    return OperacionesProgramadasDTO.CONVERTER_DTO.apply(traslado);
  }

  /**
   * Metodo encargado de realizar la logica del registro de operaciones programadas para la salida
   * por parte de un traslado
   * 
   * @param contenido
   * @param detallesArchivo
   * @param archivo
   * @return OperacionesProgramadasDTO
   * @author duvan.naranjo
   */
  private OperacionesProgramadasDTO generarOperacionIntercambioSalida(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detallesArchivo, ArchivosCargadosDTO archivo,
      boolean esAval) {

    OperacionesProgramadasDTO operacionesProgramadasDTO = null;
    PuntosDTO puntoFondoOrigen = this.consultarPuntoPorDetalle(contenido, detallesArchivo,
        Constantes.CAMPO_DETALLE_ARCHIVO_FONDO_ORIGEN);

    PuntosDTO puntoFondoDestino = this.consultarPuntoPorDetalle(contenido, detallesArchivo,
        Constantes.CAMPO_DETALLE_ARCHIVO_FONDO_DESTINO);

    if (!Objects.isNull(puntoFondoOrigen)
        && !puntoFondoOrigen.getTipoPunto().toUpperCase().trim().equals(Constantes.PUNTO_FONDO)) {
      throw new NegocioException(ApiResponseCode.ERROR_NO_ES_FONDO.getCode(),
          ApiResponseCode.ERROR_NO_ES_FONDO.getDescription(),
          ApiResponseCode.ERROR_NO_ES_FONDO.getHttpStatus());
    }
    if (!Objects.isNull(puntoFondoDestino)
        && !puntoFondoDestino.getTipoPunto().toUpperCase().trim().equals(Constantes.PUNTO_FONDO)) {
      throw new NegocioException(ApiResponseCode.ERROR_NO_ES_FONDO.getCode(),
          ApiResponseCode.ERROR_NO_ES_FONDO.getDescription(),
          ApiResponseCode.ERROR_NO_ES_FONDO.getHttpStatus());
    }
    int codigoPuntoDestino;
    if (esAval) {
      codigoPuntoDestino = puntoFondoDestino.getCodigoPunto();
    } else {
      PuntosDTO puntoEntidadDestino = this.consultarPuntoPorDetalle(contenido, detallesArchivo,
          Constantes.CAMPO_DETALLE_ARCHIVO_ENTIDAD_DESTINO);
      if (Objects.isNull(puntoEntidadDestino)) {
        throw new NegocioException(ApiResponseCode.ERROR_BANCO_EXTERNO_NO_ENCONTRADO.getCode(),
            ApiResponseCode.ERROR_BANCO_EXTERNO_NO_ENCONTRADO.getDescription(),
            ApiResponseCode.ERROR_BANCO_EXTERNO_NO_ENCONTRADO.getHttpStatus());
      }
      codigoPuntoDestino = puntoEntidadDestino.getCodigoPunto();
    }

    operacionesProgramadasDTO = OperacionesProgramadasDTO.builder()
        .codigoFondoTDV(puntoFondoOrigen.getCodigoPunto()).entradaSalida(Constantes.VALOR_SALIDA)
        .codigoPuntoOrigen(puntoFondoOrigen.getCodigoPunto()).codigoPuntoDestino(codigoPuntoDestino)
        .idArchivoCargado(Math.toIntExact(archivo.getIdArchivo()))
        .liquidaCosto(Dominios.VERDADERO)
        .build();

    return this.completarOperacionesProgramadas(operacionesProgramadasDTO, contenido,
        detallesArchivo);
  }

  /**
   * Metodo encargado de realizar la logica del registro de operaciones programadas para la entrada
   * por parte de un traslado
   * 
   * @param contenido
   * @param detallesArchivo
   * @param archivo
   * @return OperacionesProgramadasDTO
   * @author duvan.naranjo
   */
  private OperacionesProgramadasDTO generarOperacionIntercambioEntrada(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detallesArchivo, ArchivosCargadosDTO archivo,
      boolean esAval) {

    OperacionesProgramadasDTO operacionesProgramadasDTO = null;
    PuntosDTO puntoFondoDestino = this.consultarPuntoPorDetalle(contenido, detallesArchivo,
        Constantes.CAMPO_DETALLE_ARCHIVO_FONDO_DESTINO);

    PuntosDTO puntoFondoOrigen = this.consultarPuntoPorDetalle(contenido, detallesArchivo,
        Constantes.CAMPO_DETALLE_ARCHIVO_FONDO_ORIGEN);

    if (!Objects.isNull(puntoFondoDestino)
        && !puntoFondoDestino.getTipoPunto().toUpperCase().trim().equals(Constantes.PUNTO_FONDO)) {
      throw new AplicationException(ApiResponseCode.ERROR_NO_ES_FONDO.getCode(),
          ApiResponseCode.ERROR_NO_ES_FONDO.getDescription(),
          ApiResponseCode.ERROR_NO_ES_FONDO.getHttpStatus());
    }

    int codigoPuntoOrigen;
    int codigoPuntoDestino = puntoFondoDestino.getCodigoPunto();
    if (esAval) {
      codigoPuntoOrigen = puntoFondoOrigen.getCodigoPunto();
    } else {
      PuntosDTO puntoEntidadOrigen = this.consultarPuntoPorDetalle(contenido, detallesArchivo,
          Constantes.CAMPO_DETALLE_ARCHIVO_ENTIDAD_ORIGEN);
      if (Objects.isNull(puntoEntidadOrigen)) {
        throw new NegocioException(ApiResponseCode.ERROR_BANCO_EXTERNO_NO_ENCONTRADO.getCode(),
            ApiResponseCode.ERROR_BANCO_EXTERNO_NO_ENCONTRADO.getDescription(),
            ApiResponseCode.ERROR_BANCO_EXTERNO_NO_ENCONTRADO.getHttpStatus());
      }
      codigoPuntoOrigen = puntoEntidadOrigen.getCodigoPunto();
    }
    puntoFondoOrigen = puntoFondoDestino;

    operacionesProgramadasDTO = OperacionesProgramadasDTO.builder()
        .codigoFondoTDV(puntoFondoOrigen.getCodigoPunto()).entradaSalida(Constantes.VALOR_ENTRADA)
        .codigoPuntoOrigen(codigoPuntoOrigen).codigoPuntoDestino(codigoPuntoDestino)
        .idArchivoCargado(Math.toIntExact(archivo.getIdArchivo()))
        .liquidaCosto(Dominios.VERDADERO)
        .build();

    return this.completarOperacionesProgramadas(operacionesProgramadasDTO, contenido,
        detallesArchivo);
  }

  /**
   * Funcion encargada de realizar la validación si la entidad destino es igual a la entidad origen
   * 
   * @param contenido
   * @param detallesArchivo
   * @return boolean
   * @author duvan.naranjo
   */
  private boolean esTrasladoMismoBanco(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detallesArchivo) {
    DetallesDefinicionArchivoDTO entidadOrigen =
        detallesArchivo.stream().filter(deta -> deta.getNombreCampo().toUpperCase()
            .equals(Constantes.CAMPO_DETALLE_ARCHIVO_ENTIDAD_ORIGEN)).findFirst().orElse(null);
    DetallesDefinicionArchivoDTO entidadDestino =
        detallesArchivo.stream().filter(deta -> deta.getNombreCampo().toUpperCase()
            .equals(Constantes.CAMPO_DETALLE_ARCHIVO_ENTIDAD_DESTINO)).findFirst().orElse(null);

    if (!Objects.isNull(entidadOrigen) && !Objects.isNull(entidadDestino)) {
      String bancoOrigen = contenido[entidadOrigen.getId().getNumeroCampo() - 1].trim();
      String bancoDestino = contenido[entidadDestino.getId().getNumeroCampo() - 1].trim();
      if (bancoOrigen.equals(bancoDestino)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Metodo encargado de realizar el llenado de campos de operaciones programadas provenientes del
   * archivo cargado
   * HU006 Cuando tipoOperacion contine "CCE" no debe liquidar costos liquidaCosto = false
   * @param operacionesProgramadasDTO
   * @param contenido
   * @param detalleArchivo
   * @return OperacionesProgramadasDTO
   * @author duvan.naranjo
   */
  private OperacionesProgramadasDTO completarOperacionesProgramadas(
      OperacionesProgramadasDTO operacionesProgramadasDTO, String[] contenido,
      List<DetallesDefinicionArchivoDTO> detalleArchivo) {

    String fechaProgramacion = contenido[detalleArchivo.stream()
        .filter(deta -> deta.getNombreCampo().toUpperCase().trim()
            .equals(Constantes.CAMPO_DETALLE_ARCHIVO_FECHAPROGRAMACION))
        .findFirst().orElse(null).getId().getNumeroCampo() - 1].trim();

    String fechaOrigen = contenido[detalleArchivo.stream()
        .filter(deta -> deta.getNombreCampo().toUpperCase().trim()
            .equals(Constantes.CAMPO_DETALLE_ARCHIVO_FECHAORIGEN))
        .findFirst().orElse(null).getId().getNumeroCampo() - 1].trim();
    String fechaDestino = contenido[detalleArchivo.stream()
        .filter(deta -> deta.getNombreCampo().toUpperCase().trim()
            .equals(Constantes.CAMPO_DETALLE_ARCHIVO_FECHADESTINO))
        .findFirst().orElse(null).getId().getNumeroCampo() - 1].trim();
    String tipoOperacion = contenido[detalleArchivo.stream()
        .filter(deta -> deta.getNombreCampo().toUpperCase().trim()
            .equals(Constantes.CAMPO_DETALLE_ARCHIVO_TIPOSERVICIO))
        .findFirst().orElse(null).getId().getNumeroCampo() - 1].trim();

    Double valorTotal = Double.parseDouble(contenido[detalleArchivo.stream()
        .filter(deta -> deta.getNombreCampo().toUpperCase().trim()
            .equals(Constantes.CAMPO_DETALLE_ARCHIVO_VALORTOTAL))
        .findFirst().orElse(null).getId().getNumeroCampo() - 1].trim());

    DetallesDefinicionArchivoDTO idNegociacion =
        detalleArchivo.stream().filter(deta -> deta.getNombreCampo().toUpperCase().trim()
            .equals(Constantes.CAMPO_DETALLE_ARCHIVO_IDNEGOCIACION)).findFirst().orElse(null);
    String idNegoc = null;
    if (!Objects.isNull(idNegociacion)) {
      idNegoc = contenido[idNegociacion.getId().getNumeroCampo() - 1].trim();
    }

    String tasaNegociacion = contenido[detalleArchivo.stream()
        .filter(deta -> deta.getNombreCampo().toUpperCase().trim()
            .equals(Constantes.CAMPO_DETALLE_ARCHIVO_TASA))
        .findFirst().orElse(null).getId().getNumeroCampo() - 1].trim();

    List<String> listaDominioFecha = new ArrayList<>();
    listaDominioFecha.add("MM/dd/yyyy");

    operacionesProgramadasDTO
        .setFechaProgramacion(UtilsString.convertirFecha(fechaProgramacion, listaDominioFecha));
    operacionesProgramadasDTO
        .setFechaOrigen(UtilsString.convertirFecha(fechaOrigen, listaDominioFecha));
    operacionesProgramadasDTO
        .setFechaDestino(UtilsString.convertirFecha(fechaDestino, listaDominioFecha));
  
    operacionesProgramadasDTO.setTipoOperacion(this.obtenerTipoOperacion(tipoOperacion));
    operacionesProgramadasDTO.setValorTotal(valorTotal);

    Integer comisionBR = operacionesProgramadasDTO.getComisionBR();
    if (!Objects.isNull(comisionBR)) {
      operacionesProgramadasDTO.setComisionBR((int) ((valorTotal * comisionBR) / 10000));
    } else {
      operacionesProgramadasDTO.setComisionBR(0);
    }
    operacionesProgramadasDTO.setIdNegociacion(idNegoc);
    operacionesProgramadasDTO.setTasaNegociacion(tasaNegociacion);
    operacionesProgramadasDTO.setEstadoOperacion(dominioService.valorTextoDominio(
        Constantes.DOMINIO_ESTADOS_OPERACION, Dominios.ESTADOS_OPERA_PROGRAMADO));

    operacionesProgramadasDTO.setEstadoConciliacion(dominioService.valorTextoDominio(
        Constantes.DOMINIO_ESTADO_CONCILIACION, Dominios.ESTADO_CONCILIACION_NO_CONCILIADO));
    operacionesProgramadasDTO.setTipoServicio(dominioService
        .valorTextoDominio(Constantes.DOMINIO_TIPO_SERVICIO, Dominios.TIPO_SERVICIO_PROGRAMADA));
    operacionesProgramadasDTO.setUsuarioCreacion("ATH");
    operacionesProgramadasDTO.setFechaCreacion(new Date());
    operacionesProgramadasDTO.setEsCambio(false);

    return operacionesProgramadasDTO;
  }

  /**
   * Metodo encargado de obtener el tipo de operacion como se encuentre alojado en los dominios
   * 
   * @param tipoOperacion
   * @return String
   * @uthor duvan.naranjo
   * 
   */
  private String obtenerTipoOperacion(String tipoOperacion) {
    if (tipoOperacion.toUpperCase().trim().contains(Dominios.TIPO_OPERA_CONSIGNACION)) {
      return dominioService.valorTextoDominio(Constantes.DOMINIO_TIPO_OPERACION,
          Dominios.TIPO_OPERA_CONSIGNACION);
    }
    if (tipoOperacion.toUpperCase().trim().contains(Dominios.TIPO_OPERA_RETIRO)) {
      return dominioService.valorTextoDominio(Constantes.DOMINIO_TIPO_OPERACION,
          Dominios.TIPO_OPERA_RETIRO);
    }
    if (tipoOperacion.toUpperCase().trim().contains(Dominios.TIPO_OPERA_VENTA)) {
      return dominioService.valorTextoDominio(Constantes.DOMINIO_TIPO_OPERACION,
          Dominios.TIPO_OPERA_VENTA);
    }
    if (tipoOperacion.toUpperCase().trim().startsWith(Dominios.TIPO_OPERA_CAMBIO)) {
      return dominioService.valorTextoDominio(Constantes.DOMINIO_TIPO_OPERACION,
          Dominios.TIPO_OPERA_CAMBIO);
    }
    if (tipoOperacion.toUpperCase().trim().contains(Dominios.TIPO_OPERA_INTERCAMBIO)) {
      return dominioService.valorTextoDominio(Constantes.DOMINIO_TIPO_OPERACION,
          Dominios.TIPO_OPERA_INTERCAMBIO);
    }
    if (tipoOperacion.toUpperCase().trim().contains(Dominios.TIPO_OPERA_TRASLADO)) {
      return dominioService.valorTextoDominio(Constantes.DOMINIO_TIPO_OPERACION,
          Dominios.TIPO_OPERA_TRASLADO);
    }
    return null;
  }

  /**
   * Metodo encargado de realizar la consulta de un punto por detalle y nombre campo
   * 
   * @param contenido
   * @param detalle
   * @return PuntosDTO
   * @author duvan.naranjo
   */
  private PuntosDTO consultarPuntoPorDetalle(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detallesArchivo, String nombreCampo) {

    DetallesDefinicionArchivoDTO detalle = detallesArchivo.stream()
        .filter(deta -> deta.getNombreCampo().toUpperCase().equals(nombreCampo)).findFirst()
        .orElse(null);
    if (!Objects.isNull(detalle)) {
      return puntosService
          .getPuntoByNombrePunto(contenido[detalle.getId().getNumeroCampo() - 1].trim());
    }
    return null;
  }

  /**
   * Metodo encargado de realizar la consulta de un punto BanRep por detalle y nombre campo
   * 
   * @param contenido
   * @param detalle
   * @return PuntosDTO
   * @author rparra
   */
  private PuntosDTO consultarPuntoBanRepPorDetalle(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detallesArchivo, String nombreCampo) {

    DetallesDefinicionArchivoDTO detalle = detallesArchivo.stream()
        .filter(deta -> deta.getNombreCampo().toUpperCase().equals(nombreCampo)).findFirst()
        .orElse(null);
    if (!Objects.isNull(detalle)) {
      String puntoBanRepTDV = contenido[detalle.getId().getNumeroCampo() - 1].trim();
      puntoBanRepTDV = puntoBanRepTDV.substring(0, puntoBanRepTDV.lastIndexOf('-'));
      return puntosService.getPuntoByNombrePunto(puntoBanRepTDV);
    }
    return null;
  }

  /**
   * Metodo encargado de consultar un banco por medio del detalle de un archivo y su ciudad
   * 
   * @param contenido
   * @param detallesArchivo
   * @param nombreCampo
   * @return Integer
   * @author duvan.naranjo
   */
  private Integer consultarBancoPorCiudad(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detallesArchivo, String nombreCampo) {
    PuntosDTO puntoFondoDestino =
        this.consultarPuntoPorDetalle(contenido, detallesArchivo, nombreCampo);
    if (!Objects.isNull(puntoFondoDestino)) {
      PuntosDTO puntoBancoDestino = puntosService.getPuntoByTipoPuntoAndCodigoCiudad(
          Constantes.PUNTO_BANC_REP, puntoFondoDestino.getCodigoCiudad());
      return puntoBancoDestino.getCodigoPunto();
    }
    return null;
  }

  /**
   * Metodo encargado de persistir el archivo de cargue de Oficinas en OperacionesProgramadas
   * 
   * @param elemento
   * @return void
   */
  private OperacionesProgramadasDTO procesarArchivoOficinas(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detalleArchivo, ArchivosCargadosDTO archivo) {

    String orderId = determinarOrderId(contenido, detalleArchivo);
    String shipIn = determinarShipIn(contenido, detalleArchivo);
    String shipOut = determinarShipOut(contenido, detalleArchivo);
    operaciones = new OperacionesProgramadasDTO();

    if (Objects.isNull(shipIn) || shipIn.trim().equals("")) {
      shipIn = "0";
    }
    if (Objects.isNull(shipOut) || shipOut.trim().equals("")) {
      shipOut = "0";
    }
    Long valorEntra = Long.parseLong(shipIn);
    Long valorSale = Long.parseLong(shipOut);
    if(valorEntra > 0 && valorSale > 0) {
      log.info("orderId: {} - shipIn: {} - shipOut: {}", orderId, valorEntra, valorSale);
    }

    if (valorEntra > 0) {
      log.debug("shipIn: {} - orderId: {}", shipIn, orderId);
      crearSumarRegistroOficina(orderId, archivo.getIdArchivo().intValue(), S_SALIDA, valorEntra,
          contenido, detalleArchivo);
    }

    if (valorSale > 0) {
      log.debug("shipOut: {} - orderId: {}", shipOut, orderId);
      crearSumarRegistroOficina(orderId, archivo.getIdArchivo().intValue(), S_ENTRADA, valorSale,
          contenido, detalleArchivo);
    }
    return operaciones;
  }

  private void crearSumarRegistroOficina(String orderId, int idArchivo, String entraSale,
      Long valor, String[] contenido, List<DetallesDefinicionArchivoDTO> detalleArchivo) {

    OperacionesProgramadas operacionesProg = operacionesProgramadasRepository
        .findByIdServicioAndIdArchivoCargadoAndEntradaSalida(orderId, idArchivo, entraSale);
    if (Objects.isNull(operacionesProg)) {
      if (valor.intValue() != 0) {
        String transportadora = determinarTransportadora(contenido, detalleArchivo,
            Constantes.CAMPO_DETALLE_ARCHIVO_TRANSPORTADORA);
        String ciudad =
            determinarCiudad(contenido, detalleArchivo, Constantes.CAMPO_DETALLE_ARCHIVO_CIUDAD);

        ciudad = ciudadService.getCiudadPorNombreCiudadFiserv(ciudad).getNombreCiudad();
        int codigoCompensacion = determinarCodigoCompensacion(contenido, detalleArchivo);

        Fondos codigoFondoTDV =
            fondosService.getCodigoFondo(transportadora, codigoCompensacion, ciudad);
        if (Objects.isNull(codigoFondoTDV)) {
          throw new NegocioException(ApiResponseCode.ERROR_FONDOS_NO_ENCONTRADO.getCode(),
              ApiResponseCode.ERROR_FONDOS_NO_ENCONTRADO.getDescription()
                  + " no encontrado para transportadora = " + transportadora
                  + " codigoCompensacion = " + codigoCompensacion + " ciudad = " + ciudad,
              ApiResponseCode.ERROR_FONDOS_NO_ENCONTRADO.getHttpStatus());
        }
        operaciones.setCodigoFondoTDV(codigoFondoTDV.getCodigoPunto());

        operaciones.setEntradaSalida(entraSale);
        operaciones.setCodigoPuntoOrigen(
            determinarPuntoOrigen(contenido, detalleArchivo, transportadora, ciudad, entraSale));
        operaciones.setCodigoPuntoDestino(
            determinarPuntoDestino(contenido, detalleArchivo, transportadora, ciudad, entraSale));
        operaciones.setEstadoConciliacion(dominioService.valorTextoDominio(
            Constantes.DOMINIO_ESTADO_CONCILIACION, Dominios.ESTADO_CONCILIACION_NO_CONCILIADO));
        operaciones.setEstadoOperacion(determinarEstadoOperacion(contenido, detalleArchivo));
        operaciones.setFechaCreacion(new Date());
        Date fechaDestino = determinarFechaDestino(contenido, detalleArchivo,
            Constantes.CAMPO_DETALLE_ARCHIVO_FECHA_ENTREGA);
        operaciones.setFechaDestino(fechaDestino);
        operaciones.setFechaModificacion(new Date());
        operaciones.setFechaOrigen(fechaDestino);
        operaciones.setFechaProgramacion(determinarFechaProgramacion(contenido, detalleArchivo));
        operaciones.setIdArchivoCargado(idArchivo);
        operaciones.setCodigoMoneda(determinarMoneda(contenido, detalleArchivo));
        operaciones.setIdNegociacion(null);
        operaciones.getIdOperacionRelac();
        operaciones.setTasaNegociacion(null);
        operaciones.setTipoOperacion(asignarTipoOperacion(entraSale));
        if (operaciones.getFechaProgramacion().before(fechaDestino)) {
          operaciones.setTipoServicio(dominioService.valorTextoDominio(
              Constantes.DOMINIO_TIPO_SERVICIO, Dominios.TIPO_SERVICIO_PROGRAMADA));
        } else {
          operaciones.setTipoServicio(dominioService.valorTextoDominio(
              Constantes.DOMINIO_TIPO_SERVICIO, Dominios.TIPO_SERVICIO_ESPECIAL));
        }
        operaciones.setTipoTransporte(null);
        operaciones.setUsuarioCreacion(USER1);
        operaciones.setUsuarioModificacion(USER1);
        operaciones.setValorTotal(valor.doubleValue());
        operaciones.setIdServicio(orderId);
        operaciones.setEsCambio(false);
        operaciones.setLiquidaCosto(Dominios.VERDADERO);
        operaciones = crearDetalleOperacionesProgramadas(contenido, detalleArchivo, valor.doubleValue());
        OperacionesProgramadas op = OperacionesProgramadasDTO.CONVERTER_ENTITY.apply(operaciones);

        var operacionesP = operacionesProgramadasRepository
            .save(OperacionesProgramadasDTO.CONVERTER_ENTITY.apply(operaciones));
        op.getDetalleOperacionesProgramadas().forEach(detalle -> {
          detalle.setOperacionesProgramadas(operacionesP);
          detalleOperacionesProgramadasRepository.save(detalle);
        });
        operacionesP.getDetalleOperacionesProgramadas()
            .forEach(operacion -> operacion.setOperacionesProgramadas(operacionesP));
      }
    } else if (valor != 0) {
      operaciones = OperacionesProgramadasDTO.CONVERTER_DTO.apply(operacionesProg);
      operaciones.setValorTotal(operaciones.getValorTotal() + valor);
      operaciones = crearDetalleOperacionesProgramadas(contenido, detalleArchivo, valor.doubleValue());

      OperacionesProgramadas op = OperacionesProgramadasDTO.CONVERTER_ENTITY.apply(operaciones);

      op.getDetalleOperacionesProgramadas().forEach(detalle -> {
        detalle.setOperacionesProgramadas(op);
        detalleOperacionesProgramadasRepository.save(detalle);
      });

      var operacionesP = operacionesProgramadasRepository.save(op);
      operacionesP.getDetalleOperacionesProgramadas()
          .forEach(operacion -> operacion.setOperacionesProgramadas(operacionesP));
    }
  }

  /**
   * Metodo encargado de obtener el codigo de compensacion
   * 
   * @param fila
   * @return Integer
   * @author cesar.castano
   */
  private Integer determinarCodigoCompensacion(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detalleArchivo) {
    Integer compensacion = 0;
    String[] referenceId = contenido[detalleArchivo.stream()
        .filter(deta -> deta.getNombreCampo().toUpperCase().trim()
            .equals(Constantes.CAMPO_DETALLE_ARCHIVO_REFERENCEDID))
        .findFirst().orElse(null).getId().getNumeroCampo() - 1].trim().split("-");
    if (referenceId[0].equals(Constantes.CLIENTE)) {
      compensacion = Integer.parseInt(referenceId[1]);
    } else if (referenceId[1].equals(Constantes.SUCURSAL)) {
      compensacion = Integer.parseInt(referenceId[0]);
    }
    return compensacion;
  }

  /**
   * Metodo encargado de determinar el tipo de punto origen
   * 
   * @param fila
   * @return Integer
   * @author cesar.castano
   */
  private Integer determinarPuntoOrigen(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detalleArchivo, String transportadora, String ciudad, String entradaSalida) {
    Integer codigo = 0;
    if (entradaSalida.equals(Constantes.VALOR_SALIDA)) {
      return fondosService.getCodigoFondo(transportadora,
          determinarCodigoCompensacion(contenido, detalleArchivo), ciudad).getCodigoPunto();
    } else {
      codigo = asignarClienteOficina(contenido, detalleArchivo);
      return codigo;
    }
  }

  /**
   * Metodo encargado de determinar el tipo de punto destino
   * 
   * @param fila
   * @return Integer
   * @author cesar.castano
   */
  private Integer determinarPuntoDestino(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detalleArchivo, String transportadora, String ciudad, String entradaSalida) {
    Integer codigo = 0;
    if (entradaSalida.equals(Constantes.VALOR_ENTRADA)) {
      return fondosService.getCodigoFondo(transportadora,
          determinarCodigoCompensacion(contenido, detalleArchivo), ciudad).getCodigoPunto();
    } else {
      codigo = asignarClienteOficina(contenido, detalleArchivo);
      return codigo;
    }
    
  }

  /**
   * Metodo que valida la entrada o la salida
   * 
   * @param fila
   * @return Integer
   */
  private Integer asignarClienteOficina(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detalleArchivo) {
    Integer codigo = 0;
    String[] referenceId = determinarReferencedId(contenido, detalleArchivo);
    if (referenceId[0].equals(Constantes.CLIENTE)) {
      codigo = asignarCodigoCliente(contenido, detalleArchivo);
    } else if (referenceId[1].equals(Constantes.SUCURSAL)) {
      codigo = asignarCodigoOficina(contenido, detalleArchivo);
    }
    return codigo;
  }

  /**
   * Metodo encargado de asignar el codigo punto del cliente
   * 
   * @param fila
   * @return Integer
   * @author cesar.castano
   */
  private Integer asignarCodigoCliente(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detalleArchivo) {

    String[] referenceId = determinarReferencedId(contenido, detalleArchivo);
    Integer codigoPunto = bancosService.getCodigoPuntoBanco(Integer.parseInt(referenceId[1]));
    clientesCorporativosService.getCodigoCliente(codigoPunto, referenceId[2].trim());
    return 9999;
  }

  /**
   * Metodo encargado de asignar el codigo punto de la oficina
   * 
   * @param fila
   * @return Integer
   * @author cesar.castano
   */
  private Integer asignarCodigoOficina(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detalleArchivo) {

    String[] referenceId = determinarReferencedId(contenido, detalleArchivo);
    Integer codigoPunto = bancosService.getCodigoPuntoBanco(Integer.parseInt(referenceId[0]));
    return oficinaService.getCodigoPunto(Integer.parseInt(referenceId[2]), codigoPunto);

  }

  /**
   * Metodo encargado de asignar la fecha
   * 
   * @param fila
   * @return Date
   * @author cesar.castano
   */
  private Date asignarFecha(String contenido) {
    Date fecha = null;
    try {
      DateFormat formato = new SimpleDateFormat(dominioService
          .valorTextoDominio(Constantes.DOMINIO_FORMATO_FECHA, Dominios.FORMATO_FECHA_F4));
      fecha = formato.parse(contenido);
    } catch (ParseException e) {
      e.getMessage();
    }
    return fecha;
  }

  /**
   * Metodo encargado de asignar el estado de la oepracion
   * 
   * @param fila
   * @return String
   * @author cesar.castano
   */
  private String asignarEstadoOperacion(String contenido) {
    var estadoOperacion = "";
    switch (contenido) {
      case Constantes.ESTADO_PEDIDO_APRVD: {
        estadoOperacion = dominioService.valorTextoDominio(Constantes.DOMINIO_ESTADOS_OPERACION,
            Dominios.ESTADOS_OPERA_EJECUTADO);
        break;
      }
      case Constantes.ESTADO_PEDIDO_DELIV: {
        estadoOperacion = dominioService.valorTextoDominio(Constantes.DOMINIO_ESTADOS_OPERACION,
            Dominios.ESTADOS_OPERA_EJECUTADO);
        break;
      }
      case Constantes.ESTADO_PEDIDO_CANCEL: {
        estadoOperacion = dominioService.valorTextoDominio(Constantes.DOMINIO_ESTADOS_OPERACION,
            Dominios.ESTADOS_OPERA_CANCELADO);
        break;
      }
      case Constantes.ESTADO_PEDIDO_DECLI: {
        estadoOperacion = dominioService.valorTextoDominio(Constantes.DOMINIO_ESTADOS_OPERACION,
            Dominios.ESTADOS_OPERA_FALLIDO);
        break;
      }
      default:
        throw new NegocioException(ApiResponseCode.ERROR_ESTADOS_OPERACION_NO_VALIDO.getCode(),
            ApiResponseCode.ERROR_ESTADOS_OPERACION_NO_VALIDO.getDescription(),
            ApiResponseCode.ERROR_ESTADOS_OPERACION_NO_VALIDO.getHttpStatus());
    }
    return estadoOperacion;
  }

  /**
   * Metodo que retorna el tipo de Operacion
   * 
   * @param fila
   * @return String
   * @author cesar.castano
   */
  private String asignarTipoOperacion(String entraSale) {
    var tipoOperacion = "";
    if (entraSale.equals(Constantes.VALOR_SALIDA)) {
      tipoOperacion = dominioService.valorTextoDominio(Constantes.DOMINIO_TIPO_OPERACION,
          Dominios.TIPO_OPERA_PROVISION);
    } else if (entraSale.equals(Constantes.VALOR_ENTRADA)) {
      tipoOperacion = dominioService.valorTextoDominio(Constantes.DOMINIO_TIPO_OPERACION,
          Dominios.TIPO_OPERA_RECOLECCION);
    }
    return tipoOperacion;
  }


  /**
   * Metodo encargado de crear el detalle de las operaciones programadas
   * 
   * @param fila
   * @param idOperacion
   * @return DetalleOperacionesProgramadas
   * @author cesar.castano
   */
  private OperacionesProgramadasDTO crearDetalleOperacionesProgramadas(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detalleArchivo, Double valorTotal) {

    String[] fila1 = contenido[detalleArchivo.stream()
        .filter(deta -> deta.getNombreCampo().toUpperCase().trim()
            .equals(Constantes.CAMPO_DETALLE_ARCHIVO_DENOMINACION))
        .findFirst().orElse(null).getId().getNumeroCampo() - 1].trim().split(" - ");
    var detalleOperacionesDTO = new DetalleOperacionesDTO();
    if (fila1.length > 1) {
      detalleOperacionesDTO.setFamilia(fila1[1].trim());
    } else {
      detalleOperacionesDTO.setFamilia(null);
    }
    detalleOperacionesDTO.setCalidad(null);
    detalleOperacionesDTO.setDenominacion(fila1[0].trim());
    detalleOperacionesDTO.setValorDetalle(valorTotal);
    detalleOperacionesDTO.setFechaCreacion(new Date());
    detalleOperacionesDTO.setFechaModificacion(new Date());
    detalleOperacionesDTO.setUsuarioCreacion("User ATH");
    detalleOperacionesDTO.setUsuarioModificacion("User ATH");

    if (Objects.isNull(operaciones.getDetalleOperacionesProgramadasDTO())) {
      operaciones.setDetalleOperacionesProgramadasDTO(new ArrayList<>());
    }

    operaciones.getDetalleOperacionesProgramadasDTO().add(detalleOperacionesDTO);

    return operaciones;
  }

  /**
   * Metodo que se encarga de restar dias a una fecha
   * 
   * @param fecha
   * @param dias
   * @return Date
   * @author cesar.castano
   */
  public Date sumarRestarDiasFecha(Date fecha, int dias) {
    var calendar = Calendar.getInstance();
    calendar.setTime(fecha); // Configuramos la fecha que se recibe
    calendar.add(Calendar.DAY_OF_YEAR, dias); // numero de días a añadir, o restar en caso de días<0
    return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos
  }

  /**
   * Metodo que se encarga de persistir en la tabla de OperacionesProgramadas, el archivo plano de
   * CAJEROS
   * 
   * @param elemento
   * @author cesar.castano
   */
  private OperacionesProgramadasDTO procesarArchivoCajeros(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detalleArchivo, ArchivosCargadosDTO archivo) {

    OperacionesProgramadasDTO op = new OperacionesProgramadasDTO();
    String transportadora = determinarTransportadora(contenido, detalleArchivo,
        Constantes.CAMPO_DETALLE_ARCHIVO_TRANSPORTADORA);
    String nombreBanco = determinarNombreBanco(contenido, detalleArchivo);
    String ciudad =
        determinarCiudad(contenido, detalleArchivo, Constantes.CAMPO_DETALLE_ARCHIVO_CIUDAD);
    ciudad = ciudadesRepository.findByNombreCiudadFiserv(ciudad).getNombreCiudad();
    op.setCodigoFondoTDV(fondosService
        .getCodigoFondo(transportadora, dominioService.valorTextoDominio(
            Constantes.DOMINIO_TIPOS_PUNTO, Dominios.TIPOS_PUNTO_BANCO), nombreBanco, ciudad)
        .getCodigoPunto());
    op.setEntradaSalida(Constantes.SALIDA);
    op.setCodigoPuntoOrigen(fondosService
        .getCodigoFondo(transportadora, dominioService.valorTextoDominio(
            Constantes.DOMINIO_TIPOS_PUNTO, Dominios.TIPOS_PUNTO_BANCO), nombreBanco, ciudad)
        .getCodigoPunto());
    op.setCodigoPuntoDestino(
        cajerosService.getCodigoPunto(determinarReference(contenido, detalleArchivo)));
    op.setEstadoConciliacion(dominioService.valorTextoDominio(
        Constantes.DOMINIO_ESTADO_CONCILIACION, Dominios.ESTADO_CONCILIACION_NO_CONCILIADO));
    op.setEstadoOperacion(dominioService.valorTextoDominio(Constantes.DOMINIO_ESTADOS_OPERACION,
        Dominios.ESTADOS_OPERA_EJECUTADO));
    op.setFechaCreacion(new Date());
    Date fechaDestino = determinarFechaDestino(contenido, detalleArchivo,
        Constantes.CAMPO_DETALLE_ARCHIVO_FECHA_ENTREGA);
    op.setFechaDestino(fechaDestino);
    op.setFechaModificacion(new Date());
    op.setFechaOrigen(fechaDestino);
    op.setFechaProgramacion(fechaDestino);
    op.setIdArchivoCargado(archivo.getIdArchivo().intValue());
    op.setIdNegociacion(null);
    op.setIdOperacionRelac(null);
    op.setTasaNegociacion(null);
    op.setTipoOperacion(dominioService.valorTextoDominio(Constantes.DOMINIO_TIPO_OPERACION,
        Dominios.TIPO_OPERA_PROVISION));
    op.setTipoServicio(dominioService.valorTextoDominio(Constantes.DOMINIO_TIPO_SERVICIO,
        Dominios.TIPO_SERVICIO_PROGRAMADA));
    op.setTipoTransporte(null);
    op.setUsuarioCreacion("User1");
    op.setUsuarioModificacion("User1");
    op.setValorTotal(Double.parseDouble(determinarValor(contenido, detalleArchivo)));
    op.setIdServicio(determinarNumeroOrden(contenido, detalleArchivo));
    op.setLiquidaCosto(Dominios.VERDADERO);
    operacionesProgramadasRepository
        .save(OperacionesProgramadasDTO.CONVERTER_ENTITY.apply(operaciones));
    return op;
  }


  /**
   * Metodo utilizado para determinar el campo C�digo Moneda
   * 
   * @param contenido
   * @param detalleArchivo
   * @return String
   * @author cesar.castano
   */
  private String determinarMoneda(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detalleArchivo) {
    return contenido[detalleArchivo.stream()
        .filter(deta -> deta.getNombreCampo().toUpperCase().trim()
            .equals(Constantes.CAMPO_DETALLE_ARCHIVO_MONEDA))
        .findFirst().orElse(null).getId().getNumeroCampo() - 1].trim();
  }

  /**
   * Metodo utilizado para determinar el campo ShipIn
   * 
   * @param contenido
   * @param detalleArchivo
   * @return String
   * @author cesar.castano
   */
  private String determinarShipIn(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detalleArchivo) {
    return contenido[detalleArchivo.stream()
        .filter(deta -> deta.getNombreCampo().toUpperCase().trim()
            .equals(Constantes.CAMPO_DETALLE_ARCHIVO_SHIPIN))
        .findFirst().orElse(null).getId().getNumeroCampo() - 1].trim();
  }

  /**
   * Metodo utilizado para determinar el campo OrderId
   * 
   * @param contenido
   * @param detalleArchivo
   * @return String
   * @author cesar.castano
   */
  private String determinarOrderId(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detalleArchivo) {
    return contenido[detalleArchivo.stream()
        .filter(deta -> deta.getNombreCampo().toUpperCase().trim()
            .equals(Constantes.CAMPO_DETALLE_ARCHIVO_ORDERID))
        .findFirst().orElse(null).getId().getNumeroCampo() - 1].trim();
  }

  /**
   * Metodo utilizado para determinar el campo ShipOut
   * 
   * @param contenido
   * @param detalleArchivo
   * @return String
   * @author cesar.castano
   */
  private String determinarShipOut(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detalleArchivo) {
    return contenido[detalleArchivo.stream()
        .filter(deta -> deta.getNombreCampo().toUpperCase().trim()
            .equals(Constantes.CAMPO_DETALLE_ARCHIVO_SHIPOUT))
        .findFirst().orElse(null).getId().getNumeroCampo() - 1].trim();
  }

  /**
   * Metodo utilizado para determinar el campo Transportadora
   * 
   * @param contenido
   * @param detalleArchivo
   * @param constante
   * @return String
   * @author cesar.castano
   */
  private String determinarTransportadora(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detalleArchivo, String constante) {
    return contenido[detalleArchivo.stream()
        .filter(deta -> deta.getNombreCampo().toUpperCase().trim().equals(constante)).findFirst()
        .orElse(null).getId().getNumeroCampo() - 1].trim();

  }

  /**
   * Metodo utilizado para determinar el campo Ciudad
   * 
   * @param contenido
   * @param detalleArchivo
   * @param constante
   * @return String
   * @author cesar.castano
   */
  private String determinarCiudad(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detalleArchivo, String constante) {
    return contenido[detalleArchivo.stream()
        .filter(deta -> deta.getNombreCampo().toUpperCase().trim().equals(constante)).findFirst()
        .orElse(null).getId().getNumeroCampo() - 1].trim();
  }

  /**
   * Metodo utilizado para determinar el campo EstadoOperacion
   * 
   * @param contenido
   * @param detalleArchivo
   * @return String
   * @author cesar.castano
   */
  private String determinarEstadoOperacion(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detalleArchivo) {
    return asignarEstadoOperacion(contenido[detalleArchivo.stream()
        .filter(deta -> deta.getNombreCampo().toUpperCase().trim()
            .equals(Constantes.CAMPO_DETALLE_ARCHIVO_ESTATUS))
        .findFirst().orElse(null).getId().getNumeroCampo() - 1].trim());
  }

  /**
   * Metodo utilizado para determinar el campo FechaDestino
   * 
   * @param contenido
   * @param detalleArchivo
   * @param constante
   * @return String
   * @author cesar.castano
   */
  private Date determinarFechaDestino(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detalleArchivo, String constante) {
    return asignarFecha(contenido[detalleArchivo.stream()
        .filter(deta -> deta.getNombreCampo().toUpperCase().trim().equals(constante)).findFirst()
        .orElse(null).getId().getNumeroCampo() - 1].trim());
  }

  /**
   * Metodo utilizado para determinar el campo Fecha Programacion
   * 
   * @param contenido
   * @param detalleArchivo
   * @return String
   * @author cesar.castano
   */
  private Date determinarFechaProgramacion(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detalleArchivo) {
    return asignarFecha(contenido[detalleArchivo.stream()
        .filter(deta -> deta.getNombreCampo().toUpperCase().trim()
            .equals(Constantes.CAMPO_DETALLE_ARCHIVO_FECHA_APROBACION))
        .findFirst().orElse(null).getId().getNumeroCampo() - 1].trim());
  }

  /**
   * Metodo utilizado para determinar el campo ReferencedId
   * 
   * @param contenido
   * @param detalleArchivo
   * @return String
   * @author cesar.castano
   */
  private String[] determinarReferencedId(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detalleArchivo) {
    return contenido[detalleArchivo.stream()
        .filter(deta -> deta.getNombreCampo().toUpperCase().trim()
            .equals(Constantes.CAMPO_DETALLE_ARCHIVO_REFERENCEDID))
        .findFirst().orElse(null).getId().getNumeroCampo() - 1].trim().split("-");
  }

  /**
   * Metodo utilizado para determinar el campo NombreBanco
   * 
   * @param contenido
   * @param detalleArchivo
   * @return String
   * @author cesar.castano
   */
  private String determinarNombreBanco(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detalleArchivo) {
    return contenido[detalleArchivo.stream()
        .filter(deta -> deta.getNombreCampo().toUpperCase().trim()
            .equals(Constantes.CAMPO_DETALLE_ARCHIVO_BANCO))
        .findFirst().orElse(null).getId().getNumeroCampo() - 1].trim();
  }

  /**
   * Metodo utilizado para determinar el campo Reference
   * 
   * @param contenido
   * @param detalleArchivo
   * @return String
   * @author cesar.castano
   */
  private String determinarReference(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detalleArchivo) {
    return contenido[detalleArchivo.stream()
        .filter(deta -> deta.getNombreCampo().toUpperCase().trim()
            .equals(Constantes.CAMPO_DETALLE_ARCHIVO_REFERENCE))
        .findFirst().orElse(null).getId().getNumeroCampo() - 1].trim();
  }

  /**
   * Metodo utilizado para determinar el campo NumeroOrden
   * 
   * @param contenido
   * @param detalleArchivo
   * @return String
   * @author cesar.castano
   */
  private String determinarNumeroOrden(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detalleArchivo) {
    return contenido[detalleArchivo.stream()
        .filter(deta -> deta.getNombreCampo().toUpperCase().trim()
            .equals(Constantes.CAMPO_DETALLE_ARCHIVO_NUMEROORDEN))
        .findFirst().orElse(null).getId().getNumeroCampo() - 1].trim();
  }

  /**
   * Metodo utilizado para determinar el campo Valor
   * 
   * @param contenido
   * @param detalleArchivo
   * @return String
   * @author cesar.castano
   */
  private String determinarValor(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detalleArchivo) {
    return contenido[detalleArchivo.stream()
        .filter(deta -> deta.getNombreCampo().toUpperCase().trim()
            .equals(Constantes.CAMPO_DETALLE_ARCHIVO_VALOR))
        .findFirst().orElse(null).getId().getNumeroCampo() - 1].trim();
  }


}
