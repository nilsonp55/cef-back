package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.dto.CiudadesDTO;
import com.ath.adminefectivo.entities.Ciudades;
import com.querydsl.core.types.Predicate;

public interface ICiudadesService {

	/**
	 * Servicio encargado de consultar la lista de todos las Ciudades filtrados
	 * con el predicado
	 * 
	 * @param predicate
	 * @return List<CiudadesDTO>
	 * @author cesar.castano
	 */
	List<CiudadesDTO> getCiudades(Predicate predicate);

	/**
	 * Servicio encargado de consultar el nombre de una Ciudad por codigo Este
	 * servicio lanza un error en caso de que la ciudad no exista
	 * 
	 * @param codigo
	 * @return String
	 * @author cesar.castano
	 */
	String getNombreCiudad(String codigo);
	
	/**
	 * Servicio encargado de consultar el codigo de una Ciudad por nombre Este
	 * servicio lanza un error en caso de que la ciudad no exista
	 * 
	 * @param nombre
	 * @return Integer
	 * @author cesar.castano
	 */
	String getCodigoCiudad(String nombre);
	
	/**
	 * Servicio encargado de consultar la ciudad por un codigo dane de la ciudad
	 * 
	 * @param codigo
	 * @return CiudadesDTO
	 * @author duvan.naranjo
	 */
	CiudadesDTO getCiudadPorCodigoDane(String codigo);

	
	/**
	 * Servicio encargado de consultar la ciudad por un codigo brinks 
	 * 
	 * @param codigoBrinks
	 * @return CiudadesDTO
	 * @author duvan.naranjo
	 */
	Ciudades getCiudadPorCodigoDaneOrCodigoBrinks(String ciudad1);

	/**
	 * Servicio encargado de consultar una ciudad por nombre ciudad Fiserv
	 * 
	 * @param nombreCiudadFiserv
	 * @return CiudadesDTO
	 * @author duvan.naranjo
	 */
	CiudadesDTO getCiudadPorNombreCiudadFiserv(String nombreCiudad);

	/**
	 * Servicio encargado de crear una nueva ciudad.
	 *
	 * @param ciudadDTO El DTO de la ciudad a crear.
	 * @return El DTO de la ciudad creada.
	 * @author Jules (AI Assistant)
	 */
	CiudadesDTO createCiudad(CiudadesDTO ciudadDTO);

	/**
	 * Servicio encargado de consultar una ciudad por su ID.
	 *
	 * @param id El ID de la ciudad a consultar.
	 * @return El DTO de la ciudad encontrada, o null si no existe.
	 * @author Jules (AI Assistant)
	 */
	CiudadesDTO getCiudadById(String id);

	/**
	 * Servicio encargado de actualizar una ciudad existente.
	 *
	 * @param id El ID de la ciudad a actualizar.
	 * @param ciudadDTO El DTO con los datos actualizados de la ciudad.
	 * @return El DTO de la ciudad actualizada.
	 * @author Jules (AI Assistant)
	 */
	CiudadesDTO updateCiudad(String id, CiudadesDTO ciudadDTO);

	/**
	 * Servicio encargado de eliminar una ciudad por su ID.
	 *
	 * @param id El ID de la ciudad a eliminar.
	 * @author Jules (AI Assistant)
	 */
	void deleteCiudad(String id);
}
