package com.ath.adminefectivo.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import com.ath.adminefectivo.entities.Menu;

public interface MenuRepository extends JpaRepository<Menu, String>, QuerydslPredicateExecutor<Menu> {
	
	@Query("SELECT idMenu FROM Menu")
		public List<String> getAllIdMenu();
	
	@Query(value = "select coalesce(max(cast(id_menu as integer)), 0)  from menu", nativeQuery = true)
	Integer findMaxIdMenuQuery();

	Optional<Menu> findByNombre(String nombre);
}
