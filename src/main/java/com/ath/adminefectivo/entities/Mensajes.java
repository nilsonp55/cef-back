package com.ath.adminefectivo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.ath.adminefectivo.auditoria.listener.AuditoriaEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de manejar la logica de la tabla MENSAJES
 * @author cesar.castano
 *
 */
@Entity
@EntityListeners(AuditoriaEntityListener.class)
@Table(name = "MENSAJES")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "Mensajes.findAll", query = "SELECT t FROM Mensajes t")
public class Mensajes {

	@Id
	@Column(name = "ID_MENSAJE")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idMensaje;
	
	@Column(name = "MENSAJE", length = 200, nullable = false)
	private String mensaje;
	
	@OneToOne(mappedBy = "mensajes")
	private ReglasDetalleArchivo reglasDetalleArchivo;
}
