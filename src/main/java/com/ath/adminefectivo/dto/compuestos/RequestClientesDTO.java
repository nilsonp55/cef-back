package com.ath.adminefectivo.dto.compuestos;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestClientesDTO {
	
	String nombre;
	String codigoDANE;
	Integer codigoCliente;
	Integer estado;
	boolean fajado;

}
