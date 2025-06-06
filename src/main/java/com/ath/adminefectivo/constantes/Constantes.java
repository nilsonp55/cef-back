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
	public static final String FECHA_PATTERN_NO_GUION = "ddMMyyyy";
	public static final String FECHA_HORA_PATTERN = "dd-MM-yyyy hh:mm:ss a";
	public static final String FORMATO_FECHA_ARCHIVOS = "ddMMyyyyhhmmss";
	public static final String FORMATO_FECHA_ARCHIVOS_MS = "ddMMyyyyhhmmssSSS";
	public static final String ESTADO_MAESTRO_DEFINICION_ACTIVO = "ACTIVO";
	public static final String REGISTRO_ACTIVO = "ACT";
	public static final String REGISTRO_INACTIVO = "INA";
	public static final String SI = "SI";
	public static final String NO = "NO";
	public static final String ENTRADA = "1";
	public static final String SALIDA = "2";
	public static final String SALIDA_BRINKS = "0";
	public static final String SOBRANTE_OTROS_FONDOS = "1";
	public static final String FALTANTE_OTROS_FONDOS = "2";
	public static final String SOBRANTE_BRINKS = "4";
	public static final String FALTANTE_BRINKS = "5";
	public static final String SOBRANTE_TVS = "3";
	public static final String FALTANTE_TVS = "4";
	public static final Integer NUMERO_INICIA_VALORES_OTROS_FONDOS = 17;
	public static final Integer NUMERO_INICIA_VALORES_BRINKS = 18;
	public static final String NOMBRE_ENTRADA = "ENTRADA";
	public static final String NOMBRE_SALIDA = "SALIDA";
	public static final String SOBRANTE = "SOBRANTE";
	public static final String FALTANTE = "FALTANTE";
	public static final Integer INICIA_DENOMINACION_OTROS_FONDOS = 17;
	public static final Integer INICIA_DENOMINACION_BRINKS = 18;
	public static final String CLIENTE = "CLI";
	public static final String SUCURSAL = "SUC";
	public static final Integer HORA_TIPO_SERVICIO_PROGRAMADA = 18;
	public static final String NUMERO_MINIMO_ARCHIVOS_PARA_CIERRE = "NUMERO_MINIMO_ARCHIVOS";
	public static final Integer NUMERO_ARCHIVOS_CARGADOS_PRELIMINAR = 1;
	public static final Integer NUMERO_ARCHIVOS_CARGADOS_DEFINITIVA = 2;
	public static final String FECHA_DIA_PROCESO = "FECHA_DIA_PROCESO";
	public static final String PARAMETRO_PRIVATE_KEY_RSA = "PRIVATE_KEY_RSA";
	public static final String PARAMETRO_PUBLIC_KEY_RSA = "PUBLIC_KEY_RSA";
	public static final String INSTANCIA_RSA = "INSTANCIA_RSA";
	public static final String BYTES_RSA = "BYTES_RSA";
	public static final String NAME_PUBLIC_KEY_RSA = "NAME_PUBLIC_KEY";
	public static final String NAME_PRIVATE_KEY_RSA = "NAME_PRIVATE_KEY";
	public static final Integer ESTADO_REGISTRO_ACTIVO = 1;
	public static final Integer ESTADO_REGISTRO_INACTIVO = 2;
	public static final Integer TIPO_MONEDA_BRINKS = 3;


	/**
	 * Constantes estado Cargue
	 */

	public static final String ESTADO_CARGUE_PENDIENTE = "PEND";
	public static final String ESTADO_CARGUE_ERROR = "ERRADO";
	public static final String ESTADO_CARGUE_ELIMINADO = "ELIMINADO";
	public static final String ESTADO_CARGUE_VALIDO = "OK";
	public static final String ESTADO_ARCHIVO_HISTORICO = "HIS";
	
	/**
	 * Constantes de estados para auditoria procesos
	 */
	public static final String ESTADO_PROCESO_INICIO = "INICIADO";
	public static final String ESTADO_PROCESO_PROCESO = "EN PROCESO";
	public static final String ESTADO_PROCESO_PENDIENTE = "PENDIENTE";
	public static final String ESTADO_PROCESO_PROCESADO = "PROCESADO";
	public static final String ESTADO_PROCESO_ERROR = "ERROR";
	

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
	 * Constante del agrupador de los dominios  de los estados generales
	 */
	public static final String DOMINIO_ESTADO_GENERAL = "ESTADO_GENERAL";
	
	/**
	 * Constante del agrupador de los dominios  de los escalas
	 */
	public static final String DOMINIO_ESCALAS = "ESCALA";
	
	/**
	 * Constante del tipo de encriptado en dominios
	 */
	public static final String DOMINIO_TIPO_ENCRIPTADO = "TIPO_ENCRIPTADO";
	
	/**
	 * Constante del tipo de encriptado en dominios
	 */
	public static final String DOMINIO_AUDITORIA_PROCESOS = "AUDITORIA_PROCESOS";
	
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
	 * Expresión regular formato para extraer información de la mascara
	 */
	public static final String REGEX_EXTRAER_MASCARA = "\\[(.*?)\\]";
	
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
	 * Caracter serparador de directorios slash(unix)
	 */
	public static final String SEPARADOR_DIRECTORIO_UNIX = "/";
	
	/**
	 * Variable que determina que una operacion esta conciliable
	 */
	public static final String CONCILIABLE_SI = "SI";
	
	 /**
	  *  La Estructura no es validada.
	 */
	public static final String ESTRUCTURA_OK = "OK";
	
	/**
	  *  La Estructura no es validada.
	 */
	public static final String ERROR_DE_ESTRUCTURA = "El delimitador no es válido o la cantidad de campos no coincide con la estructura esperada";
	
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
	 * Constante para identificar si en un retiro se cobra comision BanRepublica
	 */
	public static final String CAMPO_DETALLE_COBROBR = "COBRO BANREP";
	
	
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
	 * Constante para indicar el valor Currency en el archivo de OFICINAS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_MONEDA = "CURRENCY";
	
	/**
	 * PROGRAMACION DEFINITIVA OFICINAS
	 */
	/**
	 * Constante para indicar el valor FECHA ENTREGA en el archivo de OFICINAS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_FECHA_ENTREGA = "FECHAENTREGA";

	/**
	 * Constante para indicar el valor FECHA APROBACION en el archivo de OFICINAS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_FECHA_APROBACION = "FECHAAPROBACIÓN";

	/**
	 * Constante para indicar el valor ORDERID en el archivo de OFICINAS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_ORDERID = "NROORDEN";

	/**
	 * Constante para indicar el valor SHIPIN en el archivo de OFICINAS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_SHIPIN = "SHIP-IN";

	/**
	 * Constante para indicar el valor SHIPOUT en el archivo de OFICINAS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_SHIPOUT = "SHIP-OUT";

	/**
	 * Constante para indicar el valor ESTATUS en el archivo de OFICINAS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_ESTATUS = "ESTATUS";

	/**
	 * Constante para indicar el valor TRANSPORTADORA en el archivo de OFICINAS Y CAJEROS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_TRANSPORTADORA = "USERFIELD2-TRANSPORTADORA";

	/**
	 * Constante para indicar el valor CIUDAD en el archivo de OFICINAS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_CIUDAD = "USERFIELD2-CIUDAD";

	/**
	 * Constante para indicar el valor REFERENCEID en el archivo de OFICINAS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_REFERENCEDID = "REFERENCEDID";

	/**
	 * Constante para indicar el valor DENOMINACION en el archivo de OFICINAS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_DENOMINACION = "DENOMINACIÓN";

	/**
	 * PROGRAMACION DEFINITIVA CAJEROS
	 */
	/**
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
	public static final String CAMPO_DETALLE_ARCHIVO_TIPOREGISTRO = "TPOREGISTRO";

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
	public static final String CAMPO_DETALLE_ARCHIVO_FECHAEJECUCION = "FECHAOPERACION";

	/**
	 * Constante para indicar el CODIGOSERVICIO en el archivo de FONDOS NO BRINKS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_CODIGOSERVICIO = "CODIGOSERVICIOTDV";

	/**
	 * Constante para indicar el ENTRADASALIDA en el archivo de FONDOS NO BRINKS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_ENTRADASALIDA = "ENTRADASALIDA";

	/**
	 * Constante para indicar el CODIGOPUNTO en el archivo de FONDOS NO BRINKS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_CODIGOPUNTO = "CODIGOPUNTO";
	
	/**
     * Constante para indicar el CODIGOPUNTO en el archivo de FONDOS NO BRINKS (PROG. DEFINTIVA)
     */
    public static final String CAMPO_DETALLE_ARCHIVO_NOMBREPUNTO = "DESCRIPCIONPUNTO";

	/**
	 * Constante para indicar el TIPOSERVICIO en el archivo de FONDOS NO BRINKS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_TIPOSERVICIOF = "TIPOSERVICIO";
	
	/**
	 * 	
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_CODIGOOPERACION = "CODIGOOPERACION";

	/**
	 * Constante para indicar el DESCRIPCIONNOVEDAD en el archivo de FONDOS NO BRINKS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_DESCRIPCIONNOVEDAD = "DESCRIPCIONNOVEDAD";

	/**
	 * Constante para indicar el MONTOTOTAL en el archivo de FONDOS NO BRINKS
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_MONTOTOTAL = "MONTOTOTAL";

	/**
	 * Constante para indicar el SIGLATDV en el archivo de FONDOS NO BRINKS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_SIGLATDV = "SIGLATDV";

	/**
	 * Constante para indicar el SIGLATRANSPORTADORA en el archivo de FONDOS NO BRINKS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_SIGLATRANSPORTADORA = "SIGLATDV";

	/**
	 * Constante para indicar el CODIGOENTIDAD en el archivo de FONDOS NO BRINKS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_CODIGONITENTIDAD = "NITBANCO";

	/**
	 * Constante para indicar el CODIGODANE en el archivo de FONDOS NO BRINKS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_CODIGODANE = "CODIGOCIUDAD";

	/**
	 * Constante para indicar el FECHAEMISION en el archivo de FONDOS NO BRINKS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_FECHAEMISION = "FECHAEMISION";

	/**
	 * Constante para indicar el CODIGOSERVICIOTRANS en el archivo de FONDOS NO BRINKS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_CODIGOSERVTRANS = "CODIGOSERVICIO";

	/**
	 * Constante para indicar el ENTRADASALIDA en el archivo de FONDOS NO BRINKS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_ENTRADAOSALIDA = "ENTRADASALIDA";

	/**
	 * Constante para indicar el VALORNOVEDAD en el archivo de FONDOS BRINKS
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_VALORNOVEDAD = "VALOR";
	
	/**
	 * Constante para indicar el MONTOTOTALNOVEDAD en el archivo de FONDOS NO BRINKS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_TIPONOVEDAD = "TIPONOVEDAD";
	
	/**
	 * Constante para indicar el CODIGOPUNTO en el archivo de FONDOS NO BRINKS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_CODIGOPUNTO_TIPO_5 = "CODIGOPUNTO";
	
	/**
	 * Constante para indicar el CODIGOPUNTO en el archivo de FONDOS NO BRINKS (PROG. DEFINTIVA)
	 */
	public static final String CAMPO_DETALLE_ARCHIVO_NOMBREPUNTO_TIPO_5 = "NOMBREPUNTO";
	
