package com.ath.adminefectivo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.ath.adminefectivo.entities.DetallesDefinicionArchivo;
import com.ath.adminefectivo.entities.id.DetallesDefinicionArchivoPK;

/**
 * Repository encargado de manejar la logica de la entidad MaestrosDefinicionArchivo
 *
 * @author CamiloBenavides
 */
public interface DetallesDefinicionArchivoRepository
		extends JpaRepository<DetallesDefinicionArchivo, DetallesDefinicionArchivoPK>, QuerydslPredicateExecutor<DetallesDefinicionArchivo> {
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = true)
	List<DetallesDefinicionArchivo> findByIdIdArchivo(String idArchivo);
	
	@Transactional(readOnly = true)
	List<DetallesDefinicionArchivo> findByIdIdArchivoOrderByNumeroCampoAsc(String idArchivo);
	
	/**
	 * 
	 * @param idArchivo
	 * @return
	 */
	List<DetallesDefinicionArchivo> findByIdIdArchivoAndIdNumeroCampo(String idArchivo, Integer numeroCampo);
	
	@Query("SELECT COUNT(d) FROM DetallesDefinicionArchivo d WHERE d.id.idArchivo = :idArchivo")
	long contarPorIdArchivo(@Param("idArchivo") String idArchivo);

}
