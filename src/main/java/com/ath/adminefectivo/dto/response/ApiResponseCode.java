package com.ath.adminefectivo.dto.response;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;

/**
 * Clase en donde se centralizan los mensajes de error y mensajes de exito
 *
 * @author CamiloBenavides
 */
@AllArgsConstructor
public enum ApiResponseCode {
	/**
	 * Codigo de respuesta exitoso
	 */
	SUCCESS("S000", "Success", HttpStatus.OK),

	/**
	 * Codigo generico de error
	 */
	GENERIC_ERROR("E400", "Ocurrió un error interno, intente de nuevo más tarde", HttpStatus.BAD_REQUEST),

	/**
	 * Error de en la persistencia de documentos
	 */
	ERROR_LIMITE_ARCHIVOS("E001", "La persistencia de archivos aplica únicamente para un archivo cargado. ",
			HttpStatus.PRECONDITION_FAILED),

	/**
	 * Error al encontrar el archivo cargado
	 */
	ERROR_ARCHIVOS_NO_EXISTE_BD("E002",
			"No se encontró el documento solicitado, por favor consulta con el administrador ",
			HttpStatus.PRECONDITION_FAILED),

	/**
	 * Error descarga del archivo desde el repositorio
	 */
	ERROR_ARCHIVOS_NO_EXISTE_REPO("E003",
			"No se encontró en el repositorio el documento solicitado, por favor consulta con el administrador ",
			HttpStatus.PRECONDITION_FAILED),

	/**
	 * Error eliminación de archivos
	 */
	ERROR_ELIMINAR_ARCHIVO_FISICO("E004", "Ocurrión un error al eliminar el documento del repositorio. ",
			HttpStatus.PRECONDITION_FAILED),

	/**
	 * Error al persistir el archivo en el respositorio
	 */
	ERROR_PERSISTIR_ARCHIVO("E005", "No se pudo persistir el documento en el repositorio. ",
			HttpStatus.PRECONDITION_FAILED),

	/**
	 * Error de que el tipo de archivo no existe
	 */
	ERROR_TIPO_CARGUE_ARCHIVO("E006", "El tipo del archivo a cargar no existe. ", HttpStatus.PRECONDITION_FAILED),

	/**
	 * Error al cargar el archivo de la ubicacion especificada
	 */
	ERROR_LECTURA_CARGUE_ARCHIVO("E007", "No se pudo cargar el archivo de la ubicacion especificada. ",
			HttpStatus.PRECONDITION_FAILED),

	/**
	 * Error el delimitador del archivo dentro del maestro archivos no es valido
	 */
	ERROR_DELIMITADOR_NO_VALIDO("E008", "El delimitador no es valido ", HttpStatus.PRECONDITION_FAILED),

	/**
	 * Error si el delimitador es null o si el el delimitador otro no existe
	 */
	ERROR_DELIMITADOR_VACIO("E009",
			"El deliminatador no puede ser nulo, y si su valor es 'Otro', el campo deliminatod otro, no puede se nulo. ",
			HttpStatus.PRECONDITION_FAILED),

	/**
	 * Error con el estado de los archivos
	 */
	ERROR_ESTADO_ARCHIVO("E010", "El estado del archivo no es valido. ", HttpStatus.PRECONDITION_FAILED),

	/**
	 * Error al intentar mover los archivos a carpetas finales
	 */
	ERROR_MOVER_ARCHIVOS("E011", "No se pudo realizar la copia del archivo a la carpeta final ",
			HttpStatus.BAD_REQUEST),

	/**
	 * Error al realizar la lectura del documento
	 */
	ERROR_LECTURA_DOCUMENTO("E012", "Ocurrió un error al realizar la conversión y lectura de los datos del archivo ",
			HttpStatus.PRECONDITION_FAILED),

	/**
	 * Error al consultar un maestro definicion
	 */
	ERROR_MAESTRO_DEFINICION_NO_VALIDO("E020", "No se encontró el maestro definición archivo consultado ",
			HttpStatus.PRECONDITION_FAILED),

	/**
	 * Error al consultar un detalle definicion archivo
	 */
	ERROR_DETALLE_DEFINICION_NO_VALIDO("E021", "No se encontró el detalle definición archivo consultado ",
			HttpStatus.PRECONDITION_FAILED),

	/**
	 * Error con la extención del archivo
	 */
	ERROR_FORMATO_NO_VALIDO("E022", "El archivo no tiene la extención parametrizada en su maestro ",
			HttpStatus.PRECONDITION_FAILED),

