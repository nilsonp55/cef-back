package com.ath.adminefectivo.repositories;

import com.ath.adminefectivo.dto.TarifasEspecialesClienteDTO;
import com.ath.adminefectivo.entities.TarifasEspecialesCliente;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository encargado de manejar la logica de la entidad tarifas especiales
 *
 * @author johan.chaparro
 */

@Repository
public interface ITarifasEspecialesRepository extends JpaRepository<TarifasEspecialesCliente, Long> {
	
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM TARIFAS_ESPECIALES_CLIENTE WHERE ID_TARIFA_ESPECIAL = :idArchivo", nativeQuery = true)
	void eliminarPorIdArchivoCargado(@Param("idArchivo") Long idArchivo);
}