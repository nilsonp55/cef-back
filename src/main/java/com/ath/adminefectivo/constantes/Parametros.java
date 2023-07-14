package com.ath.adminefectivo.constantes;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Clase encargada de exponer las codigos de los parametros persistidos en base
 * de datos
 * 
 * @author CamiloBenavides
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Parametros {

	public static final String RUTA_ARCHIVOS_PENDIENTES = "RUTA_ARCHIVOS_PENDIENTES";
	public static final String RUTA_ARCHIVOS_PROCESADOS = "RUTA_ARCHIVOS_PROCESADOS";
	public static final String RUTA_ARCHIVOS_ERRADOS = "RUTA_ARCHIVOS_ERRADOS";
	
	/**
	 * Caracter que indica el numero de linea en la cual viene el ID del archivo
	 */
	public static final String CAMPO_ID_ARCHIVO = "CAMPO_ID_ARCHIVO";
	
	/**
	 * Parametros de cierre dia
	 */	
	public static final String FECHA_DIA_ACTUAL_PROCESO = "FECHA_DIA_PROCESO";
	public static final String NUMERO_PROCESOS_TOTALES_DIA = "NUMERO_PROCESOS_DIA";
	
	/**
	 * Parametros de 
	 */
	public static final String CONSULTA_BASE_MOTOR_REGLAS ="SQL_BASE_CONSULTAS_MOTOR";

	public static final String TIPO_CARPETA = "TIPO_CARPETA";
	
	/**
	 * Codigo parametros de algoritmos
	 */
	public static final String AES256 = "AES256";
	public static final String BYTES_AES = "BYTES_AES";
	public static final String INSTANCIA_AES = "INSTANCIA_AES";
	public static final String VALUE_SECRET_KEY = "SECRET_KEY";
	public static final String SALT_VALUE = "SALT_VALUE";
	public static final String URL_FILE_PUBLIC_KEY = "URL_FILE_PUBLIC_KEY";
	
	
	

}
