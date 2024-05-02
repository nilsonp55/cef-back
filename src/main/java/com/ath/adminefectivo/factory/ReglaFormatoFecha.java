package com.ath.adminefectivo.factory;


import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.ReglasDetalleArchivoDTO;
import com.ath.adminefectivo.service.ITipoReglaInterface;
import com.ath.adminefectivo.utils.UtilsString;


@Service
public class ReglaFormatoFecha implements ITipoReglaInterface {

	@Override
	public boolean ejecutarRegla(ReglasDetalleArchivoDTO regla, String valorCampo) {
		
		   String[] values = regla.getValorIncluidos().split(",");
		   
		   Date fecha = UtilsString.toDate(valorCampo,Arrays.stream(values).toList());
		   
		   return Objects.nonNull(fecha);
		
	}
}
