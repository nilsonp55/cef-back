package com.ath.adminefectivo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
 * Entidad encargada de manejar la logica de la tabla TdvDenominCantidad
 * @author duvan.naranjo
 *
 */
@Entity
@Table(name = "TDV_DENOMIN_CANTIDAD")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "TdvDenominCantidad.findAll", query = "SELECT t FROM TdvDenominCantidad t")
public class TdvDenominCantidad {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_tdv_den_cant")
	private Integer idTdvDenCant;

	@ManyToOne
	@JoinColumn(name = "CODIGO_TDV", nullable = false)
	private Transportadoras transportadora; 
	
	@Column(name = "moneda")
	private String moneda;
	
	@Column(name = "denominacion")
	private int denominacion;
	
	@Column(name = "familia")
	private String familia;
	
	@Column(name = "cantidad_por_denom")
	private int cantidadPorDenom;

	@Column(name = "ESTADO", nullable = true)
	private int estado;

}
