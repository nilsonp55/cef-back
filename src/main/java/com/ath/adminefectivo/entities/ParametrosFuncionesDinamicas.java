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
 * Entidad encargada de manejar la logica de la tabla ParametrosFuncionesDinamicas
 * @author duvan.naranjo
 *
 */
@Entity
@Table(name = "PARAMETROS_FUNCIONES_DINAMICAS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "ParametrosFuncionesDinamicas.findAll", query = "SELECT t FROM ParametrosFuncionesDinamicas t")
public class ParametrosFuncionesDinamicas {
	
	
	@Id
	@Column(name = "ID_PARAMETRO")
	private Integer idParametro;
	
	@Column(name = "NUMERO_PARAMETRO")
	private Integer numeroParametro;
	
	@Column(name = "NOMBRE_PARAMETRO")
	private String nombreParametro;
	
	@Column(name = "TIPO_DATO_PARAMETRO")
	private String tipoDatoParametro;	
	
	@Column(name = "VALOR_DEFECTO")
	private String valorDefecto;
	
	@Column(name = "POSIBLES_VALORES")
	private String posiblesValores;
	
	@ManyToOne
	@JoinColumn(name="ID_FUNCION")
	private FuncionesDinamicas funcionesDinamicas;
}
