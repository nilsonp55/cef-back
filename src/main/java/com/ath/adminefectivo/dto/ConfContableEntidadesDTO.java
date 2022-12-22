package com.ath.adminefectivo.dto;

import java.util.Objects;
import java.util.function.Function;
import com.ath.adminefectivo.entities.Bancos;
import com.ath.adminefectivo.entities.Puntos;
import com.ath.adminefectivo.entities.ConfContableEntidades;
import com.ath.adminefectivo.entities.Transportadoras;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO para la entidad ConfContableEntidadesDTO
 * @author BayronPerez
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfContableEntidadesDTO {

	private Long consecutivo;
	
	private BancosDTO bancoAval;
	
	private Integer tipoTransaccion;
	
	private String tipoOperacion;
	
	private Integer codigoComision;
	
	private Integer tipoImpuesto;
	
	private String medioPago;
	
	private PuntosDTO codigoPuntoBancoExt;
	
	private TransportadorasDTO transportadora;
	
	private Boolean esCambio;

	private String naturaleza;
	
	private String cuentaContable;
	
	
	/**
	 * Funcion que retorna la entidad recibiendo un DTO *
	 */
	public static final Function<ConfContableEntidadesDTO, ConfContableEntidades> CONVERTER_ENTITY = (ConfContableEntidadesDTO t) -> {
		ConfContableEntidades confContableEntidades = new ConfContableEntidades();
		UtilsObjects.copiarPropiedades(t, confContableEntidades);
		
		if(!Objects.isNull(t.getBancoAval())) {
			confContableEntidades.setBancoAval(BancosDTO.CONVERTER_ENTITY.apply(t.getBancoAval()));
		}
		
		if(!Objects.isNull(t.getCodigoPuntoBancoExt())) {
			confContableEntidades.setCodigoPuntoBancoExt(PuntosDTO.CONVERTER_ENTITY.apply(t.getCodigoPuntoBancoExt()));
		}
		
		if(!Objects.isNull(t.getTransportadora())) {
			confContableEntidades.setTransportadora(TransportadorasDTO.CONVERTER_ENTITY.apply(t.getTransportadora()));
		}
		
		return confContableEntidades;
	};

	
	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<ConfContableEntidades, ConfContableEntidadesDTO> CONVERTER_DTO = (ConfContableEntidades t) -> {
		ConfContableEntidadesDTO confContableEntidadesDTO = new ConfContableEntidadesDTO();
		UtilsObjects.copiarPropiedades(t, confContableEntidadesDTO);
		
		if(!Objects.isNull(t.getBancoAval())) {
			confContableEntidadesDTO.setBancoAval(BancosDTO.CONVERTER_DTO.apply(t.getBancoAval()));
		}
		
		if(!Objects.isNull(t.getCodigoPuntoBancoExt())) {
			confContableEntidadesDTO.setCodigoPuntoBancoExt(PuntosDTO.CONVERTER_DTO.apply(t.getCodigoPuntoBancoExt()));
		}
		
		if(!Objects.isNull(t.getTransportadora())) {
			confContableEntidadesDTO.setTransportadora(TransportadorasDTO.CONVERTER_DTO.apply(t.getTransportadora()));
		}
		
		return confContableEntidadesDTO;
	};
	
}
