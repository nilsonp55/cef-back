package com.ath.adminefectivo.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de manejar la logica de la tabla COSTOS_TRANSPORTE
 * @author hector.mercado
 *
 */
@Entity
@Table(name = "COSTOS_TRANSPORTE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "CostosTransporte.findAll", query = "SELECT t FROM CostosTransporte t")
public class CostosTransporte {
	
	@Id
	@Column(name = "CONSECUTIVO_REGISTRO")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long consecutivo;
	
	@Column(name = "ENTIDAD")
	private String entidad;
	
	@Column(name = "FACTURA")
	private String factura;
	
	@Column(name = "TIPO_REGISTRO")
	private String tipoRegistro;
	
	@Column(name = "FECHA_SERVICIO_TRANSPORTE")
	private Date fechaServicioTransporte;

	@Column(name = "IDENTIFICACION_CLIENTE")
	private String identificacionCliente;
	
	@Column(name = "RAZON_SOCIAL")
	private String razonSocial;

	@Column(name = "CODIGO_PUNTO_CARGO")
	private String codigoPuntoCargo;

	@Column(name = "NOMBRE_PUNTO_CARGO")
	private String nombrePuntoCargo;
	
	@Column(name = "CODIGO_CIIU_PUNTO")
	private Integer codigoCiiuPunto;
	
	@Column(name = "CIUDAD_MUNICIPIO_PUNTO")
	private String ciudadMunicipioPunto;
	
	@Column(name = "CODIGO_CIIU_FONDO")
	private Integer codigoCiiuFondo;
	
	@Column(name = "CIUDAD_FONDO")
	private String ciudadFondo;
	
	@Column(name = "NOMBRE_TIPO_SERVICIO")
	private String nombreTipoServicio;
	
	@Column(name = "TIPO_PEDIDO")
	private String tipoPedido;

	@Column(name = "ESCALA")
	private String escala;

	@Column(name = "EXCLUSIVO_MONEDA")
	private String exclusivoMoneda;
	
	@Column(name = "MONEDA_DIVISA")
	private String monedaDivisa;
	
	@Column(name = "TRM_CONVERSION")
	private BigDecimal trmConversion;
	
	@Column(name = "VALOR_TRANSPORTADO_BILLETE")
	private BigDecimal valorTransportadoBillete;
	
	@Column(name = "VALOR_TRANSPORTADO_MONEDA")
	private BigDecimal valorTransportadoMoneda;
	
	@Column(name = "VALOR_TOTAL_TRANSPORTADO")
	private BigDecimal valorTotalTransportado;
	
	@Column(name = "NUMERO_FAJOS")
	private BigDecimal numeroFajos;
	
	@Column(name = "NUMERO_DE_BOLSAS_MONEDA")
	private BigDecimal numeroBolsasMoneda;
	
	@Column(name = "COSTO_FIJO")
	private Long costoFijo;
	
	@Column(name = "COSTO_POR_MILAJE")
	private BigDecimal costoMilaje;

	@Column(name = "COSTO_POR_BOLSA")
	private BigDecimal costoBolsa;

	@Column(name = "COSTO_FLETES")
	private Long costoFletes;

	@Column(name = "COSTO_EMISARIOS")
	private Long costoEmisarios;

	@Column(name = "OTROS_1")
	private Long otros1;
	
	@Column(name = "OTROS_2")
	private Long otros2;
	
	@Column(name = "OTROS_3")
	private Long otros3;
	
	@Column(name = "OTROS_4")
	private Long otros4;
	
	@Column(name = "OTROS_5")
	private Long otros5;
	
	@Column(name = "SUBTOTAL")
	private BigDecimal subtotal;
	
	@Column(name = "IVA")
	private BigDecimal iva;

	@Column(name = "VALOR_TOTAL")
	private BigDecimal valorTotal;
	
	@Column(name = "ESTADO_CONCILIACION")
	private String estadoConciliacion;

	@Column(name = "ESTADO")
	private String estado;

	@Column(name = "OBSERVACIONES_ATH")
	private String observacionesAth;
	
	@Column(name = "OBSERVACIONES_TDV")
	private String observacionesTdv;

	@Column(name = "ID_ARCHIVO_CARGADO")
	private Long idArchivoCargado;

	@Column(name = "ID_REGISTRO")
	private Long idRegistro;

	@Column(name = "USUARIO_CREACION")
	private String usuarioCreacion;
	
	@Column(name = "FECHA_CREACION")
	private Timestamp fechaCreacion;
	
	@Column(name = "USUARIO_MODIFICACION")
	private String usuarioModificacion;

	@Column(name = "FECHA_MODIFICACION")
	private Timestamp fechaModificacion;
	
	@Column(name = "ID_LIQUIDACION")
	private Long idLiquidacion;
	
	@Column(name = "TIPO_TRANSACCION")
	private Integer tipoTransaccion;


}
