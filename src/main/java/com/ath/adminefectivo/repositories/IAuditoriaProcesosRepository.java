package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.AuditoriaProcesos;
import com.ath.adminefectivo.entities.id.AuditoriaProcesosPK;

/**
 * Repository encargado de manejar la logica de la entidad AuditoriaProcesos
 *
 * @author duvan.naranjo
 */
public interface IAuditoriaProcesosRepository
		extends JpaRepository<AuditoriaProcesos, AuditoriaProcesosPK>, QuerydslPredicateExecutor<AuditoriaProcesos> {

}
