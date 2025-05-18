package com.ath.adminefectivo.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.DetallesDefinicionArchivoDTO;
import com.ath.adminefectivo.dto.FechasConciliacionDTO;
import com.ath.adminefectivo.dto.OperacionesCertificadasDTO;
import com.ath.adminefectivo.dto.compuestos.CodigoPuntoOrigenDestinoDTO;
import com.ath.adminefectivo.dto.compuestos.RegistroTipo1ArchivosFondosDTO;
import com.ath.adminefectivo.dto.compuestos.SobrantesFaltantesDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.ArchivosCargados;
import com.ath.adminefectivo.entities.Fondos;
import com.ath.adminefectivo.entities.OperacionesCertificadas;
import com.ath.adminefectivo.entities.RegistrosCargados;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.IOperacionesCertificadasRepository;
import com.ath.adminefectivo.service.IArchivosCargadosService;
import com.ath.adminefectivo.service.IAuditoriaProcesosService;
import com.ath.adminefectivo.service.IBancosService;
import com.ath.adminefectivo.service.ICajerosService;
import com.ath.adminefectivo.service.ICiudadesService;
import com.ath.adminefectivo.service.IClientesCorporativosService;
import com.ath.adminefectivo.service.IDetalleDefinicionArchivoService;
import com.ath.adminefectivo.service.IDominioService;
import com.ath.adminefectivo.service.IFondosService;
import com.ath.adminefectivo.service.IOficinasService;
import com.ath.adminefectivo.service.IOperacionesCertificadasService;
import com.ath.adminefectivo.service.IParametroService;
import com.ath.adminefectivo.service.IPuntosCodigoTdvService;
import com.ath.adminefectivo.service.IPuntosService;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class OperacionesCertificadasServiceImpl implements IOperacionesCertificadasService {

  @Autowired
  IOperacionesCertificadasRepository operacionesCertificadasRepository;

  @Autowired
  IFondosService fondosService;

  @Autowired
  IDominioService dominioService;

  @Autowired
  IPuntosCodigoTdvService puntosCodigoTdvService;

  @Autowired
  IOficinasService oficinaService;

  @Autowired
  IClientesCorporativosService clientesCorporativosService;

  @Autowired
  ICajerosService cajerosService;

  @Autowired
  IBancosService bancosService;

  @Autowired
  IPuntosService puntosService;

  @Autowired
  IDetalleDefinicionArchivoService detalleDefinicionArchivoService;

  @Autowired
  IArchivosCargadosService archivosCargadosService;

  @Autowired
  ICiudadesService ciudadesService;

  @Autowired
  IParametroService parametroService;

  @Autowired
  IAuditoriaProcesosService auditoriaProcesosService;

  private List<SobrantesFaltantesDTO> listaAjustesValor = new ArrayList<>();
  private static final String USER1 = "user1";
  private static final String SIN_CODIGO_SERVICIO = "SIN_CODIGO_SERVICIO";
  private String estadoConciliacion = "";
  private Date fechaHoy = new Date();
  private Date fechaProceso;


  /**
   * {@inheritDoc}
   */
  @Override
  public Boolean actualizarEstadoEnCertificadas(Integer idCertificacion, String estado) {

    Optional<OperacionesCertificadas> operaciones =
        operacionesCertificadasRepository.findById(idCertificacion);
    if (!operaciones.isPresent()) {
      throw new NegocioException(
          ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getCode(),
          ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getDescription(),
          ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getHttpStatus());
    }
    try {
      operaciones.get().setEstadoConciliacion(estado);
      operaciones.get().setFechaModificacion(new Date());
      operaciones.get().setUsuarioModificacion(USER1);
      operaciones.get().setConciliable(Constantes.SI);
      operacionesCertificadasRepository.save(operaciones.get());
    } catch (Exception e) {
      log.error("Failed to actualizarEstadoEnCertificadas: {}", e);
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer numeroOperacionesPorEstadoyFecha(FechasConciliacionDTO fechaConciliacion,
      String estado) {
    Integer cuentaCertificadas =
        operacionesCertificadasRepository.countByEstadoConciliacionAndFechaEjecucionBetween(estado,
            fechaConciliacion.getFechaConciliacionInicial(),
            fechaConciliacion.getFechaConciliacionFinal());
    if (Objects.isNull(cuentaCertificadas)) {
      throw new NegocioException(
          ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getCode(),
          ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getDescription(),
          ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getHttpStatus());
    }
    return cuentaCertificadas;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer numeroOperacionesPorEstadoFechaYConciliable(
      FechasConciliacionDTO fechaConciliacion, String estado, String conciliable) {
    Integer cuentaCertificadas = operacionesCertificadasRepository
        .countByEstadoConciliacionAndFechaEjecucionBetweenAndConciliable(estado,
            fechaConciliacion.getFechaConciliacionInicial(),
            fechaConciliacion.getFechaConciliacionFinal(), conciliable);
    if (Objects.isNull(cuentaCertificadas)) {
      throw new NegocioException(
          ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getCode(),
          ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getDescription(),
          ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getHttpStatus());
    }
    return cuentaCertificadas;
  }



  /**
   * {@inheritDoc}
   */
  @Override
  public List<OperacionesCertificadas> obtenerOperacionesCertificaciones() {
    List<OperacionesCertificadas> operacionesc =
        operacionesCertificadasRepository.conciliacionAutomatica(dominioService.valorTextoDominio(
            Constantes.DOMINIO_ESTADO_CONCILIACION, Dominios.ESTADO_CONCILIACION_NO_CONCILIADO));
    if (Objects.isNull(operacionesc)) {
      throw new NegocioException(
          ApiResponseCode.ERROR_OPERACIONES_A_CONCILIAR_NO_ENCONTRADO.getCode(),
          ApiResponseCode.ERROR_OPERACIONES_A_CONCILIAR_NO_ENCONTRADO.getDescription(),
          ApiResponseCode.ERROR_OPERACIONES_A_CONCILIAR_NO_ENCONTRADO.getHttpStatus());
    }
    return operacionesc;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public OperacionesCertificadas obtenerEntidadOperacionesCertificacionesporId(
      Integer idCertificacion) {
    OperacionesCertificadas operacionesC =
        operacionesCertificadasRepository.findById(idCertificacion).get();
    if (Objects.isNull(operacionesC)) {
      throw new NegocioException(
          ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getCode(),
          ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getDescription(),
          ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getHttpStatus());
    }
    return operacionesC;
  }

  @Override
  public void validarNoConciliables() {
    operacionesCertificadasRepository.validarnoconciliables();
  }


  /**
   * Metodo encargado de procesar los archivos de otros fondos (no Brinks)
   * 
   * @param elemento
   * @author cesar.castano
   */
  @Transactional
  @Override
  public void procesarArchivoOtrosFondos(ArchivosCargados elemento,
      List<DetallesDefinicionArchivoDTO> detalleArchivo) {

    var registro = new RegistroTipo1ArchivosFondosDTO();
    listaAjustesValor = new ArrayList<>();
    this.fechaHoy = new Date();
    this.fechaProceso = parametroService.valorParametroDate(Constantes.FECHA_DIA_PROCESO);
    this.estadoConciliacion = dominioService.valorTextoDominio(
        Constantes.DOMINIO_ESTADO_CONCILIACION, Dominios.ESTADO_CONCILIACION_NO_CONCILIADO);
    elemento.getRegistrosCargados();

    String[] fila;
    Integer tipoReg;
    Long consecutivoRegistro;
    SobrantesFaltantesDTO ajusteValor;
    for (var i = 0; i < elemento.getRegistrosCargados().size(); i++) {

      ajusteValor = new SobrantesFaltantesDTO();
      fila = elemento.getRegistrosCargados().get(i).getContenido().split(", ");
      tipoReg = Integer.parseInt(determinarTipoRegistro(fila, detalleArchivo));
      consecutivoRegistro =
          elemento.getRegistrosCargados().get(i).getId().getConsecutivoRegistro().longValue();

      switch (tipoReg) {
        case 1: {
          procesarRegistroTipo1(fila, detalleArchivo, tipoReg, registro);
          break;
        }
        case 2: {
          break;
        }
        case 3: {
          procesarRegistroTipo3(fila, detalleArchivo, tipoReg, registro, elemento,
              consecutivoRegistro);
          break;
        }
        case 4: {
          break;
        }
        case 5: {
          procesarRegistroTipo5(fila, detalleArchivo, tipoReg, ajusteValor,
              elemento.getFechaArchivo(), registro.getBancoAval(), registro.getTdv());
          break;
        }
        default:
          auditoriaProcesosService.actualizarAuditoriaProceso(
              Dominios.CODIGO_PROCESO_LOG_CERTIFICACION, this.fechaProceso,
              Constantes.ESTADO_PROCESO_ERROR,
              ApiResponseCode.ERROR_TIPO_REGISTRO_NO_VALIDO.getDescription());
          throw new NegocioException(ApiResponseCode.ERROR_TIPO_REGISTRO_NO_VALIDO.getCode(),
              ApiResponseCode.ERROR_TIPO_REGISTRO_NO_VALIDO.getDescription(),
              ApiResponseCode.ERROR_TIPO_REGISTRO_NO_VALIDO.getHttpStatus());
      }
    }
    procesarSobranteFaltanteNoBrinks();

    if (Dominios.ESTADO_VALIDACION_REPROCESO.equals(elemento.getEstadoCargue())) {
      elemento.setEstadoCargue(Dominios.ESTADO_VALIDACION_REPROCESADO);
    } else {
      elemento.setEstadoCargue(Dominios.ESTADO_VALIDACION_ACEPTADO);
    }
    archivosCargadosService.actualizarArchivosCargados(elemento);
  }

  /**
   * Metodo encargado de procesar el tipo de registro 5 de los archivos No Brinks
   * 
   * @param fila
   * @param detalleArchivo
   * @param tipoRegistro
   * @author cesar.castano
   */
  private void procesarRegistroTipo5(String[] fila,
      List<DetallesDefinicionArchivoDTO> detalleArchivo, Integer tipoRegistro,
      SobrantesFaltantesDTO ajusteValor, Date fecha, int codigoBanco, String tdv) {
    String tipoAjuste = determinarCampo(fila, detalleArchivo, tipoRegistro,
        Constantes.CAMPO_DETALLE_ARCHIVO_TIPONOVEDAD);
    String nombrePunto = determinarCampo(fila, detalleArchivo, tipoRegistro,
        Constantes.CAMPO_DETALLE_ARCHIVO_NOMBREPUNTO_TIPO_5);
    String montoTotal = determinarCampo(fila, detalleArchivo, tipoRegistro,
        Constantes.CAMPO_DETALLE_ARCHIVO_MONTOTOTAL);

    guardarSobrantesyFaltantes(tipoAjuste, nombrePunto, Double.parseDouble(montoTotal), ajusteValor,
        fecha, codigoBanco, tdv);
  }

  /**
   * Metodo encargado de procesar el tipo de registro 3 de los archivos No Brinks
   * 
   * @param fila
   * @param detalleArchivo
   * @param tipoRegistro
   * @param registro
   * @author cesar.castano
   */
  private void procesarRegistroTipo3(String[] fila,
      List<DetallesDefinicionArchivoDTO> detalleArchivo, Integer tipoRegistro,
      RegistroTipo1ArchivosFondosDTO registro, ArchivosCargados elemento,
      Long consecutivoRegistro) {
    String codigoServicio = determinarCampo(fila, detalleArchivo, tipoRegistro,
        Constantes.CAMPO_DETALLE_ARCHIVO_CODIGOSERVICIO);
    String entradaSalida = determinarCampo(fila, detalleArchivo, tipoRegistro,
        Constantes.CAMPO_DETALLE_ARCHIVO_ENTRADASALIDA);
    String codigoPunto = determinarCampo(fila, detalleArchivo, tipoRegistro,
        Constantes.CAMPO_DETALLE_ARCHIVO_CODIGOPUNTO).trim();
    String nombrePunto = determinarCampo(fila, detalleArchivo, tipoRegistro,
        Constantes.CAMPO_DETALLE_ARCHIVO_NOMBREPUNTO).trim();
    String tipoServicio = determinarCampo(fila, detalleArchivo, tipoRegistro,
        Constantes.CAMPO_DETALLE_ARCHIVO_TIPOSERVICIOF);
    if (Objects.isNull(codigoServicio) || codigoServicio.trim().isEmpty()) {
      codigoServicio = SIN_CODIGO_SERVICIO;
    }
    procesarOperacionTransporte(fila, registro, elemento, codigoServicio, entradaSalida,
        codigoPunto, nombrePunto, tipoServicio, null, consecutivoRegistro);
  }

  /**
   * Metodo encargado de procesar el tipo de registro 1 de los archivos No Brinks
   * 
   * @param fila
   * @param detalleArchivo
   * @param tipoRegistro
   * @param registro
   * @author cesar.castano
   */
  private void procesarRegistroTipo1(String[] fila,
      List<DetallesDefinicionArchivoDTO> detalleArchivo, Integer tipoRegistro,
      RegistroTipo1ArchivosFondosDTO registro) {
    String tdv = determinarCampo(fila, detalleArchivo, tipoRegistro,
        Constantes.CAMPO_DETALLE_ARCHIVO_SIGLATDV);
    String nit = determinarCampo(fila, detalleArchivo, tipoRegistro,
        Constantes.CAMPO_DETALLE_ARCHIVO_NITBANCO);
    String nitbanco = nit.substring(0, 9);
    String ciudad = determinarCampo(fila, detalleArchivo, tipoRegistro,
        Constantes.CAMPO_DETALLE_ARCHIVO_CODIGOCIUDAD);
    String ciudad2 = String.valueOf(Integer.parseInt(ciudad.trim()));
    Fondos fondo = asignarFondo(tdv, nitbanco, ciudad2);
    registro.setTdv(fondo.getTdv());
    registro.setCodigoPunto(fondo.getCodigoPunto());
    Date fecha = determinarFechaEjecucion(fila, detalleArchivo, tipoRegistro,
        Constantes.CAMPO_DETALLE_ARCHIVO_FECHAEJECUCION);
    registro.setFechaEjecucion(fecha);
    registro.setBancoAval(fondo.getBancoAVAL());
    registro.setCodigoDane(ciudad2);
  }

  /**
   * Metodo encargado de procesar las operaciones del registro tipo 03
   * 
   * @param fila
   * @param registro
   * @param elemento
   * @author cesar.castano
   */
  private void procesarOperacionTransporte(String[] fila, RegistroTipo1ArchivosFondosDTO registro,
      ArchivosCargados elemento, String codigoServicio, String entradaSalida, String codigoPunto, 
      String nombrePunto, String tipoServicio, String codigoOperacion, Long consecutivoRegistro) {

    String codigoPropio = codigoPunto + nombrePunto;
    OperacionesCertificadas certificadas;
    CodigoPuntoOrigenDestinoDTO codigoPuntoOrigenDestino;
    Integer longitud = 0;
    String entradaSal = asignarEntradaSalida(entradaSalida);
    codigoPuntoOrigenDestino = obtenerCodigoPuntoOrigenDestino(entradaSal, registro, codigoPropio,
        codigoServicio, elemento.getIdArchivo());
    certificadas = codigoPuntoOrigenDestino.getCertificadas();
    if ((elemento.getIdModeloArchivo().equals(Dominios.TIPO_ARCHIVO_ITVCS))
        || (elemento.getIdModeloArchivo().equals(Dominios.TIPO_ARCHIVO_IATCS))
        || (elemento.getIdModeloArchivo().equals(Dominios.TIPO_ARCHIVO_IPRCS))
        || (elemento.getIdModeloArchivo().equals(Dominios.TIPO_ARCHIVO_IVGLS))) {
      longitud = fila.length;
    } else {
      longitud = fila.length - 1;
    }
    if (Objects.isNull(certificadas)) {
      var operaciones = new OperacionesCertificadasDTO();

      operaciones.setCodigoFondoTDV(registro.getCodigoPunto());
      operaciones.setCodigoPropioTDV(codigoPropio);
      operaciones.setCodigoPuntoDestino(codigoPuntoOrigenDestino.getCodigoPuntoDestino());
      operaciones.setCodigoPuntoOrigen(codigoPuntoOrigenDestino.getCodigoPuntoOrigen());
      operaciones.setCodigoServicioTdv(codigoServicio);
      operaciones.setConciliable(null);
      operaciones.setEntradaSalida(entradaSal);
      operaciones.setEstadoConciliacion(this.estadoConciliacion);
      operaciones.setFallidaOficina(null);
      operaciones.setFechaCreacion(this.fechaHoy);
      operaciones.setFechaEjecucion(registro.getFechaEjecucion());
      operaciones.setFechaModificacion(this.fechaHoy);
      operaciones.setIdArchivoCargado(elemento.getIdArchivo());
      operaciones.setConsecutivoRegistro(consecutivoRegistro);
      operaciones.setTipoOperacion(
          asignarTipoOperacion(entradaSal, codigoPuntoOrigenDestino.getCodigoPuntoOrigen(),
              codigoPuntoOrigenDestino.getCodigoPuntoDestino()));
      operaciones.setTipoServicio(
          dominioService.valorTextoDominio(Constantes.DOMINIO_TIPO_SERVICIO, tipoServicio));
      operaciones.setUsuarioCreacion(USER1);
      operaciones.setUsuarioModificacion(USER1);
      operaciones.setValorFaltante(0.0);
      operaciones.setValorSobrante(0.0);
      operaciones.setCodigoOperacion(codigoOperacion);
      if ((elemento.getIdModeloArchivo().equals(Dominios.TIPO_ARCHIVO_ITVCS))
          || (elemento.getIdModeloArchivo().equals(Dominios.TIPO_ARCHIVO_IATCS))
          || (elemento.getIdModeloArchivo().equals(Dominios.TIPO_ARCHIVO_IPRCS))
          || (elemento.getIdModeloArchivo().equals(Dominios.TIPO_ARCHIVO_IVGLS))) {
        operaciones.setValorTotal(
            asignarValorTotal(fila, Constantes.INICIA_DENOMINACION_OTROS_FONDOS, longitud));
        operaciones.setMoneda(Constantes.MONEDA_COP);

      } else {
        operaciones.setValorTotal(
            asignarValorTotal(fila, Constantes.INICIA_DENOMINACION_BRINKS, longitud));
        operaciones.setMoneda(asignarMoneda(fila, Constantes.TIPO_MONEDA_BRINKS));
      }

      operaciones.setCodigoPuntoCodigotdv(codigoPunto);
      operaciones.setDescripcionPuntoCodigotdv(nombrePunto);
      
      if (!operaciones.getValorTotal().equals(0.0)) {
        operacionesCertificadasRepository
            .save(OperacionesCertificadasDTO.CONVERTER_ENTITY.apply(operaciones));
      }
      
    } else {
      if ((elemento.getIdModeloArchivo().equals(Dominios.TIPO_ARCHIVO_ITVCS))
          || (elemento.getIdModeloArchivo().equals(Dominios.TIPO_ARCHIVO_IATCS))
          || (elemento.getIdModeloArchivo().equals(Dominios.TIPO_ARCHIVO_IPRCS))
          || (elemento.getIdModeloArchivo().equals(Dominios.TIPO_ARCHIVO_IVGLS))) {
        certificadas.setValorTotal(certificadas.getValorTotal()
            + asignarValorTotal(fila, Constantes.INICIA_DENOMINACION_OTROS_FONDOS, longitud));
      } else {
        certificadas.setValorTotal(certificadas.getValorTotal()
            + asignarValorTotal(fila, Constantes.INICIA_DENOMINACION_BRINKS, longitud));
      }
      if (!certificadas.getValorTotal().equals(0.0)) {
        operacionesCertificadasRepository.save(certificadas);
      }
    }
  }

  /**
   * Metodo encargado de obtener el codigo Punto Destino y Origen
   * 
   * @param entradaSalida
   * @param codigoPropio
   * @param codigoServicio
   * @return CodigoPuntoOrigenDestinoDTO
   */
  private CodigoPuntoOrigenDestinoDTO obtenerCodigoPuntoOrigenDestino(String entradaSalida,
      RegistroTipo1ArchivosFondosDTO registro, String codigoPropio, String codigoServicio,
      Long idArchivo) {

    var codigoPuntoOrigenDestino = new CodigoPuntoOrigenDestinoDTO();
    Integer codigoPuntoOrigen = 0;
    Integer codigoPuntoDestino = 0;
    OperacionesCertificadas certificadas = null;


    if (entradaSalida.equals(Constantes.NOMBRE_ENTRADA)) {
      codigoPuntoDestino = registro.getCodigoPunto();
      codigoPuntoOrigen = puntosCodigoTdvService.getCodigoPunto(codigoPropio, registro.getTdv(),
          registro.getBancoAval(), registro.getCodigoDane());
      if (!codigoServicio.equals(SIN_CODIGO_SERVICIO)) {
        certificadas = operacionesCertificadasRepository
            .findByCodigoFondoTDVAndCodigoPuntoOrigenAndCodigoPuntoDestinoAndCodigoServicioTdvAndEntradaSalidaAndFechaEjecucionAndCodigoPropioTDVAndIdArchivoCargado(
                codigoPuntoDestino, codigoPuntoOrigen, codigoPuntoDestino, codigoServicio,
                Constantes.NOMBRE_ENTRADA, registro.getFechaEjecucion(), codigoPropio, idArchivo);
      }

    } else {

      codigoPuntoOrigen = registro.getCodigoPunto();
      codigoPuntoDestino = puntosCodigoTdvService.getCodigoPunto(codigoPropio, registro.getTdv(),
          registro.getBancoAval(), registro.getCodigoDane());

      if (!codigoServicio.equals(SIN_CODIGO_SERVICIO)) {
        certificadas = operacionesCertificadasRepository
            .findByCodigoFondoTDVAndCodigoPuntoOrigenAndCodigoPuntoDestinoAndCodigoServicioTdvAndEntradaSalidaAndFechaEjecucionAndCodigoPropioTDVAndIdArchivoCargado(
                codigoPuntoOrigen, codigoPuntoDestino, codigoPuntoOrigen, codigoServicio,
                Constantes.NOMBRE_SALIDA, registro.getFechaEjecucion(), codigoPropio, idArchivo);
      }

    }
    codigoPuntoOrigenDestino.setCertificadas(certificadas);
    codigoPuntoOrigenDestino.setCodigoPuntoDestino(codigoPuntoDestino);
    codigoPuntoOrigenDestino.setCodigoPuntoOrigen(codigoPuntoOrigen);
    return codigoPuntoOrigenDestino;
  }

  /**
   * Metodo encargado de obtener la entrada o la salida
   * 
   * @param entSal
   * @return String
   * @author cesar.castano
   */
  private String asignarEntradaSalida(String entSal) {
    var entradaSalida = "";
    if (entSal.equals(Constantes.ENTRADA)) {
      entradaSalida = Constantes.NOMBRE_ENTRADA;
    } else if (entSal.equals(Constantes.SALIDA)) {
      entradaSalida = Constantes.NOMBRE_SALIDA;
    } else if (entSal.equals(Constantes.SALIDA_BRINKS)) {
      entradaSalida = Constantes.NOMBRE_SALIDA;
    }
    return entradaSalida;
  }

  /**
   * Metodo encargado de asignar el codigo fondo TDV del registro tipo 01
   * 
   * @return Fondos
   * @author cesar.castano
   */
  private Fondos asignarFondo(String transportadora, String nit, String ciudad) {
    if (transportadora.equals("SEG")) {
      transportadora = "TVS";
    }
    var fondo = fondosService.getCodigoFondoCertificacion(transportadora, nit, ciudad);
    if (Objects.isNull(fondo)) {

      auditoriaProcesosService.actualizarAuditoriaProceso(Dominios.CODIGO_PROCESO_LOG_CERTIFICACION,
          this.fechaProceso, Constantes.ESTADO_PROCESO_ERROR,
          ApiResponseCode.ERROR_FONDOS_NO_ENCONTRADO.getDescription()
              + "no existe para transportadora = " + transportadora + " NIT = " + nit + " Ciudad = "
              + ciudad);
      throw new NegocioException(ApiResponseCode.ERROR_FONDOS_NO_ENCONTRADO.getCode(),
          ApiResponseCode.ERROR_FONDOS_NO_ENCONTRADO.getDescription()
              + "no existe para transportadora = " + transportadora + " NIT = " + nit + " Ciudad = "
              + ciudad,
          ApiResponseCode.ERROR_FONDOS_NO_ENCONTRADO.getHttpStatus());
    }
    return fondo;
  }

  /**
   * Metodo encargado de asignar la fecha
   * 
   * @param fila
   * @return Date
   * @author cesar.castano
   */
  private Date asignarFecha(String fila) {
    Date fecha = null;
    try {
      DateFormat formato = new SimpleDateFormat(dominioService
          .valorTextoDominio(Constantes.DOMINIO_FORMATO_FECHA, Dominios.FORMATO_FECHA_F1));
      fecha = formato.parse(fila);
    } catch (ParseException e) {
      e.getMessage();
    }
    return fecha;
  }

  /**
   * Metodo encargado de asignar el tipo de operacion
   *
   * @return String
   * @author cesar.castano
   */
  private String asignarTipoOperacion(String entradaSalida, Integer codPuntoOrigen,
      Integer codPuntoDestino) {

    var tipoOperacion = "";

    if (entradaSalida.equals(Constantes.NOMBRE_SALIDA)) {
      tipoOperacion = procesarProvisiones(codPuntoDestino);
      if (tipoOperacion.isEmpty()) {
        tipoOperacion = procesarConsignaciones(codPuntoDestino);
        if (tipoOperacion.isEmpty()) {
          tipoOperacion = this.procesarOperacionOtros(codPuntoDestino);
        }
      }
    } else if (entradaSalida.equals(Constantes.NOMBRE_ENTRADA)) {
      tipoOperacion = procesarRecolleciones(codPuntoOrigen);
      if (tipoOperacion.isEmpty()) {
        tipoOperacion = procesarRetiros(codPuntoOrigen);
        if (tipoOperacion.isEmpty()) {
          tipoOperacion = this.procesarOperacionOtros(codPuntoOrigen);
        }
      }
    }

    return tipoOperacion;
  }

  private String procesarOperacionOtros(Integer codigoPunto) {
    var tipoOperacion = "";
    var punto = puntosService.getPuntoById(codigoPunto);
    if (!Objects.isNull(punto) && "BANCO".equals(punto.getTipoPunto())) {
      tipoOperacion = dominioService.valorTextoDominio(Constantes.DOMINIO_TIPO_OPERACION,
          Dominios.TIPO_OPERA_VENTA);
    } else {
      tipoOperacion = dominioService.valorTextoDominio(Constantes.DOMINIO_TIPO_OPERACION,
          Dominios.TIPO_OPERA_TRASLADO);
    }
    return tipoOperacion;
  }

  /**
   * Metodo que asignar las provisiones
   * 
   * @param codigoPunto
   * @return String
   * @author cesar.castano
   */
  private String procesarProvisiones(Integer codigoPunto) {
    var tipoOperacion = "";
    if (oficinaService.getCodigoPuntoOficina(codigoPunto)
        || clientesCorporativosService.getCodigoPuntoCliente(codigoPunto)
        || cajerosService.getCodigoPuntoCajero(codigoPunto)) {
      tipoOperacion = dominioService.valorTextoDominio(Constantes.DOMINIO_TIPO_OPERACION,
          Dominios.TIPO_OPERA_PROVISION);
    }
    return tipoOperacion;
  }

  /**
   * Metodo que asignar las recolecciones
   * 
   * @param codigoPunto
   * @return String
   * @author cesar.castano
   */
  private String procesarRecolleciones(Integer codigoPunto) {
    var tipoOperacion = "";
    if (oficinaService.getCodigoPuntoOficina(codigoPunto)
        || clientesCorporativosService.getCodigoPuntoCliente(codigoPunto)
        || cajerosService.getCodigoPuntoCajero(codigoPunto)) {
      tipoOperacion = dominioService.valorTextoDominio(Constantes.DOMINIO_TIPO_OPERACION,
          Dominios.TIPO_OPERA_RECOLECCION);
    }
    return tipoOperacion;
  }

  /**
   * Metodo que asignar las consignaciones
   * 
   * @param codigoPunto
   * @return String
   * @author cesar.castano
   */
  private String procesarConsignaciones(Integer codigoPunto) {
    var tipoOperacion = "";
    if (puntosService.getEntidadPuntoBanrep(dominioService.valorTextoDominio(
        Constantes.DOMINIO_TIPOS_PUNTO, Dominios.TIPOS_PUNTO_BAN_REP), codigoPunto)) {
      tipoOperacion = dominioService.valorTextoDominio(Constantes.DOMINIO_TIPO_OPERACION,
          Dominios.TIPO_OPERA_CONSIGNACION);
    }
    return tipoOperacion;
  }

  /**
   * Metodo que asigna los retiros
   * 
   * @param codigoPunto
   * @return String
   * @author cesar.castano
   */
  private String procesarRetiros(Integer codigoPunto) {
    var tipoOperacion = "";
    if (puntosService.getEntidadPuntoBanrep(dominioService.valorTextoDominio(
        Constantes.DOMINIO_TIPOS_PUNTO, Dominios.TIPOS_PUNTO_BAN_REP), codigoPunto)) {
      tipoOperacion = dominioService.valorTextoDominio(Constantes.DOMINIO_TIPO_OPERACION,
          Dominios.TIPO_OPERA_RETIRO);
    }
    return tipoOperacion;
  }

  /**
   * Metodo encargado de asignar el valor del detalle
   * 
   * @param fila
   * @param numeroInicia
   * @param longitud
   * @return Double
   * @author cesar.castano
   */
  private Double asignarValorTotal(String[] fila, Integer numeroInicia, Integer longitud) {
    Double valorAcumulado = 0.0;
    int menorDenominacionCop = parametroService.valorParametroEntero(Constantes.MIN_DENOM_COP);
    for (var i = numeroInicia; i < longitud; i = i + 2) {
      Double denonimacion = Double.parseDouble(fila[i].trim());
      if (denonimacion >= menorDenominacionCop) {
        valorAcumulado = valorAcumulado + (denonimacion * Double.parseDouble(fila[i + 1].trim()));
      }
    }
    return valorAcumulado;
  }

  /**
   * Metodo encargado dedeterminar la moneda del registro tipo 3
   * 
   * @param fila
   * @param numeroInicia
   * @return String Código de la moneda
   * @author cesar.castano
   */
  private String asignarMoneda(String[] fila, Integer numeroInicia) {

    String moneda = Constantes.MONEDA_COP;
    try {
      moneda =
          dominioService.valorTextoDominio(Constantes.DOMINIO_DIVISAS, fila[numeroInicia].trim());
    } catch (AplicationException ae) {
      log.error("asignarMoneda: {}", ae.getMessage());
    }
    return moneda;
  }

  /**
   * Metodo encargado de guardar los valores sobrantes y faltantes en una lista
   *
   * @param ajusteValor
   * @author cesar.castano
   */
  private void guardarSobrantesyFaltantes(String tipoAjuste, String nombrePunto, Double valor,
      SobrantesFaltantesDTO ajusteValor, Date fecha, int codigoBanco, String tdv) {
    ajusteValor.setTipoAjuste(tipoAjuste);
    ajusteValor.setNombrePunto(nombrePunto);
    ajusteValor.setValor(valor);
    ajusteValor.setFecha(fecha);
    ajusteValor.setCodigoBanco(codigoBanco);
    ajusteValor.setTdv(tdv);
    listaAjustesValor.add(ajusteValor);
  }

  /**
   * Metodo encargado de procesar los valores faltantes y sobrantes del registro tipo 05
   * 
   * @author cesar.castano
   */
  private void procesarSobranteFaltanteBrinks() {
    for (var i = 0; i < listaAjustesValor.size(); i++) {
      if (listaAjustesValor.get(i).getTipoAjuste().equals(Constantes.SOBRANTE_BRINKS)) {
        log.info("Procesar sobrantes Brinks");
        actualizarValorSobrante(listaAjustesValor.get(i).getNombrePunto(),
            listaAjustesValor.get(i).getValor());
      } else if (listaAjustesValor.get(i).getTipoAjuste().equals(Constantes.FALTANTE_BRINKS)) {
        log.info("Procesar faltantes Brinks");
        actualizarValorFaltante(listaAjustesValor.get(i).getNombrePunto(),
            listaAjustesValor.get(i).getValor());
      }
    }
  }

  /**
   * Metodo encargado de procesar los valores faltantes y sobrantes del registro tipo 05
   * 
   * @author cesar.castano
   */
  private void procesarSobranteFaltanteNoBrinks() {
    for (var i = 0; i < listaAjustesValor.size(); i++) {
      if (listaAjustesValor.get(i).getTipoAjuste().equals(Constantes.SOBRANTE_OTROS_FONDOS)
          || listaAjustesValor.get(i).getTipoAjuste().equals(Constantes.SOBRANTE_TVS)) {
        actualizarValorSobranteNoBrinks(listaAjustesValor.get(i).getNombrePunto(),
            listaAjustesValor.get(i).getValor(), listaAjustesValor.get(i).getFecha(),
            listaAjustesValor.get(i).getTdv(), listaAjustesValor.get(i).getCodigoBanco());
      } else if (listaAjustesValor.get(i).getTipoAjuste().equals(Constantes.FALTANTE_OTROS_FONDOS)
          || listaAjustesValor.get(i).getTipoAjuste().equals(Constantes.FALTANTE_TVS)) {
        actualizarValorFaltanteNoBrinks(listaAjustesValor.get(i).getNombrePunto(),
            listaAjustesValor.get(i).getValor(), listaAjustesValor.get(i).getFecha(),
            listaAjustesValor.get(i).getTdv(), listaAjustesValor.get(i).getCodigoBanco());
      }
    }
  }

  /**
   * Metodo encargado de actualizar los valores sobrantes del registro 05
   * 
   * @param codigoServicio
   * @param valor
   * @author cesar.castano
   */
  private void actualizarValorSobrante(String codigoServicio, Double valor) {
    List<OperacionesCertificadas> ocertificadas =
        operacionesCertificadasRepository.findByCodigoServicioTdv(codigoServicio);
    if (Objects.isNull(ocertificadas)) {

      throw new NegocioException(
          ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getCode(),
          ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getDescription(),
          ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getHttpStatus());
    }
    for (OperacionesCertificadas operacionesCertificadas : ocertificadas) {
      operacionesCertificadas.setValorSobrante(valor);
      operacionesCertificadasRepository.save(operacionesCertificadas);
    }
  }

  /**
   * Metodo encargado de actualizar los valores faltantes del registro 05
   * 
   * @param codigoServicio
   * @param valor
   * @author cesar.castano
   */
  private void actualizarValorFaltante(String codigoServicio, Double valor) {
    List<OperacionesCertificadas> ocertificadas =
        operacionesCertificadasRepository.findByCodigoServicioTdv(codigoServicio);
    if (Objects.isNull(ocertificadas)) {

      throw new NegocioException(
          ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getCode(),
          ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getDescription(),
          ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getHttpStatus());
    }
    for (OperacionesCertificadas operacionesCertificadas : ocertificadas) {
      operacionesCertificadas.setValorFaltante(valor);
      operacionesCertificadasRepository.save(operacionesCertificadas);
    }
  }

  /**
   * Metodo encargado de actualizar los valores sobrantes del registro 05
   * 
   * @param nombrePunto
   * @param valor
   * @param fecha
   * @param tdv
   * @param codigoBanco
   */
  private void actualizarValorSobranteNoBrinks(String nombrePunto, Double valor, Date fecha,
      String tdv, int codigoBanco) {
    Integer codigoPunto =
        puntosCodigoTdvService.getCodigoPunto(nombrePunto, tdv, codigoBanco, "100");

    if (!Objects.isNull(codigoPunto)) {
      List<OperacionesCertificadas> ocertificadas = operacionesCertificadasRepository
          .findByCodigoPuntoDestinoAndEntradaSalidaAndFechaEjecucion(codigoPunto,
              Constantes.VALOR_SALIDA, fecha);

      if (Objects.isNull(ocertificadas)) {
        ocertificadas = operacionesCertificadasRepository
            .findByCodigoPuntoOrigenAndEntradaSalidaAndFechaEjecucion(codigoPunto,
                Constantes.VALOR_ENTRADA, fecha);
      }

      if (Objects.isNull(ocertificadas)) {

        throw new NegocioException(
            ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getCode(),
            ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getDescription(),
            ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getHttpStatus());
      }
      for (OperacionesCertificadas operacionesCertificadas : ocertificadas) {
        operacionesCertificadas.setValorSobrante(valor);
        operacionesCertificadasRepository.save(operacionesCertificadas);
        break;
      }
    }

  }

  /**
   * Metodo encargado de actualizar los valores faltantes del registro 05
   *
   * @param valor
   * @author cesar.castano
   */
  private void actualizarValorFaltanteNoBrinks(String nombrePunto, Double valor, Date fecha,
      String tdv, int codigoBanco) {
    Integer codigoPunto =
        puntosCodigoTdvService.getCodigoPunto(nombrePunto, tdv, codigoBanco, "100");

    if (!Objects.isNull(codigoPunto)) {
      List<OperacionesCertificadas> ocertificadas = operacionesCertificadasRepository
          .findByCodigoPuntoDestinoAndEntradaSalidaAndFechaEjecucion(codigoPunto, "SALIDAS", fecha);

      if (Objects.isNull(ocertificadas)) {
        ocertificadas = operacionesCertificadasRepository
            .findByCodigoPuntoOrigenAndEntradaSalidaAndFechaEjecucion(codigoPunto, "ENTRADAS",
                fecha);
      }

      if (Objects.isNull(ocertificadas)) {

        throw new NegocioException(
            ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getCode(),
            ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getDescription(),
            ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getHttpStatus());
      }
      for (OperacionesCertificadas operacionesCertificadas : ocertificadas) {
        operacionesCertificadas.setValorFaltante(valor);
        operacionesCertificadasRepository.save(operacionesCertificadas);
        break;
      }
    }

  }

  /**
   * Metodo encargado de procesar los archivos de la Brinks
   * 
   * @param elemento
   * @author cesar.castano
   */
  @Transactional
  @Override
  public void procesarArchivoBrinks(ArchivosCargados elemento,
      List<DetallesDefinicionArchivoDTO> detalleArchivo) {
    log.info("Archivo: {}", elemento.getNombreArchivo());
    var registro = new RegistroTipo1ArchivosFondosDTO();
    listaAjustesValor = new ArrayList<>();
    this.fechaHoy = new Date();
    this.fechaProceso = parametroService.valorParametroDate(Constantes.FECHA_DIA_PROCESO);
    this.estadoConciliacion = dominioService.valorTextoDominio(
        Constantes.DOMINIO_ESTADO_CONCILIACION, Dominios.ESTADO_CONCILIACION_NO_CONCILIADO);
    List<RegistrosCargados> registrosOrdenados = elemento.getRegistrosCargados();
    String[] fila;
    Integer tipoReg;
    Long consecutivoRegistro;
    SobrantesFaltantesDTO ajusteValor;

    for (var i = 0; i < elemento.getRegistrosCargados().size(); i++) {

      ajusteValor = new SobrantesFaltantesDTO();
      fila = elemento.getRegistrosCargados().get(i).getContenido().split(", ");
      tipoReg = Integer.parseInt(determinarTipoRegistro(fila, detalleArchivo));
      consecutivoRegistro = registrosOrdenados.get(i).getId().getConsecutivoRegistro().longValue();

      switch (tipoReg) {
        case 1: {
          String tdv = determinarCampo(fila, detalleArchivo, tipoReg,
              Constantes.CAMPO_DETALLE_ARCHIVO_SIGLATRANSPORTADORA);
          String nit = determinarCampo(fila, detalleArchivo, tipoReg,
              Constantes.CAMPO_DETALLE_ARCHIVO_CODIGONITENTIDAD);
          String nitbanco = nit.substring(0, 9);
          String ciudad = determinarCampo(fila, detalleArchivo, tipoReg,
              Constantes.CAMPO_DETALLE_ARCHIVO_CODIGODANE);
          String ciudad1 = String.valueOf(Integer.parseInt(ciudad.trim()));

          String codigoDaneCiudad =
              ciudadesService.getCiudadPorCodigoDaneOrCodigoBrinks(ciudad1).getCodigoDANE();

          Fondos fondo = asignarFondo(tdv, nitbanco, ciudad1);
          registro.setTdv(fondo.getTdv());
          registro.setCodigoPunto(fondo.getCodigoPunto());
          Date fecha = determinarFechaEjecucion(fila, detalleArchivo, tipoReg,
              Constantes.CAMPO_DETALLE_ARCHIVO_FECHAEJECUCION);
          registro.setFechaEjecucion(fecha);
          registro.setBancoAval(fondo.getBancoAVAL());
          registro.setCodigoDane(codigoDaneCiudad);

          break;
        }
        case 2, 4, 6, 7: {
          break;
        }
        case 3: {
          String codigoServicio = determinarCampo(fila, detalleArchivo, tipoReg,
              Constantes.CAMPO_DETALLE_ARCHIVO_CODIGOSERVTRANS);
          String entradaSalida = determinarCampo(fila, detalleArchivo, tipoReg,
              Constantes.CAMPO_DETALLE_ARCHIVO_ENTRADAOSALIDA);
          String codigoPunto = determinarCampo(fila, detalleArchivo, tipoReg,
              Constantes.CAMPO_DETALLE_ARCHIVO_CODIGOPUNTO).trim();
          String nombrePunto = determinarCampo(fila, detalleArchivo, tipoReg,
              Constantes.CAMPO_DETALLE_ARCHIVO_NOMBREPUNTO).trim();
          String tipoServicio = determinarCampo(fila, detalleArchivo, tipoReg,
              Constantes.CAMPO_DETALLE_ARCHIVO_TIPOSERVICIOF);
          String codigoOperacion = determinarCampo(fila, detalleArchivo, tipoReg,
              Constantes.CAMPO_DETALLE_ARCHIVO_CODIGOOPERACION);

          if (Objects.isNull(codigoServicio) || codigoServicio.trim().isEmpty()) {
            codigoServicio = SIN_CODIGO_SERVICIO;
          }
          
          if(StringUtils.isEmpty(tipoServicio)) {
            tipoServicio = Constantes.VALOR_DOMINIO_TIPO_SERVICIO_PROGRAMADA;
          }
          
          procesarOperacionTransporte(fila, registro, elemento, codigoServicio, entradaSalida,
              codigoPunto, nombrePunto, tipoServicio, codigoOperacion, consecutivoRegistro);
          break;
        }
        case 5: {
          String tipoNovedad = determinarCampo(fila, detalleArchivo, tipoReg,
              Constantes.CAMPO_DETALLE_ARCHIVO_TIPONOVEDAD);
          String codigoServicio = determinarCampo(fila, detalleArchivo, tipoReg,
              Constantes.CAMPO_DETALLE_ARCHIVO_CODIGOSERVICIO);
          String montoTotal = determinarCampo(fila, detalleArchivo, tipoReg,
              Constantes.CAMPO_DETALLE_ARCHIVO_VALORNOVEDAD);
          guardarSobrantesyFaltantes(tipoNovedad, codigoServicio, Double.parseDouble(montoTotal),
              ajusteValor, elemento.getFechaArchivo(), registro.getBancoAval(), registro.getTdv());
          break;
        }
        default:
          auditoriaProcesosService.actualizarAuditoriaProceso(
              Dominios.CODIGO_PROCESO_LOG_CERTIFICACION, this.fechaProceso,
              Constantes.ESTADO_PROCESO_ERROR,
              ApiResponseCode.ERROR_TIPO_REGISTRO_NO_VALIDO.getDescription());

          throw new NegocioException(ApiResponseCode.ERROR_TIPO_REGISTRO_NO_VALIDO.getCode(),
              ApiResponseCode.ERROR_TIPO_REGISTRO_NO_VALIDO.getDescription(),
              ApiResponseCode.ERROR_TIPO_REGISTRO_NO_VALIDO.getHttpStatus());
      }
    }
    procesarSobranteFaltanteBrinks();
    if (Dominios.ESTADO_VALIDACION_REPROCESO.equals(elemento.getEstadoCargue())) {
      elemento.setEstadoCargue(Dominios.ESTADO_VALIDACION_REPROCESADO);
    } else {
      elemento.setEstadoCargue(Dominios.ESTADO_VALIDACION_ACEPTADO);
    }
    log.info("Actualizar archivos cargados: {}", elemento.getNombreArchivo());
    archivosCargadosService.actualizarArchivosCargados(elemento);
  }

  /**
   * Metodo para determinar el campo Tipo de Registro
   * 
   * @param contenido
   * @param detalleArchivo
   * @return String
   * @author cesar.castano
   */
  private String determinarTipoRegistro(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detalleArchivo) {
    return contenido[detalleArchivo.stream()
        .filter(deta -> deta.getNombreCampo().toUpperCase().trim()
            .equals(Constantes.CAMPO_DETALLE_ARCHIVO_TIPOREGISTRO)
            && deta.getId().getTipoRegistro().equals(Integer.parseInt(contenido[0])))
        .findFirst().orElse(null).getId().getNumeroCampo() - 1].trim();
  }

  /**
   * Metodo para determinar el campo Fecha de Ejecucion
   * 
   * @param contenido
   * @param detalleArchivo
   * @param tipoRegistro
   * @return String
   * @author cesar.castano
   */
  private Date determinarFechaEjecucion(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detalleArchivo, Integer tipoRegistro, String constante) {
    return asignarFecha(contenido[detalleArchivo.stream()
        .filter(deta -> deta.getNombreCampo().toUpperCase().trim().equals(constante)
            && deta.getId().getTipoRegistro().equals(tipoRegistro))
        .findFirst().orElse(null).getId().getNumeroCampo() - 1].trim());
  }

  /**
   * Metodo para determinar el campo
   * 
   * @param contenido
   * @param detalleArchivo
   * @param tipoRegistro
   * @param constante
   * @return String
   * @author cesar.castano
   */
  private String determinarCampo(String[] contenido,
      List<DetallesDefinicionArchivoDTO> detalleArchivo, Integer tipoRegistro, String constante) {
    return contenido[detalleArchivo.stream()
        .filter(detalle -> detalle.getNombreCampo().toUpperCase().trim().equals(constante)
            && detalle.getId().getTipoRegistro().equals(tipoRegistro))
        .findFirst().orElse(null).getId().getNumeroCampo() - 1].trim();
  }

  @Override
  public String procesarArchivosAlcance() {
    return operacionesCertificadasRepository.procesarArchivosAlcance();
  }

}
