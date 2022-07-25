package com.ath.adminefectivo.dto;

import java.util.Date;
import java.util.Objects;
import java.util.function.Function;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.ath.adminefectivo.entities.Bancos;
import com.ath.adminefectivo.entities.Ciudades;
import com.ath.adminefectivo.entities.ConfContableEntidades;
import com.ath.adminefectivo.entities.OperacionesProgramadas;
import com.ath.adminefectivo.entities.Puntos;
import com.ath.adminefectivo.entities.TiposCentrosCostos;
import com.ath.adminefectivo.entities.TransaccionesContables;
import com.ath.adminefectivo.entities.TransaccionesInternas;
import com.ath.adminefectivo.entities.Transportadoras;
import com.ath.adminefectivo.utils.UtilsObjects;

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
	
	private int consecutivoDia;
	
	private Integer tipoTransaccion;
	
	private BancosDTO bancoAval;
	
	private TiposCentrosCostosDTO codigoCentro;
	
	private String naturaleza;
	
	private ConfContableEntidadesDTO cuentaContable;
	
	private String codigoMoneda;
	
	private Double valor;
	
	private String tipoProceso;
	
	private String numeroComprobante;

	private String tipoIdentificacion;
	
	private Integer idTercero;
	
	private String nombreTercero;
	
	private String identificador;
	
	private String descripcion;
	
	private String referencia1;
	
	private String referencia2;
	
	
	
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
        
        

        if(!Objects.isNull(t.getBancoAval())) {
        	transaccionesContables.setBancoAval(BancosDTO.CONVERTER_ENTITY.apply(t.getBancoAval()));
        }
        if(!Objects.isNull(t.getCodigoCentro())) {
        	transaccionesContables.setCodigoCentro(TiposCentrosCostosDTO.CONVERTER_ENTITY.apply(t.getCodigoCentro()));	
        }
        if(!Objects.isNull(t.getCuentaContable())) {
        	transaccionesContables.setCuentaContable(ConfContableEntidadesDTO.CONVERTER_ENTITY.apply(t.getCuentaContable()));	
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
        
        

        if(!Objects.isNull(t.getBancoAval())) {
        	transaccionesContablesDTO.setBancoAval(BancosDTO.CONVERTER_DTO.apply(t.getBancoAval()));
        }
        if(!Objects.isNull(t.getCodigoCentro())) {
        	transaccionesContablesDTO.setCodigoCentro(TiposCentrosCostosDTO.CONVERTER_DTO.apply(t.getCodigoCentro()));	
        }
        if(!Objects.isNull(t.getCuentaContable())) {
        	transaccionesContablesDTO.setCuentaContable(ConfContableEntidadesDTO.CONVERTER_DTO.apply(t.getCuentaContable()));	
        }

        
        
        return transaccionesContablesDTO;
	};
	
}
