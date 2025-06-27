package com.ath.adminefectivo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de manejar la logica de la tabla FONDOS
 * @author cesar.castano
 *
 */
@Entity
@Table(name = "CAJEROS_ATM")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "CajerosATM.findAll", query = "SELECT t FROM CajerosATM t")
public class CajerosATM {

	@Id
	@Column(name = "CODIGO_PUNTO")
	private Integer codigoPunto;
	
	@OneToOne
    @JoinColumn(name = "CODIGO_PUNTO", insertable = false, unique = false)
    private Puntos punto;
	
	@Column(name = "CODIGO_ATM")
	private String codigoATM;
	
	@Column(name = "COD_BANCO_AVAL")
	private Integer bancoAval;
	
	@Column(name = "TARIFA_RUTEO")
	private Double tarifaRuteo;
	
	@Column(name = "TARIFA_VERIFICACION")
	private Double tarifaVerificacion;
	
}