	/**
	 * Error mascara invalida
	 */
	ERROR_MASCARA_NO_VALIDA("E023", "El archivo no tiene la mascara parametrizada en su maestro",
			HttpStatus.PRECONDITION_FAILED),

	/**
	 * Error no se puede obtener la fecha del nombre del archivo
	 */
	ERROR_FECHA_NO_VALIDA("E024", "El archivo no cuenta con una fecha valida", HttpStatus.PRECONDITION_FAILED),

	/**
	 * Error la fecha que tiene el archivo no corresponde a la fecha del sistema
	 */
	ERROR_FECHA_ARCHIVO_DIA("E025", "La fecha del archivo no corresponde al la fecha actual",
			HttpStatus.PRECONDITION_FAILED),

	/**
	 * Error no exite la caperta solicitada
	 */
	ERROR_CARPETA_NO_ENCONTRADA("E026", "No se encontró la carpeta solicitada, o no se pudo acceder.",
			HttpStatus.PRECONDITION_FAILED),

	/**
	 * Error al convertir el tipo de registro enviado en el archivo a entero
	 */
	ERROR_TIPO_REGISTRO("E030", "El tipo registro enviado no es válido. ", HttpStatus.PRECONDITION_FAILED),

	/**
	 * El valor del campo multiformato no es valido.
	 */
	ERROR_MULTIFORMATO_INVALIDO("E031", "El valor del campo multiformato no es valido. ",
			HttpStatus.PRECONDITION_FAILED),

	/**
	 * No se encontró la posicion del campo multiformato en el documento solicitado.
	 */
	ERROR_MULTIFORMATO_POSICION("E032", "No se encontró la posicion del campo multiformato en el documento solicitado ",
			HttpStatus.PRECONDITION_FAILED),

	/**
	 * Ocurrió un error al consultar la table de puntos código
	 */
	ERROR_PUNTOS_CODIGO_NO_ENCONTRADO("E903", "Ocurrió un error al consultar la table de puntos código",
			HttpStatus.PRECONDITION_FAILED),

	/**
	 * Ocurrió un error al consultar la table de fondos
	 */
	ERROR_FONDOS_NO_ENCONTRADO("E904", "Ocurrió un error al consultar la tabla de fondos, el fondo no existe. ",
			HttpStatus.PRECONDITION_FAILED),

	/**
	 * Ocurrió un error al consultar la table de puntos
	 */
	ERROR_PUNTOS_NO_ENCONTRADO("E905", "Ocurrió un error al consultar la table de puntos",
			HttpStatus.PRECONDITION_FAILED),

	/**
	 * Ocurrió un error al consultar la table de ciudades
	 */
	ERROR_CIUDADES_NO_ENCONTRADO("E906", "Ocurrió un error al consultar la table de ciudades",
			HttpStatus.PRECONDITION_FAILED),

	/**
	 * Ocurrió un error al consultar la table de clientes corporativos
	 */
	ERROR_CLIENTES_CORPORATIVOS_NO_ENCONTRADO("E915", "Ocurrió un error al consultar la table de clientes corporativos", HttpStatus.PRECONDITION_FAILED),

	/**
	 * Ocurrió un error al consultar la table de ciudades
	 */
	ERROR_BANCOS_NO_ENCONTRADO("E911", "Ocurrió un error al consultar la tabla de bancos",
			HttpStatus.PRECONDITION_FAILED),
	/**
	 * Ocurrió un error al consultar la table de oficinas
	 */
	ERROR_OFICINAS_NO_ENCONTRADO("E914", "Ocurrió un error al consultar la table de oficinas", HttpStatus.PRECONDITION_FAILED),
	
	/**
	 * Ocurrió un error al consultar la table de cajeros
	 */
	ERROR_CAJEROS_NO_ENCONTRADO("E919", "Ocurrió un error al consultar la tabla de cajeros", HttpStatus.PRECONDITION_FAILED),
	
	/**
	 * Ocurrió un error al consultar la table de estados de la operacion
	 */
	ERROR_ESTADOS_OPERACION_NO_VALIDO("E917", "Ocurrió un error, estados de la operacion no validos", HttpStatus.PRECONDITION_FAILED),
	
