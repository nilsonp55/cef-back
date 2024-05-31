package com.ath.adminefectivo.entities;

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
@Table(name = "CONF_CONTABLE_ENTIDADES")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "ConfContableEntidades.findAll", query = "SELECT t FROM ConfContableEntidades t")
public class ConfContableEntidades {

	@Id
	@Column(name = "CONSECUTIVO")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long consecutivo;
	
	@ManyToOne
	@JoinColumn(name = "BANCO_AVAL", nullable = false)
	private Bancos bancoAval;
	
	@Column(name = "TIPO_TRANSACCION")
	private Integer tipoTransaccion;
	
	@Column(name = "TIPO_OPERACION")
	private String tipoOperacion;
	
	@Column(name = "CODIGO_COMISION")
	private Integer codigoComision;
	
	@Column(name = "TIPO_IMPUESTO")
	private Integer tipoImpuesto;
	
	@Column(name = "MEDIO_PAGO")
	private String medioPago;
	
	@ManyToOne
	@JoinColumn(name = "CODIGO_PUNTO_BANCO_EXT", nullable = true)
	private Puntos codigoPuntoBancoExt;
	
	@ManyToOne
	@JoinColumn(name = "CODIGO_TDV", nullable = true)
	private Transportadoras transportadora;
	
	@Column(name = "ES_CAMBIO")
	private Boolean esCambio;

	@Column(name = "NATURALEZA")
	private String naturaleza;
	
	@Column(name = "CUENTA_CONTABLE")
	private String cuentaContable;
}