public static final String DELETE_SEPARADORES = "";
	
	public static final String CADENA_VACIA = "";
	
	/**
	 * Nombre de archivos generados xls 
	 */
	public static final String CTB_BBOG_MANANA = "Contabilidad_BBOG_Cambios_";
	public static final String CTB_BBOG_TARDE = "Contabilidad_BBOG_";
	public static final String CTB_BOCC_MANANA = "Contabilidad_BOCC_Manana";
	public static final String CTB_BOCC_TARDE = "Contabilidad_BOCC_Tarde";
	public static final String CTB_BPOP_MANANA = "Contabilidad_BPOP_Manana";
	public static final String CTB_BPOP_TARDE = "Contabilidad_BPOP_Tarde";
	public static final String CTB_BAVV_MANANA = "Contabilidad_BAVV_Manana";
	public static final String CTB_BAVV_TARDE = "Contabilidad_BAVV_Tarde";
	
	public static final String EXTENSION_ARCHIVO_XLS = ".xls";
	public static final String EXTENSION_ARCHIVO_XLSX = ".xlsx";
	public static final String EXTENSION_ARCHIVO_TXT = ".txt";
	
	/**
	 * URL en la cual se almacenaran los archivos generados de contabilidad
	 */
	public static final String URL_ARCHIVOS_CONTABLES_S3 = "CEfectivo/Contabilidad/";
	public static final String URL_ARCHIVOS_CONTABLES_ENVIADOS_S3 = "CEfectivo/Contabilidad/Enviados/";
	
	/**
	 * Separador del tipo de mes año en el proceso de liquidacion 
	 * de la clasificacion de costos
	 */
	public static final String SEPARADOR_COSTOS_CLASIFICACION_MES_ANIO = "-";
	
	public static final String COMISION_APLICAR_CLASIFICACION_DETERIORADO = "CLASIFICACION DETERIORADO";
	public static final String COMISION_APLICAR_CLASIFICACION_FAJADO = "CLASIFICACION FAJADO";
	public static final String COMISION_APLICAR_CLASIFICACION_MONEDA = "CLASIFICACION MONEDA";
	
	public static final String TIPO_OPERACION_CONSOLIDACION_FAJADA = "CONSOLIDACION FAJADA";
	public static final String TIPO_OPERACION_CONSOLIDACION_NO_FAJADA = "CONSOLIDACION NO FAJADA";
	public static final String TIPO_OPERACION_VERIFICACION_BILLETE = "VERIFICACION BILLETE";
	public static final String TIPO_OPERACION_VERIFICACION_MONEDA = "VERIFICACION MONEDA";
	public static final String TIPO_OPERACION_CLASIFICACION_BILLETE = "CLASIFICACION BILLETE";
	public static final String TIPO_OPERACION_CLASIFICACION_MONEDA = "CLASIFICACION MONEDA";
	public static final String TIPO_OPERACION_COSTO_PAQUETEO = "COSTO PAQUETEO";
	
	public static final String FACTOR_BILLETE = "BILLETE";
	public static final String FACTOR_MONEDA = "MONEDA";
	
	
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
	public static final int ID_GENERICO = 99999;
	
	/**
	 * Se generaron las clasificacion de costos de forma exitosa.
	 */
	public static final String MENSAJE_GENERO_CLASIFICACION_COSTOS_CORRECTO = "Se ha generado la clasificación de costos de forma correcta.";
	public static final String MENSAJE_GENERO_CLASIFICACION_COSTOS_ERRONEO = "No se ha generó ninguna clasificación de costos.";

	public static final String DOMINIO_DIVISAS = "DIVISAS";
	
	public static final String MIN_DENOM_COP = "MIN_DENOM_COP";
	public static final Integer BANCO_BOGOTA = 297;
	public static final String MONEDA_COP = "COP";
	
	
	/**
	 * Constantes de ArchivosLiquidacion.
	 */
	public static final String LIQUIDACION_INDICATIVO = "LIQ_";
	public static final String LIQUIDACION_AGRUPADOR = "LIQCO";
	public static final String MAESTRO_ARCHIVO_TRANSPORTE = "LIQTP";
	public static final String MAESTRO_ARCHIVO_PROCESAMIENTO = "LIQPR";
	public static final Integer NUMERO_MAXIMO_ITEMS_S3 = 100;
	public static final Integer TIPO_REGISTROS_ELIMINADOS = 1;
	public static final char DECIMAL_SEPARATOR = '.';
	public static final String USUARIO_PROCESA_ARCHIVO = "ATH";
	public static final String CAMPO_OBSERVACION_ERRORES = "OBSERVACION";
	
	/**
	 * Constantes de Operaciones Liquidacion
	 */
	public static final String OPERACIONES_LIQUIDACION_CONCILIADAS = "CONCILIADAS";
	public static final String OPERACIONES_LIQUIDACION_IDENTIFICADAS_CON_DIFERENCIAS = "IDENTIFICADAS_CON_DIFERENCIAS";
	public static final String OPERACIONES_LIQUIDACION_LIQUIDADAS_NO_COBRADAS = "LIQUIDADAS_NO_COBRADAS";
	public static final String OPERACIONES_LIQUIDACION_REMITIDAS_NO_IDENTIFICADAS = "REMITIDAS_NO_IDENTIFICADAS";
	public static final String OPERACIONES_LIQUIDACION_LIQUIDADAS_NO_COBRADAS_ELIMINADAS = "LIQUIDADAS_NO_COBRADAS_ELIMINADAS";
	public static final String ESTADO_CONCILIACION_MANUAL = "MANUAL";
	public static final String ESTADO_CONCILIACION_RECHAZADA = "RECHAZADA";
	
	public static final String VALOR_DOMINIO_TIPO_SERVICIO_PROGRAMADA = "P";
	
	public static final String ERROR_GENERAL = "ERROR";
	public static final String REGISTRO_NO_ENCONTRADO = "Registro no encontrado";

	public static final String ESTADO_OPERACION_NO_REALIZADA = "NO PUDO REALIZAR LA OPERACION";
	public static final String OPERACION_RECHAZAR = "RECHAZAR";
	public static final String OPERACION_ACEPTAR = "ACEPTAR";
	public static final String ESTADO_CONCILIACION_AUTOMATICO = "AUTOMATICO";
	public static final String ESTADO_CONCILIACION_ACEPTADO = "ACEPTADO";

	/**
	 * Estados Archivos
	 */
	public static final String ESTADO_NO_CONCILIADO = "NO CONCILIADO";
	
	public static final String OPERACION_EN_ESTADO_CONCILIADO = "Operacion en estado CONCILIADA, no se puede actualizar";

	/**
	 * Listas de Conciliacion
	 */
	
	public static final String LISTA_CONCILIACION_IDLIQUIDACIONAPP = "ID_LIQUIDACION_APP";
	public static final String LISTA_CONCILIACION_IDLIQUIDACIONTDV = "ID_LIQUIDACION_TDV";
	public static final String LISTA_CONCILIACION_CONSECUTIVO = "CONSEUTIVO_REGISTRO";

}
