package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.entities.ConfParametrosRetencion;

public interface ConfParametrosRetencionService {

    List<ConfParametrosRetencion> getAllParametrosRetencion();

    ConfParametrosRetencion getParametroRetencionById(Integer id);

    ConfParametrosRetencion createParametroRetencion(ConfParametrosRetencion parametroRetencion);

    ConfParametrosRetencion updateParametroRetencion(Integer id, ConfParametrosRetencion parametroRetencion);

    void deleteParametroRetencion(Integer id);
}
