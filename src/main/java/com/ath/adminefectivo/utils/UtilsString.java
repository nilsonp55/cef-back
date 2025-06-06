package com.ath.adminefectivo.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.exception.AplicationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Clase que contiene los servicios utilitarios de los Strings
 *
 * @author CamiloBenavides
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2
public class UtilsString {

	/**
	 * Retorna true cuando el la cadena de texto corresponde a un numero entero
	 * 
	 * @param str
	 * @return boolean
	 * @author CamiloBenavides
	 */
	public static boolean isNumeroEntero(String str) {
		return str.trim().matches(Constantes.REGEX_FORMATO_NUMERO_ENTERO);
	}

	/**
	 * Retorna true cuando la cadena de texto corresponde a un numero decimal
	 * 
	 * @param str
	 * @return boolean
	 * @author CamiloBenavides
	 */
	public static boolean isNumeroDecimal(String str) {
		return str.trim().matches(Constantes.REGEX_FORMATO_NUMERO_DECIMAL);
	}

	/**
	 * Retorna true cuando la cadena de texto corresponde a un numero decimal y sus
	 * sus decimales son menores que al valor permitido
	 * 
	 * @param str
	 * @param numeroDecimales
	 * @return boolean
	 * @author CamiloBenavides
	 */
	public static boolean validarNumeroDecimal(String str, int numeroDecimales) {
		return isNumeroDecimal(str) && contarDecimales(str) <= numeroDecimales;
	}

	/**
	 * Retorna true cuando la cadena de texto corresponde a char, incluyendo los
	 * caracteres especiales
	 * 
	 * @param str
	 * @return boolean
	 * @author CamiloBenavides
	 */
	public static boolean isChar(String str) {
		var arrayChar = str.trim().toCharArray();
		return arrayChar.length == 1;
	}

	/**
	 * Retorna el numero de decimales que contiene un candena de texto, que
	 * corresponde a un numero decimal
	 * 
	 * @param str
	 * @return int
	 * @author CamiloBenavides
	 */
	public static int contarDecimales(String str) {
		String[] array = str.trim().split(Constantes.REGEX_PUNTO);
		return array.length < 2 ? 0 : array[1].length();
	}

	/**
	 * Retorna true si la cadena de texto corresponde a una hora en formato 24H o am
	 * pm
	 * 
	 * @param str
	 * @return boolean
	 * @author CamiloBenavides
	 */
	public static boolean isHoras(String str) {
		return str.trim().matches(Constantes.REGEX_FORMATO_VALIDO_HORAS)
				|| str.trim().matches(Constantes.REGEX_FORMATO_VALIDO_HORAS_24H);
	}

	/**
	 * Retorna true si la cadena de texto corresponde a una fecha sin horas, ni
	 * minutos recibe una lista de formatos de fecha validos,
	 * 
	 * @param str
	 * @param listFormato
	 * @return
	 * @return boolean
	 * @author CamiloBenavides
	 */
	public static boolean isFecha(String str, List<String> listFormato) {
		String[] strArray = new String[listFormato.size()];
		strArray = listFormato.toArray(strArray);
		log.debug("str: {} - listFormato: {}", str, listFormato.toString());
		if(Objects.isNull(str)) {
		  log.debug("str isNull return");
			return false;
		}
		
		try {
			Date fechaConvertida = DateUtils.parseDate(str, strArray);
			log.debug("str parseDate return - parseDate: {}", fechaConvertida.toString());
			return true;
		} catch (Exception e) {
		  log.debug("parseDate Exception return - str: {} - ex: {}", str, e.getMessage());
			return false;
		}

	}

