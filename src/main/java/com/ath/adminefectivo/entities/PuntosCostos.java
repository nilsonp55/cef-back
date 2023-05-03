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
 * Entidad encargada de manejar la logica de la tabla PUNTOS_COSTOS
 * @author cesar.castano
 *
 */
@Entity
@Table(name = "PUNTOS_COSTOS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "PuntosCostos.findAll", query = "SELECT t FROM PuntosCostos t")
public class PuntosCostos {

	@Id
	@Column(name= "id_puntos_costos")
	private int idPuntosCostos;

	@ManyToOne
	@JoinColumn(name = "codigo_punto", nullable = false)
	private Puntos punto;

	@Column(name= "costo_personalizado")
	private String costoPersonalizado;

	@Column(name= "costo_fijo")
	private int costoFijo;

	@Column(name= "costo_milaje_ruteo")
	private int costoMilajeRuteo;

	@Column(name= "costo_moneda")
	private int costoMoneda;

	@Column(name= "fajado")
	private boolean fajado;

	@Column(name= "id_tarifas_generales")
	private long idTarifasGenerales;

	@Column(name= "mixto", nullable = true)
	private Boolean mixto;
	
	@Column(name = "estado", nullable = true)
	private int estado;
}
