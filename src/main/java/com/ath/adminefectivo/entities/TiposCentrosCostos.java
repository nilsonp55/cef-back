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
 * Entidad encargada de manejar la logica de la tabla TiposCuentas
 * @author cesar.castano
 *
 */

@Entity
@Table(name = "TIPOS_CENTROS_COSTOS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "TiposCentrosCostos.findAll", query = "SELECT t FROM TiposCentrosCostos t")
public class TiposCentrosCostos {
	@Id
	@Column(name = "TIPO_CENTRO")
	private String tipoCentro;
	
	@Column(name = "NOMBRE_CENTRO")
	private String nombreCentro;
	
	@Column(name = "CODIGO_CENTRO")
	private String codigoCentro;
	
	@Column(name = "TABLA_CENTROS")
	private String tablaCentros;
	
}
