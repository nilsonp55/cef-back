package com.ath.adminefectivo.dto.compuestos;

import java.util.Date;

import com.ath.adminefectivo.entities.OperacionesCertificadas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que contiene la informacion deL codigo punto origen y destino
 *
 * @author CamiloBenavides
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CodigoPuntoOrigenDestinoDTO {

	private Integer codigoPuntoOrigen;

	private Integer codigoPuntoDestino;

	private OperacionesCertificadas certificadas;
}