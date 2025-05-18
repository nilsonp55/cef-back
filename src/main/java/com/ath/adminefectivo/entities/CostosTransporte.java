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
	private Long consecutivoTransporte;
	
	@Column(name = "ENTIDAD")
	private String entidadTransporte;
	
	@Column(name = "FACTURA")
	private String facturaTransporte;
	
	@Column(name = "TIPO_REGISTRO")
	private String tipoRegistroTransporte;
	
	@Column(name = "FECHA_SERVICIO_TRANSPORTE")
	private Date fechaServicioTransporte;

	@Column(name = "IDENTIFICACION_CLIENTE")
	private String identificacionClienteTransporte;
	
	@Column(name = "RAZON_SOCIAL")
	private String razonSocialTransporte;

	@Column(name = "CODIGO_PUNTO_CARGO")
	private String codigoPuntoCargoTransporte;

	@Column(name = "NOMBRE_PUNTO_CARGO")
	private String nombrePuntoCargoTransporte;
	
	@Column(name = "CODIGO_CIIU_PUNTO")
	private Integer codigoCiiuPuntoTransporte;
	
	@Column(name = "CIUDAD_MUNICIPIO_PUNTO")
	private String ciudadMunicipioPunto;
	
	@Column(name = "CODIGO_CIIU_FONDO")
	private Integer codigoCiiuFondoTransporte;
	
	@Column(name = "CIUDAD_FONDO")
	private String ciudadFondoTransporte;
	
	@Column(name = "NOMBRE_TIPO_SERVICIO")
	private String nombreTipoServicioTransporte;
	
	@Column(name = "TIPO_PEDIDO")
	private String tipoPedidoTransporte;

	@Column(name = "ESCALA")
	private String escalaTransporte;

	@Column(name = "EXCLUSIVO_MONEDA")
	private String exclusivoMonedaTransporte;
	
	@Column(name = "MONEDA_DIVISA")
	private String monedaDivisaTransporte;
	
	@Column(name = "TRM_CONVERSION")
	private BigDecimal trmConversionTransporte;
	
	@Column(name = "VALOR_TRANSPORTADO_BILLETE")
	private BigDecimal valorTransportadoBillete;
	
	@Column(name = "VALOR_TRANSPORTADO_MONEDA")
	private BigDecimal valorTransportadoMoneda;
	
	@Column(name = "VALOR_TOTAL_TRANSPORTADO")
	private BigDecimal valorTotalTransportado;
	
	@Column(name = "NUMERO_FAJOS")
	private BigDecimal numeroFajosTransporte;
	
	@Column(name = "NUMERO_DE_BOLSAS_MONEDA")
	private BigDecimal numeroBolsasMonedaTransporte;
	
	@Column(name = "COSTO_FIJO")
	private Long costoFijoTransporte;
	
	@Column(name = "COSTO_POR_MILAJE")
	private BigDecimal costoMilajeTransporte;

	@Column(name = "COSTO_POR_BOLSA")
	private BigDecimal costoBolsaTransporte;

	@Column(name = "COSTO_FLETES")
	private Long costoFletesTransporte;

	@Column(name = "COSTO_EMISARIOS")
	private Long costoEmisariosTransporte;

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
	private BigDecimal subtotalTransporte;
	
	@Column(name = "IVA")
	private BigDecimal ivaTransporte;

	@Column(name = "VALOR_TOTAL")
	private BigDecimal valorTotalTransporte;
	
	@Column(name = "ESTADO_CONCILIACION")
	private String estadoConciliacionTransporte;

	@Column(name = "ESTADO")
	private String estadoTransporte;

	@Column(name = "OBSERVACIONES_ATH")
	private String observacionesAthTransporte;
	
	@Column(name = "OBSERVACIONES_TDV")
	private String observacionesTdvTransporte;

	@Column(name = "ID_ARCHIVO_CARGADO")
	private Long idArchivoCargadoTransporte;

	@Column(name = "ID_REGISTRO")
	private Long idRegistroTransporte;

	@Column(name = "USUARIO_CREACION")
	private String usuarioCreacionTransporte;
	
	@Column(name = "FECHA_CREACION")
	private Timestamp fechaCreacionTransporte;
	
	@Column(name = "USUARIO_MODIFICACION")
	private String usuarioModificacionTransporte;

	@Column(name = "FECHA_MODIFICACION")
	private Timestamp fechaModificacionTransporte;
	
	@Column(name = "ID_LIQUIDACION")
	private Long idLiquidacionTransporte;
	
	@Column(name = "TIPO_TRANSACCION")
	private Integer tipoTransaccionTransporte;

	@Column(name = "CODIGO_TDV")
	private String codigoTdvTransportadora;

}
