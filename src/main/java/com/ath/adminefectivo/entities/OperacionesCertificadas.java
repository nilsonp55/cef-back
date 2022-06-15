package com.ath.adminefectivo.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de manejar la logica de la tabla OPERACIONES_CERTIFICADAS
 * @author cesar.castano
 *
 */
@Entity
@Table(name = "OPERACIONES_CERTIFICADAS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "OperacionesCertificadas.findAll", query = "SELECT t FROM OperacionesCertificadas t")
public class OperacionesCertificadas {

	@Id
	@Column(name = "ID_CERTIFICACION")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idCertificacion;
	
	@Column(name = "CODIGO_FONDO_TDV")
	private Integer codigoFondoTDV;
	
	@Column(name = "CODIGO_PUNTO_ORIGEN")
	private Integer codigoPuntoOrigen;
	
	@Column(name = "CODIGO_PUNTO_DESTINO")
	private Integer codigoPuntoDestino;
	
	@Column(name = "FECHA_EJECUCION")
	private Date fechaEjecucion;
	
	@Column(name = "TIPO_OPERACION")
	private String tipoOperacion;
	
	@Column(name = "TIPO_SERVICIO")
	private String tipoServicio;
	
	@Column(name = "ESTADO_CONCILIACION")
	private String estadoConciliacion;
	
	@Column(name = "CONCILIABLE")
	private String conciliable;
	
	@Column(name = "VALOR_TOTAL")
	private Double valorTotal;
	
	@Column(name = "VALOR_FALTANTE")
	private Double valorFaltante;
	
	@Column(name = "VALOR_SOBRANTE")
	private Double valorSobrante;
	
	@Column(name = "FALLIDA_OFICINA")
	private String fallidaOficina;
	
	@Column(name = "USUARIO_CREACION")
	private String usuarioCreacion;
	
	@Column(name = "USUARIO_MODIFICACION")
	private String usuarioModificacion;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_CREACION")
	private Date fechaCreacion;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_MODIFICACION")
	private Date fechaModificacion;
	
	@Column(name = "CODIGO_SERVICO_TDV")
	private String codigoServicioTdv;
	
	@Column(name = "ENTRADA_SALIDA")
	private String entradaSalida;
	
	@Column(name = "ID_ARCHIVO_CARGADO")
	private Long idArchivoCargado;
	
	@OneToMany(mappedBy = "operacionesCertificadas")
	private List<ConciliacionServicios> conciliacionServicios;
}
