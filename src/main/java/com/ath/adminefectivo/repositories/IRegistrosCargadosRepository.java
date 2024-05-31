package com.ath.adminefectivo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.RegistrosCargados;
import com.ath.adminefectivo.entities.id.RegistrosCargadosPK;

/**
 * Repository encargado de manejar la logica de la entidad RegistrosCargados
 *
 * @author duvan.naranjo
 */
public interface IRegistrosCargadosRepository extends JpaRepository<RegistrosCargados, RegistrosCargadosPK>, QuerydslPredicateExecutor<RegistrosCargados> {

	/**
	 * Consulta la lista de registros Cargados por su idArchivo
	 * 
	 * @param idArchivo
	 * @return List<RegistrosCargados>
	 * @author duvan.naranjo
	 */
	public List<RegistrosCargados> findByIdIdArchivo(Long idArchivo);

}
