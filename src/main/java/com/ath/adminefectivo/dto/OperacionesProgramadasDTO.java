package com.ath.adminefectivo.dto;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

import javax.persistence.Column;

import com.ath.adminefectivo.entities.ConciliacionServicios;
import com.ath.adminefectivo.entities.DetalleOperacionesProgramadas;
import com.ath.adminefectivo.entities.OperacionesProgramadas;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene la estructura del archivo de Operaciones Programadas
 * @author cesar.castano
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OperacionesProgramadasDTO {

	private Integer idOperacion;
	
	private Integer codigoFondoTDV;
	
	private String entradaSalida;
	
	private Integer codigoPuntoOrigen;
	
	private Integer codigoPuntoDestino;
	
	private Date fechaProgramacion;
	
	private Date fechaOrigen;
	
	private Date fechaDestino;
	
	private String tipoOperacion;
	
	private String tipoTransporte;
	
	private Double valorTotal;
	
	private String estadoOperacion;
	
	private String idNegociacion;
	
	private String tasaNegociacion;
	
	private String estadoConciliacion;
	
	private Integer idArchivoCargado;
	
	private Integer idOperacionRelac;
	
	private String tipoServicio;
		
	private String usuarioCreacion;
	
	private String usuarioModificacion;
	
	private Date fechaCreacion;
	
	private Date fechaModificacion;
	
	private boolean esCambio;
	
	private String idServicio;
	
	private Integer comisionBR;
	
	private Boolean esEntrada;
	
	private Integer idOrdenTDV;
	
	private String codigoMoneda;

	private List<ConciliacionServicios> conciliacionServicios;
	
	private List<DetalleOperacionesProgramadas> detalleOperacionesProgramadas;
	
	/**
	 * Funcion que convierte el archivo DTO ProgramadasNoConciliadasDTO a Entity OperacionesProgramadas
	 * @author cesar.castano
	 */
	public static final Function<OperacionesProgramadasDTO, OperacionesProgramadas> CONVERTER_ENTITY = (OperacionesProgramadasDTO t) -> {

		var operacionesProgramadas = new OperacionesProgramadas();
		UtilsObjects.copiarPropiedades(t, operacionesProgramadas);		

		return operacionesProgramadas;
	};
	
	/**
	 * Funcion que convierte la entity OperacionesProgramadas a archivo DTO ProgramadasNoConciliadasDTO
	 * @author cesar.castano
	 */
	public static final Function<OperacionesProgramadas, OperacionesProgramadasDTO> CONVERTER_DTO = (OperacionesProgramadas t) -> {

		var operacionesProgramadasDTO = new OperacionesProgramadasDTO();
		UtilsObjects.copiarPropiedades(t, operacionesProgramadasDTO);		

		return operacionesProgramadasDTO;
	};

}
