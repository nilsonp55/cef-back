package com.ath.adminefectivo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.ath.adminefectivo.auditoria.listener.AuditoriaEntityListener;

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
@EntityListeners(AuditoriaEntityListener.class)
@Table(name = "CAJEROS_ATM")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "Cajeros.findAll", query = "SELECT t FROM Cajeros t")
public class Cajeros {

	@Id
	@Column(name = "CODIGO_PUNTO")
	private Integer codigoPunto;
	
	@Column(name = "CODIGO_ATM")
	private String codigoATM;
	
	@Column(name = "CODIGO_BANCO_AVAL")
	private Integer codigoBancoAval;
	
	@Column(name = "NOMBRE_CAJERO")
	private String nombreCajero;
	
	@Column(name = "TARIFA_RUTEO")
	private Double tarifaRuteo;
	
	@Column(name = "TARIFA_VERIFICACION")
	private Double tarifaVerificacion;
	
}
