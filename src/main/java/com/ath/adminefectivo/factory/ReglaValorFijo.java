package com.ath.adminefectivo.factory;


import java.util.Objects;

import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.ReglasDetalleArchivoDTO;
import com.ath.adminefectivo.service.ITipoReglaInterface;


@Service
public class ReglaValorFijo implements ITipoReglaInterface {

	@Override
	public boolean ejecutarRegla(ReglasDetalleArchivoDTO regla, String valorCampo) {
		return Objects.equals(regla.getValorFijo(), valorCampo);
	}
}
