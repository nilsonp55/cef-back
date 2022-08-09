package com.ath.adminefectivo.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de manejar la logica de la tabla OPERACIONES_PROGRAMADAS
 * @author cesar.castano
 *
 */
@Entity
@Table(name = "OPERACIONES_PROGRAMADAS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "OperacionesProgramadas.findAll", query = "SELECT t FROM OperacionesProgramadas t")
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
	
}
