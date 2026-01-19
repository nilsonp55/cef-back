package com.ath.adminefectivo.entities;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.ath.adminefectivo.auditoria.listener.AuditoriaEntityListener;

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
@EntityListeners(AuditoriaEntityListener.class)
@Table(name = "PUNTOS_COSTOS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "PuntosCostos.findAll", query = "SELECT t FROM PuntosCostos t")
public class PuntosCostos {

	@Id
	@Column(name= "id_puntos_costos")
	private Long idPuntosCostos;

	@ManyToOne
	@JoinColumn(name = "codigo_punto", nullable = false)
	private Puntos punto;

	@Column(name= "costo_personalizado")
	private String costoPersonalizado;

	@Column(name= "costo_fijo", precision = 10, scale = 3)
	private BigDecimal costoFijo;

	@Column(name= "costo_milaje_ruteo", precision = 10, scale = 3)
	private BigDecimal costoMilajeRuteo;

	@Column(name= "costo_moneda", precision = 10, scale = 3)
	private BigDecimal costoMoneda;

	@Column(name= "fajado")
	private boolean fajado;

	@Column(name= "id_tarifas_generales")
	private long idTarifasGenerales;

	@Column(name= "mixto", nullable = true)
	private Boolean mixto;
	
	@Column(name = "estado", nullable = true)
	private int estado;
}
