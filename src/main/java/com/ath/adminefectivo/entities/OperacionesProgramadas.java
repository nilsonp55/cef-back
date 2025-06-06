package com.ath.adminefectivo.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.ath.adminefectivo.dto.compuestos.OperacionIntradiaDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad encargada de manejar la logica de la tabla OPERACIONES_PROGRAMADAS
 * 
 * @author cesar.castano
 *
 */
@NamedNativeQuery(name = "OperacionesProgramadas.consultaOperacionesIntradiaSalida",
    query = "select fo.banco_aval as bancoAVAL, op.codigo_punto_destino as codigoPunto, op.entrada_salida as entradaSalida "
        + "from operaciones_programadas  op, fondos fo, bancos ba "
        + "where fo.codigo_punto  = op.codigo_fondo_tdv  "
        + "and ba.codigo_punto  = op.codigo_punto_destino  " + "and ba.es_aval  = false "
        + "and ba.cobra_intraday  = true " + "and op.tipo_operacion  in (:tipoOperacion) "
        + "and op.entrada_salida  = :entradaSalida "
        + "and op.fecha_origen  between  :fechaInicio and :fechaFin "
        + "group by fo.banco_aval, op.codigo_punto_destino, op.entrada_salida  ",
    resultSetMapping = "Mapping.OperacionIntradiaDTO")

@NamedNativeQuery(name = "OperacionesProgramadas.consultaOperacionesIntradiaEntrada",
    query = "select fo.banco_aval as bancoAVAL, op.codigo_punto_origen as codigoPunto, op.entrada_salida as entradaSalida "
        + "from operaciones_programadas  op, fondos fo, bancos ba "
        + "where fo.codigo_punto  = op.codigo_fondo_tdv  "
        + "and ba.codigo_punto  = op.codigo_punto_origen  " + "and ba.es_aval  = false "
        + "and ba.cobra_intraday  = true " + "and op.tipo_operacion  in (:tipoOperacion) "
        + "and op.entrada_salida  = :entradaSalida "
        + "and op.fecha_origen  between  :fechaInicio and :fechaFin "
        + "group by fo.banco_aval, op.codigo_punto_origen, op.entrada_salida  ",
    resultSetMapping = "Mapping.OperacionIntradiaDTO")

@SqlResultSetMapping(name = "Mapping.OperacionIntradiaDTO",
    classes = @ConstructorResult(targetClass = OperacionIntradiaDTO.class,
        columns = {@ColumnResult(name = "bancoAVAL"), @ColumnResult(name = "codigoPunto"),
            @ColumnResult(name = "entradaSalida")}))
@Entity
@Table(name = "OPERACIONES_PROGRAMADAS")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "OperacionesProgramadas.findAll",
    query = "SELECT t FROM OperacionesProgramadas t")
public class OperacionesProgramadas {

  @Id
  @Column(name = "ID_OPERACION")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer idOperacion;

  @Column(name = "CODIGO_FONDO_TDV")
  private Integer codigoFondoTDV;

  @Column(name = "ENTRADA_SALIDA")
  private String entradaSalida;

  @Column(name = "CODIGO_PUNTO_ORIGEN")
  private Integer codigoPuntoOrigen;

  @Column(name = "CODIGO_PUNTO_DESTINO")
  private Integer codigoPuntoDestino;

  @Column(name = "FECHA_PROGRAMACION")
  private Date fechaProgramacion;

  @Column(name = "FECHA_ORIGEN")
  private Date fechaOrigen;

  @Column(name = "FECHA_DESTINO")
  private Date fechaDestino;

  @Column(name = "TIPO_OPERACION")
  private String tipoOperacion;

  @Column(name = "TIPO_TRANSPORTE")
  private String tipoTransporte;

  @Column(name = "VALOR_TOTAL")
  private Double valorTotal;

  @Column(name = "ESTADO_OPERACION")
  private String estadoOperacion;

  @Column(name = "ID_NEGOCIACION")
  private String idNegociacion;

  @Column(name = "TASA_NEGOCIACION")
  private String tasaNegociacion;

  @Column(name = "ESTADO_CONCILIACION")
  private String estadoConciliacion;

  @Column(name = "ID_ARCHIVO_CARGADO")
  private Integer idArchivoCargado;

  @Column(name = "ID_OPERACION_RELAC")
  private Integer idOperacionRelac;

  @Column(name = "TIPO_SERVICIO")
  private String tipoServicio;

  @Column(name = "USUARIO_CREACION")
  private String usuarioCreacion;

  @Column(name = "USUARIO_MODIFICACION")
  private String usuarioModificacion;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "FECHA_CREACION")
  private Date fechaCreacion;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "FECHA_MODIFICACION")
  private Date fechaModificacion;

  @Column(name = "ES_CAMBIO")
  private boolean esCambio;

  @Column(name = "ID_SERVICIO")
  private String idServicio;

  @OneToMany(mappedBy = "operacionesProgramadas")
  private List<ConciliacionServicios> conciliacionServicios;

  @OneToMany(mappedBy = "operacionesProgramadas")
  private List<DetalleOperacionesProgramadas> detalleOperacionesProgramadas;

  @Column(name = "COMISION_BR")
  private Integer comisionBR;

  @Column(name = "ES_ENTRADA")
  private Boolean esEntrada;

  @Column(name = "ID_ORDEN_TDV")
  private Integer idOrdenTDV;

  @Column(name = "CODIGO_MONEDA")
  private String codigoMoneda;

  @Column(name = "NOMBRE_TDV")
  private String tdv;

  @Column(name = "NOMBRE_BANCO_AVAL")
  private String bancoAVAL;

  @Column(name = "TIPO_PUNTO_ORIGEN")
  private String tipoPuntoOrigen;

  @Column(name = "TIPO_PUNTO_DESTINO")
  private String tipoPuntoDestino;
  
  /*
  * HU006 Determina si se liquida costo para la operacion
  */
  @Column(name = "LIQUIDA_COSTO")
 private boolean liquidaCosto;

  @Transient
  private String nombreBanco;

  @Transient
  private String nombreTransportadora;

  @Transient
  private String nombreCiudadOrigen;

  @Transient
  private String nombreCiudadDestino;

  @Transient
  private String nombrePuntoOrigen;

  @Transient
  private String nombrePuntoDestino;

  @Transient
  private String tipoConciliacion;

  @Transient
  private Integer idConciliacion;

  @Transient
  private Date fechaEjecucion;

  @Transient
  private String nombreFondoTDV;

  @Transient
  private String oficina;
}
