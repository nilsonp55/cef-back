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
	
	//@ManyToOne
	//@JoinColumn(name = "COD_BANCO_AVAL", nullable = false)
	//private Bancos codBancoAval;
	
	@Column(name = "BANCO_AVAL")
	private Integer bancoAval;
	
	@Column(name = "FAJADO")
	private Boolean fajado;
	
	@Column(name = "REFAJILLADO")
	private Boolean refagillado;
	
	@Column(name = "TARIFA_RUTEO")
	private Double tarifaRuteo;
	
	@Column(name = "TARIFA_VERIFICACION")
	private Double tarifaVerificacion;
	
	
	@OneToOne
	@JoinColumn(name = "CODIGO_PUNTO", nullable = false)
	private Puntos puntos;
	
}
