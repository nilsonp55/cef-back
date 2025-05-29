package com.ath.adminefectivo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.entities.OtrosCostosFondo;
import com.ath.adminefectivo.repositories.OtrosCostosFondoRepository;
import com.ath.adminefectivo.service.IOtrosCostosFondoService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OtrosCostosFondoServiceImpl implements IOtrosCostosFondoService {

    @Autowired
    private OtrosCostosFondoRepository otrosCostosFondoRepository;

    @Override
    public OtrosCostosFondo save(OtrosCostosFondo otrosCostosFondo) {
        return otrosCostosFondoRepository.save(otrosCostosFondo);
    }

    @Override
    public List<OtrosCostosFondo> consultarPorFechaSaldoYCodigoPuntoFondo(Integer codigoPuntoFondo, Date fechaSaldo) {
        return otrosCostosFondoRepository.findByIdCodigoPuntoFondoAndIdFechaSaldo(codigoPuntoFondo, fechaSaldo);
    }

}