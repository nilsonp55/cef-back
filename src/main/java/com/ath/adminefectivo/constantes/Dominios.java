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
public final class Dominios {

	public static final String DELIMITADOR_TABULADOR = "T";
	public static final String DELIMITADOR_BLANK = "B";
	public static final String DELIMITADOR_CSV = "C";
	public static final String DELIMITADOR_PIPE = "P";
	public static final String DELIMITADOR_OTRO = "O";
	
	public static final String TIPO_DATO_ENTERO = "N";
	public static final String TIPO_DATO_DECIMAL = "D";
	public static final String TIPO_DATO_TEXTO = "T";
	public static final String TIPO_DATO_CARACTER = "C";
	public static final String TIPO_DATO_FECHA = "F";
	public static final String TIPO_DATO_HORA = "H";
	public static final String TIPO_FECHA_HORA = "Y";
	
	public static final String PERIODICIDAD_DIARIA = "D";
	public static final String PERIODICIDAD_SEMANAL = "S";
	public static final String PERIODICIDAD_QUINCENAL = "Q";
	public static final String PERIODICIDAD_MENSUAL = "M";
	public static final String PERIODICIDAD_BIMENSUAL = "B";
	public static final String PERIODICIDAD_TRIMESTRAL = "T";
	public static final String PERIODICIDAD_SEMESTRAL = "X";
	public static final String PERIODICIDAD_ANUAL = "A";
	
	public static final String MULTIFORMATO_LINEA_INICIAL = "I";
	public static final String MULTIFORMATO_CODIGO_STD = "C";
	
	public static final String VIGENCIA_ACTIVO = "A";
	public static final String VIGENCIA_REEMPLAZO = "R";
	public static final String VIGENCIA_PROCESADO = "P";
	public static final String VIGENCIA_LEIDO = "L";
	
	public static final String ESTADO_CONCILIACION_CONCILIADO = "CONCILIADA";
	public static final String ESTADO_CONCILIACION_NO_CONCILIADO = "NO_CONCILIADA";
	public static final String ESTADO_CONCILIACION_FALLIDA = "FALLIDA";
	public static final String ESTADO_CONCILIACION_FUERA_DE_CONCILIACION = "FUERA_DE_CONCILIACION";
	public static final String ESTADO_CONCILIACION_POSPUESTA = "POSPUESTA";
	public static final String ESTADO_CONCILIACION_CANCELADA = "CANCELADA";


	
	public static final String TIPO_CONCILIACION_MANUAL = "M";
	public static final String TIPO_CONCILIACION_AUTOMATICA = "A";
	
	public static final String TIPO_OPERA_RETIRO= "RETIRO";
	public static final String TIPO_OPERA_CONSIGNACION= "CONSIGNACION";
	public static final String TIPO_OPERA_CAMBIO= "CAMBIO";
	public static final String TIPO_OPERA_VENTA= "VENTA";
	public static final String TIPO_OPERA_TRASLADO= "TRASLADO";
	public static final String TIPO_OPERA_INTERCAMBIO= "INTERCAMBIO";
	public static final String TIPO_OPERA_RECOLECCION= "RECOLECCION";
	public static final String TIPO_OPERA_PROVISION= "PROVISION";
	
	public static final String ESTADOS_OPERA_PROGRAMADO = "P";
	public static final String ESTADOS_OPERA_EJECUTADO = "E";
	public static final String ESTADOS_OPERA_CANCELADO = "C";
	public static final String ESTADOS_OPERA_FALLIDO = "F";
	public static final String ESTADOS_OPERA_FALLIDO_NO = "FNF";

	
	public static final String TIPOS_TRANSPORTE_URBANO = "U";
	public static final String TIPOS_TRANSPORTE_INTERURBANO1 = "I2";
	public static final String TIPOS_TRANSPORTE_INTERURBANO2 = "I2";
	public static final String TIPOS_TRANSPORTE_AEREO_COMERCIAL = "A";
	public static final String TIPOS_TRANSPORTE_AEREO_CHARTER = "AC";
	
	public static final String TIPO_SERVICIO_PROGRAMADA = "P";
	public static final String TIPO_SERVICIO_ESPECIAL = "E";
	
	public static final String TIPOS_PUNTO_FONDO = "F";
	public static final String TIPOS_PUNTO_BAN_REP = "BR";
	public static final String TIPOS_PUNTO_OFICINA = "O";
	public static final String TIPOS_PUNTO_CAJERO = "C";
	public static final String TIPOS_PUNTO_CLIENTE = "CL";
	public static final String TIPOS_PUNTO_BANCO = "B";
	
	public static final String FORMATO_FECHA_HORA_F1 = "F1";
	public static final String FORMATO_FECHA_HORA_F2 = "F2";
	public static final String FORMATO_FECHA_HORA_F3 = "F3";
	
	public static final String FORMATO_FECHA_F1 = "F1";
	public static final String FORMATO_FECHA_F2 = "F2";
	public static final String FORMATO_FECHA_F3 = "F3";
	
