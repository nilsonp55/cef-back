package com.ath.adminefectivo.dto;

import java.util.Date;
import java.util.function.Function;

import com.ath.adminefectivo.entities.Bancos;
import com.ath.adminefectivo.entities.Ciudades;
import com.ath.adminefectivo.entities.OperacionesProgramadas;
import com.ath.adminefectivo.entities.Puntos;
import com.ath.adminefectivo.entities.TransaccionesInternas;
import com.ath.adminefectivo.entities.Transportadoras;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO para la entidad TransaccionesInternasDTO
 * @author BayronPerez
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransaccionesInternasDTO {

	private Long idTransaccionesInternas;
	
	private String consecutivoDia;
	
	private Bancos bancoAval;
	
	private Date fecha;
	
	private OperacionesProgramadas idOperacion;
	
	private Integer idGenerico;
	
	private Integer tipoTransaccion;
	
	private String codigoMoneda;
	
	private Integer valor;
	
	private Integer tasaNoEje;
	
	private Integer tasaEjeCop;
	
	private Puntos codigoPunto;
	
	private String tipoOperacion;
	
	private Integer codigoComision;
	
	private Integer tipoImpuesto;
	
	private Transportadoras codigoTdv;
	
	private Puntos codigoPuntoBancoExt;
	
	private Ciudades ciudad;

	private Boolean esCambio;
	
	private String tipoProceso;
	
	private Integer estado;
	
	private String tasaNegociacion;
	
	private String medioPago;
	
	/**
	 * Funcion que retorna la entidad recibiendo un DTO *
	 */
	public static final Function<TransaccionesInternasDTO, TransaccionesInternas> CONVERTER_ENTITY = (TransaccionesInternasDTO t) -> {
		TransaccionesInternas transaccionesInternas = new TransaccionesInternas();
		UtilsObjects.copiarPropiedades(t, transaccionesInternas);
		return transaccionesInternas;
	};

	
	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<TransaccionesInternas, TransaccionesInternasDTO> CONVERTER_DTO = (TransaccionesInternas t) -> {
		TransaccionesInternasDTO transaccionesInternasDTO = new TransaccionesInternasDTO();
		UtilsObjects.copiarPropiedades(t, transaccionesInternasDTO);
		return transaccionesInternasDTO;
	};
	
}
