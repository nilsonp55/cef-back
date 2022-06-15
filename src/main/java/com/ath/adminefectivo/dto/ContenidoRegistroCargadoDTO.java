package com.ath.adminefectivo.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO del contenido del registro cargado de Oficinas
 * @author cesar.castano
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContenidoRegistroCargadoDTO {

	private String numeroOrden;
	
	private String tipoEntidad;
	
	private String estatus;
	
	private Date fechaEntrega;
	
	private Date fechaAprobacion;
	
	private String ReferencedID;
	
	private String DescripcionCortaPunto;
	
	private String userFile1Entidad;
	
	private String userFile2Ciudad;
	
	private String userFile3Transportadora;
	
//	private String codigoMoneda;
	
	private String shipIn;
	
	private String shipOut;
	
	private String denominacionFamilia;
}
