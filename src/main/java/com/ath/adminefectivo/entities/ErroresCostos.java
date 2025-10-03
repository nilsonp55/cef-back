package com.ath.adminefectivo.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.ath.adminefectivo.auditoria.listener.AuditoriaEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de manejar la logica de la tabla 
 * @author bayron.perez
 */

@Entity
@EntityListeners(AuditoriaEntityListener.class)
@Table(name = "ERRORES_COSTOS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "ErroresCostos.findAll", query = "SELECT t FROM ErroresCostos t")
public class ErroresCostos {

	@Id
	@Column(name = "ID_ERRORES_COSTOS")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idErroresCostos;
	
	@Column(name = "FECHA")
	private Date fecha;
	
	@Column(name = "MENSAJE_ERROR")
	private String mensajeError;
	
	@Column(name = "ID_SEQ_GRUPO")
	private Integer seqGrupo;
	
	@Column(name = "ESTADO")
	private String estado;
}
