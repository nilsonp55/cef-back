package com.ath.adminefectivo.dto.compuestos;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import com.ath.adminefectivo.dto.CiudadesDTO;
import com.ath.adminefectivo.dto.ErroresContablesDTO;
import com.ath.adminefectivo.dto.TransaccionesContablesDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO encargado de contener la informacion del del retorno de la liquidacion de costos 
 * 
 * @author prv_dnaranjo
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RespuestaLiquidarCostosDTO {
		
	private Date fechaLiquidacion;
	
	private Date fechaEjecucion;
	
	private Integer codigoBanco;
	
	private String nombreBancoAval;
	
	private String codigoTdv; 
	
	private String nombreTdv;
	
	private String tipoOperacion;
	
	private String tipoServicio;
	
	private Integer puntoOrigen; 
	
	private String nombrePuntoOrigen;
	
	private Integer codigoDanePuntoOrigen;
	
	private String nombreCiudadPuntoOrigen;
	
	private Integer puntoDestino; 
	
	private String nombrePuntoDestino;
	
	private Integer codigoDanePuntoDestino;
	
	private String nombreCiudadPuntoDestino;
	
	private String escala;
	
	private Integer numeroFajos;
	
	private Integer residuoBilletes;
	
	private Integer numeroBolsas;
	
	private Integer residuoMonedas;
	
	private Double valorBilletes;
	
	private Double valorMonedas;
	
	private Double valorTotal;
	
	private Integer numeroParadas;
	
	private Integer milajeParadas;
	
	private Double milajePorRuteo;
	
	private Double milajeVerificacion;
	
	private Double costoRuteoVerificacion;
	
	private Double clasificacionFajado;
	
	private Double clasificacionNoFajado;
	
	private Integer billeteResiduo;
	
	private Double clasificacionMonedas;
	
	private Integer modenaResiduo;
	
	private Double costoEmisario;
	
	private Double costoPaqueteo;
	
	private Double tasaAeroportuaria;
	
	private Double costoCharter;
	
	private Integer idSeqGrupo;
	
	
	
	
	

}
