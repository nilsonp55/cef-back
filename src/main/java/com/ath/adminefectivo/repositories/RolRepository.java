package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.ath.adminefectivo.entities.Rol;

/**
 * Repository encargado de manejar la logica de la entidad Rol
 * @author bayron.perez
 */

public interface RolRepository extends JpaRepository<Rol, String > {
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE controlefect.ROL SET id_rol = :idRol, nombre = :nombre, descripcion = :descripcion, estado = :estado WHERE id_rol = :previousId", nativeQuery= true)
	int updateRolAndIdRol(
			@Param("idRol") String idRol,
			@Param("nombre") String nombre,
			@Param("descripcion") String descripcion,
			@Param("estado") String estado,
			@Param("previousId") String previousId);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE controlefect.ROL SET nombre = :nombre, descripcion = :descripcion, estado = :estado WHERE id_rol = :previousId", nativeQuery= true)
	int updateRol(
			@Param("nombre") String nombre,
			@Param("descripcion") String descripcion,
			@Param("estado") String estado,
			@Param("previousId") String previousId);
}
