package com.ath.adminefectivo.dto;

import java.util.Date;
import java.util.function.Function;

import com.ath.adminefectivo.entities.OperacionesProgramadas;
import com.ath.adminefectivo.utils.UtilsObjects;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Clase que contiene la estructura del archivo de Operaciones Programadas No Conciliadas
 * 
 * @author cesar.castano
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Log4j2
public class ProgramadasNoConciliadasDTO {

  @JsonProperty("idOperacion")
  private Integer idOperacion;

  @JsonProperty("codigoFondoTDV")
  private Integer codigoFondoTDV;

  @JsonProperty("entradaSalida")
  private String entradaSalida;

  @JsonProperty("codigoPuntoOrigen")
  private Integer codigoPuntoOrigen;

  @JsonProperty("codigoPuntoDestino")
  private Integer codigoPuntoDestino;

  @JsonProperty("fechaProgramacion")
  private Date fechaProgramacion;

  @JsonProperty("fechaOrigen")
  private Date fechaOrigen;

  @JsonProperty("fechaDestino")
  private Date fechaDestino;

  @JsonProperty("tipoOperacion")
  private String tipoOperacion;

  @JsonProperty("tipoTransporte")
  private String tipoTransporte;

  @JsonProperty("valorTotal")
  private Double valorTotal;

  @JsonProperty("estadoOperacion")
  private String estadoOperacion;

  @JsonProperty("idNegociacion")
  private String idNegociacion;

  @JsonProperty("tasaNegociacion")
  private String tasaNegociacion;

  @JsonProperty("estadoConciliacion")
  private String estadoConciliacion;

  @JsonProperty("idArchivoCargado")
  private Integer idArchivoCargado;

  @JsonProperty("idOperacionRelac")
  private Integer idOperacionRelac;

  @JsonProperty("tipoServicio")
  private String tipoServicio;

  @JsonProperty("usuarioCreacion")
  private String usuarioCreacion;

  @JsonProperty("usuarioModificacion")
  private String usuarioModificacion;

  @JsonProperty("fechaCreacion")
  private Date fechaCreacion;

  @JsonProperty("fechaModificacion")
  private Date fechaModificacion;

  @JsonProperty("idServicio")
  private String idServicio;

  @JsonProperty("ciudadOrigen")
  private String nombreCiudadOrigen;

  @JsonProperty("ciudadDestino")
  private String nombreCiudadDestino;

  private String tdv;

  private String bancoAVAL;

  private String nombreFondoTDV;

  private String nombrePuntoOrigen;

  private String nombrePuntoDestino;

  private String tipoPuntoOrigen;

  private String tipoPuntoDestino;

  private String oficina;

  /**
   * Funcion que convierte el archivo DTO ProgramadasNoConciliadasDTO a Entity
   * OperacionesProgramadas
   * 
   * @author cesar.castano
   */
  public static final Function<ProgramadasNoConciliadasDTO, OperacionesProgramadas> CONVERTER_ENTITY =
      (ProgramadasNoConciliadasDTO t) -> {
        log.debug("Converter_ENTITY operacion programada: {}", t.getIdOperacion());
        var operacionesProgramadas = new OperacionesProgramadas();
        UtilsObjects.copiarPropiedades(t, operacionesProgramadas);

        return operacionesProgramadas;
      };

  /**
   * Funcion que convierte la entity OperacionesProgramadas a archivo DTO
   * ProgramadasNoConciliadasDTO
   * 
   * @author cesar.castano
   */
  public static final Function<OperacionesProgramadas, ProgramadasNoConciliadasDTO> CONVERTER_DTO =
      (OperacionesProgramadas t) -> {
        log.debug("Converter_DTO operacion programada: {}", t.getIdOperacion());
        var operacionesProgramadasDto = new ProgramadasNoConciliadasDTO();
        UtilsObjects.copiarPropiedades(t, operacionesProgramadasDto);

        return operacionesProgramadasDto;
      };
}
