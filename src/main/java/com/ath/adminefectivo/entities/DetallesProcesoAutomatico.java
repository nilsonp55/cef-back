package com.ath.adminefectivo.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.ath.adminefectivo.auditoria.listener.AuditoriaEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de manejar la logica de la tabla DETALLES_PROCESO_AUTOMATICO
 * @author DUVAN.NARANJO
 *
 */
@Entity
@EntityListeners(AuditoriaEntityListener.class)
@Table(name = "DETALLES_PROCESO_AUTOMATICO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "DetallesProcesoAutomatico.findAll", query = "SELECT t FROM DetallesProcesoAutomatico t")
public class DetallesProcesoAutomatico {
	
	@Id
	@Column(name = "ID_DETALLE_PROCESO")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idDetalleProceso;
	
	@Column(name = "ID_ARCHIVO")
	private long idArchivo;
	
	@Column(name = "NOMBRE_ARCHIVO")
	private Date nombreArchivo;
	
	@Column(name = "RESULTADO")
	private String resultado;
	
	@Column(name = "MENSAJE_ERROR")
	private String mensajeError;

	@ManyToOne
	@JoinColumn(name = "ID_REGISTRO", insertable = false, updatable = false)
	private BitacoraAutomaticos bitacoraAutomaticos;
	
}
