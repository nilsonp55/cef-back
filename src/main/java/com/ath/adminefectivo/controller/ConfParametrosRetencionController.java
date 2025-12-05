package com.ath.adminefectivo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ath.adminefectivo.entities.ConfParametrosRetencion;
import com.ath.adminefectivo.service.ConfParametrosRetencionService;

@Controller
@RequestMapping("${endpoint.ParametrosRetencion}")
public class ConfParametrosRetencionController {

    private final ConfParametrosRetencionService service;

    public ConfParametrosRetencionController(ConfParametrosRetencionService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ConfParametrosRetencion>> getAllParametrosRetencion() {
        return new ResponseEntity<>(service.getAllParametrosRetencion(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConfParametrosRetencion> getParametroRetencionById(@PathVariable Integer id) {
        return new ResponseEntity<>(service.getParametroRetencionById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ConfParametrosRetencion> createParametroRetencion(@RequestBody ConfParametrosRetencion parametroRetencion) {
        return new ResponseEntity<>(service.createParametroRetencion(parametroRetencion), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConfParametrosRetencion> updateParametroRetencion(@PathVariable Integer id, @RequestBody ConfParametrosRetencion parametroRetencion) {
        return new ResponseEntity<>(service.updateParametroRetencion(id, parametroRetencion), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteParametroRetencion(@PathVariable Integer id) {
        service.deleteParametroRetencion(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
