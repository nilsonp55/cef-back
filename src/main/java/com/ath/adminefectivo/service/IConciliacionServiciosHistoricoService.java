package com.ath.adminefectivo.service;

import java.util.Optional;

import com.ath.adminefectivo.entities.ConciliacionServicios;

/**
 * @author cesar.castano
 */
public interface IConciliacionServiciosHistoricoService {

	/**
	 * Servicio encargado de crear registro de conciliacion en la tabla de Conciliacion Servicios Historico
	 * @param regConciliado
	 * @return Boolean
	 * @author cesar.castano
	 */
	Boolean crearRegistroEnConciliacionHistorico(Optional<ConciliacionServicios> regConciliado);
}
