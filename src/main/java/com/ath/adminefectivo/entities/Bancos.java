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
 * Entidad encargada de manejar la logica de la tabla BANCOS
 * @author cesar.castano
 *
 */
@Entity
@Table(name = "BANCOS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "Bancos.findAll", query = "SELECT t FROM Bancos t")
public class Bancos {
	
	@Column(name = "ES_AVAL")
	private Boolean esAVAL;
	
	@Column(name = "CODIGO_COMPENSACION")
	private Integer codigoCompensacion;
	
	@Id
	@Column(name = "CODIGO_PUNTO")
	private Integer codigoPunto;
	
	@Column(name = "NUMERO_NIT")
	private String numeroNit;
	
	@Column(name = "ABREVIATURA")
	private String abreviatura;
	
	@Column(name = "NOMBRE_BANCO")
	private String nombreBanco;
	
	@OneToOne
	@JoinColumn(name = "COD_PUNTO")
	private Puntos puntos;
	
}
