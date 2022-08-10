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
 * Entidad encargada de manejar la logica de la tabla transacciones contables
 * @author duvan.naranjo
 *
 */

@Entity
@Table(name = "TRANSACCIONES_CONTABLES")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "TransaccionesContables.findAll", query = "SELECT t FROM TransaccionesContables t")
public class TransaccionesContables {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_TRANSACCIONES_CONTABLES")
	private Long idTransaccionesContables;
	
	
	@ManyToOne
	@JoinColumn(name = "ID_OPERACION", nullable = false)
	private OperacionesProgramadas idOperacion;
	
	@Column(name = "ID_GENERICO")
	private Integer idGenerico;
	
	@Column(name = "FECHA")
	private Date fecha;
	
	@Column(name = "CONSECUTIVO_DIA")
	private String consecutivoDia;
	
	@Column(name = "TIPO_TRANSACCION")
	private Integer tipoTransaccion;
	
	@ManyToOne
	@JoinColumn(name = "BANCO_AVAL", nullable = true)
	private Bancos bancoAval;
	
	@Column(name = "CODIGO_CENTRO")
	private String codigoCentro;
	
	@Column(name = "NATURALEZA")
	private String naturaleza;
	
	@Column(name = "CUENTA_CONTABLE", nullable = false)
	private String cuentaContable;
	
	@Column(name = "CODIGO_MONEDA")
	private String codigoMoneda;
	
	@Column(name = "VALOR")
	private Integer valor;
	
	@Column(name = "TIPO_PROCESO")
	private String tipoProceso;
	
	@Column(name = "NUMERO_COMPROBANTE")
	private String numeroComprobante;

	@Column(name = "TIPO_IDENTIFICACION")
	private String tipoIdentificacion;
	
	@Column(name = "ID_TERCERO")
	private Integer idTercero;
	
	@Column(name = "NOMBRE_TERCERO")
	private String nombreTercero;
	
	@Column(name = "IDENTIFICADOR")
	private String identificador;
	
	@Column(name = "DESCRIPCION")
	private String descripcion;
	
	@Column(name = "REFERENCIA1")
	private String referencia1;
	
	@Column(name = "REFERENCIA2")
	private String referencia2;
	
	@ManyToOne
	@JoinColumn(name = "ID_TRANSACCIONES_INTERNAS", nullable = true)
	private TransaccionesInternas idTransaccionesInternas;

	
}
