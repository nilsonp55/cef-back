package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ath.adminefectivo.entities.OtrosCostosFondo;
import com.ath.adminefectivo.entities.id.OtrosCostosFondoPK;

import java.util.Date;
import java.util.List;

@Repository
public interface OtrosCostosFondoRepository extends JpaRepository<OtrosCostosFondo, OtrosCostosFondoPK> {

    // Método personalizado para consultar por fecha_saldo y codigo_punto_fondo
    List<OtrosCostosFondo> findByIdCodigoPuntoFondoAndIdFechaSaldo(Integer codigoPuntoFondo, Date fechaSaldo);
}