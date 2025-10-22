package com.ath.adminefectivo.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ath.adminefectivo.dto.AuditLogProcessDTO;
import com.ath.adminefectivo.dto.AuditoriaLogDTO;

public interface IAuditoriaLogsService {
	
	Page<AuditoriaLogDTO> consultarLogAdministrativos(LocalDate fechaInicial, LocalDate fechaFinal,
            String usuario, String ipOrigen, String opcionMenu,
            Pageable pageable);
	

	Page<AuditLogProcessDTO> consultarLogProceso(
            LocalDate fechaInicial,
            LocalDate fechaFinal,
            String usuario,
            String ipOrigen,
            String nombreProceso,
            String estadoHttp,
            Pageable pageable
    );

}
