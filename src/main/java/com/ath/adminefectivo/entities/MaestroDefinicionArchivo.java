package com.ath.adminefectivo.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de manejar la logica de la tabla MAESTROS_DEFINICION_ARCHIVO
 * @author cesar.castano
 *
 */
@Entity
@Table(name = "MAESTRO_DEFINICION_ARCHIVO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "MaestroDefinicionArchivo.findAll", query = "SELECT t FROM MaestroDefinicionArchivo t")
public class MaestroDefinicionArchivo {

	@Id
	@Column(name = "ID_MAESTRO_DEFINICION_ARCHIVO")
	private String idMaestroDefinicionArchivo;
	
	@Column(name = "DESCRIPCION_ARCH", length = 50, nullable = false)
	private String descripcionArch;
	
	@Column(name = "OBJETIVO", length = 1, nullable = false)
	private String objetivo;
	
	@Column(name = "AGRUPADOR")
	private String agrupador;
	
	@Column(name = "EXTENSION", length = 5)
	private String extension;
	
	@Column(name = "DELIMITADOR_BASE", length = 1, nullable = false)
	private String delimitadorBase;
	
	@Column(name = "DELIMITADOR_OTRO", length = 1, nullable = true)
	private String delimitadorOtro;
	
	@Column(name = "MULTIFORMATO", nullable = false)
	private boolean multiformato;
	
	@Column(name = "CAMPO_MULTIFORMATO", nullable = true)
	private int campoMultiformato;
	
	@Column(name = "PERIODICIDAD", length = 1, nullable = false)
	private String periodicidad;
	
	@Column(name = "CANTIDAD", nullable = false)
	private short cantidad;
	
	@Column(name = "MASCARA_ARCH", length = 100, nullable = false)
	private String mascaraArch;
	
	@Column(name = "UBICACION", length = 200, nullable = false)
	private String ubicacion;
	
	@Column(name = "VALIDA_ESTRUCTURA", nullable = false)
	private boolean validaEstructura;
	
	@Column(name = "VALIDA_CONTENIDO", nullable = false)
	private boolean validaContenido;
	
	@Column(name = "CABECERA", nullable = false)
	private boolean cabecera;
	
	@Column(name = "CONTROL_FINAL", nullable = false)
	private boolean controlFinal;
	
	@Column(name = "METODO", length = 2, nullable = false)
	private String metodo;
	
	@Column(name = "CANTIDAD_MINIMA", nullable = false)
	private boolean cantidadMinima;
	
	@Column(name = "NUMERO_CANTIDAD_MINIMA", length = 5, nullable = false)
	private int numeroCantidadMinima;
	
	@Column(name = "ESTADO")
	private String estado;
	
	@Column(name = "USUARIO_CREACION")
	private String usuarioCreacion;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_CREACION")
	private Date fechaCreacion;

	@Column(name = "USUARIO_MODIFICACION")
	private String usuarioModificacion;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_MODIFICACION")
	private Date fechaModificacion;
	
	@Column(name = "TIPO_ENCRIPTADO")
	private String tipoDeEncriptado;
}
