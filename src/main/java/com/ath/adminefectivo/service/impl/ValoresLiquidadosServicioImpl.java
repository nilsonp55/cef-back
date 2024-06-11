package com.ath.adminefectivo.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.ParametrosLiquidacionCostoDTO;
import com.ath.adminefectivo.dto.ValorLiquidadoDTO;
import com.ath.adminefectivo.dto.ValoresLiquidadosDTO;
import com.ath.adminefectivo.dto.compuestos.CostosCharterDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.LogProcesoDiario;
import com.ath.adminefectivo.entities.Puntos;
import com.ath.adminefectivo.entities.ValoresLiquidados;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.IOficinasRepository;
import com.ath.adminefectivo.repositories.IValoresLiquidadosRepository;
import com.ath.adminefectivo.repositories.LogProcesoDiarioRepository;
import com.ath.adminefectivo.service.IAuditoriaProcesosService;
import com.ath.adminefectivo.service.ICiudadesService;
import com.ath.adminefectivo.service.IDominioService;
import com.ath.adminefectivo.service.IParametrosLiquidacionCostosService;
import com.ath.adminefectivo.service.IPuntosService;
import com.ath.adminefectivo.service.ITransportadorasService;
import com.ath.adminefectivo.service.IValoresLiquidadosService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ValoresLiquidadosServicioImpl implements IValoresLiquidadosService {

  @Autowired
  IValoresLiquidadosRepository valoresLiquidadosRepository;

  @Autowired
  IOficinasRepository oficinasRepository;

  @Autowired
  ParametroServiceImpl parametroServiceImpl;

  @Autowired
  LogProcesoDiarioRepository logProcesoDiarioRepository;

  @Autowired
  IPuntosService puntosService;

  @Autowired
  IDominioService dominioService;

  @Autowired
  ITransportadorasService transportadorasService;

  @Autowired
  ICiudadesService ciudadesService;

  @Autowired
  IParametrosLiquidacionCostosService parametrosLiquidacionCostosService;

  @Autowired
  IAuditoriaProcesosService auditoriaProcesosService;

  /**
   * {@inheritDoc}
   */
  @Override
  public Boolean actualizaCostosFletesCharter(CostosCharterDTO costos) {
    Boolean estado = false;
    try {
      ValoresLiquidados valores =
          valoresLiquidadosRepository.consultarPorIdLiquidacion(costos.getIdLiquidacion());
      if (Objects.isNull(valores)) {
        throw new NegocioException(ApiResponseCode.ERROR_VALORES_LIQUIDADOS_NO_ENCONTRADO.getCode(),
            ApiResponseCode.ERROR_VALORES_LIQUIDADOS_NO_ENCONTRADO.getDescription(),
            ApiResponseCode.ERROR_VALORES_LIQUIDADOS_NO_ENCONTRADO.getHttpStatus());
      }
      valores.setParametrosLiquidacionCosto(parametrosLiquidacionCostosService
          .getParametrosLiquidacionCostosById(costos.getIdLiquidacion()).orElse(null));
      valores.setCostoCharter(costos.getCostosCharter());
      valoresLiquidadosRepository.save(valores);
      estado = true;
    } catch (Exception e) {
      throw new NegocioException(ApiResponseCode.ERROR_VALORES_LIQUIDADOS_NO_ENCONTRADO.getCode(),
          ApiResponseCode.ERROR_VALORES_LIQUIDADOS_NO_ENCONTRADO.getDescription(),
          ApiResponseCode.ERROR_VALORES_LIQUIDADOS_NO_ENCONTRADO.getHttpStatus());
    }
    return estado;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String procesarPackageCostos() {
    // Se obtiene fecha sistema para validaciones
    Date fecha = parametroServiceImpl.valorParametroDate(Constantes.FECHA_DIA_PROCESO);

    // Se valida que la conciliacion este cerrada para poder ejecutar el package
    List<LogProcesoDiario> logProcesoDiarios =
        logProcesoDiarioRepository.findByFechaCreacion(fecha);
    auditoriaProcesosService.actualizarAuditoriaProceso(Dominios.CODIGO_PROCESO_LOG_LIQUIDACION,
        fecha, Constantes.ESTADO_PROCESO_INICIO, Constantes.ESTADO_PROCESO_PROCESO);

    var procesoDiarioConciliacionCerrad = false;
    var procesoDiarioLiquidacionPendiente = false;
    for (int i = 0; i < logProcesoDiarios.size(); i++) {
      LogProcesoDiario item = logProcesoDiarios.get(i);
      if (item.getCodigoProceso().equals(Dominios.CODIGO_PROCESO_LOG_CONCILIACION)) {
        procesoDiarioConciliacionCerrad =
            item.getEstadoProceso().equals(Dominios.ESTADO_PROCESO_DIA_COMPLETO);
      }
      if (item.getCodigoProceso().equals(Dominios.CODIGO_PROCESO_LOG_LIQUIDACION)) {
        procesoDiarioLiquidacionPendiente =
            item.getEstadoProceso().equals(Dominios.ESTADO_PROCESO_DIA_PENDIENTE);
      }
    }

    if (!procesoDiarioConciliacionCerrad || !procesoDiarioLiquidacionPendiente) {
      auditoriaProcesosService.actualizarAuditoriaProceso(Dominios.CODIGO_PROCESO_LOG_LIQUIDACION,
          fecha, Constantes.ESTADO_PROCESO_ERROR,
          ApiResponseCode.ERROR_PROCESO_CONSTO_VALORES_LIQUIDADOS.getDescription());
      throw new NegocioException(
          ApiResponseCode.ERROR_PROCESO_VALIDACION_CIERRE_CONSTO_VALORES_LIQUIDADOS.getCode(),
          ApiResponseCode.ERROR_PROCESO_VALIDACION_CIERRE_CONSTO_VALORES_LIQUIDADOS
              .getDescription(),
          ApiResponseCode.ERROR_PROCESO_VALIDACION_CIERRE_CONSTO_VALORES_LIQUIDADOS
              .getHttpStatus());
    }
    try {
      // Se ejecutan procedimientos
      String parametro = valoresLiquidadosRepository.armarParametrosLiquida(fecha);

      if (Integer.parseInt(parametro) <= 0) {
        auditoriaProcesosService.actualizarAuditoriaProceso(Dominios.CODIGO_PROCESO_LOG_LIQUIDACION,
            fecha, Constantes.ESTADO_PROCESO_ERROR,
            ApiResponseCode.ERROR_PROCESO_CONSTO_VALORES_LIQUIDADOS_SIN_PARAM.getDescription());
        throw new NegocioException(
            ApiResponseCode.ERROR_PROCESO_CONSTO_VALORES_LIQUIDADOS_SIN_PARAM.getCode(),
            ApiResponseCode.ERROR_PROCESO_CONSTO_VALORES_LIQUIDADOS_SIN_PARAM.getDescription(),
            ApiResponseCode.ERROR_PROCESO_CONSTO_VALORES_LIQUIDADOS_SIN_PARAM.getHttpStatus());
      }
      auditoriaProcesosService.actualizarAuditoriaProceso(Dominios.CODIGO_PROCESO_LOG_LIQUIDACION,
          fecha, Constantes.ESTADO_PROCESO_PROCESO, "Terminó armado de parámetros de liquidación");

      valoresLiquidadosRepository.liquidarCostos(Integer.parseInt(parametro));
      auditoriaProcesosService.actualizarAuditoriaProceso(Dominios.CODIGO_PROCESO_LOG_LIQUIDACION,
          fecha, Constantes.ESTADO_PROCESO_PROCESADO, Constantes.ESTRUCTURA_OK);
      return "Se proceso con exito";
    } catch (Exception e) {
      auditoriaProcesosService.actualizarAuditoriaProceso(Dominios.CODIGO_PROCESO_LOG_LIQUIDACION,
          fecha, Constantes.ESTADO_PROCESO_ERROR,
          ApiResponseCode.ERROR_PROCESO_CONSTO_VALORES_LIQUIDADOS.getDescription());

      throw new NegocioException(ApiResponseCode.ERROR_PROCESO_CONSTO_VALORES_LIQUIDADOS.getCode(),
          ApiResponseCode.ERROR_PROCESO_CONSTO_VALORES_LIQUIDADOS.getDescription() + "  " + e,
          ApiResponseCode.ERROR_PROCESO_CONSTO_VALORES_LIQUIDADOS.getHttpStatus());
    }
  }

  /**
   * 
   * @return
   */
  @Override
  public ValoresLiquidadosDTO consultarLiquidacionCostos() {

    int idSeq = 0;

    Date fechaActual = parametroServiceImpl.valorParametroDate(Constantes.FECHA_DIA_PROCESO);
    List<ParametrosLiquidacionCostoDTO> respuestaLiquidarCostosDTO =
        this.generarRespuestaLiquidacionCostos(fechaActual);
    ValoresLiquidadosDTO valoresLiquidadosDTO = new ValoresLiquidadosDTO();
    // Sacamos los dos cumero de la cadena de texto


    Integer cantidad1 = 0;
    Integer cantidad2 = 0;
    if (!respuestaLiquidarCostosDTO.isEmpty()) {
      idSeq = respuestaLiquidarCostosDTO.get(0).getSeqGrupo();
      cantidad1 = valoresLiquidadosRepository.consultarCantidadValoresLiquidadosByIdSeqGrupo(idSeq);
      cantidad2 =
          valoresLiquidadosRepository.consultarCantidadErroresValoresLiquidadosByIdSeqGrupo(idSeq);
    }

    valoresLiquidadosDTO.setRespuestaLiquidarCostos(respuestaLiquidarCostosDTO);
    valoresLiquidadosDTO.setCantidadOperacionesLiquidadas(cantidad1);
    valoresLiquidadosDTO.setRegistrosConError(cantidad2);

    return valoresLiquidadosDTO;

  }

  /**
   * Metodo encargado de realizar la respuesta de la liquidacion de costos que es el DTO el cual se
   * muestra en la tabla valores liquidados consulta
   *
   * @param valoresLiquidados
   * @return List<ParametrosLiquidacionCostoDTO>
   * @author duvan.naranjo
   */

  private List<ParametrosLiquidacionCostoDTO> generarRespuestaLiquidacionCostos(Date fechaSistema) {
    log.debug(fechaSistema);
    List<ParametrosLiquidacionCostoDTO> respuest =
        parametrosLiquidacionCostosService.consultarParametrosLiquidacionCostos(fechaSistema);

    respuest.forEach(valorLiquidado -> {
      valorLiquidado.setNombreBanco(puntosService.getNombrePunto(dominioService
          .valorTextoDominio(Constantes.DOMINIO_TIPOS_PUNTO, Dominios.TIPOS_PUNTO_BANCO),
          valorLiquidado.getCodigoBanco()));

      valorLiquidado.setNombreTdv(
          transportadorasService.getNombreTransportadora(valorLiquidado.getCodigoTdv()));

      Puntos puntoOrigen = puntosService.getPuntoById(valorLiquidado.getPuntoOrigen());
      Puntos puntoDestino = puntosService.getPuntoById(valorLiquidado.getPuntoDestino());

      if ("ENTRADA".equals(valorLiquidado.getEntradaSalida())) {
        valorLiquidado.setNombrePunto(puntoOrigen.getNombrePunto());
        valorLiquidado.setNombreFondo(puntoDestino.getNombrePunto());
        if (!Objects.isNull(puntoOrigen.getCodigoCiudad())) {
          valorLiquidado
              .setNombreCiudadPunto(ciudadesService.getNombreCiudad(puntoOrigen.getCodigoCiudad()));
        }
        puntoDestino = puntoOrigen;
      } else {
        valorLiquidado.setNombreFondo(puntoOrigen.getNombrePunto());
        valorLiquidado.setNombrePunto(puntoDestino.getNombrePunto());
        if (!Objects.isNull(puntoDestino.getCodigoCiudad())) {
          valorLiquidado.setNombreCiudadPunto(
              ciudadesService.getNombreCiudad(puntoDestino.getCodigoCiudad()));
        }
      }

      if (!"CLIENTE".equals(valorLiquidado.getTipoPunto())) {
        valorLiquidado.setNombreCliente(valorLiquidado.getNombreBanco());
      }

      if ("OFICINA".equals(valorLiquidado.getTipoPunto())) {
        var oficina = oficinasRepository.findByCodigoPunto(puntoDestino.getCodigoPunto());
        valorLiquidado.setCodigoOficina(oficina.getCodigoOficina());
      }

      // se calcula total liquidado:
      valorLiquidado
          .setTotalLiquidado(calcularTotalLiquidado(valorLiquidado.getValoresLiquidadosDTO()));

    });

    return respuest;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ValorLiquidadoDTO consultarValoresLiquidadosPorIdLiquidacion(Long idLiquidacion) {
    ValoresLiquidados valorLiquidado =
        valoresLiquidadosRepository.consultarPorIdLiquidacion(idLiquidacion);
    if (!Objects.isNull(valorLiquidado)) {
      return ValorLiquidadoDTO.CONVERTER_DTO.apply(valorLiquidado);
    }
    return new ValorLiquidadoDTO();

  }

  private Double calcularTotalLiquidado(ValorLiquidadoDTO valoresLiquidados) {

    double total = valoresLiquidados.getClasificacionFajado()
        + valoresLiquidados.getClasificacionNoFajado() + valoresLiquidados.getCostoCharter()
        + valoresLiquidados.getCostoEmisario() + valoresLiquidados.getCostoFijoParada()
        + valoresLiquidados.getCostoMoneda() + valoresLiquidados.getCostoPaqueteo()
        + valoresLiquidados.getMilajePorRuteo() + valoresLiquidados.getMilajeVerificacion()
        + valoresLiquidados.getModenaResiduo() + valoresLiquidados.getTasaAeroportuaria();
    return total;
  }
  
}
