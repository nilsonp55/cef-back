package com.ath.adminefectivo.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RespuestaContableDTO {
	
	
	private int bancoAval;
	
	private String nombreBancoAval;
	
	private String abreviaturaBancoAval;
	
	private String naturalezaContable;
	
	private String cuentaMayor;
	
	private String subAuxiliar;
	
	private String tipoIdentificacion;
	
	private String codigoMoneda;
	
	private Integer valor;
	
	private String centroCosto;
	
	private String centroBeneficio;
	
	private String ordenCo;
	
	private String areaFuncional;
	
	private String identificador;
	
	private String descripcionTransaccion;
	
	private Integer terceroGL;
	
	private String nombreTerceroGL;
	
	private Date fechaConversion;
	
	private String claveReferencia1;
	
	private String claveReferencia2;
	
}

