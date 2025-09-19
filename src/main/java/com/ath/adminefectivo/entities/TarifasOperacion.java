package com.ath.adminefectivo.entities;

import java.math.BigDecimal;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de manejar la logica de la tabla TarifasOperacion
 * @author duvan.naranjo
 *
 */
@Entity
@Table(name = "TARIFAS_OPERACION")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "TarifasOperacion.findAll", query = "SELECT t FROM TarifasOperacion t")
public class TarifasOperacion {
	
	@Id
	@Column(name = "ID_TARIFAS_OPERACION")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idTarifasOperacion;

	@ManyToOne
	@JoinColumn(name = "CODIGO_BANCO", nullable = false)
	private Bancos banco;

	@ManyToOne
	@JoinColumn(name = "CODIGO_TDV", nullable = false)
	private Transportadoras transportadora; 

	@Column(name = "TIPO_PUNTO")
	private String tipoPunto; 

	@Column(name = "TIPO_OPERACION")
	private String tipoOperacion;

	@Column(name = "TIPO_SERVICIO")
	private String tipoServicio;

	@Column(name = "ESCALA")
	private String escala;

	@Column(name = "BILLETES")
	private String billetes; 

	@Column(name = "MONEDAS")
	private String monedas; 

	@Column(name = "FAJADO")
	private String fajado;

	@Column(name = "COMISION_APLICAR")
	private String comisionAplicar;

	@Column(name = "VALOR_TARIFA")
	private Double valorTarifa; 

	@Column(name = "ESTADO", nullable = true)
	private int estado;
	
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

	@Column(name = "FECHA_VIGENCIA_INI")
	private Date fechaVigenciaIni;

	@Column(name = "FECHA_VIGENCIA_FIN")
	private Date fechaVigenciaFin;
	
	@Column(name = "LIMITE_COMISION_APLICAR")
    private Integer limiteComisionAplicar;

    @Column(name = "VALOR_COMISION_ADICIONAL")
    private BigDecimal valorComisionAdicional;
    
    @Column(name = "ID_ARCHIVO_CARGADO")
	private Integer idArchivoCargado;

	@Column(name = "ID_REGISTRO")
	private Integer idRegistro;

}
