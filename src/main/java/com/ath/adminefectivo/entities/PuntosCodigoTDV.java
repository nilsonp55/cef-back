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
	@Column(name = "id_punto_codigo_tdv")
	private Integer idPuntoCodigoTdv;
	
	@Column(name = "CODIGO_PUNTO")
	private Integer codigoPunto;
	
	@Column(name = "CODIGO_TDV")
	private String codigoTDV;
	
	@Column(name = "CODIGO_PROPIO_TDV")
	private String codigoPropioTDV;
	
	@ManyToOne
	@JoinColumn(name = "COD_PUNTO", nullable = false)
	private Puntos puntos;
	
	@ManyToOne
	@JoinColumn(name = "codigo_banco", nullable = true)
	private Bancos bancos;
	
	@Column(name = "ciudad_fondo", nullable = true)
	private String ciudadCodigo;
	
	
	@Column(name = "ESTADO", nullable = true)
	private int estado;
	
}
