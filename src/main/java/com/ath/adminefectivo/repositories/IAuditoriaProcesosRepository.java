package com.ath.adminefectivo.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.AuditoriaProcesos;
import com.ath.adminefectivo.entities.id.AuditoriaProcesosPK;

/**
 * Repository encargado de manejar la logica de la entidad AuditoriaProcesos
 *
 * @author duvan.naranjo
 * @author prv_nparra
 */
public interface IAuditoriaProcesosRepository
		extends JpaRepository<AuditoriaProcesos, AuditoriaProcesosPK>, QuerydslPredicateExecutor<AuditoriaProcesos> {

	@Query("SELECT ap.fechaCreacion FROM AuditoriaProcesos ap GROUP BY ap.fechaCreacion ORDER BY ap.fechaCreacion")
	List<Date> AuditoriaProcesosFechasProcesadas();
}