	/**
	 * Ocurrió un error al consultar los modelos del archivo cargado
	 */
	ERROR_MODELOS_ARCHIVO_NO_VALIDO("E921", "Ocurrió un error, modelo del archivo cargado no valido", HttpStatus.PRECONDITION_FAILED),


	/**
	 * Ocurrió un error al consultar la table de conciliados
	 */
	ERROR_CONCILIADOS_NO_ENCONTRADO("E907", "Ocurrió un error, no hay datos de servicios conciliados",
			HttpStatus.PRECONDITION_FAILED),

	/**
	 * Ocurrió un error al consultar la table de oepraciones programadas
	 */
	ERROR_OPERACIONES_PROGRAMADAS_NO_ENCONTRADO("E908", "No hay datos de operaciones programadas",
			HttpStatus.PRECONDITION_FAILED),


	/**
	 * Ocurrió un error al consultar la table de oepraciones programadas
	 */
	ERROR_DETALLE_OPERACIONES_PROGRAMADAS_NO_ENCONTRADO("E918", "No hay datos de detalle de operaciones programadas", HttpStatus.PRECONDITION_FAILED),
	
	/**
	 * Ocurrió un error al consultar la table de operaciones certificadas
	 */
	ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO("E909", "No hay datos de operaciones certificadas",
			HttpStatus.PRECONDITION_FAILED),

	/**
	 * Ocurrió un error al consultar la table de fondos
	 */
	ERROR_OPERACIONES_A_CONCILIAR_NO_ENCONTRADO("E910", "No hay datos de operaciones a conciliar",
			HttpStatus.PRECONDITION_FAILED),

	/**
	 * Ocurrió un error al consultar la tabla de transportadoras
	 */
	ERROR_TRANSPORTADORAS_NO_ENCONTRADO("E912", "Ocurrió un error al consultar la table de transportadoras",
			HttpStatus.PRECONDITION_FAILED),

	/**
	 * Ocurrió un error al de que no es un fondo
	 */
	ERROR_NO_ES_FONDO("E920", "Ocurrió un error el valor indicado no es un fondo. ", HttpStatus.PRECONDITION_FAILED),

	/**
	 * Ocurrió un error al de que no es un BANREP
	 */
	ERROR_NO_ES_BANREP("E921", "Ocurrió un error el valor indicado no es un punto BAN REP. ",
			HttpStatus.PRECONDITION_FAILED),
	
	/**
	 * Ocurrió un error al de que no son bancos iguales
	 */
	ERROR_NO_ES_IGUAL_BANCO("E922", "Ocurrió un error el valor indicado en el banco origen no es igual al banco destino. ",
			HttpStatus.PRECONDITION_FAILED),

	/**
	 * Ocurrió un error al consultar la tabla de sitios clientes
	 */
	ERROR_SITIOS_CLIENTES_NO_ENCONTRADO("E916", "Ocurrió un error al consultar la table de sitios clientes", HttpStatus.PRECONDITION_FAILED),
	
	/**
	 * Ocurrió un error al consultar la tabla de archivos cargados
	 */
	ERROR_ARCHICOS_CARGADOS_NO_ENCONTRADO("E913", "No hay datos de archivos cargados", HttpStatus.PRECONDITION_FAILED),
	
	/**
	 * Ocurrió un error al consultar el tipo de registro
	 */
	ERROR_TIPO_REGISTRO_NO_VALIDO("E926", "Tipo de registro no valido", HttpStatus.PRECONDITION_FAILED),
	
	/**
	 * Ocurrió un error al consultar la table de conciliados
	 */
	ERROR_VALORES_LIQUIDADOS_NO_ENCONTRADO("E937", "Ocurrió un error, no hay datos de valores liquidados",
			HttpStatus.PRECONDITION_FAILED),
	
	/**
	 * Ocurrió un error al consultar la table de conciliados
	 */
	ERROR_PROCESO_CONSTO_VALORES_LIQUIDADOS("E938", "Ocurrió un error al ejecutar el procedimiento no se encontro para la fecha de liquidación la conciliación en estado cerrado o pendiente el estado de la liquidación",
			HttpStatus.PRECONDITION_FAILED),
	
