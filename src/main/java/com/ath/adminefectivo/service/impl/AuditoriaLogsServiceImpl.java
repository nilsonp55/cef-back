package com.ath.adminefectivo.service.impl;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.ath.adminefectivo.dto.AuditLogProcessDTO;
import com.ath.adminefectivo.dto.AuditoriaLogDTO;
import com.ath.adminefectivo.entities.audit.AuditLogProcessEntity;
import com.ath.adminefectivo.entities.audit.AuditoriaLogEntity;
import com.ath.adminefectivo.entities.audit.QAuditLogProcessEntity;
import com.ath.adminefectivo.entities.audit.QAuditoriaLogEntity;
import com.ath.adminefectivo.repositories.AuditLogProcessRepository;
import com.ath.adminefectivo.repositories.AuditoriaLogRepository;
import com.ath.adminefectivo.service.IAuditoriaLogsService;
import com.querydsl.core.BooleanBuilder;
import org.springframework.util.StringUtils;
import com.querydsl.core.types.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuditoriaLogsServiceImpl implements IAuditoriaLogsService{
	
	@Autowired
	AuditoriaLogRepository auditoriaLogRepository;
	
	@Autowired
	AuditLogProcessRepository auditLogProcessRepository;

	@Override
	public Page<AuditoriaLogDTO> consultarLogAdministrativos(LocalDate fechaInicial, LocalDate fechaFinal,
	                                                         String usuario, String ipOrigen, String opcionMenu,
	                                                         Pageable pageable) {

	    QAuditoriaLogEntity q = QAuditoriaLogEntity.auditoriaLogEntity;
	    BooleanBuilder builder = new BooleanBuilder();

	    // rango opcional de fechas
	    if (fechaInicial != null && fechaFinal != null) {
	        LocalDateTime inicio = fechaInicial.atStartOfDay();
	        LocalDateTime fin = fechaFinal.atTime(LocalTime.MAX);
	        builder.and(q.fechaHora.between(inicio, fin));
	    } else if (fechaInicial != null) {
	        LocalDateTime inicio = fechaInicial.atStartOfDay();
	        builder.and(q.fechaHora.goe(inicio));
	    } else if (fechaFinal != null) {
	        LocalDateTime fin = fechaFinal.atTime(LocalTime.MAX);
	        builder.and(q.fechaHora.loe(fin));
	    }

	    // opcionales
	    if (StringUtils.hasText(usuario)) {
	        builder.and(q.usuario.equalsIgnoreCase(usuario));
	    }
	    if (StringUtils.hasText(ipOrigen)) {
	        builder.and(q.ipOrigen.equalsIgnoreCase(ipOrigen));
	    }
	    if (StringUtils.hasText(opcionMenu)) {
	        builder.and(q.opcionMenu.containsIgnoreCase(opcionMenu));
	    }

	    Page<AuditoriaLogEntity> pageEntities;
	    if (builder.hasValue()) {
	        pageEntities = auditoriaLogRepository.findAll(builder.getValue(), pageable);
	    } else {
	        pageEntities = auditoriaLogRepository.findAll(pageable);
	    }

	    return pageEntities.map(AuditoriaLogDTO.CONVERTER_DTO::apply);
	}
	
	@Override
    public Page<AuditLogProcessDTO> consultarLogProceso(LocalDate fechaInicial,
                                                        LocalDate fechaFinal,
                                                        String usuario,
                                                        String ipOrigen,
                                                        String nombreProceso,
                                                        String estadoHttp,
                                                        Pageable pageable) {

        QAuditLogProcessEntity q = QAuditLogProcessEntity.auditLogProcessEntity;
        BooleanBuilder builder = new BooleanBuilder();

        // Fechas
        if (fechaInicial != null && fechaFinal != null) {
            LocalDateTime inicio = fechaInicial.atStartOfDay();
            LocalDateTime fin = fechaFinal.atTime(LocalTime.MAX);
            builder.and(q.fechaHoraProc.between(inicio, fin));
        } else if (fechaInicial != null) {
            builder.and(q.fechaHoraProc.goe(fechaInicial.atStartOfDay()));
        } else if (fechaFinal != null) {
            builder.and(q.fechaHoraProc.loe(fechaFinal.atTime(LocalTime.MAX)));
        }

        // Filtros opcionales
        if (StringUtils.hasText(usuario)) {
            builder.and(q.usuarioProc.equalsIgnoreCase(usuario));
        }
        if (StringUtils.hasText(ipOrigen)) {
            builder.and(q.ipOrigenProc.equalsIgnoreCase(ipOrigen));
        }
        if (StringUtils.hasText(nombreProceso)) {
            builder.and(q.nombreProcesoProc.equalsIgnoreCase(nombreProceso));
        }
        if (StringUtils.hasText(estadoHttp)) {
            builder.and(q.estadoHttpProc.equalsIgnoreCase(estadoHttp));
        }

        Page<AuditLogProcessEntity> pageEntities = builder.hasValue()
                ? auditLogProcessRepository.findAll(builder.getValue(), pageable)
                : auditLogProcessRepository.findAll(pageable);

        return pageEntities.map(AuditLogProcessDTO.CONVERTER_DTO::apply);
    }
}