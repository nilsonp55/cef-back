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
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de manejar la logica de la tabla COSTOS_PROCESAMIENTO
 * @author hector.mercado
 *
 */
@Entity
@Table(name = "COSTOS_PROCESAMIENTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "CostosProcesamiento.findAll", query = "SELECT t FROM CostosProcesamiento t")
public class CostosProcesamiento {
	
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

	@Column(name = "FECHA_PROCESAMIENTO")
	private Date fechaProcesamiento;
	
	@Column(name = "IDENTIFICACION_CLIENTE")
	private String identificacionCliente;
	
	@Column(name = "RAZON_SOCIAL")
	private String razonSocial;

	@Column(name = "CODIGO_PUNTO_CARGO")
	private String codigoPuntoCargo;

	@Column(name = "NOMBRE_PUNTO_CARGO")
	private String nombrePuntoCargo;
	
	@Column(name = "CODIGO_CIIU_FONDO")
	private Integer codigoCiiuFondo;
	
	@Column(name = "CIUDAD_FONDO")
	private String ciudadFondo;
	
	@Column(name = "NOMBRE_TIPO_SERVICIO")
	private String nombreTipoServicio;
	
	@Column(name = "MONEDA_DIVISA")
	private String monedaDivisa;
	
	@Column(name = "TRM_CONVERSION")
	private BigDecimal trmConversion;
	
	@Column(name = "VALOR_PROCESADO_BILLETE")
	private BigDecimal valorProcesadoBillete;
	
	@Column(name = "VALOR_PROCESADO_MONEDA")
	private BigDecimal valorProcesadoMoneda;
	
	@Column(name = "VALOR_TOTAL_PROCESADO")
	private BigDecimal valorTotalProcesado;
	
	@Column(name = "FACTOR_DE_LIQUIDACION")
	private String factorLiquidacion;
	
	@Column(name = "BASE_DE_LIQUIDACION")
	private BigDecimal baseLiquidacion;
	
	@Column(name = "TARIFA")
	private BigDecimal tarifa;
	
	@Column(name = "COSTO_SUBTOTAL")
	private BigDecimal costoSubtotal;
	
	@Column(name = "PORCENTAJE_AIU")
	private Integer porcentajeAiu;

	@Column(name = "PORCENTAJE_IVA")
	private Integer porcentajeIva;
	
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
	
	@Column(name = "CODIGO_TDV")
	private String codigoTdv;
	
	@Column(name = "CODIGO_PUNTO_INTERNO")
	private Integer codigoPuntoInterno;

	@Column(name = "TIPO_PUNTO")
	private String tipoPunto;

	@Column(name = "CODIGO_PUNTO_FONDO")
	private Integer codigoPuntoFondo;

	@Column(name = "NOMBRE_FONDO")
	private String nombreFondo;
	
	@Column(name = "TIPO_OPERACION")
	private String tipoOperacion;
	
	@Column(name = "ENTRADA_SALIDA")
	private String entradaSalida;

	@Column(name = "CLASIFICACION_FAJADO")
	private Double clasificacionFajado = 0.0;

	@Column(name = "CLASIFICACION_NO_FAJADO")
	private Double clasificacionNoFajado = 0.0;

	@Column(name = "COSTO_PAQUETEO")
	private Double costoPaqueteo = 0.0;

	@Column(name = "MONEDA_RESIDUO")
	private Double monedaResiduo = 0.0;

	@Column(name = "BILLETE_RESIDUO")
	private Double billeteResiduo = 0.0;
	
	/**
	 * Campos auxiliares usados en cálculos transitorios del procesamiento.
	 * No está persistido en base de datos.
	 */
	
	@Transient
	private BigDecimal valorAlmacenamientoBillete;

	@Transient
	private BigDecimal valorAlmacenamientoMoneda;
}