	/**
	 * Ocurrió un error al consultar la table de conciliados
	 */
	ERROR_PROCESO_CONSTO_VALORES_LIQUIDADOS_SIN_PARAM("E938", "No se puede ejecutar el procedimiento ya que no se encontro para la fecha registros para la liquidación",
			HttpStatus.PRECONDITION_FAILED),
	
	
	/**
	 * Ocurrió un error al consultar la table de conciliados
	 */
	ERROR_PROCESO_VALIDACION_CIERRE_CONSTO_VALORES_LIQUIDADOS("E939", "No se puede ejecutar la liquidación de costos ya que la conciliación no ha sido cerrada",
			HttpStatus.PRECONDITION_FAILED),
	/**
	 * No se puede realizar el cierre del dia por que no han finalizado todos los
	 * proceso
	 */
	ERROR_PROCESOS_NO_COMPLETADOS("E040",
			"No se puede realizar el cierre del dia, existen procesos pendientes para la fecha actual. ",
			HttpStatus.PRECONDITION_FAILED),

	/**
	 * No se puede encontrar un dia habil que cumpla con los criterios
	 * parametrizados.
	 */
	ERROR_CALCULO_DIA_HABIL("E041", "No se puede encontrar un dia habil que cumpla con los criterios parametrizados. ",
			HttpStatus.PRECONDITION_FAILED),

	/**
	 * No la regla de validacion no tiene un formato valido
	 */
	ERROR_FORMATO_REGLA_NO_VALIDA("E042", "La regla de validación no tiene un formato valido. ",
			HttpStatus.BAD_REQUEST),

	/**
	 * Error de en la persistencia de documentos
	 */
	ERROR_COPIAR_PROPIEDADES("E901", "Ocurrió un error en la implementación del metodo copiar propiedades  ",
			HttpStatus.BAD_REQUEST),

	/**
	 * Error cuando no existe el valor de un domino tipo texto
	 */
	ERROR_DOMINIO_NOT_FOUND("E902", "No se encontró el valor del dominio consultado.  ", HttpStatus.BAD_REQUEST),
	
	/**
	 * Error en la libreria openCSV el leer el documento.
	 */
	ERROR_CONVERSION_CSV("E903", "Ocurrió un error al leer el archivo CSV", HttpStatus.PRECONDITION_FAILED),

	/**
	 * Error cuando no existe el valor de un parametro
	 */
	ERROR_PARAMETRO_NOT_FOUND("E904", "No se encontró el valor del parámetro consultado.  ", HttpStatus.BAD_REQUEST),

	/**
	 * El parametro no contiene un entero valido
	 */
	ERROR_PARAMETRO_NO_ENTERO("E905", "El valor del parámetro no corresponde a un número entero.  ",
			HttpStatus.BAD_REQUEST),

	/**
	 * El parametro no contiene un entero valido
	 */
	ERROR_PARAMETRO_NO_FECHA("E906", "El valor del parámetro no corresponde a una fecha válida. ",
			HttpStatus.BAD_REQUEST),
	
	/**
	 * Error cuando no existe el valor de un domino tipo texto
	 */
	ERROR_DOMINIO_EXIST("E907", "Ya existe un dominio con el valor consultado.  ", HttpStatus.BAD_REQUEST),

	/**
	 * El parametro no contiene un entero valido
	 */
	ERROR_ARMAR_PARAMETROS_LIQUIDA("E935", "Existe un error en la ejecucion del SP armar_parametros_liquida. ",
			HttpStatus.BAD_REQUEST),
	
	/**
	 * El parametro no contiene un entero valido
	 */
	ERROR_LIQUIDACION_COSTOS("E936", "Existe un error en la ejecucion del SP liquidar_costos. ",
			HttpStatus.BAD_REQUEST),

	/**
	 * Error cuando no existe el valor de una regla
	 */
	ERROR_REGLA_NOT_FOUND("E910", "No se encontró el valor de la regla consultado.  ", HttpStatus.BAD_REQUEST),
	
	/**
	 * Error tipo de regla no implementada
	 */
	ERROR_TIPO_REGLA_NOT_FOUND("E911", "No se encontró el tipo de regla consultado.  ", HttpStatus.BAD_REQUEST),

	/**
	 * Error punto ya existente
	 */
	ERROR_PUNTO_EXIST("E912", "Punto ya existente.  ", HttpStatus.CONFLICT),
	
	/**
	 * Error banco ya existente
	 */
	ERROR_BANCO_EXIST("E913", "Banco ya existente.  ", HttpStatus.CONFLICT),
	
	/**
	 * Error punto ya existente
	 */
	ERROR_FONDO_EXIST("E914", "Fondo ya existente.  ", HttpStatus.CONFLICT),
	
