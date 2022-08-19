package com.ath.adminefectivo.constantes;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Interfaz encargada de exponer las constantes logicas del aplicativo
 * 
 * @author CamiloBenavides
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constantes {

	public static final String ZONE_ID = "America/Bogota";
	public static final String TIMEZONE = "EST";
	public static final String FECHA_PATTERN = "dd-MM-yyyy";
	public static final String FECHA_HORA_PATTERN = "dd-MM-yyyy hh:mm:ss a";
	public static final String FORMATO_FECHA_ARCHIVOS = "ddMMyyyyhhmmss";
	public static final String REGISTRO_ACTIVO = "ACTIVO";
	public static final String REGISTRO_INACTIVO = "INA";
	public static final String SI = "SI";
	public static final String NO = "NO";
	public static final String ENTRADA = "I";
	public static final String SALIDA = "O";
	public static final String SOBRANTE_OTROS_FONDOS = "3";
	public static final String FALTANTE_OTROS_FONDOS = "4";
	public static final String SOBRANTE_BRINKS = "4";
	public static final String FALTANTE_BRINKS = "5";
	public static final Integer NUMERO_INICIA_VALORES_OTROS_FONDOS = 17;
	public static final Integer NUMERO_INICIA_VALORES_BRINKS = 18;
	public static final String NOMBRE_ENTRADA = "ENTRADAS";
	public static final String NOMBRE_SALIDA = "SALIDAS";
	public static final String SOBRANTE = "SOBRANTE";
	public static final String FALTANTE = "FALTANTE";
	public static final Integer INICIA_DENOMINACION_OTROS_FONDOS = 17;
	public static final Integer INICIA_DENOMINACION_BRINKS = 18;
	public static final String CLIENTE = "CLI";
	public static final String SUCURSAL = "SUC";
	public static final Integer HORA_TIPO_SERVICIO_PROGRAMADA = 18;
	public static final String NUMERO_MINIMO_ARCHIVOS_PARA_CIERRE = "NUMERO_MINIMO_ARCHIVOS";
	public static final String ESTADO_MAESTRO_DEFINICION_ACTIVO = "ACTIVO";


	/**
	 * Constantes estado Cargue
	 */

	public static final String ESTADO_CARGUE_PENDIENTE = "PEND";
	public static final String ESTADO_CARGUE_ERROR = "ERROR";
	public static final String ESTADO_CARGUE_VALIDO = "OK";
	

	/**
	 * Constantes estado Pedido
	 */

	public static final String ESTADO_PEDIDO_APRVD = "APRVD";
	public static final String ESTADO_PEDIDO_DELIV = "DELIV";
	public static final String ESTADO_PEDIDO_CANCEL = "CANCEL";
	public static final String ESTADO_PEDIDO_DECLI = "DECLI";

	/**
	 * Formatos de fecha
	 */
	public static final String FECHA_PATTERN_DD_MM_YYYY = "dd-MM-yyyy";
	public static final String FECHA_PATTERN_DD_MM_YYYY_WITH_SLASH = "dd/MM/yyyy";
	public static final String FECHA_PATTERN_YYYY_MM_DD = "yyyy-MM-dd";
	public static final String FECHA_PATTERN_YYYYMMDD = "yyyyMMdd";
	public static final String FECHA_PATTERN_HHMMSS = "hhmmss";
	public static final String FECHA_PATTERN_HH_MM_SS = "HH:mm:ss";
	public static final String FECHA_HORA_PATTERN_DD_MM_YYYY_HH_MM_SS = "dd-MM-yyyy HH:mm:ss";
	public static final String FECHA_HORA_PATTERN_DD_MM_YYYY_T_HH_MM_SS = "dd-MM-yyyy'T'HH:mm:ss";

	/**
	 * Constantes tipo puntos
	 */
	public static final String PUNTO_BANCO = "BANCO";
	public static final String PUNTO_BANC_REP = "BAN_REP";
	public static final String PUNTO_CAJERO = "CAJERO";
	public static final String PUNTO_SITIO_CLIENTE = "CLIENTE";
	public static final String PUNTO_FONDO = "FONDO";
	public static final String PUNTO_OFICINA = "OFICINA";
	
	/**
	 * Constantes nombres archivo
	 */

	public static final String NOMBRE_ARCHIVO_OTROS_FONDOS = "ITVCS";
	public static final String NOMBRE_ARCHIVO_BRINKS = "BRINK";

	
	/*
	 * Parametro header necesario para el envio de los documentos a front
	 */
	public static final String PARAMETER_HEADER = "attachment;filename=";

	/**
	 * PROGRAMACION PRELIMINAR
	 */
	public static final Object PROGRAMACION_PRELIMINAR = "PRELIMINAR";

	/**
	 * Constante del agrupador de los delimitadores
	 */
	public static final String DOMINIO_DELIMITADOR = "DELIMITADOR";

	/**
	 * Constante del agrupador de los delimitadores
	 */
	public static final String DOMINIO_MULTIFORMATO = "MULTIFORMATO";
	
	/**
	 * Constante del agrupador de vigencia
	 */
	public static final String DOMINIO_VIGENCIA = "VIGENCIA";
	
	/**
	 * Constante del agrupador de los estados
	 */
	public static final String DOMINIO_ESTADO = "ESTADO";
	
	/**
	 * Constante del agrupador de los tipos de operacion
	 */
	public static final String DOMINIO_TIPO_OPERACION = "TIPO_OPERACION";
	
	/**
	 * Constante del agrupador de los estados de operacion
	 */
	public static final String DOMINIO_ESTADOS_OPERACION = "ESTADOS_OPERACION";
	
	/**
	 * Constante del agrupador de los tipos de transporte
	 */
	public static final String DOMINIO_TIPOS_TRANSPORTE = "TIPOS_TRANSPORTE";
	
	/**
	 * Constante del agrupador de los tipo de servicio
	 */
	public static final String DOMINIO_TIPO_SERVICIO = "TIPO_SERVICIO";
	
	/**
	 * Constante del agrupador de los estados de conciliacion
	 */
	public static final String DOMINIO_ESTADO_CONCILIACION = "ESTADO_CONCILIACION";
	
	/**
	 * Constante del agrupador de los tipos de conciliacion
	 */
	public static final String DOMINIO_TIPOS_CONCILIACION = "TIPO_CONCILIACION";
	
	/**
	 * Constante del agrupador de los tipos punto
	 */
	public static final String DOMINIO_FORMATO_FECHA_HORA = "FORMATO_FECHA_HORA";
	
	/**
	 * Constante del agrupador de los tipos punto
	 */
	public static final String DOMINIO_FORMATO_FECHA = "FORMATO_FECHA";

	/**
	 * Constante del formato de fecha Hora F1
	 */
	public static final String DOMINIO_FORMATO_FECHA_HORA_F1 = "FORMATO_FECHA_HORA_1";
	
	/**
	 * Constante del formato de fecha Hora F2
	 */
	public static final String DOMINIO_FORMATO_FECHA_HORA_F2 = "FORMATO_FECHA_HORA_2";

	/**
	 * Constante del formato de fecha Hora F3
	 */
	public static final String DOMINIO_FORMATO_FECHA_HORA_F3 = "FORMATO_FECHA_HORA_3";
	
	/**
	 * Constante del formato de fecha F1
	 */
	public static final String DOMINIO_FORMATO_FECHA_F1 = "FORMATO_FECHA";
	
	/**
	 * Constante del formato de fecha F2
	 */
	public static final String DOMINIO_FORMATO_FECHA_F2 = "FORMATO_FECHA";
	
	/**
	 * Constante del agrupador de los tipos punto
	 */
	public static final String DOMINIO_TIPOS_PUNTO = "TIPOS_PUNTO";
	

	
	/**
	 * Constante del agrupador de los tipos punto
	 */
	public static final String DOMINIO_CARACTER_INVALIDO_TX = "CARACTER_INVALIDO_TX";
	
	/**
	 * Constante del agrupador de los tipos punto
	 */
	public static final String DOMINIO_TIPO_ARCHIVO = "TIPO_ARCHIVO";
	
	/**
	 * Constante del agrupador de los dominios  de las comisiones
	 */
	public static final String DOMINIO_COMISIONES = "COMISIONES";
	
	/**
	 * Constante del agrupador de los dominios  de los impuestos
	 */
	public static final String DOMINIO_IMPUESTOS = "IMPUESTOS";
	
	/**
	 * Constante del agrupador de los dominios  de los medios de pago
	 */
	public static final String DOMINIO_MEDIOS_PAGO = "MEDIOS_PAGO";
	
	/**
	 * Delimintadores de los archivos
	 */
	public static final String DELIMITADOR_OTROS = "O";

	/**
	 * Constante del tipo registro estandar
	 */
	public static final String CODIGO_MULTIFORMATO_STD = "CODIGO_MULTIFORMATO_STD";
	
	/**
	 * Constante ID del multiformato por estandar
	 */
	public static final int ID_MULTIFORMATO_STD = 1;
	
	/**
	 * Constante de linea inicial de multiformato estandar
	 */
	public static final String MULTIFORMATO_LINEA_INICIAL = "MULTIFORMATO_LINEA_INICIAL";

	/**
	 * Expresión regular del punto
	 */
	public static final String EXPRESION_REGULAR_PUNTO = "\\.";

	public static final String REGEX_PUNTO = "\\.";
	
	/**
	 * Expresión regular formato valido de horas en formato am/pm 
	 */
	public static final String REGEX_FORMATO_VALIDO_HORAS = "^(?:0?[0-9]|1[0-2]):[0-5][0-9](:[0-5][0-9])?\\s?([AaPp][Mm])?$";
	
	/**
	 * Expresión regular formato valido de horas en formato 24 horas
	 */
	public static final String REGEX_FORMATO_VALIDO_HORAS_24H = "^([01]?[0-9]|2[0-3]):[0-5][0-9](:[0-5][0-9])?$";
	
	/**
	 * Expresión regular formato valido números enteros
	 */
	public static final String REGEX_FORMATO_NUMERO_ENTERO = "[+-]?\\d*";
	
	/**
	 * Expresión regular formato valido números decimales
	 */
	public static final String REGEX_FORMATO_NUMERO_DECIMAL = "[+-]?\\d*(\\.\\d+)?";
	
	/**
	 * Expresión regular para permitir numeros de 1 a 9
	 */
	public static final String REGEX_NUMEROS_PERMITIDOS = "[1-9]";
	
	/**
	 * Caracter serparador de las extenciones de los archivos
	 */
	public static final String SEPARADOR_EXTENSION_ARCHIVO = ".";
	
	/**
	 * Caracter serparador los registros separados por punto y coma
	 */
	public static final String SEPARADOR_PUNTO_Y_COMA = ";";

	/**
	 * Caracter separador de las fechas de los archivos
	 */
	public static final String SEPARADOR_FECHA_ARCHIVO = "_";
	
	/**
	 * Variable que determina que una operacion esta conciliable
	 */
	public static final String CONCILIABLE_SI = "SI";
	
	 /**
	  *  La Estructura no es validada.
	 */
	public static final String ESTRUCTURA_OK = "OK";
	
	/**
	 * Constante de control y cierre de procesos, para evitar ciclos infinitos
	 */
	public static final int NUMERO_MAXIMO_CICLOS = 30;
	
	/**
	 * Constante para identificar el tipo servicio de un archivo
	 */
	public static final String CAMPO_ARCHIVO_TIPO_SERVICIO = "TIPOSERVICIO";
	
	/**
	 * Constante para identificar el banco origen de un archivo
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_ENTIDAD_ORIGEN = "ENTIDADORIGEN";
	
	
	/**
	 * Constante para identificar el banco origen de un archivo
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_ENTIDAD_DESTINO = "ENTIDADDESTINO";
	
	/**
	 * Constante para identificar el fondo origen de un archivo
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_FONDO_ORIGEN = "CODIGOORIGEN";
	
	/**
	 * Constante para identificar el fondo destino de un archivo
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_FONDO_DESTINO = "CODIGODESTINO";
	
	/**
	 * Constante para identificar la fecha programacion
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_FECHAPROGRAMACION= "FECHAPROGRAMACION";
	
	/**
	 * Constante para identificar la fecha de origen
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_FECHAORIGEN= "FECHAORIGEN";
	
	/**
	 * Constante para identificar la fecha de destino
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_FECHADESTINO = "FECHADESTINO";
	
	/**
	 * Constante para identificar el campo tipo servicio del archivo
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_TIPOSERVICIO = "TIPOSERVICIO";
	
	/**
	 * Constante para identificar el campo valor total del archivo
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_VALORTOTAL = "VALOR TOTAL";
	
	/**
	 * Constante para identificar el campo id negociacion del archivo
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_IDNEGOCIACION = "IDNEGOCIACION";
	
	/**
	 * Constante para identificar el campo tasa del archivo
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_TASA = "TASA";
	
	/**
	 * Constante para indicar el valor SALIDA
	 */
	public static final String VALOR_SALIDA = "SALIDA";
	
	/**
	 * Constante para indicar el valor ENTRADA
	 */
	public static final String VALOR_ENTRADA = "ENTRADA";
	
	/**
	 * PROGRAMACION DEFINITIVA
	 */
	/**
	 * Constante para indicar el valor FECHA ENTREGA en el archivo de OFICINAS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_FECHA_ENTREGA = "FECHAENTREGA";

	/**
	 * Constante para indicar el valor FECHA APROBACION en el archivo de OFICINAS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_FECHA_APROBACION = "FECHAAPROBACION";

	/**
	 * Constante para indicar el valor ORDERID en el archivo de OFICINAS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_ORDERID = "ORDERID";

	/**
	 * Constante para indicar el valor SHIPIN en el archivo de OFICINAS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_SHIPIN = "SHIPIN";

	/**
	 * Constante para indicar el valor SHIPOUT en el archivo de OFICINAS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_SHIPOUT = "SHIPOUT";

	/**
	 * Constante para indicar el valor ESTATUS en el archivo de OFICINAS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_ESTATUS = "ESTATUS";

	/**
	 * Constante para indicar el valor TRANSPORTADORA en el archivo de OFICINAS Y CAJEROS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_TRANSPORTADORA = "TRANSPORTADORA";

	/**
	 * Constante para indicar el valor CIUDAD en el archivo de OFICINAS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_CIUDAD = "CIUDAD";

	/**
	 * Constante para indicar el valor REFERENCEID en el archivo de OFICINAS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_REFERENCEDID = "REFERENCEDID";

	/**
	 * Constante para indicar el valor DENOMINACION en el archivo de OFICINAS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_DENOMINACION = "DENOMINACION";

	/**
	 * Constante para indicar el valor nombrebanco en el archivo de CAJEROS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_BANCO = "NOMBREBANCO";

	/**
	 * Constante para indicar el valor referencedid en el archivo de CAJEROS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_REFERENCE = "REFERENCE";

	/**
	 * Constante para indicar el valor numeroOrden en el archivo de CAJEROS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_NUMEROORDEN = "NUMEROORDEN";

	/**
	 * Constante para indicar el valor en el archivo de CAJEROS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_VALOR = "SHIPIN";

	/**
	 * PROGRAMACION CERTIFICACIONES
	 */

	/**
	 * Constante para indicar el valor en el archivo de FONDOS NO BRINKS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_TIPOREGISTRO = "TIPOREGISTRO";

	/**
	 * Constante para indicar el NITBANCO en el archivo de FONDOS NO BRINKS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_NITBANCO = "NITBANCO";

	/**
	 * Constante para indicar el CIUDAD en el archivo de FONDOS NO BRINKS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_CODIGOCIUDAD = "CODIGOCIUDAD";

	/**
	 * Constante para indicar el FECHAEMISION en el archivo de FONDOS NO BRINKS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_FECHAEJECUCION = "FECHAEMISION";

	/**
	 * Constante para indicar el CODIGOSERVICIO en el archivo de FONDOS NO BRINKS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_CODIGOSERVICIO = "CODIGOSERVICIOTDV";

	/**
	 * Constante para indicar el ENTRADASALIDA en el archivo de FONDOS NO BRINKS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_ENTRADASALIDA = "DESCRIPCIONES";

	/**
	 * Constante para indicar el CODIGOPUNTO en el archivo de FONDOS NO BRINKS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_CODIGOPUNTO = "CODIGOPUNTO";

	/**
	 * Constante para indicar el TIPOSERVICIO en el archivo de FONDOS NO BRINKS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_TIPOSERVICIOF = "TIPOSERVICIO";

	/**
	 * Constante para indicar el DESCRIPCIONNOVEDAD en el archivo de FONDOS NO BRINKS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_DESCRIPCIONNOVEDAD = "DESCRIPCIONNOVEDAD";

	/**
	 * Constante para indicar el MONTOTOTAL en el archivo de FONDOS NO BRINKS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_MONTOTOTAL = "MONTOTALNOVEDAD";

	/**
	 * Constante para indicar el SIGLATDV en el archivo de FONDOS NO BRINKS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_SIGLATDV = "SIGLATDV";

	/**
	 * Constante para indicar el SIGLATRANSPORTADORA en el archivo de FONDOS NO BRINKS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_SIGLATRANSPORTADORA = "SIGLATRANSPORTADORA";

	/**
	 * Constante para indicar el CODIGOENTIDAD en el archivo de FONDOS NO BRINKS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_CODIGONITENTIDAD = "CODIGONITENTIDAD";

	/**
	 * Constante para indicar el CODIGODANE en el archivo de FONDOS NO BRINKS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_CODIGODANE = "CODIGODANE";

	/**
	 * Constante para indicar el FECHAEMISION en el archivo de FONDOS NO BRINKS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_FECHAEMISION = "FECHAEMISION";

	/**
	 * Constante para indicar el CODIGOSERVICIOTRANS en el archivo de FONDOS NO BRINKS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_CODIGOSERVTRANS = "CODIGOSERVTRANS";

	/**
	 * Constante para indicar el ENTRADASALIDA en el archivo de FONDOS NO BRINKS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_ENTRADAOSALIDA = "DESCRIPCIONENTRADASALIDA";

	/**
	 * Constante para indicar el MONTOTOTALNOVEDAD en el archivo de FONDOS NO BRINKS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_MONTOTOTALNOVEDAD = "MONTOTOTALNOVEDAD";
	
	//////////////////////////////////////// MENSAJES ///////////////////////////////////////////
	/**
	 * Mensaje de campo requerido 
	 */
	public static final String CAMPO_REQUERIDO = "El campo es requerido y no puede ser nulo o vacio.";
	
	/**
	 * Mensaje de campo no valido 
	 */
	public static final String CAMPO_NO_VALIDO ="El tipo campo no es válido";
	
	/**
	 * 
	 */
	public static final String DETALLE_NO_VALIDO = "El detalle para este elemento no es válido.";
	
	/**
	 * Error lanzado en caso de que la regla de no tenga un mensaje definido
	 */
	public static final String MENSAJE_ERROR_VALIDACION_CAMPOS = "Ocurrió un error con al regla número {0}";
	
	/**
	 * No se encontraron archivos por procesar en el proceso de operaciones programadas
	 */
	public static final String MENSAJE_NO_SE_ENCONTRARON_ARCHIVOS_OP = "No se encontraron archivos por procesar operaciones programadas.";
	
	/**
	 * Se generaron las operaciones programadas de forma exitosa.
	 */
	public static final String MENSAJE_GENERO_OPERACIONES_PROGRAMADAS_CORRECTO = "Se han generado las operaciones programadas de forma correcta.";
	
	public static final String DELETE_SEPARADORES = "";
}
