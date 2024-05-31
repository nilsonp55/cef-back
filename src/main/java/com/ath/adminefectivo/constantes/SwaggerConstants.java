package com.ath.adminefectivo.constantes;



import lombok.AccessLevel;
import lombok.NoArgsConstructor;



/**
 * Clase que contiene las constantes de swagger
 *
 * @author CamiloBenavides
 */
@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class SwaggerConstants {


	public static final String DESCRIPTION = "Aplicativo empleado para la administracion del efectivo para los clientes ATH. ";


	public static final String TITLE = "admin-efectivo";


	public static final String CODE_VERSION = "v1.0.1";


	public static final String MAIL_CONTACT = "impresion177@gmail.com";
	
	public static final String NAME_CONTACT = "Camilo Benavides";

	public static final String PACKAGE = "com.ath.adminefectivo.controller";

	public static final String RESPONSE_MESSAGE_200 = "Successfull request";

	public static final String RESPONSE_MESSAGE_FORBIDDEN = "Bad request";

	
	public static final String RESPONSE_MESSAGE_INTERNAL_SERVER_ERROR = "Internal Server Error";

}