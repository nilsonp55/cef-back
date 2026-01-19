package com.ath.adminefectivo.dto.compuestos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Dto encardo de manejar la informacion de la conciliacion de transporte 
 * @author hector.mercado
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ConciliacionCostosTransporteDTO implements Serializable {
	
	static final long serialVersionUID = 6512941737390567965L;
	
	private Integer consecutivo;
	
	private String entidad;
	
	private String factura;
	
	private String tipoRegistro;
	
	private Date fechaServicioTransporte;

	private String identificacionCliente;
	
	private String razonSocial;

	private String codigoPuntoCargo;

	private String nombrePuntoCargo;
	
	private Integer codigoCiiuPunto;
	
	private String ciudadMunicipioPunto;
	
	private Integer codigoCiiuFondo;
	
	private String ciudadFondo;
	
	private String nombreTipoServicio;
	
	private String tipoPedido;

	private String escala;

	private String exclusivoMoneda;
	
	private String monedaDivisa;
	
	private BigDecimal trmConversion;
	
	private BigDecimal valorTransportadoBillete;
	
	private BigDecimal valorTransportadoMoneda;
	
	private BigDecimal valorTotalTransportado;
	
	private BigDecimal numeroFajos;
	
	private BigDecimal numeroBolsasMoneda;
	
	private Integer costoFijo;
	
	private BigDecimal costoMilaje;

	private BigDecimal costoBolsa;

	private Integer costoFletes;

	private Integer costoEmisarios;

	private Integer otros1;
	
	private Integer otros2;
	
	private Integer otros3;
	
	private Integer otros4;
	
	private Integer otros5;
	
	private BigDecimal subtotal;
	
	private BigDecimal iva;

	private BigDecimal valorTotal;
	
	private String estadoConciliacion;

	private String estado;

	private String observacionesAth;
	
	private String observacionesTdv;

	private Long idArchivoCargado;

	private Integer idRegistro;

	private String usuarioCreacion;
	
	private Timestamp fechaCreacion;
	
	private String usuarioModificacion;

	private Timestamp fechaModificacion;

}
