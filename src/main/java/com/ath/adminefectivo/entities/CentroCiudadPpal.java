package com.ath.adminefectivo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ath.adminefectivo.auditoria.listener.AuditoriaEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de manejar la logica de la tabla Centros_ciudad_ppal
 * @author prv_nparra
 */

@Entity
@EntityListeners(AuditoriaEntityListener.class)
@Table(name = "CENTROS_CIUDAD_PPAL")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CentroCiudadPpal {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_CENTRO_CIUDAD_PPAL")
	private Integer idCentroCiudadPpal;
	
	@ManyToOne
	@JoinColumn(name = "CODIGO_DANE", nullable = false)
	private Ciudades codigoDane;

	@ManyToOne
	@JoinColumn(name = "BANCO_AVAL", nullable = false)
	private Bancos bancoAval;
	
	@Column(name = "CODIGO_CENTRO")
	private String codigoCentro;

}
