package com.ath.adminefectivo.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.exception.AplicationException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene los servicios utilitarios de los Strings
 *
 * @author CamiloBenavides
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
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


}
