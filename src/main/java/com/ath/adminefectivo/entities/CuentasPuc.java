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
 * @author bayronPerez
 *
 */

@Entity
@Table(name = "CUENTAS_PUC")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "CuentasPuc.findAll", query = "SELECT t FROM CuentasPuc t")
public class CuentasPuc {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_CUENTAS_PUC")
	private Long idCuentasPuc;
	
	@ManyToOne
	@JoinColumn(name = "CUENTA_CONTABLE", nullable = false)
	private ConfContableEntidades cuentaContable;
	
	@ManyToOne
	@JoinColumn(name = "BANCO_AVAL", nullable = false)
	private Bancos bancoAval;
	
	@Column(name = "NOMBRE_CUENTA")
	private String nombreCuenta;
	
	@Column(name = "IDENTIFICADOR")
	private String identificador;
	
	@ManyToOne
	@JoinColumn(name = "TIPO_CENTRO", nullable = false)
	private TiposCentrosCostos tiposCentrosCostos;
	
	@ManyToOne
	@JoinColumn(name = "TIPO_CUENTA", nullable = false)
	private TiposCuentas tiposCuentas;
	
	@Column(name = "ESTADO")
	private Boolean estado;
}
