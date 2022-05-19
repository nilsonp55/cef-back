package com.ath.adminefectivo.dto.compuestos;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que contiene la información de un token del motor de reglas
 *
 * @author rparra
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenMotorDTO {


	public static final int PARENTESIS = 1;
	public static final int NUMERO = 2;
	public static final int AND = 3;
	public static final int OR = 4;
	public static final int OTRO = 5;
	public static final int FIN = 6;
	
	private int tipo;
	private String regla;
	private int conector;
	private boolean resultado;
	private String restoRegla;
	private Collection<TokenMotorDTO> subTokens;
	
	

	
}
