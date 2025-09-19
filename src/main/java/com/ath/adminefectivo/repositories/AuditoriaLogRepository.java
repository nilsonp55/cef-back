package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import com.ath.adminefectivo.entities.audit.AuditoriaLogEntity;

@Repository
public interface AuditoriaLogRepository
		extends JpaRepository<AuditoriaLogEntity, Long>, QuerydslPredicateExecutor<AuditoriaLogEntity> {
}