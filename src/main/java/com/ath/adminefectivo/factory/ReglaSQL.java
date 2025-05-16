package com.ath.adminefectivo.factory;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Parametros;
import com.ath.adminefectivo.dto.ListaDetalleDTO;
import com.ath.adminefectivo.dto.ReglasDetalleArchivoDTO;
import com.ath.adminefectivo.dto.compuestos.ResultadoReglaDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.repositories.IGeneralRepository;
import com.ath.adminefectivo.service.IParametroService;
import com.ath.adminefectivo.service.ITipoReglaInterface;
import com.ath.adminefectivo.service.ITipoReglaInterfaceCostos;

@Service
public class ReglaSQL implements ITipoReglaInterface, ITipoReglaInterfaceCostos {

	@Autowired
	IGeneralRepository generalRepository;

	@Autowired
	IParametroService parametroService;

	private String sqlBaseMotor;
	
	@PostConstruct
	private void cargueInicial() {
		this.sqlBaseMotor = parametroService.valorParametro(Parametros.CONSULTA_BASE_MOTOR_REGLAS);
	}

	@Override
	public boolean ejecutarRegla(ReglasDetalleArchivoDTO reglaVO, String valorCampo) {
		List<ResultadoReglaDTO> resultadosReglas = new ArrayList<>();
		String consulta = MessageFormat.format(this.sqlBaseMotor, reglaVO.getTablasAUsar(),
				reglaVO.getExpresionSQL());

		var resultadoConsulta = generalRepository.ejecutarQueryNativa(consulta, valorCampo);
		resultadosReglas.add(new ResultadoReglaDTO(reglaVO.getIdRegla(), valorCampo, resultadoConsulta));
		return resultadoConsulta;

	}
	
	@Override
	public boolean ejecutarRegla(ReglasDetalleArchivoDTO reglaVO, String valorCampo,
			ValidacionArchivoDTO validacionArchivo, int index, Map<String, ListaDetalleDTO> detalleDefinicionMap) {
		List<ResultadoReglaDTO> resultadosReglas = new ArrayList<>();
		String consulta = MessageFormat.format(this.sqlBaseMotor, reglaVO.getTablasAUsar(), reglaVO.getExpresionSQL());
				
		//Si se ha configurado un valor fijo en la regla, este se utilizará como valor por defecto para valorCampo.
		if (reglaVO.getValorFijo() != null) {
			String valorFijoRaw = reglaVO.getValorFijo();
			if (valorFijoRaw.startsWith("{{") && valorFijoRaw.endsWith("}}")) {
				String valorFijo = valorFijoRaw.substring(2, valorFijoRaw.length() - 2);
				
				// Si getvalorCampoRegla retorna null, se asume que ya se inyectó al detalleDefinicionMap
				String valorFijoMap = generalRepository.getvalorCampoRegla(valorFijo, validacionArchivo, detalleDefinicionMap);
				if (valorFijoMap != null) {
					valorCampo = valorFijoMap;
				}
			} else {
				valorCampo = valorFijoRaw;
			}
		}
		
		consulta = generalRepository.queryBuilder(consulta, detalleDefinicionMap);
		var resultadoConsulta = generalRepository.ejecutarQueryNativa(consulta, valorCampo);
		resultadosReglas.add(new ResultadoReglaDTO(reglaVO.getIdRegla(), valorCampo, resultadoConsulta));
		return resultadoConsulta;
	}
}