	/**
	 * Error oficina ya existente
	 */
	ERROR_OFICINA_EXIST("E915", "Oficina ya existente.  ", HttpStatus.CONFLICT),
	
	/**
	 * Error sitio cliente ya existente
	 */
	ERROR_SITIO_CLIENTE_EXIST("E916", "Sitio cliente ya existente.  ", HttpStatus.CONFLICT),
	
	/**
	 * Error cajero ya existente
	 */
	ERROR_CAJERO_EXIST("E917", "Cajero ya existente.  ", HttpStatus.CONFLICT),
	
	/**
	* Ocurrió un error al consultar cuentas puc
	*/
	ERROR_CUENTAS_PUC_NO_ENCONTRADO("E919", "Ocurrió un error al consultar la table de cuentas puc.  ", HttpStatus.CONFLICT),
	
	/**
	 * Error puntos ya existentes
	 */
	ERROR_CUENTAS_PUC_EXIST("E920", "Punto ya existente.  ", HttpStatus.CONFLICT),
	
	/**
	 * Error puntos ya existentes
	 */
	ERROR_CUENTAS_PUC_NO_EXIST("E921", "Punto no existente.  ", HttpStatus.CONFLICT),
	
	/**
	* Ocurrió un error al consultar tipo cuentas
	*/
	ERROR_TIPOS_CUENTAS_NO_ENCONTRADO("E922", "Ocurrió un error al consultar la table de cuentas puc.  ", HttpStatus.CONFLICT),
	
	/**
	 * Error tipos cuentas ya existentes
	 */
	ERROR_TIPOS_CUENTAS_EXIST("E923", "Tipos Cuentas ya existente.  ", HttpStatus.CONFLICT),
	
	/**
	 * Error tipo cuentas ya existentes
	 */
	ERROR_TIPOS_CUENTAS_NO_EXIST("E924", "Tipos cuentas no existente.  ", HttpStatus.CONFLICT),
	
	/**
	* Ocurrió un error al consultar conf contable
	*/
	ERROR_CONF_CONTABLE_ENTIDAD_NO_ENCONTRADO("E925", "Ocurrió un error al consultar la table de Conf Contable Entidad.  ", HttpStatus.CONFLICT),
	
	/**
	 * Error conf contable ya existentes
	 */
	ERROR_CONF_CONTABLE_ENTIDAD_EXIST("E926", "Conf Contable Entidad ya existente.  ", HttpStatus.CONFLICT),
	
	/**
	 * Error con contable
	 */
	ERROR_CONF_CONTABLE_ENTIDAD_NO_EXIST("E927", "Conf Contable Entidad no existente.  ", HttpStatus.CONFLICT),
	/**
	 * Código asociado a la respuesta
	 */
	/**
	 * Error ID existentes
	 */
	ID_NOT_NULL("E924", "El ID no puede ser nulo.  ", HttpStatus.CONFLICT),


	ERROR_GUARDANDO_ARCHIVO("E918", "Error guardando archivo", HttpStatus.CONFLICT),

	ERROR_ACCEDIENDO_S3("E919", "Error accediendo al S3", HttpStatus.CONFLICT), 
	/**
	 * Ocurrió un error al consultar el Log Proceso Diario
	 */
	ERROR_LOGPROCESODIARIO_NO_ENCONTRADO("E927", "Log Proceso Diario no encontrado o el proceso se encuentra cerrado. ", HttpStatus.PRECONDITION_FAILED),

	/**
	 * Ocurrió un error al consultar el Log Proceso Diario
	 */
	ERROR_LOGPROCESODIARIO_YA_EXISTE("E928", "Log Proceso Diario ya existe", HttpStatus.PRECONDITION_FAILED),

	/**
	 * Ocurrió un error al consultar el Log Proceso Diario
	 */
	ERROR_CODIGO_PROCESO_NO_EXISTE("E929", "Codigo Proceso no existe en Log Proceso Diario o se encuentra en estado Cerrado", HttpStatus.PRECONDITION_FAILED),

	/**
	 * Ocurrió un error al consultar el Log Proceso Diario
	 */
	ERROR_PROCESO_YA_CERRADO("E930", "Error, Proceso Diario Definitivo ya esta CERRADO", HttpStatus.PRECONDITION_FAILED),

	/**
	 * Ocurrió un error al consultar el Log Proceso Diario
	 */
	ERROR_PROCESO_SIGUE_ABIERTO("E930", "Error, Proceso Diario Definitivo sigue ABIERTO", HttpStatus.PRECONDITION_FAILED),

