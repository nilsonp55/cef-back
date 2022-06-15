package com.ath.adminefectivo.service;

import com.ath.adminefectivo.dto.compuestos.DetalleOperacionesDTO;
import com.ath.adminefectivo.entities.DetalleOperacionesProgramadas;

/**
 * Interfaz de los servicios relacionados con la entidad DetalleOperacionesProgramadas
 * @author cesar.castano
 */
public interface IDetalleOperacionesProgramadasService {

	/**
	 * Metodo encargado de crear el registro en la tabla DetalleOperacionesProgramadas
	 * @return DetalleOperacionesProgramadas
	 * @author cesar.castano
	 */
	DetalleOperacionesProgramadas crearRegistroDetalle(DetalleOperacionesDTO detalleOperacionesDTO);
	
	/**
	 * Metodo encargado de obtener el valor sumarizado del detalle por idOperacion
	 * @return Integer
	 * @author cesar.castano
	 */
	Double obtenerValorDetalle(Integer idOpreacion);
}
