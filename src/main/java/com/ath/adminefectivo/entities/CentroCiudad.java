package com.ath.adminefectivo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
 * Entidad encargada de manejar la logica de la tabla TiposCuentas
 * @author cesar.castano
 */

@Entity
@EntityListeners(AuditoriaEntityListener.class)
@Table(name = "CENTROS_CIUDAD")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "CentroCiudad.findAll", query = "SELECT t FROM CentroCiudad t")
public class CentroCiudad {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_CENTRO_CIUDAD")
	private Integer idCentroCiudad;
	
	@ManyToOne
	@JoinColumn(name = "CIUDAD_DANE", nullable = false)
	private Ciudades ciudadDane;

	@ManyToOne
	@JoinColumn(name = "BANCO_AVAL", nullable = false)
	private Bancos bancoAval;
	
	@Column(name = "CODIGO_CENTRO")
	private String codigoCentro;
}