	/**
	 * Ocurrió un error al consultar el Log Proceso Diario
	 */
	ERROR_FALTAN_ARCHIVOS_POR_CARGAR("E931", "Error, El numero de archivos a cargar no es valido", HttpStatus.PRECONDITION_FAILED),

	/**
	 * Ocurrió un error al consultar el Log Proceso Diario
	 */
	ERROR_HAY_ARCHIVOS_FALLIDOS_CARGUE_CERTIFICACION("E932", "Error, Hay archivos fallidos de cargue de certificacion", HttpStatus.PRECONDITION_FAILED),

	/**
	 * Ocurrió un error al consultar el Log Proceso Diario
	 */
	ERROR_NO_CUMPLE_MINIMO_ARCHIVOS_CARGADOS_CERTIFICACION("E933", "Error, No cumple con el minimo de archivos cargados certificacion", HttpStatus.PRECONDITION_FAILED),

	/**
	 * Error no formato de fecha o fecha no valida
	 */
	ERROR_FECHA_CONTABILIDAD("E934", "Error, Ocurrio un error en la conversion de la fecha del sistema para el proceso de contabilidad. ", HttpStatus.PRECONDITION_FAILED),
	
	/**
	 * Ocurrió un error al consultar el estado de carga Conciliacion
	 */
	ERROR_TIPO_CONTABLES("E936", "Error, EXISTEN ERRORES CONTABLES", HttpStatus.PRECONDITION_FAILED),
	
	/**
	 * Ocurrió un error al consultar el estado de carga Preliminar
	 */
	ERROR_ESTADO_CARGA_PRELIMINAR("E940", "Error, ESTADO PRELIMINAR DEBE ESTAR CERRADO PARA EL CIERRE CONTABLE", HttpStatus.PRECONDITION_FAILED),

	/**
	 * Ocurrió un error al consultar el estado de carga Conciliacion
	 */
	ERROR_ESTADO_CARGA_CONCILIACION("E942", "Error, ESTADO CONCILIACION DEBE ESTAR CERRADO PARA EL CIERRE CONTABLE", HttpStatus.PRECONDITION_FAILED),
	
	/**
	 * Ocurrió un error al consultar el estado de carga Conciliacion
	 */
	ERROR_TIPO_CONTABILIDAD("E944", "Error, TIPO DE CONTABILIDAD DEBE SER AM O PM", HttpStatus.PRECONDITION_FAILED),
	
	/**
	 * Ocurrió un error al consultar el estado de carga Conciliacion
	 */
	ERROR_CREACION_OPERACION_PROGRAMADA("E945", "Error, Ocurrió un error al crear la operacion programada. ", HttpStatus.PRECONDITION_FAILED),
	
	/**
	 * Ocurrió un error al validar la contabilidad porque ya esta cerrada
	 */
	PROCESO_CONTABILIDAD_CERRADA("E946", "El proceso de contabilidad AM o PM ya se encuentra cerrada para la fecha. ", HttpStatus.PRECONDITION_FAILED),
	
	/**
	 * Ocurrió un error al consultar la table de ciudades
	 */
	ERROR_BANCO_EXTERNO_NO_ENCONTRADO("E947", "Error, El banco externo en el proceso de intercambio no fue encontrado. ",
			HttpStatus.PRECONDITION_FAILED),
	
	/**
	 * Ocurrió un error al consultar la tabla Tarifas Operacion o no fue encontrado
	 */
	ERROR_TARIFAS_OPERACION_NO_ENCONTRADO("E948", "Ocurrió un error al consultar la tabla de Tarifas Operacion, no fue encontrado",
			HttpStatus.PRECONDITION_FAILED),
	/**
	 * Ocurrió un error al consultar la tabla Tarifas Operacion o no fue encontrado
	 */
	ERROR_PUNTOS_CODIGO_TDV_NO_ENCONTRADO("E949", "Ocurrió un error al consultar la tabla de Puntos Codigo TDV, no fue encontrado",
			HttpStatus.PRECONDITION_FAILED);
	
	/**
	 * Código asociado a la respuesta
	 */
	private String code;

	/**
	 * Descripción asociada a la respuesta
	 */
	private String description;

	/**
	 * {@link HttpStatus}.
	 */
	private HttpStatus httpStatus;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	
	
	
}
