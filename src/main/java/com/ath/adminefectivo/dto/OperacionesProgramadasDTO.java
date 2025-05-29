package com.ath.adminefectivo.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import com.ath.adminefectivo.dto.compuestos.DetalleOperacionesDTO;
import com.ath.adminefectivo.entities.ConciliacionServicios;
import com.ath.adminefectivo.entities.DetalleOperacionesProgramadas;
import com.ath.adminefectivo.entities.OperacionesProgramadas;
import com.ath.adminefectivo.utils.UtilsObjects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase que contiene la estructura del archivo de Operaciones Programadas
 * @author cesar.castano
 */

@Setter
@Getter
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
	
	private List<DetalleOperacionesDTO> detalleOperacionesProgramadasDTO;
	
	private String tdv;

	private String bancoAVAL;
	
	private String tipoPuntoOrigen;
	
	private String tipoPuntoDestino;
	
	/*
	 * HU006 Determina si se liquida costo para la operacion
	 */
	private boolean liquidaCosto;
	
	/**
	 * Funcion que convierte el archivo DTO ProgramadasNoConciliadasDTO a Entity OperacionesProgramadas
	 * @author cesar.castano
	 */
	public static final Function<OperacionesProgramadasDTO, OperacionesProgramadas> CONVERTER_ENTITY = (OperacionesProgramadasDTO t) -> {
		var operacionesProgramadas = new OperacionesProgramadas();
		UtilsObjects.copiarPropiedades(t, operacionesProgramadas);

		if(!Objects.isNull(t.getDetalleOperacionesProgramadasDTO())) {
			
			List<DetalleOperacionesProgramadas> listDetalleOperaciones = new ArrayList<>();
			t.getDetalleOperacionesProgramadasDTO().forEach(detalle -> 
				listDetalleOperaciones.add(DetalleOperacionesDTO.CONVERTER_ENTITY.apply(detalle))
			);
			operacionesProgramadas.setDetalleOperacionesProgramadas(listDetalleOperaciones);
		}
		return operacionesProgramadas;
	};
	
	/**
	 * Funcion que convierte la entity OperacionesProgramadas a archivo DTO ProgramadasNoConciliadasDTO
	 * @author cesar.castano
	 */
	public static final Function<OperacionesProgramadas, OperacionesProgramadasDTO> CONVERTER_DTO = (OperacionesProgramadas t) -> {

		var operacionesProgramadasDTO = new OperacionesProgramadasDTO();
		UtilsObjects.copiarPropiedades(t, operacionesProgramadasDTO);		
		if(!Objects.isNull(t.getDetalleOperacionesProgramadas())){
			List<DetalleOperacionesDTO> detalleOperacionDTO = new ArrayList<>();
			t.getDetalleOperacionesProgramadas().forEach(detalle ->
				detalleOperacionDTO.add(DetalleOperacionesDTO.CONVERTER_DTO.apply(detalle))
			);
		}
		return operacionesProgramadasDTO;
	};

}
