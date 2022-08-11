package com.ath.adminefectivo.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de manejar la logica de la tabla CuentasPuc
 * @author cesar.castano
 *
 */

@Entity
@Table(name = "TRANSACCIONES_INTERNAS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "TransaccionesInternas.findAll", query = "SELECT t FROM TransaccionesInternas t")
public class TransaccionesInternas {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_TRANSACCIONES_INTERNAS")
	private Long idTransaccionesInternas;
	
	@Column(name = "CONSECUTIVO_DIA")
	private String consecutivoDia;
	
	@ManyToOne
	@JoinColumn(name = "BANCO_AVAL", nullable = false)
	private Bancos bancoAval;
	
	@Column(name = "FECHA")
	private Date fecha;
	
	@ManyToOne
	@JoinColumn(name = "ID_OPERACION", nullable = true)
	private OperacionesProgramadas idOperacion;
	
	@Column(name = "ID_GENERICO")
	private Integer idGenerico;
	
	@Column(name = "TIPO_TRANSACCION")
	private Integer tipoTransaccion;
	
	@Column(name = "CODIGO_MONEDA")
	private String codigoMoneda;
	
	@Column(name = "VALOR")
	private Integer valor;
	
	@Column(name = "TASA_MO_EJE")
	private Integer tasaNoEje;
	
	@Column(name = "TASA_EJE_COP")
	private Integer tasaEjeCop;
	
	@ManyToOne
	@JoinColumn(name = "CODIGO_PUNTO", nullable = false)
	private Puntos codigoPunto;
	
	@Column(name = "TIPO_OPERACION")
	private String tipoOperacion;
	
	@Column(name = "CODIGO_COMISION")
	private Integer codigoComision;
	
	@Column(name = "TIPO_IMPUESTO")
	private Integer tipoImpuesto;
	
	@ManyToOne
	@JoinColumn(name = "CODIGO_TDV", nullable = true)
	private Transportadoras codigoTdv;
	
	@ManyToOne
	@JoinColumn(name = "CODIGO_PUNTO_BANCO_EXT", nullable = true)
	private Puntos codigoPuntoBancoExt;
	
	@ManyToOne
	@JoinColumn(name = "CODIGO_CIUDAD", nullable = true)
	private Ciudades ciudad;

	@Column(name = "ES_CAMBIO")
	private Boolean esCambio;
	
	@Column(name = "TIPO_PROCESO")
	private String tipoProceso;
	
	@Column(name = "ESTADO")
	private Integer estado;
	
	@Column(name = "TASA_NEGOCIACION")
	private String tasaNegociacion;
	
	@Column(name = "MEDIO_PAGO", length = 15)
	private String medioPago;
	

	
	
}
