package com.ath.adminefectivo.entities;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.ath.adminefectivo.entities.id.DetallesDefinicionArchivoPK;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de manejar la logica de la tabla DETALLES_DEFINICION_ARCHIVO
 * @author cesar.castano
 *
 */
@Entity
@Table(name = "DETALLES_DEFINICION_ARCHIVO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "DetallesDefinicionArchivo.findAll", query = "SELECT t FROM DetallesDefinicionArchivo t")
public class DetallesDefinicionArchivo {

	@EmbeddedId
	private DetallesDefinicionArchivoPK id;
	
	@Column(name = "NOMBRE_CAMPO", length = 30, nullable = false)
	private String nombreCampo;
	
	@Column(name = "TIPO_DATO", length = 1, nullable = false)
	private String tipoDato;
	
	@Column(name = "DECIMALES", nullable = false)
	private Integer decimales;
	
	@Column(name = "REQUERIDOS", nullable = false)
	private boolean requeridos;
	
	@Column(name = "VALIDAR_REGLAS", nullable = false)
	private boolean validarReglas;
	
	@Column(name = "MULTIPLES_REGLAS", nullable = false)
	private boolean multiplesReglas;
	
	@Column(name = "EXPRESION_REGLA", length = 200, nullable = true)
	private String expresionRegla;
	
}
