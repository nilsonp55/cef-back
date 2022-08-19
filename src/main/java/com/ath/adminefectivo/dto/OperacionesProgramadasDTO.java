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
	
	/**
	 * Funcion que convierte el archivo DTO ProgramadasNoConciliadasDTO a Entity OperacionesProgramadas
	 * @author cesar.castano
	 */
	public static final Function<OperacionesProgramadasDTO, OperacionesProgramadas> CONVERTER_ENTITY = (OperacionesProgramadasDTO t) -> {
		var operacionesProgramadas = new OperacionesProgramadas();
		operacionesProgramadas.setCodigoFondoTDV(t.getCodigoFondoTDV());
		operacionesProgramadas.setCodigoMoneda(t.getCodigoMoneda());
		operacionesProgramadas.setCodigoPuntoDestino(t.getCodigoPuntoDestino());
		operacionesProgramadas.setCodigoPuntoOrigen(t.getCodigoPuntoOrigen());
		operacionesProgramadas.setComisionBR(t.getComisionBR());
		operacionesProgramadas.setEntradaSalida(t.getEntradaSalida());
		operacionesProgramadas.setEsCambio(t.isEsCambio());
		operacionesProgramadas.setEsEntrada(t.getEsEntrada());
		operacionesProgramadas.setEstadoConciliacion(t.getEstadoConciliacion());
		operacionesProgramadas.setEstadoOperacion(t.getEstadoOperacion());
		operacionesProgramadas.setFechaCreacion(t.getFechaCreacion());
		operacionesProgramadas.setFechaDestino(t.getFechaDestino());
		operacionesProgramadas.setFechaModificacion(t.getFechaModificacion());
		operacionesProgramadas.setFechaOrigen(t.getFechaOrigen());
		operacionesProgramadas.setFechaProgramacion(t.getFechaProgramacion());
		operacionesProgramadas.setIdArchivoCargado(t.getIdArchivoCargado());
		operacionesProgramadas.setIdNegociacion(t.getIdNegociacion());
		operacionesProgramadas.setIdOperacion(t.getIdOperacion());
		operacionesProgramadas.setIdOperacionRelac(t.getIdOperacionRelac());
		operacionesProgramadas.setIdOrdenTDV(t.getIdOrdenTDV());
		operacionesProgramadas.setIdServicio(t.getIdServicio());
		operacionesProgramadas.setTasaNegociacion(t.getTasaNegociacion());
		operacionesProgramadas.setTipoOperacion(t.getTipoOperacion());
		operacionesProgramadas.setTipoServicio(t.getTipoServicio());
		operacionesProgramadas.setTipoTransporte(t.getTipoTransporte());
		operacionesProgramadas.setUsuarioCreacion(t.getUsuarioCreacion());
		operacionesProgramadas.setUsuarioModificacion(t.getUsuarioModificacion());
		operacionesProgramadas.setValorTotal(t.getValorTotal());

		if(!Objects.isNull(t.getDetalleOperacionesProgramadasDTO())) {
			
			List<DetalleOperacionesProgramadas> listDetalleOperaciones = new ArrayList<>();
			
			t.getDetalleOperacionesProgramadasDTO().forEach(detalle -> {
				DetalleOperacionesProgramadas detalleEntidad = 
					DetalleOperacionesDTO.CONVERTER_ENTITY.apply(detalle);
				listDetalleOperaciones.add(detalleEntidad);
			});
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

		return operacionesProgramadasDTO;
	};

}
