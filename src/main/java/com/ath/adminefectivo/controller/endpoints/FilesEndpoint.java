package com.ath.adminefectivo.controller.endpoints;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Clase empleada para organizar y administrar los endpoints referentes al
 * manejo de archivos
 *
 * @author CamiloBenavides
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilesEndpoint {

	public static final String V1_0_1 = "/v1.0.1/ade/archivos";

	public static final String SAVE_FILES = "/guardar-archivos";
	
	public static final String DOWNLOAD_FILE_BY_ID = "/descargar-id/{id}";
	
	public static final String EXCEPTION_MANAGER = "/exception";
	
	public static final String SAVE_FILE = "/guardar";

}
