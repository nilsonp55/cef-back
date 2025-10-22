package com.ath.adminefectivo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.ath.adminefectivo.auditoria.listener.AuditoriaEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de manejar la logica de la tabla REGLAS_DETALLE_ARCHIVO
 * @author cesar.castano
 *
 */
@Entity
@EntityListeners(AuditoriaEntityListener.class)
@Table(name = "REGLAS_DETALLE_ARCHIVO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "ReglasDetalleArchivo.findAll", query = "SELECT t FROM ReglasDetalleArchivo t")
public class ReglasDetalleArchivo {

	@Id
	@Column(name = "ID_REGLA")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idRegla;
	
	@Column(name = "DESCRIPCION_REGLA", length = 200, nullable = false)
	private String descripcionRegla;
	
	@Column(name = "TIPO_REGLA", length = 3, nullable = false)
	private String tipoRegla;
	
	@Column(name = "VALOR_FIJO", length = 50, nullable = true)
	private String valorFijo;
	
	@Column(name = "VALOR_INCLUIDOS", nullable = true)
	private String valorIncluidos;
	
	@Column(name = "VALOR_EXCLUIDOS", nullable = true)
	private String valorExcluidos;
	
	@Column(name = "TABLAS_A_USAR", nullable = true)
	private String tablasAUsar;
	
	@Column(name = "EXPRESION_SQL", nullable = true)
	private String expresionSQL;
	
	@Column(name = "ID_MENSAJE_ERROR", nullable = true, insertable = false, updatable = false)
	private Integer idMensaje;
	
	@OneToOne
	@JoinColumn(name="ID_MENSAJE_ERROR")
	private Mensajes mensajes;
}