	public static final String CARACTER_INVALIDO_TX_C1 = "C1";
	public static final String CARACTER_INVALIDO_TX_C2 = "C2";
	public static final String CARACTER_INVALIDO_TX_C3 = "C3";
	
	public static final String COSTO_FLETE_CHARTER = "CHARTER";
	
	/**
	 * Estados de los registros cargados
	 */	
	public static final String ESTADO_VALIDACION_ACEPTADO = "ACEPTADO";
	public static final String ESTADO_VALIDACION_REGISTRO_ERRADO = "ERRADO";
	public static final String ESTADO_VALIDACION_CORRECTO = "OK";
	public static final String ESTADO_VALIDACION_REEMPLAZADO = "REEMPLAZADO";
	
	/**
	 * Dominio de agrupamiento de los detalles de archivo
	 */	
	public static final String AGRUPADOR_DEFINICION_ARCHIVOS_CERTIFICACION = "CERTI";
	public static final String AGRUPADOR_DEFINICION_ARCHIVOS_PRELIMINARES = "IPP";
	public static final String AGRUPADOR_DEFINICION_ARCHIVOS_DEFINITIVO = "DEFIN";
	public static final String AGRUPADOR_DEFINICION_ARCHIVOS_CONCILIACION = "CONCI";
	
	/**
	 * Dominios estados de los procesos del dia
	 */	
	public static final String ESTADO_PROCESO_DIA_COMPLETO = "CERRADO"; //Deve ser COMPLETADO
	public static final String ESTADO_PROCESO_DIA_PROCESO = "PROCESO";
	public static final String ESTADO_PROCESO_DIA_PENDIENTE = "PENDIENTE";
	
	/**
	 * Dominios dias no habiles
	 */
	public static final String DOMINIO_DIAS_NO_HABILES = "DIASNOHABILES";
	
	/**
	 * Tipos de reglas validos por el motor de reglas
	 */
	public static final String TIPO_REGLA_FIJA = "FIJ";
	public static final String TIPO_REGLA_INCLUIDOS = "INC";
	public static final String TIPO_REGLA_EXCLUIDOS = "EXC";
	public static final String TIPO_REGLA_CONSULTA_SQL = "SQL";
	
	/**
	 * Tipos de archivo 
	 */
	public static final String TIPO_ARCHIVO_IPPSV = "IPPSV";
	public static final String TIPO_ARCHIVO_ISTRC = "ISTRC";
	public static final String TIPO_ARCHIVO_ITVCS = "ITVCS";
	public static final String TIPO_ARCHIVO_ISRPO = "ISRPO";
	public static final String TIPO_ARCHIVO_ISRPC = "ISRPC";
	public static final String TIPO_ARCHIVO_IBBCS = "IBBCS";
	public static final String TIPO_ARCHIVO_IBMCS = "IBMCS";
	public static final String TIPO_ARCHIVO_IATCS = "IATCS";
	public static final String TIPO_ARCHIVO_IPRCS = "IPRCS";
	
	/**
	 * Codigo Proceso 
	 */
	public static final String CODIGO_PROCESO_LOG_PRELIMINAR = "CARG_PRELIMINAR";
	public static final String CODIGO_PROCESO_LOG_DEFINITIVO = "CARG_DEFINITIVO";
	public static final String CODIGO_PROCESO_LOG_CERTIFICACION = "CARG_CERTIFICACION";
	public static final String CODIGO_PROCESO_LOG_CONCILIACION = "CONCILIACION";
	public static final String CODIGO_PROCESO_LOG_LIQUIDACION = "LIQUIDACION";
	
	
	/**
	 * COMISIONES
	 */
	public static final String COMISION_1 = "1";
	public static final String COMISION_2 = "2";
	public static final String COMISION_3 = "3";
	
	/*
	 * IMPUESTOS
	 */
	public static final String IMPUESTO_IVA = "1";
	
	/**
	 * MEDIOS_PAGO
	 */
	public static final String MEDIOS_PAGO_ABONO = "ABONO";
	public static final String MEDIOS_PAGO_DESCUENTO = "DESCUENTO";
	public static final String  MEDIOS_PAGO_CARGO_A_CUENTA = "CARGO_A_CUENTA";

	
	/**
	 * Dominios estados de Contabilidad
	 */	
	public static final int ESTADO_CONTABILIDAD_GENERADO = 1;
	public static final int ESTADO_CONTABILIDAD_ERROR_CONTABLE = 2;
	
	/**
	 * Dominios estados generales del sistema
	 */
	public static final int ESTADO_GENERAL_OK = 1;
	public static final int ESTADO_GENERAL_PENDIENTE = 2;
	public static final int ESTADO_GENERAL_ERROR = 3;
	public static final int ESTADO_GENERAL_CORREGIDO = 4;
	public static final int ESTADO_GENERAL_ELIMINADO = 5;
	
}
