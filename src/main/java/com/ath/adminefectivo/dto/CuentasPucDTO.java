package com.ath.adminefectivo.dto;

import java.util.function.Function;

import com.ath.adminefectivo.entities.Bancos;
import com.ath.adminefectivo.entities.ConfContableEntidades;
import com.ath.adminefectivo.entities.CuentasPuc;
import com.ath.adminefectivo.entities.TiposCentrosCostos;
import com.ath.adminefectivo.entities.TiposCuentas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO para la entidad CuentasPucDTO
 * @author BayronPerez
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CuentasPucDTO {

	private Long idCuentasPuc;
	
	private String cuentaContable;
	
	private BancosDTO bancoAval;
	
	private String nombreCuenta;
	
	private String identificador;
	
	private TiposCentrosCostosDTO tiposCentrosCostos;
	
	private TiposCuentasDTO tiposCuentas;
	
	private Boolean estado;
	
	
	
	/**
	 * Funcion que retorna la entidad recibiendo un DTO *
	 */
	public static final Function<CuentasPucDTO, CuentasPuc> CONVERTER_ENTITY = (CuentasPucDTO t) -> {
		
		CuentasPuc cuentasPuc = new CuentasPuc();
		cuentasPuc.setIdCuentasPuc(t.getIdCuentasPuc());	
		
		cuentasPuc.setCuentaContable(t.getCuentaContable());
		
		Bancos bancos = new Bancos();
		bancos.setCodigoPunto(t.getBancoAval().getCodigoPunto());
		cuentasPuc.setBancoAval(bancos);
		
		cuentasPuc.setNombreCuenta(t.getNombreCuenta());
		cuentasPuc.setIdentificador(t.getIdentificador());
		cuentasPuc.setEstado(t.getEstado());
		TiposCentrosCostos tiposCentrosCosto = new TiposCentrosCostos();
		tiposCentrosCosto.setTipoCentro(t.getTiposCentrosCostos().getTipoCentro());
		cuentasPuc.setTiposCentrosCostos(tiposCentrosCosto);
		
		TiposCuentas tiposCuentas = new TiposCuentas();
		tiposCuentas.setTipoCuenta(t.getTiposCuentas().getTipoCuenta());
		cuentasPuc.setTiposCuentas(tiposCuentas);
		
		return cuentasPuc;
		
	};

	
	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<CuentasPuc, CuentasPucDTO> CONVERTER_DTO = (CuentasPuc t) -> {
		CuentasPucDTO cuentasPuc = new CuentasPucDTO();
		cuentasPuc.setIdCuentasPuc(t.getIdCuentasPuc());	
		
		cuentasPuc.setCuentaContable(t.getCuentaContable());
		
		BancosDTO bancos = new BancosDTO();
		bancos.setCodigoPunto(t.getBancoAval().getCodigoPunto());
		cuentasPuc.setBancoAval(bancos);
		
		cuentasPuc.setNombreCuenta(t.getNombreCuenta());
		cuentasPuc.setIdentificador(t.getIdentificador());
		cuentasPuc.setEstado(t.getEstado());
		
		TiposCentrosCostosDTO tiposCentrosCosto = new TiposCentrosCostosDTO();
		tiposCentrosCosto.setTipoCentro(t.getTiposCentrosCostos().getTipoCentro());
		cuentasPuc.setTiposCentrosCostos(tiposCentrosCosto);
		
		TiposCuentasDTO tiposCuentas = new TiposCuentasDTO();
		tiposCuentas.setTipoCuenta(t.getTiposCuentas().getTipoCuenta());
		cuentasPuc.setTiposCuentas(tiposCuentas);
		
		return cuentasPuc;
	};
	
}
