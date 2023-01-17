package com.ath.adminefectivo.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de manejar la logica de la tabla FONDOS
 * @author cesar.castano
 *
 */
@Entity
@Table(name = "CAJEROS_ATM")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "CajerosATM.findAll", query = "SELECT t FROM CajerosATM t")
public class CajerosATM {

	@Id
	@Column(name = "CODIGO_PUNTO")
	private Integer codigoPunto;
	
	@Column(name = "CODIGO_ATM")
	private Integer codigoATM;
	
	//@ManyToOne
	//@JoinColumn(name = "COD_BANCO_AVAL", nullable = false)
	//private Bancos bancoAval;
	
	@Column(name = "TARIFA_RUTEO")
	private Double tarifaRuteo;
	
	@Column(name = "TARIFA_VERIFICACION")
	private Double tarifaVerificacion;
	
	@OneToOne
	@JoinColumn(name = "COD_PUNTO", nullable = false)
	private Puntos puntos;
	
}
