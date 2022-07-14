package com.ath.adminefectivo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
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
	
	/**
	 * 
	 * @param idArchivo
	 * @return
	 */
	List<DetallesDefinicionArchivo> findByIdIdArchivoAndIdNumeroCampo(String idArchivo, Integer numeroCampo);

}
