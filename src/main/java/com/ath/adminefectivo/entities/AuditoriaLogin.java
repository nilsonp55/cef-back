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
 * Entidad encargada de manejar la logica de la tabla BANCOS
 * @author cesar.castano
 *
 */
@Entity@EntityListeners(AuditoriaEntityListener.class)
@Table(name = "AUDITORIA_LOGIN")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "AuditoriaLogin.findAll", query = "SELECT t FROM AuditoriaLogin t")
public class AuditoriaLogin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "USUARIO")
	private String usuario;
	
	@Column(name = "FECHA_INGRESO")
	private Date fechaIngreso;
}
