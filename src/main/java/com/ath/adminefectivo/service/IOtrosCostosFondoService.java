package com.ath.adminefectivo.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import com.ath.adminefectivo.entities.OtrosCostosFondo;

public interface IOtrosCostosFondoService {
	
	// Método para insertar o actualizar un objeto OtrosCostosFondo
    OtrosCostosFondo save(OtrosCostosFondo otrosCostosFondo);

    // Método para consultar por fecha_saldo y codigo_punto_fondo
    List<OtrosCostosFondo> consultarPorFechaSaldoYCodigoPuntoFondo(Integer codigoPuntoFondo, Date fechaSaldo);

}
