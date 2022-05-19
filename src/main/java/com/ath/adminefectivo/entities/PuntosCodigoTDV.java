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
 * Entidad encargada de manejar la logica de la tabla PUNTOS CODIGO TDV
 * @author cesar.castano
 *
 */
@Entity
@Table(name = "PUNTOS_CODIGO_TDV")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "PuntosCodigoTDV.findAll", query = "SELECT t FROM PuntosCodigoTDV t")
public class PuntosCodigoTDV {

	@Id
	@Column(name = "CODIGO_TDV")
	private String codigoTDV;
	
	@Column(name = "CODIGO_PUNTO")
	private Integer codigoPunto;
	
	@Column(name = "CODIGO_PROPIO_TDV")
	private String codigoPropioTDV;
	
}
