package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.entities.ErroresCostos;

/**
 * Interfaz de los servicios a la tabla de ErroresCostos
 * @author bayron.perez
 */
public interface ErroresCostosService {

	List<ErroresCostos> obtenerErroresCostosByIdSeqGrupo(Integer idSeqGrupo);
}
