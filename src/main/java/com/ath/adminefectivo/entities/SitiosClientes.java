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
 * Entidad encargada de manejar la logica de la tabla SITIOS CLIENTES
 * @author Bayron Andres Perez Muñoz
 *
 */

@Entity
@Table(name = "SITIOS_CLIENTE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "SitiosClientes.findAll", query = "SELECT t FROM SitiosClientes t")
public class SitiosClientes {

	@Id
	@Column(name = "CODIGO_PUNTO")
	private Integer codigoPunto;
	
	@Column(name = "CODIGO_CLIENTE")
	private Integer codigoCliente;
	
	@Column(name = "FAJADO")
	private Boolean fajado;
	
	@ManyToOne
	@JoinColumn(name = "COD_PUNTO", nullable = false)
	private Puntos puntos;
}
