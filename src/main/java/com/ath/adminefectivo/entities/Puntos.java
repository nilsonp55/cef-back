package com.ath.adminefectivo.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de manejar la logica de la tabla PUNTOS
 * @author cesar.castano
 *
 */
@Entity
@Table(name = "PUNTOS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "Puntos.findAll", query = "SELECT t FROM Puntos t")
public class Puntos {

	@Id
	@Column(name = "CODIGO_PUNTO")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer codigoPunto;
	
	@Column(name = "TIPO_PUNTO")
	private String tipoPunto;
	
	@Column(name = "NOMBRE_PUNTO")
	private String nombrePunto;
	
	@Column(name = "CODIGO_CIUDAD")
	private String codigoCiudad;
	
//	@ManyToOne
//	@JoinColumn(name = "CODIGO_CIUDAD", insertable = false, updatable = false)
//	private Ciudades ciudades;
	
	@Column(name = "ESTADO")
	private String estado;
	
	@OneToOne(mappedBy = "puntos", cascade = {CascadeType.ALL}, orphanRemoval = true)
	private Oficinas oficinas;
	
	@OneToOne(mappedBy = "puntos", cascade = {CascadeType.ALL}, orphanRemoval = true)
	private SitiosClientes sitiosClientes;
	
	@OneToMany(mappedBy = "puntos", cascade = {CascadeType.ALL}, orphanRemoval = true)
	private List<PuntosCodigoTDV> puntosCodigoTDV;
	
	@OneToOne(mappedBy = "puntos", cascade = {CascadeType.ALL}, orphanRemoval = true)
	private Fondos fondos;
	
	@OneToOne(mappedBy = "puntos", cascade = {CascadeType.ALL}, orphanRemoval = true)
	private CajerosATM cajeroATM;
	
	@OneToOne(mappedBy = "puntos", cascade = {CascadeType.ALL}, orphanRemoval = true)
	private Bancos bancos;
}
