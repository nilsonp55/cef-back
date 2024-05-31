package com.ath.adminefectivo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Entidad encargada de manejar la logica de la tabla BANCOS
 * @author cesar.castano
 *
 */
@Entity
@Table(name = "BANCOS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BancoSimpleInfoEntity {
	
	@Id
	@Column(name = "CODIGO_PUNTO")
	private Integer codigoPunto;
	
	@Column(name = "ABREVIATURA")
	private String abreviatura;
	
	@Column(name = "NOMBRE_BANCO")
	private String nombreBanco;
	
	@Column(name = "ES_AVAL")
	private String esAval;

}
