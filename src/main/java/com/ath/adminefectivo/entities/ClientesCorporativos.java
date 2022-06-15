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
 * Entidad encargada de manejar la logica de la tabla CLIENTES CORPORATIVOS
 * @author cesar.castano
 *
 */
@Entity
@Table(name = "CLIENTES_CORPORATIVOS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "ClientesCorporativos.findAll", query = "SELECT t FROM ClientesCorporativos t")
public class ClientesCorporativos {

	@Id
	@Column(name = "CODIGO_CLIENTE")
	private Integer codigoCliente;
	
	@Column(name = "CODIGO_BANCO_AVAL")
	private Integer codigoBancoAval;
	
	@Column(name = "NOMBRE_CLIENTE")
	private String nombreCliente;
	
	@Column(name = "TIPOID")
	private String tipoId;
	
	@Column(name = "IDENTIFICACION")
	private String identificacion;
	
	@Column(name = "TARIFA_SEPARACION")
	private Double tarifaSeparacion;
	
}
