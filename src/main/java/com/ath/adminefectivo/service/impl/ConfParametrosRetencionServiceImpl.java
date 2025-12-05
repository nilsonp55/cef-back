package com.ath.adminefectivo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ath.adminefectivo.entities.ConfParametrosRetencion;
import com.ath.adminefectivo.exception.NotFoundException;
import com.ath.adminefectivo.repositories.ConfParametrosRetencionRepository;
import com.ath.adminefectivo.service.ConfParametrosRetencionService;

@Service
public class ConfParametrosRetencionServiceImpl implements ConfParametrosRetencionService {

    private final ConfParametrosRetencionRepository repository;

    public ConfParametrosRetencionServiceImpl(ConfParametrosRetencionRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ConfParametrosRetencion> getAllParametrosRetencion() {
        return repository.findAll();
    }

    @Override
    public ConfParametrosRetencion getParametroRetencionById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("ParametroRetencion no encontrado con id: " + id));
    }

    @Override
    public ConfParametrosRetencion createParametroRetencion(ConfParametrosRetencion parametroRetencion) {
        return repository.save(parametroRetencion);
    }

    @Override
    public ConfParametrosRetencion updateParametroRetencion(Integer id, ConfParametrosRetencion parametroRetencion) {
        repository.findById(id)
                .orElseThrow(() -> new NotFoundException("ParametroRetencion no encontrado con id: " + id));

        parametroRetencion.setIdParametroRetencion(id);
        return repository.save(parametroRetencion);
    }

    @Override
    public void deleteParametroRetencion(Integer id) {
        repository.findById(id)
                .orElseThrow(() -> new NotFoundException("ParametroRetencion no encontrado con id: " + id));
        repository.deleteById(id);
    }
}
