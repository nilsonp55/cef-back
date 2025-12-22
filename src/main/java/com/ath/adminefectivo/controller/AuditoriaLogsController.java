package com.ath.adminefectivo.controller;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ath.adminefectivo.dto.AuditLogProcessDTO;
import com.ath.adminefectivo.dto.AuditoriaLogDTO;
import com.ath.adminefectivo.service.IAuditoriaLogsService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("${endpoints.AuditoriaLogs}")
public class AuditoriaLogsController {
	
    private final IAuditoriaLogsService auditoriaLogService;

    public AuditoriaLogsController(IAuditoriaLogsService auditoriaLogService) {
        this.auditoriaLogService = auditoriaLogService;
    }

    @GetMapping(value = "${endpoints.AuditoriaLogs.consultar.admin}", produces = "application/json")
    public ResponseEntity<Page<AuditoriaLogDTO>> consultarAuditoria(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicial,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFinal,
            @RequestParam(required = false) String usuario,
            @RequestParam(required = false, name = "ipOrigen") String ipOrigen,
            @RequestParam(required = false, name = "opcionMenu") String opcionMenu,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.debug("Solicitud consultarAuditoria - fechaInicial: {}, fechaFinal: {}, usuario: {}, ipOrigen: {}, opcionMenu: {}",
                fechaInicial, fechaFinal, usuario, ipOrigen, opcionMenu);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "fechaHora"));

        Page<AuditoriaLogDTO> resultados = auditoriaLogService.consultarLogAdministrativos(
                fechaInicial, fechaFinal, usuario, ipOrigen, opcionMenu, pageable
        );

        return ResponseEntity.ok(resultados);
    }
    

    @GetMapping(value = "${endpoints.AuditoriaLogs.consultar.proc}", produces = "application/json")
    public ResponseEntity<Page<AuditLogProcessDTO>> consultarAuditoriaProcess(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicial,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFinal,
            @RequestParam(required = false) String usuario,
            @RequestParam(required = false, name = "ipOrigen") String ipOrigen,
            @RequestParam(required = false) String opcionMenu,
            @RequestParam(required = false) String estadoHttp,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.debug("Solicitud consultarAuditoriaProcess - fechaInicial: {}, fechaFinal: {}, usuario: {}, ipOrigen: {}, opcionMenu: {}, estadoHttp: {}",
                fechaInicial, fechaFinal, usuario, ipOrigen, opcionMenu, estadoHttp);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "fechaHoraProc"));

        Page<AuditLogProcessDTO> resultados = auditoriaLogService.consultarLogProceso(
                fechaInicial, fechaFinal, usuario, ipOrigen, opcionMenu, estadoHttp, pageable
        );

        return ResponseEntity.ok(resultados);
    }
}