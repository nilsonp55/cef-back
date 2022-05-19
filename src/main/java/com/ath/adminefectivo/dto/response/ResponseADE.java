package com.ath.adminefectivo.dto.response;


import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


/**
 * Clase con la informacion de la respuesta http del servicio 
 *
 * @author CamiloBenavides
 */
@Setter
@Getter
@Builder
public class ResponseADE implements Serializable{

	private static final long serialVersionUID = 1L;

	private String code;
	
	private String description;

	private String source;

}