	/**
	 * Retorna true si la cadena de texto corresponde a una fecha con horas, y
	 * minutos recibe una lista de formatos de fecha validos,
	 * 
	 * @param str
	 * @param listFormato
	 * @return boolean
	 * @author CamiloBenavides
	 */
	public static boolean isDateWithHours(String str, List<String> listFormato) {
		String[] strArray;

		if (listFormato.isEmpty()) {
			strArray = new String[] { Constantes.FECHA_HORA_PATTERN_DD_MM_YYYY_HH_MM_SS };
		} else {
			strArray = new String[listFormato.size()];
			strArray = listFormato.toArray(strArray);
		}

		try {
			DateUtils.parseDate(str, strArray);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * Valida si la cadena de texto contiene parametros no validos, los caracteres
	 * invalidos son recibidos por parametro, en caso de que la lista sea vacia,
	 * significara que todos los caracteres son permitidos y por tal motivo
	 * retornara siempre true
	 * 
	 * @param str
	 * @param caracteres
	 * @return boolean
	 * @author CamiloBenavides
	 */
	public static boolean isTexto(String str, List<String> caracteres) {
		String[] strArray = new String[caracteres.size()];
		strArray = caracteres.toArray(strArray);

		return !StringUtils.containsAny(str, strArray);

	}

	/**
	 * Valida si el valor es un numero en caso de que no sea un numero retorna falso
	 * 
	 * @param numero
	 * @return boolean
	 * @author duvan.naranjo
	 */
	public static boolean isNumero(String numero) {

		try {
			Integer.parseInt(numero);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}

	}
	
	/**
	 * Convierte una cadena de numero entero a entero
	 * 
	 * @param numero
	 * @return Integer
	 * @author hector.mercado
	 */
	public static Integer toInteger(String numero) {

		Integer num = 0;
		
		try {
			num = Integer.parseInt(numero);
		} catch (NumberFormatException e) {
			num = 0;
		}

		return num;
		
	}
	
	/**
	 * Convierte una cadena de numero Long a Long
	 * 
	 * @param numero
	 * @return Integer
	 * @author hector.mercado
	 */
	public static Long toLong(String numero) {

		Long num = 0l;
		
		try {
			num = Long.parseLong(numero);
		} catch (NumberFormatException e) {
			num = 0l;
		}

		return num;
		
	}
	
	/**
	 * convierte una cadena de numero decimal a decimal 
	 * 
	 * @param str
	 * @return BigDecimal
	 * @author CamiloBenavides
	 */
	public static BigDecimal toDecimal(String str, char separador) {
	
		var symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator(separador);
		
		var decimalFormat = new DecimalFormat();
		decimalFormat.setDecimalFormatSymbols(symbols);
		decimalFormat.setParseBigDecimal(true);
		
		var bDecimal = new BigDecimal(0);
		try {
			bDecimal = (BigDecimal) decimalFormat.parse(str);
		} catch (ParseException e) {
			bDecimal = new BigDecimal(0);
		}
		
		return bDecimal;
		
	}

	
	

	/**
	 * recibe un string y lo convierte en tipo date segun los dominios existentes
	 * 
	 * @param fecha
	 * @param listaDominioFecha
	 * @return Date
	 * @author duvan.naranjo
	 */
	public static Date convertirFecha(String fecha, List<String> listaDominioFecha) {
		String[] strArray = new String[listaDominioFecha.size()];
		strArray = listaDominioFecha.toArray(strArray);
		if (isFecha(fecha, listaDominioFecha)) {
			try {
				return DateUtils.parseDateStrictly(fecha, strArray);
			} catch (ParseException e) {
				throw new AplicationException(ApiResponseCode.ERROR_PARAMETRO_NO_FECHA.getCode(),
						ApiResponseCode.ERROR_PARAMETRO_NO_FECHA.getDescription(),
						ApiResponseCode.ERROR_PARAMETRO_NO_FECHA.getHttpStatus());
			}
		}
		return null;
	}
	
	/**
	 * recibe un string y lo convierte en tipo date segun los dominios existentes
	 * 
	 * @param fecha
	 * @param listaDominioFecha
	 * @return Date
	 * @author hector.mercado
	 */
	public static Date toDate(String fecha, List<String> listaDominioFecha) {
	
		String[] strArray = new String[listaDominioFecha.size()];
		strArray = listaDominioFecha.toArray(strArray);
		if (isFecha(fecha, listaDominioFecha)) {
			try {
				return DateUtils.parseDateStrictly(fecha, strArray);
			} catch (ParseException e) {
				return null;
			}
		}
		return null;
	}

	
	/**
	 * recibe una fecha, le resta un dia y devuelve nueva fecha
	 * 
	 * @param fecha
	 * @param dias
	 * @return Date
	 * @author cesar.castano
	 */
	public static Date restarDiasAFecha(Date fecha, int dias) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(Calendar.DAY_OF_YEAR, dias);
		return calendar.getTime();
	}

	/**
	 * Retorna valor Date si la cadena de texto corresponde a una fecha con horas, y
	 * minutos recibe una lista de formatos de fecha validos. Si no cumple el
	 * formato retorna nulo
	 * 
	 * @param str
	 * @param listFormato
	 * @return boolean
	 * @author rparra
	 */
	public static Date toDateWithHours(String str, List<String> listFormato) {
		String[] strArray;
		if (listFormato.isEmpty()) {
			strArray = new String[] { Constantes.FECHA_HORA_PATTERN_DD_MM_YYYY_HH_MM_SS };
		} else {
			strArray = new String[listFormato.size()];
			strArray = listFormato.toArray(strArray);
		}
		try {
			return DateUtils.parseDate(str, strArray);
		} catch (Exception e) {
			return null;
		}
	}

	public static double calcularDiferenciaAbsoluta(double valor1, double valor2) {
		return Math.abs(valor1 - valor2);
	}

	// Helper method to sanitize input using Apache Commons Lang
	public static String sanitizeInput(String input) {
		if (input == null) {
			return "";
		}
		// Remove accents and check if the string is alphanumeric
		String sanitized = StringUtils.stripAccents(input);
		return StringUtils.isAlphanumeric(sanitized) ? sanitized : "";
	}
}
