package com.ath.adminefectivo.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.service.ITipoReglaInterface;

@Service
public class TipoReglaFactory {

	@Autowired
	ReglaSQL reglaSql;

	@Autowired
	ReglaValorFijo reglaValorFijo;

	@Autowired
	ReglaIncluidos reglaIncluidos;

	@Autowired
	ReglaExcluidos reglaExcluidos;
	
	@Autowired
	ReglaFormatoFecha reglaFormatoFecha;


	public ITipoReglaInterface getInstance(String tipoRegla) {

		switch (tipoRegla) {

		case Dominios.TIPO_REGLA_FIJA:
			return reglaValorFijo;

		case Dominios.TIPO_REGLA_INCLUIDOS:
			return reglaIncluidos;

		case Dominios.TIPO_REGLA_EXCLUIDOS:
			return reglaExcluidos;

		case Dominios.TIPO_REGLA_CONSULTA_SQL:
			return reglaSql;
			
		case Dominios.TIPO_REGLA_FORMATO_FECHA:
			return reglaFormatoFecha;
			
		default:
			throw new AplicationException(ApiResponseCode.ERROR_TIPO_REGLA_NOT_FOUND.getCode(),
					ApiResponseCode.ERROR_TIPO_REGLA_NOT_FOUND.getDescription(),
					ApiResponseCode.ERROR_TIPO_REGLA_NOT_FOUND.getHttpStatus());
		}

	}
}
