package com.ath.adminefectivo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de manejar la logica de la tabla TRANSPORTADORAS
 * @author cesar.castano
 *
 */
@Entity
@Table(name = "TRANSPORTADORAS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "Transportadoras.findAll", query = "SELECT t FROM Transportadoras t")
public class Transportadoras {

	@Id
	@Column(name = "CODIGO")
	private String codigo;
	
	@Column(name = "NOMBRE_TRANSPORTADORA")
	private String nombreTransportadora;
	
	@Column(name = "ABREVIATURA")
	private String abreviatura;
	
	@Column(name = "ABREVIATURA_2")
	private String abreviatura2;
	
	@Column(name = "NOMBRE_2")
	private String nombre2;
}
