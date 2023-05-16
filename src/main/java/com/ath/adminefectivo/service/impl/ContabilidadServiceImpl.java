package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.BancosDTO;
import com.ath.adminefectivo.dto.CiudadesDTO;
import com.ath.adminefectivo.dto.ErroresContablesDTO;
import com.ath.adminefectivo.dto.FondosDTO;
import com.ath.adminefectivo.dto.OperacionesProgramadasDTO;
import com.ath.adminefectivo.dto.PuntosDTO;
import com.ath.adminefectivo.dto.RespuestaContableDTO;
import com.ath.adminefectivo.dto.TransaccionesInternasDTO;
import com.ath.adminefectivo.dto.TransportadorasDTO;
import com.ath.adminefectivo.dto.compuestos.ContabilidadDTO;
import com.ath.adminefectivo.dto.compuestos.ConteoContabilidadDTO;
import com.ath.adminefectivo.dto.compuestos.OperacionIntradiaDTO;
import com.ath.adminefectivo.entities.Puntos;
import com.ath.adminefectivo.service.IBancosService;
import com.ath.adminefectivo.service.ICiudadesService;
import com.ath.adminefectivo.service.IContabilidadService;
import com.ath.adminefectivo.service.IDominioService;
import com.ath.adminefectivo.service.IErroresContablesService;
import com.ath.adminefectivo.service.IFondosService;
import com.ath.adminefectivo.service.IParametroService;
import com.ath.adminefectivo.service.IPuntosService;
import com.ath.adminefectivo.service.ITransaccionesContablesService;
import com.ath.adminefectivo.service.ITransaccionesInternasService;
import com.ath.adminefectivo.service.ITransportadorasService;

@Service
public class ContabilidadServiceImpl implements IContabilidadService {

	@Autowired
	IPuntosService puntosService;

	@Autowired
	ICiudadesService ciudadesService;

	@Autowired
	ITransaccionesInternasService transaccionesInternasService;

	@Autowired
	ITransaccionesContablesService transaccionesContablesService;

	@Autowired
	IBancosService bancosService;

	@Autowired
	IFondosService fondosService;

	@Autowired
	ITransportadorasService transportadorasService;

	@Autowired
	IDominioService dominioService;
	
	@Autowired
	IErroresContablesService erroresContablesService;
	
	@Autowired
	IParametroService parametroService;

