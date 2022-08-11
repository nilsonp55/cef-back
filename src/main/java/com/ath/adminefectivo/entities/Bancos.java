package com.ath.adminefectivo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
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

	@Id
	@Column(name = "CODIGO_PUNTO")
	private Integer codigoPunto;
	
	@Column(name = "CODIGO_COMPENSACION")
	private Integer codigoCompensacion;
	
	@Column(name = "NUMERO_NIT")
	private String numeroNit;
	
	@Column(name = "ABREVIATURA")
	private String abreviatura;
	
	@Column(name = "ES_AVAL")
	private Boolean esAVAL;
	
	//@ManyToOne
	//@JoinColumn(name = "COD_PUNTO", nullable = false)
	//private Puntos puntos;
	
}
