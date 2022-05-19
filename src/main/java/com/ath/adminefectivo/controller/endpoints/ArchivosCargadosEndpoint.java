package com.ath.adminefectivo.controller.endpoints;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Clase empleada para organizar y administrar los endpoints de ArchivosCargados
 *
 * @author CamiloBenavides
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArchivosCargadosEndpoint {
	
	public static final String V1_0_1 = "/v1.0.1/ade/archivos-cargados";	
	
	public static final String FINDALL = "/consultar";
	
	public static final String FINDALL_PAGE = "/consultar-page";
	
	public static final String SAVE = "/guardar";
	
	public static final String DELETE = "/eliminar";
	

}
