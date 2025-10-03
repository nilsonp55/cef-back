package com.ath.adminefectivo.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.ath.adminefectivo.auditoria.listener.AuditoriaEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de manejar la logica de la tabla Saldos Fondos
 * @author duvan.naranjo
 *
 */
@Entity
@EntityListeners(AuditoriaEntityListener.class)
@Table(name = "SALDOS_FONDOS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "SaldosFondos.findAll", query = "SELECT t FROM SaldosFondos t")
public class SaldosFondos {

	@Id
	@Column(name= "id_saldos_fondos")
	private int idSaldosFondos;

	@Column(name= "codigo_fondo")
	private int codigoFondo;

	@Column(name= "tdv")
	private String transportadora;

	@Column(name= "banco_aval")
	private int bancoAval;

	@Column(name= "codigo_ciudad")
	private String codigoCiudad;

	@Column(name= "fecha")
	private Date fecha;

	@Column(name= "saldo_inicial_monedas")
	private long saldoInicialMonedas;

	@Column(name= "saldo_final_monedas")
	private long saldoFinalMonedas;

	@Column(name= "saldo_inicial_rem")
	private long saldoInicialRem;

	@Column(name= "saldo_final_rem")
	private long saldoFinalRem;

	@Column(name= "saldo_inicial_bestd")
	private long saldoInicialBestd;

	@Column(name= "saldo_final_bestd")
	private long saldoFinalBestd;

	@Column(name= "saldo_inicial_crlt")
	private long saldoInicialCrlt;

	@Column(name= "saldo_final_crlt")
	private long saldoFinalCrlt;

	@Column(name= "saldo_inicial_dtro")
	private long saldoInicialDtro;

	@Column(name= "saldo_final_dtro")
	private long saldoFinalDtro;
	
	@Column(name = "estado")
	private int estado;
}
