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
 * Entidad encargada de manejar la logica de la tabla OFICINAS
 * @author Bayron Andres Perez Muñoz
 *
 */
@Entity
@Table(name = "OFICINAS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "Oficinas.findAll", query = "SELECT t FROM Oficinas t")
public class Oficinas {

	@Id
	@Column(name = "CODIGO_PUNTO")
	private Integer codigoPunto;
	
	@Column(name = "CODIGO_OFICINA")
	private Integer codigoOficina;
	
	@Column(name = "BANCO_AVAL")
	private Integer bancoAVAL;
	
	@Column(name = "FAJADO")
	private Boolean fajado;
	
	@Column(name = "REFAJILLADO")
	private Boolean refajillado;
	
	@Column(name = "TARIFA_RUTEO")
	private Double tarifaRuteo;
	
	@Column(name = "TARIFA_VERIFICACION")
	private Double tarifaVerificacion;
	
	@OneToOne
	@JoinColumn(name = "CODIGO_PUNTO", nullable = false)
	private Puntos puntos;
	
}
