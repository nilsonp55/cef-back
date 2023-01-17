package com.ath.adminefectivo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.MaestroDefinicionArchivo;

/**
 * Repository encargado de manejar la logica de la entidad MaestrosDefinicionArchivo
 *
 * @author CamiloBenavides
 */
public interface MaestroDefinicionArchivoRepository
		extends JpaRepository<MaestroDefinicionArchivo, String>, QuerydslPredicateExecutor<MaestroDefinicionArchivo> {
	


	/**
	 * Consulta la lista de maestros deifinicion por agrupador y estado
	 * 
	 * @param agrupador
	 * @param estado
	 * @return List<MaestroDefinicionArchivo>
	 * @author CamiloBenavides
	 */
	public List<MaestroDefinicionArchivo> findByAgrupadorAndEstado(String agrupador, String estado);
	
	@Query("SELECT mda FROM MaestroDefinicionArchivo mda "
		 + "WHERE substring(mda.mascaraArch, 1, 2) = ?1")
	public MaestroDefinicionArchivo findByMascaraArchLike(String inicialMascara);

}
