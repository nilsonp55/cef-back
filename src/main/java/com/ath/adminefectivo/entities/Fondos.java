package com.ath.adminefectivo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "FONDOS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "Fondos.findAll", query = "SELECT t FROM Fondos t")
public class Fondos {

	@Id
	@Column(name = "CODIGO_PUNTO")
	private Integer codigoPunto;
	
	@Column(name = "TDV")
	private String tdv;
	
	@Column(name = "BANCO_AVAL")
	private Integer bancoAVAL;
	
	@Column(name = "NOMBRE_FONDO")
	private String nombreFondo;
	
	@OneToOne
	@JoinColumn(name = "COD_PUNTO", nullable = false)
	private Puntos puntos;
}
