package com.ath.adminefectivo.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
 * Entidad encargada de manejar la logica de la tabla ComprobantesContables
 * @author cesar.castano
 *
 */

@Entity
@Table(name = "COMPROBANTES_CONTABLES")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "ComprobantesContables.findAll", query = "SELECT t FROM ComprobantesContables t")
public class ComprobantesContables {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_COMPROBANTES_CONTABLES")
	private Long idComprobantesContables;
	
	@Column(name = "NUMERO_COMPROBANTE")
	private Integer numeroComprobante;
	
	@ManyToOne
	@JoinColumn(name = "BANCO_AVAL", nullable = false)
	private Bancos bancoAval;
	
	@Column(name = "FECHA")
	private Date fecha;
	
	@Column(name = "TIPO_PROCESO")
	private char tipoProceso;
	
	@Column(name = "ESTADO")
	private String estado;
}