	int consecutivoDia = 1;
	int consecutivoMovContable = 1;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int generarContabilidad(String tipoContabilidad,
			List<OperacionesProgramadasDTO> listadoOperacionesProgramadas) {

		listadoOperacionesProgramadas.forEach(operacionProgramada -> 
			this.procesarRegistrosContabilidad(tipoContabilidad, operacionProgramada)
		);

		return consecutivoDia;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int generarMovimientosContables(Date fechaInicio, Date fechaFin, String tipoContabilidad,
			int estadoContabilidadGenerado) {
		boolean result = transaccionesInternasService.generarMovimientosContables(fechaInicio, fechaFin, tipoContabilidad, estadoContabilidadGenerado);
		if(result) {
			return 1;
		}
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int generarContabilidadIntradia(String tipoContabilidad,
			List<OperacionIntradiaDTO> listadoOperacionesProgramadasIntradia, int consecutivoDia) {
		this.consecutivoDia = consecutivoDia;
		if (listadoOperacionesProgramadasIntradia.isEmpty()) {
			return consecutivoDia;
		}
		List<TransaccionesInternasDTO> listadoTransaccionesIntradia = new ArrayList<>();
		listadoOperacionesProgramadasIntradia.forEach(operacionIntradia -> 
			listadoTransaccionesIntradia
					.addAll(procesarRegistrosContabilidadIntradia(tipoContabilidad, operacionIntradia))
		);

		return consecutivoDia;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContabilidadDTO generarRespuestaContabilidad(Date fechaSistema, String tipoContabilidad,
			String mensaje) {
		ContabilidadDTO contabilidadDTO = new ContabilidadDTO();
		
		ConteoContabilidadDTO conteoContabilidadDTO = transaccionesContablesService.generarConteoContabilidad(fechaSistema, tipoContabilidad);
		contabilidadDTO.setConteoContabilidadDTO(conteoContabilidadDTO);	
		
		contabilidadDTO.setMensaje(mensaje);
		
		List<ErroresContablesDTO> erroresContables = erroresContablesService.consultarErroresContablesByFechaAndTipoProceso(fechaSistema, tipoContabilidad);
		contabilidadDTO.setErroresContablesDTO(erroresContables);
	
		List<RespuestaContableDTO> respuestaContableDTO = transaccionesContablesService.getCierreContable(fechaSistema,tipoContabilidad,0);
		contabilidadDTO.setRespuestasContablesDTO(respuestaContableDTO);
		
		return contabilidadDTO;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TransaccionesInternasDTO> generarRespuestaProcesoContables() {
		return erroresContablesService.generarRespuestaProcesoContables();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void procesoEliminarExistentes(String tipoContabilidad,
			List<OperacionesProgramadasDTO> operacionesProgramadas, Date fechaInicio, Date fechaFin) {
			transaccionesContablesService.deleteTransaccionesContablesByFechasAndTipoProceso(fechaInicio, fechaFin, tipoContabilidad);
			transaccionesInternasService.deleteTransaccionesInternasByFechasAndTipoProceso(fechaInicio, fechaFin,tipoContabilidad);
		
	}

	/**
	 * ----------------------------------- METODOS PRIVADOS-----------------------------------
	 */

	/**
	 * 
	 * @param operacionProgramada
	 * @return
	 */
	private int procesarRegistrosContabilidad(String tipoContabilidad, OperacionesProgramadasDTO operacionProgramada) {
		if (tipoContabilidad.equals("PM")) {
			if (operacionProgramada.getTipoOperacion().equals("CONSIGNACION") && !operacionProgramada.isEsCambio()) {
				return this.procesarContabilidadConsignacion(tipoContabilidad, operacionProgramada);
			} else if (operacionProgramada.getTipoOperacion().equals("RETIRO") && !operacionProgramada.isEsCambio()) {
				return this.procesarContabilidadRetiro(tipoContabilidad, operacionProgramada);
			} else if (operacionProgramada.getTipoOperacion().equals("VENTA")) {
				return this.procesarContabilidadVenta(tipoContabilidad, operacionProgramada);
			}
		}
		if (tipoContabilidad.equals("AM")) {
			if(operacionProgramada.getTipoOperacion().equals("TRASLADO") ) {
				return this.procesarContabilidadTraslado(tipoContabilidad, operacionProgramada);
			}else if(operacionProgramada.getTipoOperacion().equals("INTERCAMBIO") ) {
				return this.procesarContabilidadIntercambio(tipoContabilidad, operacionProgramada);
			}else if(operacionProgramada.getTipoOperacion().equals("CONSIGNACION") && operacionProgramada.isEsCambio()) {
				return this.procesarContabilidadConsignacionCambio(tipoContabilidad, operacionProgramada);
			}else if(operacionProgramada.getTipoOperacion().equals("RETIRO") && operacionProgramada.isEsCambio()) {
				return this.procesarContabilidadRetiroCambio(tipoContabilidad, operacionProgramada);
			}
		}

		return 0;
	}

	/**
	 * 
	 * @param operacionProgramada
	 * @return
	 */
	private List<TransaccionesInternasDTO> procesarRegistrosContabilidadIntradia(String tipoContabilidad,
			OperacionIntradiaDTO operacionIntradia) {
		
		List<TransaccionesInternasDTO> listadoTransaccionesInternas = new ArrayList<>();
		long valorImpuesto = 0;
		
		if (operacionIntradia.getEntradaSalida().equals(Constantes.VALOR_ENTRADA)) {
			TransaccionesInternasDTO operacionIntradia21 = generarTransaccionInternaIntradia(tipoContabilidad, 21,
					operacionIntradia);
			listadoTransaccionesInternas.add(operacionIntradia21);	
			
			if (isCiudadCobroIVA(operacionIntradia.getCodigoPunto()) && operacionIntradia.getBancoAVAL() == Constantes.BANCO_BOGOTA) {
				TransaccionesInternasDTO operacionIntradia22 = generarTransaccionInternaIntradia(tipoContabilidad, 22,
						operacionIntradia);
				operacionIntradia22
						.setValor(this.calcularValorConImpuesto(operacionIntradia21.getValor(), Dominios.IMPUESTO_IVA));
				operacionIntradia22.setTipoImpuesto(Integer.valueOf(Dominios.IMPUESTO_IVA));
				listadoTransaccionesInternas.add(operacionIntradia22);
				valorImpuesto = operacionIntradia22.getValor();
			}	

			TransaccionesInternasDTO operacionIntradia23 = generarTransaccionInternaIntradia(tipoContabilidad, 23,
					operacionIntradia);
			operacionIntradia23.setCodigoComision(null);
			operacionIntradia23.setMedioPago(Dominios.MEDIOS_PAGO_ABONO);
			operacionIntradia23.setValor( operacionIntradia21.getValor() - valorImpuesto);
			listadoTransaccionesInternas.add(operacionIntradia23);

		} else if (operacionIntradia.getEntradaSalida().equals(Constantes.VALOR_SALIDA)) {
			TransaccionesInternasDTO operacionIntradia11 = generarTransaccionInternaIntradia(tipoContabilidad, 11,
					operacionIntradia);
			listadoTransaccionesInternas.add(operacionIntradia11);

			if (isCiudadCobroIVA(operacionIntradia.getCodigoPunto()) && operacionIntradia.getBancoAVAL() == Constantes.BANCO_BOGOTA ) {
				TransaccionesInternasDTO operacionIntradia12 = generarTransaccionInternaIntradia(tipoContabilidad, 12,
						operacionIntradia);
				operacionIntradia12
						.setValor(this.calcularValorConImpuesto(operacionIntradia11.getValor(), Dominios.IMPUESTO_IVA));
				operacionIntradia12.setTipoImpuesto(Integer.valueOf(Dominios.IMPUESTO_IVA));
				listadoTransaccionesInternas.add(operacionIntradia12);
				valorImpuesto = operacionIntradia12.getValor();
			}	

			TransaccionesInternasDTO operacionIntradia13 = generarTransaccionInternaIntradia(tipoContabilidad, 13,
					operacionIntradia);
			operacionIntradia13.setCodigoComision(null);
			operacionIntradia13.setMedioPago(Dominios.MEDIOS_PAGO_CARGO_A_CUENTA);
			operacionIntradia13.setValor( operacionIntradia11.getValor() - valorImpuesto);
			listadoTransaccionesInternas.add(operacionIntradia13);
		}
		return listadoTransaccionesInternas;
	}

	/**
	 * Determina si para la ciudad de un punto se debe cobrar impuesto IVA
	 */
	private boolean isCiudadCobroIVA(Integer codigoPunto) {
	
		String codigoDane = puntosService.getPuntoById(codigoPunto).getCodigoCiudad();
		if (!Objects.isNull(codigoDane)) {
			return ciudadesService.getCiudadPorCodigoDane(codigoDane).getCobroIva();
		}
		else {
			return true;
		}
	}

	/**
	 * 
	 * @param operacionProgramada
	 * @return
	 */
	private int procesarContabilidadConsignacion(String tipoProceso, OperacionesProgramadasDTO operacionProgramada) {

		generarTransaccionInterna(tipoProceso, 10, operacionProgramada, operacionProgramada.getCodigoFondoTDV());

		return consecutivoDia;
	}

	/**
	 * 
	 * @param operacionProgramada
	 * @return
	 */
	private int procesarContabilidadRetiro(String tipoProceso, OperacionesProgramadasDTO operacionProgramada) {
		generarTransaccionInterna(tipoProceso, 20, operacionProgramada, operacionProgramada.getCodigoFondoTDV());
		
		if(!Objects.isNull(operacionProgramada.getComisionBR()) && operacionProgramada.getComisionBR() > 0) {
			TransaccionesInternasDTO transaccionInternaDTOComision = generarTransaccionInterna(tipoProceso, 21, operacionProgramada, operacionProgramada.getCodigoFondoTDV());
			transaccionInternaDTOComision.setValor(operacionProgramada.getComisionBR().longValue());
			transaccionInternaDTOComision.setCodigoComision(Integer.valueOf(Dominios.COMISION_1));
			transaccionesInternasService.saveTransaccionesInternasById(transaccionInternaDTOComision);
			
			long valorImpuesto = 0;
			if (isCiudadCobroIVA(operacionProgramada.getCodigoFondoTDV()) ) {
				TransaccionesInternasDTO transaccionInternaDTOImpuesto = generarTransaccionInterna(tipoProceso, 22, operacionProgramada, operacionProgramada.getCodigoFondoTDV());
				valorImpuesto = this.calcularValorConImpuesto(operacionProgramada.getComisionBR(), Dominios.IMPUESTO_IVA);
				transaccionInternaDTOImpuesto.setValor(valorImpuesto);
				transaccionInternaDTOImpuesto.setCodigoComision(Integer.valueOf(Dominios.COMISION_1));
				transaccionInternaDTOImpuesto.setTipoImpuesto(Integer.valueOf(Dominios.IMPUESTO_IVA));
				transaccionesInternasService.saveTransaccionesInternasById(transaccionInternaDTOImpuesto);
			}			
			
			TransaccionesInternasDTO transaccionInternaDTOMedioPago = generarTransaccionInterna(tipoProceso, 23, operacionProgramada, operacionProgramada.getCodigoFondoTDV());
			long valorD = operacionProgramada.getComisionBR() + valorImpuesto;
			transaccionInternaDTOMedioPago.setValor(valorD);
			transaccionInternaDTOMedioPago.setMedioPago(Dominios.MEDIOS_PAGO_DESCUENTO);	
			transaccionesInternasService.saveTransaccionesInternasById(transaccionInternaDTOMedioPago);

		}

		return consecutivoDia;
	}

	/**
	 * 
	 * @param operacionProgramada
	 * @return
	 */
	private int procesarContabilidadVenta(String tipoProceso, OperacionesProgramadasDTO operacionProgramada) {
		
		PuntosDTO puntoOrigen = PuntosDTO.CONVERTER_DTO
				.apply(puntosService.getPuntoById(operacionProgramada.getCodigoPuntoOrigen()));
		PuntosDTO puntoDestino = PuntosDTO.CONVERTER_DTO
				.apply(puntosService.getPuntoById(operacionProgramada.getCodigoPuntoDestino()));
		long valorComision;
		long valorImpuesto = 0;
		long valorPago;
		PuntosDTO  puntoBancoExterno = null;
		if (operacionProgramada.getEntradaSalida().equals("ENTRADA")) {
			puntoBancoExterno = puntoConsultarBancoAval(puntoOrigen);
			TransaccionesInternasDTO transaccionInternaDTOVenta10 = generarTransaccionInterna(tipoProceso, 20,
					operacionProgramada, operacionProgramada.getCodigoFondoTDV());
			transaccionInternaDTOVenta10.setCodigoPuntoBancoExt(puntoBancoExterno);
			transaccionesInternasService.saveTransaccionesInternasById(transaccionInternaDTOVenta10);
			
			if(Integer.valueOf(operacionProgramada.getTasaNegociacion()) > 0) {
				TransaccionesInternasDTO transaccionInternaDTOVenta11 = generarTransaccionInterna(tipoProceso, 21, operacionProgramada, operacionProgramada.getCodigoFondoTDV());

				Double valorComisionDouble = (operacionProgramada.getValorTotal() * Double.valueOf(operacionProgramada.getTasaNegociacion()).intValue()) / 10000;
				valorComision = valorComisionDouble.longValue();
				transaccionInternaDTOVenta11.setValor(valorComision);
				transaccionInternaDTOVenta11.setTasaNegociacion(operacionProgramada.getTasaNegociacion());
				transaccionInternaDTOVenta11.setCodigoPuntoBancoExt(puntoBancoExterno);
				transaccionInternaDTOVenta11.setCodigoComision(Integer.valueOf(Dominios.COMISION_2));
				transaccionesInternasService.saveTransaccionesInternasById(transaccionInternaDTOVenta11);

				BancosDTO bancoDestinoDTO = bancosService.validarPuntoBancoEsAval(puntoBancoExterno.getCodigoPunto());
				if (Objects.isNull(bancoDestinoDTO) && isCiudadCobroIVA(operacionProgramada.getCodigoFondoTDV()) ) {	
					TransaccionesInternasDTO transaccionInternaDTOVenta12 = generarTransaccionInterna(tipoProceso, 22,
							operacionProgramada, operacionProgramada.getCodigoFondoTDV());
					valorImpuesto = this.calcularValorConImpuesto(valorComision, Dominios.IMPUESTO_IVA);
					transaccionInternaDTOVenta12.setValor(valorImpuesto);
					transaccionInternaDTOVenta12.setTipoImpuesto(Integer.valueOf(Dominios.IMPUESTO_IVA));
					transaccionesInternasService.saveTransaccionesInternasById(transaccionInternaDTOVenta12);
				}
													
				TransaccionesInternasDTO transaccionInternaDTOVenta13 = generarTransaccionInterna(tipoProceso, 23, operacionProgramada, operacionProgramada.getCodigoFondoTDV());
				valorPago = transaccionInternaDTOVenta11.getValor() + valorImpuesto;
				transaccionInternaDTOVenta13.setValor(valorPago);
				transaccionInternaDTOVenta13.setMedioPago(Dominios.MEDIOS_PAGO_ABONO);
				transaccionesInternasService.saveTransaccionesInternasById(transaccionInternaDTOVenta13);

			}

		} else if (operacionProgramada.getEntradaSalida().equals("SALIDA")) {
			puntoBancoExterno = puntoConsultarBancoAval(puntoDestino);
			TransaccionesInternasDTO transaccionInternaDTOVenta20 = generarTransaccionInterna(tipoProceso, 10,
					operacionProgramada, operacionProgramada.getCodigoFondoTDV());
			transaccionInternaDTOVenta20.setCodigoPuntoBancoExt(puntoBancoExterno);
			transaccionInternaDTOVenta20.setTasaNegociacion(operacionProgramada.getTasaNegociacion());
			transaccionesInternasService.saveTransaccionesInternasById(transaccionInternaDTOVenta20);

			if (Integer.valueOf(operacionProgramada.getTasaNegociacion()) > 0) {

				TransaccionesInternasDTO transaccionInternaDTOVenta21 = generarTransaccionInterna(tipoProceso, 11,
						operacionProgramada, operacionProgramada.getCodigoFondoTDV());
				transaccionInternaDTOVenta21.setTasaNegociacion(operacionProgramada.getTasaNegociacion());
				Double valorComisionDouble = (operacionProgramada.getValorTotal() * Integer.valueOf(operacionProgramada.getTasaNegociacion())) / 10000;
				valorComision = valorComisionDouble.longValue();
				transaccionInternaDTOVenta21.setValor(valorComision);
				transaccionInternaDTOVenta21.setCodigoComision(Integer.valueOf(Dominios.COMISION_2));
				transaccionInternaDTOVenta21.setCodigoPuntoBancoExt(puntoBancoExterno);
				transaccionesInternasService.saveTransaccionesInternasById(transaccionInternaDTOVenta21);

				BancosDTO bancoDestinoDTO = bancosService.validarPuntoBancoEsAval(puntoBancoExterno.getCodigoPunto());
				if (Objects.isNull(bancoDestinoDTO) && isCiudadCobroIVA(operacionProgramada.getCodigoFondoTDV()) ) {	
					TransaccionesInternasDTO transaccionInternaDTOVenta22 = generarTransaccionInterna(tipoProceso, 12,
							operacionProgramada, operacionProgramada.getCodigoFondoTDV());
					valorImpuesto = this.calcularValorConImpuesto(valorComision, Dominios.IMPUESTO_IVA);
					transaccionInternaDTOVenta22.setValor(valorImpuesto);
					transaccionInternaDTOVenta22.setCodigoComision(Integer.valueOf(Dominios.COMISION_2));
					transaccionInternaDTOVenta22.setTipoImpuesto(Integer.valueOf(Dominios.IMPUESTO_IVA));
					transaccionesInternasService.saveTransaccionesInternasById(transaccionInternaDTOVenta22);
				}
				
				TransaccionesInternasDTO transaccionInternaDTOVenta23 = generarTransaccionInterna(tipoProceso, 13, operacionProgramada, operacionProgramada.getCodigoFondoTDV());
				valorPago = transaccionInternaDTOVenta21.getValor() +  valorImpuesto;
				transaccionInternaDTOVenta23.setValor(valorPago);
				transaccionInternaDTOVenta23.setMedioPago(Dominios.MEDIOS_PAGO_DESCUENTO);
				transaccionesInternasService.saveTransaccionesInternasById(transaccionInternaDTOVenta23);
			}

		}

		return consecutivoDia;
	}
	
	/**
	 * 
	 * @param operacionProgramada
	 * @return
	 */
	private int procesarContabilidadTraslado(String tipoProceso, OperacionesProgramadasDTO operacionProgramada) {

		if(Constantes.NOMBRE_SALIDA.equals(operacionProgramada.getEntradaSalida())) {	
			TransaccionesInternasDTO transaccionInternaDTOTraslado10 = generarTransaccionInterna(tipoProceso, 10, operacionProgramada,
					operacionProgramada.getCodigoPuntoOrigen());
			transaccionesInternasService.saveTransaccionesInternasById(transaccionInternaDTOTraslado10);
		}
		else { 	
			TransaccionesInternasDTO transaccionInternaDTOTraslado20 = generarTransaccionInterna(tipoProceso, 20, operacionProgramada,
					operacionProgramada.getCodigoPuntoDestino());
			transaccionesInternasService.saveTransaccionesInternasById(transaccionInternaDTOTraslado20);
		}
		return consecutivoDia;
	}
	
	/**
	 * 
	 * @param operacionProgramada
	 * @return
	 */
	private int procesarContabilidadIntercambio(String tipoProceso, OperacionesProgramadasDTO operacionProgramada) {

		if(Constantes.NOMBRE_SALIDA.equals(operacionProgramada.getEntradaSalida())) {
			TransaccionesInternasDTO transaccionInternaDTOInter10 = generarTransaccionInterna(tipoProceso, 10, operacionProgramada,
					operacionProgramada.getCodigoFondoTDV());
			transaccionesInternasService.saveTransaccionesInternasById(transaccionInternaDTOInter10);
		}  // es una entrada
		else {
			TransaccionesInternasDTO transaccionInternaDTOInter20 = generarTransaccionInterna(tipoProceso, 20, operacionProgramada,
					operacionProgramada.getCodigoFondoTDV());
			transaccionesInternasService.saveTransaccionesInternasById(transaccionInternaDTOInter20);
		}
		return consecutivoDia;
	}
	
	/**
	 * 
	 * @param operacionProgramada
	 * @return
	 */
	private int procesarContabilidadConsignacionCambio(String tipoProceso, OperacionesProgramadasDTO operacionProgramada) {

		TransaccionesInternasDTO transaccionInternaDTOConsignacion10 = generarTransaccionInterna(tipoProceso, 10, operacionProgramada,
				operacionProgramada.getCodigoFondoTDV());
		transaccionInternaDTOConsignacion10.setEsCambio(true);
		transaccionesInternasService.saveTransaccionesInternasById(transaccionInternaDTOConsignacion10);

		return consecutivoDia;
	}
	
	/**
	 * 
	 * @param operacionProgramada
	 * @return
	 */
	private int procesarContabilidadRetiroCambio(String tipoProceso, OperacionesProgramadasDTO operacionProgramada) {
		
		TransaccionesInternasDTO transaccionInternaDTO = generarTransaccionInterna(tipoProceso, 20, operacionProgramada, operacionProgramada.getCodigoFondoTDV());
		transaccionInternaDTO.setEsCambio(true);
		transaccionesInternasService.saveTransaccionesInternasById(transaccionInternaDTO);
		return consecutivoDia;
	}

	private TransaccionesInternasDTO generarTransaccionInterna(String tipoProceso, Integer tipoTransaccion,
			OperacionesProgramadasDTO operacionProgramada, Integer codigoPunto) {
		
		Date fechaSistema = parametroService.valorParametroDate(Constantes.FECHA_DIA_PROCESO);
		
				Double valorD = operacionProgramada.getValorTotal();
		TransaccionesInternasDTO transaccionInternaDTO = TransaccionesInternasDTO.builder()
				.idOperacion(operacionProgramada).consecutivoDia(String.valueOf(consecutivoDia))
				.fecha(fechaSistema).tipoTransaccion(tipoTransaccion)
				.codigoMoneda(operacionProgramada.getCodigoMoneda()).valor(valorD.longValue())
				.tasaEjeCop(1).tasaNoEje(1).tipoOperacion(operacionProgramada.getTipoOperacion())
				.tipoProceso(tipoProceso).estado(Dominios.ESTADO_CONTABILIDAD_GENERADO).esCambio(false).build();

		Puntos puntoTDV = puntosService.getPuntoById(codigoPunto);
		transaccionInternaDTO.setCodigoPunto(PuntosDTO.CONVERTER_DTO.apply(puntoTDV));
		CiudadesDTO ciudadFondo = ciudadesService.getCiudadPorCodigoDane(puntoTDV.getCodigoCiudad());
		transaccionInternaDTO.setCiudad(ciudadFondo);

		FondosDTO fondoDTO = fondosService.getFondoByCodigoPunto(puntoTDV.getCodigoPunto());
		TransportadorasDTO transportadoraDTO = transportadorasService.getTransportadoraPorCodigo(fondoDTO.getTdv());

		transaccionInternaDTO.setCodigoTdv(transportadoraDTO);

		consecutivoDia++;
		BancosDTO bancoAval = bancosService.validarPuntoBancoEsAval(fondoDTO.getBancoAVAL());
		 if(Objects.isNull(bancoAval)) {
			 transaccionInternaDTO.setEstado(1);
			 return null;
		 }
		 transaccionInternaDTO.setBancoAval(bancoAval);
		 return TransaccionesInternasDTO.CONVERTER_DTO.apply(transaccionesInternasService.saveTransaccionesInternasById(transaccionInternaDTO));
	}

	private long calcularValorConImpuesto(long long1, String impuesto) {
		Integer valorImpuesto = dominioService.valorNumericoDominio(Constantes.DOMINIO_IMPUESTOS, impuesto).intValue();
		return (long1 * valorImpuesto) / 100;
	}

	/**
	 * 
	 * @param tipoProceso
	 * @param tipoTransaccion
	 * @param operacionProgramada
	 * @param codigoPunto
	 * @return TransaccionesInternasDTO
	 */
	private TransaccionesInternasDTO generarTransaccionInternaIntradia(String tipoProceso, Integer tipoTransaccion,
			OperacionIntradiaDTO transaccionIntradia) {
		
		TransaccionesInternasDTO transaccionInternaDTO = TransaccionesInternasDTO.builder().idOperacion(null)
				.idGenerico(null).consecutivoDia(String.valueOf(consecutivoDia)).codigoMoneda("COP")
				.valor(dominioService.valorNumericoDominio(Constantes.DOMINIO_COMISIONES, Dominios.COMISION_3).longValue())
				.tasaEjeCop(1).tasaNoEje(1).tipoTransaccion(tipoTransaccion).tipoOperacion("VENTA")
				.tipoProceso(tipoProceso).estado(Dominios.ESTADO_CONTABILIDAD_GENERADO).codigoComision(dominioService
						.valorNumericoDominio(Constantes.DOMINIO_COMISIONES, Dominios.COMISION_3).intValue())
				.esCambio(false).build();

		BancosDTO bancoAval = bancosService.findBancoByCodigoPunto(transaccionIntradia.getBancoAVAL());
		transaccionInternaDTO.setBancoAval(bancoAval);

		PuntosDTO puntoBancoAval = PuntosDTO.CONVERTER_DTO
				.apply(puntosService.getPuntoById(bancoAval.getCodigoPunto()));
		transaccionInternaDTO.setCodigoPunto(puntoBancoAval);

		BancosDTO bancoDestino = bancosService.findBancoByCodigoPunto(transaccionIntradia.getCodigoPunto());
		PuntosDTO puntoBancoDestino = PuntosDTO.CONVERTER_DTO
				.apply(puntosService.getPuntoById(bancoDestino.getCodigoPunto()));
		transaccionInternaDTO.setCodigoPuntoBancoExt(puntoConsultarBancoAval(puntoBancoDestino));
		consecutivoDia++;
		return TransaccionesInternasDTO.CONVERTER_DTO
				.apply(transaccionesInternasService.saveTransaccionesInternasById(transaccionInternaDTO));
	}
	
	private PuntosDTO puntoConsultarBancoAval(PuntosDTO punto) {
		if(punto.getTipoPunto().equals(dominioService.valorTextoDominio(Constantes.DOMINIO_TIPOS_PUNTO, Dominios.TIPOS_PUNTO_FONDO))){
			Integer idBanco = fondosService.getEntidadFondo(punto.getCodigoPunto()).getBancoAVAL();
			return PuntosDTO.CONVERTER_DTO.apply(puntosService.getPuntoById(idBanco));
		}
		
		return punto;
		
	}



}
