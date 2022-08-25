package com.ath.adminefectivo.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de manejar la logica de la tabla ErroresContables
 * @author duvan.naranjo
 *
 */
@Entity
@Table(name = "ERRORES_CONTABLES")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "ErroresContables.findAll", query = "SELECT t FROM ErroresContables t")
public class ErroresContables {
	
	@Id
	@Column(name = "ID_ERRORES_CONTABLES")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idErroresContables;
	
	@Column(name = "ID_TRANSACCIONES_INTERNAS")
	private Long transaccionInterna;
	
	@Column(name = "FECHA")
	private Date fecha;
	
	@Column(name = "MENSAJE_ERROR")
	private String mensajeError;
	
}
