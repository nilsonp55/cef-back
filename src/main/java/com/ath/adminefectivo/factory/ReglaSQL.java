package com.ath.adminefectivo.factory;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import com.ath.adminefectivo.constantes.Parametros;
import com.ath.adminefectivo.dto.ReglasDetalleArchivoDTO;
import com.ath.adminefectivo.dto.compuestos.ResultadoReglaDTO;
import com.ath.adminefectivo.repositories.IGeneralRepository;
import com.ath.adminefectivo.service.IParametroService;
import com.ath.adminefectivo.service.ITipoReglaInterface;

@Service
public class ReglaSQL implements ITipoReglaInterface {

	@Autowired
	IGeneralRepository generalRepository;

	@Autowired
	IParametroService parametroService;

	private String sqlBaseMotor;

	private List<ResultadoReglaDTO> resultadosReglas = new ArrayList<>();
	
	@PostConstruct
	private void cargueInicial() {
		this.sqlBaseMotor = parametroService.valorParametro(Parametros.CONSULTA_BASE_MOTOR_REGLAS);
	}

	@Override
	public boolean ejecutarRegla(ReglasDetalleArchivoDTO reglaVO, String valorCampo) {

		ResultadoReglaDTO resultadoPrevio = resultadosReglas.stream()
				.filter(x -> Objects.equals(x.getIdRegla(), reglaVO.getIdRegla())
						&& Objects.equals(x.getParametro(), valorCampo))
				.findAny().orElse(null);

		if (Objects.nonNull(resultadoPrevio)) {
			return resultadoPrevio.isResultado();
		} else {
			String consulta = MessageFormat.format(this.sqlBaseMotor, reglaVO.getTablasAUsar(),
					reglaVO.getExpresionSQL());

			var resultadoConsulta = generalRepository.ejecutarQueryNativa(consulta, valorCampo);
			resultadosReglas.add(new ResultadoReglaDTO(reglaVO.getIdRegla(), valorCampo, resultadoConsulta));
			return resultadoConsulta;

		}
	}
}
