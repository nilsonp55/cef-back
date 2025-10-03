package com.ath.adminefectivo.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ath.adminefectivo.auditoria.listener.AuditoriaEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de manejar la logica de la tabla BITACORA_AUTOMATICOS
 * @author DUVAN.NARANJO
 *
 */
@Entity
@EntityListeners(AuditoriaEntityListener.class)
@Table(name = "BITACORA_AUTOMATICOS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "BitacoraAutomaticos.findAll", query = "SELECT t FROM BitacoraAutomaticos t")
public class BitacoraAutomaticos {
	
	@Id
	@Column(name = "ID_REGISTRO")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idRegistro;
	
	@Column(name = "FECHA_SISTEMA")
	private Date fechaSistema;
	
	@Column(name = "FECHA_HORA_INICIO")
	private Date fechaHoraInicio;
	
	@Column(name = "FECHA_HORA_FINAL")
	private Date fechaHoraFinal;
	
	@Column(name = "CODIGO_PROCESO")
	private String codigoProceso;
	
	@Column(name = "RESULTADO")
	private String resultado;
	
	@Column(name = "MENSAJE_ERROR")
	private String mensajeError;
	
	@OneToMany(mappedBy = "bitacoraAutomaticos", cascade = CascadeType.PERSIST)
	private List<DetallesProcesoAutomatico> detallesProcesosAutomaticos;
	
}
