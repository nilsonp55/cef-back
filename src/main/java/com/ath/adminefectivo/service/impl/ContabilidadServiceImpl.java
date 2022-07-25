package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.delegate.IContabilidadDelegate;
import com.ath.adminefectivo.dto.ArchivosCargadosDTO;
import com.ath.adminefectivo.dto.BancosDTO;
import com.ath.adminefectivo.dto.CiudadesDTO;
import com.ath.adminefectivo.dto.FondosDTO;
import com.ath.adminefectivo.dto.OperacionesProgramadasDTO;
import com.ath.adminefectivo.dto.PuntosDTO;
import com.ath.adminefectivo.dto.TransaccionesContablesDTO;
import com.ath.adminefectivo.dto.TransaccionesInternasDTO;
import com.ath.adminefectivo.dto.TransportadorasDTO;
import com.ath.adminefectivo.dto.compuestos.ErroresCamposDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionLineasDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.ArchivosCargados;
import com.ath.adminefectivo.entities.FallasArchivo;
import com.ath.adminefectivo.entities.FallasRegistro;
import com.ath.adminefectivo.entities.Puntos;
import com.ath.adminefectivo.entities.RegistrosCargados;
import com.ath.adminefectivo.entities.TransaccionesInternas;
import com.ath.adminefectivo.entities.id.FallasRegistroPK;
import com.ath.adminefectivo.entities.id.RegistrosCargadosPK;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.ArchivosCargadosRepository;
import com.ath.adminefectivo.service.IArchivosCargadosService;
import com.ath.adminefectivo.service.IBancosService;
import com.ath.adminefectivo.service.ICiudadesService;
import com.ath.adminefectivo.service.IContabilidadService;
import com.ath.adminefectivo.service.IDominioService;
import com.ath.adminefectivo.service.IFondosService;
import com.ath.adminefectivo.service.IPuntosService;
import com.ath.adminefectivo.service.ITransaccionesContablesService;
import com.ath.adminefectivo.service.ITransaccionesInternasService;
import com.ath.adminefectivo.service.ITransportadorasService;
import com.querydsl.core.types.Predicate;

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
	
	
	int consecutivoDia = 1;
	int consecutivoMovContable = 1;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int generarContabilidad(String tipoContabilidad, List<OperacionesProgramadasDTO> listadoOperacionesProgramadas) {
		
		listadoOperacionesProgramadas.forEach(operacionProgramada ->  {
			this.procesarRegistrosContabilidad(tipoContabilidad, operacionProgramada);
		});
		
		return consecutivoDia;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int generarMovimientosContables(String tipoContabilidad,
			List<TransaccionesInternasDTO> listadoTransaccionesInternas) {
		listadoTransaccionesInternas.forEach(transaccionInterna ->  {
			this.procesarTransaccionesInternas(tipoContabilidad, transaccionInterna);
		});
		return consecutivoMovContable;
	}
	
	
	/**
	 * 
	 * @param tipoContabilidad
	 * @param transaccionInterna
	 */
private void procesarTransaccionesInternas(String tipoContabilidad, TransaccionesInternasDTO transaccionInterna) {
		// TODO Auto-generated method stub
		
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
		if(tipoContabilidad.equals("PM")) {
			if(operacionProgramada.getTipoOperacion().equals("CONSIGNACION")){
				return this.procesarContabilidadConsignacion(tipoContabilidad, operacionProgramada);
			}else if(operacionProgramada.getTipoOperacion().equals("RETIRO")) {
				return this.procesarContabilidadRetiro(tipoContabilidad, operacionProgramada);
			}else if(operacionProgramada.getTipoOperacion().equals("VENTA")) {
				return this.procesarContabilidadVenta(tipoContabilidad, operacionProgramada);
			}
		}
		
		return 0;
	}

	/**
	 * 
	 * @param operacionProgramada
	 * @return
	 */
	private int procesarContabilidadConsignacion(String tipoProceso, OperacionesProgramadasDTO operacionProgramada) {
		
		TransaccionesInternasDTO transaccionInternaDTO = generarTransaccionInterna(tipoProceso, 10, operacionProgramada, operacionProgramada.getCodigoFondoTDV());
				
		return consecutivoDia;
	}
	
	/**
	 * 
	 * @param operacionProgramada
	 * @return
	 */
	private int procesarContabilidadRetiro(String tipoProceso, OperacionesProgramadasDTO operacionProgramada) {
		TransaccionesInternasDTO transaccionInternaDTO = generarTransaccionInterna(tipoProceso, 20, operacionProgramada, operacionProgramada.getCodigoFondoTDV());
		
		if(!Objects.isNull(operacionProgramada.getComisionBR()) && operacionProgramada.getComisionBR() > 0) {
			TransaccionesInternasDTO transaccionInternaDTOComision = generarTransaccionInterna(tipoProceso, 21, operacionProgramada, operacionProgramada.getCodigoFondoTDV());
			transaccionInternaDTOComision.setValor(Double.valueOf(operacionProgramada.getComisionBR()));
			transaccionInternaDTOComision.setCodigoComision(Integer.valueOf(Dominios.COMISION_1));
			transaccionesInternasService.saveTransaccionesInternasById(transaccionInternaDTOComision);
			
			TransaccionesInternasDTO transaccionInternaDTOImpuesto = generarTransaccionInterna(tipoProceso, 22, operacionProgramada, operacionProgramada.getCodigoFondoTDV());
			
			Double valorImpuesto = this.calcularValorConImpuesto(operacionProgramada.getValorTotal(), Dominios.IMPUESTO_IVA);
			transaccionInternaDTOImpuesto.setValor(valorImpuesto);
			transaccionInternaDTOImpuesto.setCodigoComision(Integer.valueOf(Dominios.COMISION_1));
			transaccionInternaDTOImpuesto.setTipoImpuesto(Integer.valueOf(Dominios.IMPUESTO_IVA));
			
			transaccionesInternasService.saveTransaccionesInternasById(transaccionInternaDTOImpuesto);
			
			
			TransaccionesInternasDTO transaccionInternaDTOMedioPago = generarTransaccionInterna(tipoProceso, 23, operacionProgramada, operacionProgramada.getCodigoFondoTDV());
			transaccionInternaDTOMedioPago.setValor(operacionProgramada.getComisionBR() + valorImpuesto);
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
		TransaccionesInternasDTO transaccionInternaDTOVenta = null;
		
		if(operacionProgramada.getEntradaSalida().equals("SALIDA")) {
			PuntosDTO puntoDestino = PuntosDTO.CONVERTER_DTO.apply(puntosService.getPuntoById(operacionProgramada.getCodigoPuntoDestino()));
			
			TransaccionesInternasDTO  transaccionInternaDTOVenta10 = generarTransaccionInterna(tipoProceso, 10, operacionProgramada, operacionProgramada.getCodigoFondoTDV());
			transaccionInternaDTOVenta10.setCodigoPuntoBancoExt(puntoDestino);
			transaccionesInternasService.saveTransaccionesInternasById(transaccionInternaDTOVenta10);
			
			if(Integer.valueOf(operacionProgramada.getTasaNegociacion()) > 0) {
				TransaccionesInternasDTO transaccionInternaDTOVenta11 = generarTransaccionInterna(tipoProceso, 11, operacionProgramada, operacionProgramada.getCodigoFondoTDV());
				Double valorComision = operacionProgramada.getValorTotal() * Double.valueOf(operacionProgramada.getTasaNegociacion());
				transaccionInternaDTOVenta11.setValor(valorComision);
				transaccionInternaDTOVenta11.setTasaNegociacion(operacionProgramada.getTasaNegociacion());
				transaccionInternaDTOVenta11.setCodigoPuntoBancoExt(puntoDestino);
				transaccionInternaDTOVenta11.setCodigoComision(Integer.valueOf(Dominios.COMISION_2));
				transaccionesInternasService.saveTransaccionesInternasById(transaccionInternaDTOVenta11);
				
				FondosDTO fondoDestinoDTO = fondosService.getFondoByCodigoPunto(puntoDestino.getCodigoPunto());
				BancosDTO bancoDestinoDTO = bancosService.validarPuntoBancoEsAval(fondoDestinoDTO.getBancoAVAL());
				if(Objects.isNull(bancoDestinoDTO)) {
					TransaccionesInternasDTO transaccionInternaDTOVenta12 = generarTransaccionInterna(tipoProceso, 12, operacionProgramada, operacionProgramada.getCodigoFondoTDV());
					valorComision = this.calcularValorConImpuesto(valorComision, Dominios.IMPUESTO_IVA);
					transaccionInternaDTOVenta12.setValor(valorComision);
					transaccionInternaDTOVenta12.setTipoImpuesto(Integer.valueOf(Dominios.IMPUESTO_IVA));
					transaccionesInternasService.saveTransaccionesInternasById(transaccionInternaDTOVenta12);
				}
									
				TransaccionesInternasDTO transaccionInternaDTOVenta13 = generarTransaccionInterna(tipoProceso, 13, operacionProgramada, operacionProgramada.getCodigoFondoTDV());
				transaccionInternaDTOVenta13.setValor(transaccionInternaDTOVenta11.getValor() + valorComision);
				transaccionInternaDTOVenta13.setMedioPago(Dominios.MEDIOS_PAGO_ABONO);
				transaccionesInternasService.saveTransaccionesInternasById(transaccionInternaDTOVenta13);
				
				
			}
			
			
		}else if(operacionProgramada.getEntradaSalida().equals("ENTRADA")) {
			PuntosDTO puntoOrigen = PuntosDTO.CONVERTER_DTO.apply(puntosService.getPuntoById(operacionProgramada.getCodigoPuntoOrigen()));
			TransaccionesInternasDTO transaccionInternaDTOVenta20 = generarTransaccionInterna(tipoProceso, 20, operacionProgramada, operacionProgramada.getCodigoFondoTDV());
			transaccionInternaDTOVenta20.setCodigoPuntoBancoExt(puntoOrigen);
			transaccionInternaDTOVenta20.setTasaNegociacion(operacionProgramada.getTasaNegociacion());
			transaccionesInternasService.saveTransaccionesInternasById(transaccionInternaDTOVenta20);
			
			
			if(Integer.valueOf(operacionProgramada.getTasaNegociacion()) > 0) {
				TransaccionesInternasDTO transaccionInternaDTOVenta21 = generarTransaccionInterna(tipoProceso, 21, operacionProgramada, operacionProgramada.getCodigoFondoTDV());
				transaccionInternaDTOVenta21.setTasaNegociacion(operacionProgramada.getTasaNegociacion());
				Double valorComision = operacionProgramada.getValorTotal() * Double.valueOf(operacionProgramada.getTasaNegociacion());
				transaccionInternaDTOVenta21.setValor(valorComision);
				transaccionInternaDTOVenta21.setCodigoComision(Integer.valueOf(Dominios.COMISION_2));
				transaccionInternaDTOVenta21.setCodigoPuntoBancoExt(puntoOrigen);
				transaccionesInternasService.saveTransaccionesInternasById(transaccionInternaDTOVenta21);
				
				FondosDTO fondoOrigenDTO = fondosService.getFondoByCodigoPunto(puntoOrigen.getCodigoPunto());
				BancosDTO bancoOrigenDTO = bancosService.validarPuntoBancoEsAval(fondoOrigenDTO.getBancoAVAL());
				if(Objects.isNull(bancoOrigenDTO)) {
					TransaccionesInternasDTO transaccionInternaDTOVenta22 = generarTransaccionInterna(tipoProceso, 22, operacionProgramada, operacionProgramada.getCodigoFondoTDV());
					valorComision = this.calcularValorConImpuesto(valorComision, Dominios.IMPUESTO_IVA);
					transaccionInternaDTOVenta22.setValor(valorComision);
					transaccionInternaDTOVenta22.setCodigoComision(Integer.valueOf(Dominios.COMISION_2));
					transaccionInternaDTOVenta22.setTipoImpuesto(Integer.valueOf(Dominios.IMPUESTO_IVA));
					transaccionesInternasService.saveTransaccionesInternasById(transaccionInternaDTOVenta22);
				}
				
				TransaccionesInternasDTO transaccionInternaDTOVenta23 = generarTransaccionInterna(tipoProceso, 23, operacionProgramada, operacionProgramada.getCodigoFondoTDV());
				transaccionInternaDTOVenta23.setValor(transaccionInternaDTOVenta21.getValor() +  valorComision);
				transaccionInternaDTOVenta23.setMedioPago(Dominios.MEDIOS_PAGO_DESCUENTO);
				transaccionesInternasService.saveTransaccionesInternasById(transaccionInternaDTOVenta23);
			}
			
		}
				
		return consecutivoDia;
	}
	
	private TransaccionesInternasDTO generarTransaccionInterna(String tipoProceso, Integer tipoTransaccion, OperacionesProgramadasDTO operacionProgramada, Integer codigoPunto) {
		TransaccionesInternasDTO transaccionInternaDTO = TransaccionesInternasDTO.builder()
				.idOperacion(operacionProgramada).consecutivoDia(consecutivoDia).fecha(operacionProgramada.getFechaProgramacion()).
				tipoTransaccion(tipoTransaccion).codigoMoneda(operacionProgramada.getCodigoMoneda()).valor(operacionProgramada.getValorTotal()).tasaEjeCop(1).tasaNoEje(1).
				tipoOperacion(operacionProgramada.getTipoOperacion()).tipoProceso(tipoProceso).estado("Dominios.GENERADO").
				esCambio(false).
				build();
		

		Puntos puntoTDV = puntosService.getPuntoById(codigoPunto);
		transaccionInternaDTO.setCodigoTdv(PuntosDTO.CONVERTER_DTO.apply(puntoTDV));
		transaccionInternaDTO.setCodigoPunto(PuntosDTO.CONVERTER_DTO.apply(puntoTDV));
		CiudadesDTO ciudadFondo = ciudadesService.getCiudadPorCodigoDane(puntoTDV.getCodigoCiudad());
		transaccionInternaDTO.setCiudad(ciudadFondo);	
		
		FondosDTO fondoDTO = fondosService.getFondoByCodigoPunto(puntoTDV.getCodigoPunto());
		TransportadorasDTO transportadoraDTO = transportadorasService.getTransportadoraPorCodigo(fondoDTO.getTdv());
		
		transaccionInternaDTO.setTransportadora(transportadoraDTO);
		
		
		consecutivoDia++;
		BancosDTO bancoAval = bancosService.validarPuntoBancoEsAval(fondoDTO.getBancoAVAL());
		 if(Objects.isNull(bancoAval)) {
			 transaccionInternaDTO.setEstado("ERROR_CONTABLE");
			 return null;
		 }
		 transaccionInternaDTO.setBancoAval(bancoAval);
		 return TransaccionesInternasDTO.CONVERTER_DTO.apply(transaccionesInternasService.saveTransaccionesInternasById(transaccionInternaDTO));
	}
	
	private TransaccionesContablesDTO generarMovimientoContable(String tipoProceso, Integer tipoTransaccion, TransaccionesInternasDTO transaccionesInternasDTO) {
		 TransaccionesContablesDTO transaccionesContablesDTO = TransaccionesContablesDTO.builder()
				.idOperacion(transaccionesInternasDTO.getIdOperacion()).idGenerico(transaccionesInternasDTO.getIdGenerico()).consecutivoDia(consecutivoDia).fecha(transaccionesInternasDTO.getFecha())
				.tipoTransaccion(tipoTransaccion).bancoAval(transaccionesInternasDTO.getBancoAval()).codigoMoneda(transaccionesInternasDTO.getCodigoMoneda())
				.valor(transaccionesInternasDTO.getValor()).tipoProceso(tipoProceso).bancoAval(transaccionesInternasDTO.getBancoAval())
				.build();
		 
		 
		
		consecutivoMovContable++;
		return TransaccionesContablesDTO.CONVERTER_DTO.apply(transaccionesContablesService.saveTransaccionesContablesById(transaccionesContablesDTO));
	}
	
	private Double calcularValorConImpuesto(Double valor, String impuesto) {
		Double valorImpuesto = Double.valueOf(dominioService.valorNumericoDominio(Constantes.DOMINIO_IMPUESTOS, impuesto));
		return (valor*valorImpuesto)/100;
	}
	
	

}
