package com.ath.adminefectivo.factory;

import java.util.Arrays;

import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.ReglasDetalleArchivoDTO;
import com.ath.adminefectivo.service.ITipoReglaInterface;

@Service
public class ReglaExcluidos implements ITipoReglaInterface {

	@Override
	public boolean ejecutarRegla(ReglasDetalleArchivoDTO reglaVO, String valorCampo) {
		String[] values = reglaVO.getValorExcluidos().split(",");
		return Arrays.stream(values).noneMatch(valorCampo::equals);
	}

}
