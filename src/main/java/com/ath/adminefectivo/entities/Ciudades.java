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
 * Entidad encargada de manejar la logica de la tabla CIUDADES
 * @author cesar.castano
 *
 */
@Entity
@Table(name = "CIUDADES")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "Ciudades.findAll", query = "SELECT t FROM Ciudades t")
public class Ciudades {
	
	@Id
	@Column(name = "CODIGO_DANE")
	private String codigoDANE;
	
	@Column(name = "NOMBRE_CIUDAD")
	private String nombreCiudad;
	
	@Column(name = "NOMBRE_CIUDAD_FISERV")
	private String nombreCiudadFiserv;
	
	@Column(name = "CODIGO_BRINKS", nullable = true)
	private Integer codigoBrinks;
	
	@Column(name = "COBRO_IVA", nullable = true)
	private Boolean cobroIva;
	
}
