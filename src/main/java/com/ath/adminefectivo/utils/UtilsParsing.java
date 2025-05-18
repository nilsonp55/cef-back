package com.ath.adminefectivo.utils;

import java.math.BigDecimal;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene los servicios utilitarios de los Strings
 *
 * @author CamiloBenavides
 */
public class UtilsParsing {

	/**
	 * convierte una cadena de numero decimal a decimal 
	 * 
	 * @param Long
	 * @return BigDecimal
	 * @author hector.mercado
	 */
	public static BigDecimal longToDecimal(Long numero) {
	
		var bDecimal = BigDecimal.ZERO;
		try {
			bDecimal = BigDecimal.valueOf(numero);
		} catch (Exception e) {
			//debe retornar Zero
		}
		return bDecimal;
		
	}
	
	/**
	 * convierte una cadena de numero decimal a decimal 
	 * 
	 * @param Double
	 * @return BigDecimal
	 * @author hector.mercado
	 */
	public static BigDecimal doubleToDecimal(Double numero) {
	
		var bDecimal = BigDecimal.ZERO;
		try {
			bDecimal = BigDecimal.valueOf(numero);
		} catch (Exception e) {
			//debe retornar Zero
		}
		return bDecimal;
		
	}
	
	/**
	 * convierte una cadena de numero decimal a decimal 
	 * 
	 * @param Integer
	 * @return BigDecimal
	 * @author hector.mercado
	 */
	public static BigDecimal integerToDecimal(Integer numero) {
	
		var bDecimal = BigDecimal.ZERO;
		try {
			bDecimal = BigDecimal.valueOf(numero);
		} catch (Exception e) {
			//debe retornar Zero
		}
		return bDecimal;
		
	}
	
	/**
	 * convierte una cadena de numero decimal a decimal 
	 * 
	 * @param Long
	 * @return BigDecimal
	 * @author hector.mercado
	 */
	public static Long doubleToLong(Double numero) {
	
		var num = 0l;
		try {
			num = numero.longValue();
		} catch (Exception e) {
			//debe retornar Zero
		}
		return num;
		
	}

	private UtilsParsing() {
		//It should not be called::No debería ser llamado
		throw new IllegalStateException("Constructor no deberia ser llamado");
	}

}
