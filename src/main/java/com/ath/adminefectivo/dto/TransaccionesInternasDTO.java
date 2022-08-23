package com.ath.adminefectivo.dto;

import java.util.Date;
import java.util.Objects;
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
 * 
 * @author BayronPerez
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransaccionesInternasDTO {

	private Long idTransaccionesInternas;
	
	private String consecutivoDia;
	
	private BancosDTO bancoAval;

	private Date fecha;

	private OperacionesProgramadasDTO idOperacion;

	private Integer idGenerico;

	private Integer tipoTransaccion;

	private String codigoMoneda;
	
	private Integer valor;
	
	private Integer tasaNoEje;

	private Integer tasaEjeCop;

	private PuntosDTO codigoPunto;

	private String tipoOperacion;

	private Integer codigoComision;

	private Integer tipoImpuesto;

	private TransportadorasDTO codigoTdv;

	private PuntosDTO codigoPuntoBancoExt;

	private CiudadesDTO ciudad;

	private Boolean esCambio;

	private String tipoProceso;
	
	private int estado;
	
	private String tasaNegociacion;

	private String medioPago;

	/**
	 * Funcion que retorna la entidad recibiendo un DTO *
	 */
	public static final Function<TransaccionesInternasDTO, TransaccionesInternas> CONVERTER_ENTITY = (
			TransaccionesInternasDTO t) -> {

		var transaccionesInternas = new TransaccionesInternas();

		transaccionesInternas.setCodigoComision(t.getCodigoComision());
		transaccionesInternas.setCodigoMoneda(t.getCodigoMoneda());
		transaccionesInternas.setConsecutivoDia(t.getConsecutivoDia());
		transaccionesInternas.setEsCambio(t.getEsCambio());
		transaccionesInternas.setEstado(t.getEstado());
		transaccionesInternas.setFecha(t.getFecha());
		transaccionesInternas.setIdGenerico(t.getIdGenerico());
		transaccionesInternas.setIdTransaccionesInternas(t.getIdTransaccionesInternas());
		transaccionesInternas.setTasaEjeCop(t.getTasaEjeCop());
		transaccionesInternas.setTasaNoEje(t.getTasaNoEje());
		transaccionesInternas.setTipoImpuesto(t.getTipoImpuesto());
		transaccionesInternas.setTipoOperacion(t.getTipoOperacion());
		transaccionesInternas.setTipoProceso(t.getTipoProceso());
		transaccionesInternas.setValor(t.getValor());
		transaccionesInternas.setTasaNegociacion(t.getTasaNegociacion());
		transaccionesInternas.setMedioPago(t.getMedioPago());
		transaccionesInternas.setTipoTransaccion(t.getTipoTransaccion());

		if (!Objects.isNull(t.getBancoAval())) {
			transaccionesInternas.setBancoAval(BancosDTO.CONVERTER_ENTITY.apply(t.getBancoAval()));
		}
		if (!Objects.isNull(t.getCiudad())) {
			transaccionesInternas.setCiudad(CiudadesDTO.CONVERTER_ENTITY.apply(t.getCiudad()));
		}
		if (!Objects.isNull(t.getCodigoPunto())) {
			transaccionesInternas.setCodigoPunto(PuntosDTO.CONVERTER_ENTITY.apply(t.getCodigoPunto()));
		}

		if (!Objects.isNull(t.getCodigoPuntoBancoExt())) {
			transaccionesInternas.setCodigoPuntoBancoExt(PuntosDTO.CONVERTER_ENTITY.apply(t.getCodigoPuntoBancoExt()));
		}

		if (!Objects.isNull(t.getCodigoTdv())) {
			transaccionesInternas.setCodigoTdv(TransportadorasDTO.CONVERTER_ENTITY.apply(t.getCodigoTdv()));
		}

		if (!Objects.isNull(t.getIdOperacion())) {
			transaccionesInternas.setIdOperacion(OperacionesProgramadasDTO.CONVERTER_ENTITY.apply(t.getIdOperacion()));
		}

		return transaccionesInternas;
	};

	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<TransaccionesInternas, TransaccionesInternasDTO> CONVERTER_DTO = (
			TransaccionesInternas t) -> {
		TransaccionesInternasDTO transaccionesInternasDTO = new TransaccionesInternasDTO();

		transaccionesInternasDTO.setCodigoComision(t.getCodigoComision());
		transaccionesInternasDTO.setCodigoMoneda(t.getCodigoMoneda());
		transaccionesInternasDTO.setConsecutivoDia(t.getConsecutivoDia());
		transaccionesInternasDTO.setEsCambio(t.getEsCambio());
		transaccionesInternasDTO.setEstado(t.getEstado());
		transaccionesInternasDTO.setFecha(t.getFecha());
		transaccionesInternasDTO.setIdGenerico(t.getIdGenerico());
		transaccionesInternasDTO.setIdTransaccionesInternas(t.getIdTransaccionesInternas());
		transaccionesInternasDTO.setTasaEjeCop(t.getTasaEjeCop());
		transaccionesInternasDTO.setTasaNoEje(t.getTasaNoEje());
		transaccionesInternasDTO.setTipoImpuesto(t.getTipoImpuesto());
		transaccionesInternasDTO.setTipoOperacion(t.getTipoOperacion());
		transaccionesInternasDTO.setTipoProceso(t.getTipoProceso());
		transaccionesInternasDTO.setValor(t.getValor());
		transaccionesInternasDTO.setTasaNegociacion(t.getTasaNegociacion());
		transaccionesInternasDTO.setMedioPago(t.getMedioPago());
		transaccionesInternasDTO.setTipoTransaccion(t.getTipoTransaccion());

		if (!Objects.isNull(t.getBancoAval())) {
			transaccionesInternasDTO.setBancoAval(BancosDTO.CONVERTER_DTO.apply(t.getBancoAval()));
		}
		if (!Objects.isNull(t.getCiudad())) {
			transaccionesInternasDTO.setCiudad(CiudadesDTO.CONVERTER_DTO.apply(t.getCiudad()));
		}
		if (!Objects.isNull(t.getCodigoPunto())) {
			transaccionesInternasDTO.setCodigoPunto(PuntosDTO.CONVERTER_DTO.apply(t.getCodigoPunto()));
		}

		if (!Objects.isNull(t.getCodigoPuntoBancoExt())) {
			transaccionesInternasDTO.setCodigoPuntoBancoExt(PuntosDTO.CONVERTER_DTO.apply(t.getCodigoPuntoBancoExt()));
		}

		if (!Objects.isNull(t.getCodigoTdv())) {
			transaccionesInternasDTO.setCodigoTdv(TransportadorasDTO.CONVERTER_DTO.apply(t.getCodigoTdv()));
		}

		if (!Objects.isNull(t.getIdOperacion())) {
			transaccionesInternasDTO.setIdOperacion(OperacionesProgramadasDTO.CONVERTER_DTO.apply(t.getIdOperacion()));
		}

		return transaccionesInternasDTO;
	};

}
