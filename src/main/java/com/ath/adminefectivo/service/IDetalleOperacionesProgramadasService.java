package com.ath.adminefectivo.service;

/**
 * Interfaz de los servicios relacionados con la entidad DetalleOperacionesProgramadas
 * @author cesar.castano
 */
public interface IDetalleOperacionesProgramadasService {

	/**
	 * Metodo encargado de obtener el valor sumarizado del detalle por idOperacion
	 * @return Integer
	 * @author cesar.castano
	 */
	Double obtenerValorDetalle(Integer idOpreacion);
	
}
