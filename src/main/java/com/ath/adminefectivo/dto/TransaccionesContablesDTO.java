package com.ath.adminefectivo.dto;

import java.util.Date;
import java.util.Objects;
import java.util.function.Function;

import com.ath.adminefectivo.entities.TransaccionesContables;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO para la entidad TransaccionesContablesDTO
 * @author duvan.naranjo 
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransaccionesContablesDTO {

	private Long idTransaccionesContables;
	
	private OperacionesProgramadasDTO idOperacion;
	
	private Integer idGenerico;
	
	private Date fecha;
	
	private String consecutivoDia;
	
	private Integer tipoTransaccion;
	
	private BancosDTO bancoAval;
	
	private String codigoCentro;
	
	private String naturaleza;
	
	private String cuentaContable;
	
	private String codigoMoneda;
	
	private long valor;
	
	private String tipoProceso;
	
	private String numeroComprobante;

	private String tipoIdentificacion;
	
	private Integer idTercero;
	
	private String nombreTercero;
	
	private String identificador;
	
	private String descripcion;
	
	private String referencia1;
	
	private String referencia2;
	
	private TransaccionesInternasDTO idTransaccionesInternas;

	private String cuentaAuxiliar;
	
	
	/**
	 * Funcion que retorna la entidad recibiendo un DTO *
	 */
	public static final Function<TransaccionesContablesDTO, TransaccionesContables> CONVERTER_ENTITY = (
			TransaccionesContablesDTO t) -> {

        var transaccionesContables = new TransaccionesContables();
        
        transaccionesContables.setIdGenerico(t.getIdGenerico());
        transaccionesContables.setFecha(t.getFecha());
        transaccionesContables.setConsecutivoDia(t.getConsecutivoDia());
        transaccionesContables.setTipoTransaccion(t.getTipoTransaccion());
        transaccionesContables.setNaturaleza(t.getNaturaleza());
        transaccionesContables.setCodigoMoneda(t.getCodigoMoneda());
        transaccionesContables.setValor(t.getValor());
        transaccionesContables.setTipoProceso(t.getTipoProceso());
        transaccionesContables.setNumeroComprobante(t.getNumeroComprobante());
        transaccionesContables.setTipoIdentificacion(t.getTipoIdentificacion());
        transaccionesContables.setIdTercero(t.getIdTercero());
        transaccionesContables.setNombreTercero(t.getNombreTercero());
        transaccionesContables.setIdentificador(t.getIdentificador());
        transaccionesContables.setDescripcion(t.getDescripcion());
        transaccionesContables.setReferencia1(t.getReferencia1());
        transaccionesContables.setReferencia2(t.getReferencia2());
        transaccionesContables.setCodigoCentro(t.getCodigoCentro());
        transaccionesContables.setCuentaContable(t.getCuentaContable());
        transaccionesContables.setCuentaAuxiliar(t.getCuentaAuxiliar());
        

        if(!Objects.isNull(t.getBancoAval())) {
        	transaccionesContables.setBancoAval(BancosDTO.CONVERTER_ENTITY.apply(t.getBancoAval()));
        }

        
        
        return transaccionesContables;
    };

	
	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<TransaccionesContables, TransaccionesContablesDTO> CONVERTER_DTO = (TransaccionesContables t) -> {
        var transaccionesContablesDTO = new TransaccionesContablesDTO();
        
        transaccionesContablesDTO.setIdGenerico(t.getIdGenerico());
        transaccionesContablesDTO.setFecha(t.getFecha());
        transaccionesContablesDTO.setConsecutivoDia(t.getConsecutivoDia());
        transaccionesContablesDTO.setTipoTransaccion(t.getTipoTransaccion());
        transaccionesContablesDTO.setNaturaleza(t.getNaturaleza());
        transaccionesContablesDTO.setCodigoMoneda(t.getCodigoMoneda());
        transaccionesContablesDTO.setValor(t.getValor());
        transaccionesContablesDTO.setTipoProceso(t.getTipoProceso());
        transaccionesContablesDTO.setNumeroComprobante(t.getNumeroComprobante());
        transaccionesContablesDTO.setTipoIdentificacion(t.getTipoIdentificacion());
        transaccionesContablesDTO.setIdTercero(t.getIdTercero());
        transaccionesContablesDTO.setNombreTercero(t.getNombreTercero());
        transaccionesContablesDTO.setIdentificador(t.getIdentificador());
        transaccionesContablesDTO.setDescripcion(t.getDescripcion());
        transaccionesContablesDTO.setReferencia1(t.getReferencia1());
        transaccionesContablesDTO.setReferencia2(t.getReferencia2());
        transaccionesContablesDTO.setCodigoCentro(t.getCodigoCentro());
        transaccionesContablesDTO.setCuentaContable(t.getCuentaContable());
        transaccionesContablesDTO.setCuentaAuxiliar(t.getCuentaAuxiliar());
        

        if(!Objects.isNull(t.getBancoAval())) {
        	transaccionesContablesDTO.setBancoAval(BancosDTO.CONVERTER_DTO.apply(t.getBancoAval()));
        }

        
        
        return transaccionesContablesDTO;
	};
	
}
