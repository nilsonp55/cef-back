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
import com.ath.adminefectivo.dto.OperacionesProgramadasDTO;
import com.ath.adminefectivo.dto.PuntosDTO;
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
import com.ath.adminefectivo.service.ICiudadesService;
import com.ath.adminefectivo.service.IContabilidadService;
import com.ath.adminefectivo.service.IPuntosService;
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
	
	
	int consecutivoDia = 1;
	
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
		
		TransaccionesInternasDTO transaccionInternaDTO = generarTransaccionInterna(tipoProceso, 10, operacionProgramada);
				
		return consecutivoDia;
	}
	
	/**
	 * 
	 * @param operacionProgramada
	 * @return
	 */
	private int procesarContabilidadRetiro(String tipoProceso, OperacionesProgramadasDTO operacionProgramada) {
		TransaccionesInternasDTO transaccionInternaDTO = generarTransaccionInterna(tipoProceso, 20, operacionProgramada);
		
		if(!Objects.isNull(operacionProgramada.getComisionBR())) {
			TransaccionesInternasDTO transaccionInternaDTOComision = generarTransaccionInterna(tipoProceso, 21, operacionProgramada);
			TransaccionesInternasDTO transaccionInternaDTOImpuesto = generarTransaccionInterna(tipoProceso, 22, operacionProgramada);
			TransaccionesInternasDTO transaccionInternaDTOMedioPago = generarTransaccionInterna(tipoProceso, 23, operacionProgramada);
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
			transaccionInternaDTOVenta = generarTransaccionInterna(tipoProceso, 10, operacionProgramada);
			transaccionInternaDTOVenta = generarTransaccionInterna(tipoProceso, 11, operacionProgramada);
			transaccionInternaDTOVenta = generarTransaccionInterna(tipoProceso, 12, operacionProgramada);
			transaccionInternaDTOVenta = generarTransaccionInterna(tipoProceso, 13, operacionProgramada);
		}else if(operacionProgramada.getEntradaSalida().equals("ENTRADA")) {
			transaccionInternaDTOVenta = generarTransaccionInterna(tipoProceso, 20, operacionProgramada);
			transaccionInternaDTOVenta = generarTransaccionInterna(tipoProceso, 21, operacionProgramada);
			transaccionInternaDTOVenta = generarTransaccionInterna(tipoProceso, 22, operacionProgramada);
			transaccionInternaDTOVenta = generarTransaccionInterna(tipoProceso, 23, operacionProgramada);
		}
				
		return consecutivoDia;
	}
	
	private TransaccionesInternasDTO generarTransaccionInterna(String tipoProceso, Integer tipoTransaccion, OperacionesProgramadasDTO operacionProgramada) {
		TransaccionesInternasDTO transaccionInternaDTO = TransaccionesInternasDTO.builder()
				.idOperacion(operacionProgramada).consecutivoDia(consecutivoDia).fecha(operacionProgramada.getFechaProgramacion()).
				tipoTransaccion(tipoTransaccion).codigoMoneda(operacionProgramada.getCodigoMoneda()).valor(operacionProgramada.getValorTotal()).tasaEjeCop(1).tasaNoEje(1).
				tipoOperacion(operacionProgramada.getTipoOperacion()).tipoProceso(tipoProceso).estado("Dominios.GENERADO").
				esCambio(operacionProgramada.isEsCambio()).
				build();
		

		Puntos puntoTDV = puntosService.getPuntoById(operacionProgramada.getCodigoFondoTDV());
		transaccionInternaDTO.setCodigoTdv(PuntosDTO.CONVERTER_DTO.apply(puntoTDV));
		transaccionInternaDTO.setCodigoPunto(PuntosDTO.CONVERTER_DTO.apply(puntoTDV));
		CiudadesDTO ciudadFondo = ciudadesService.getCiudadPorCodigoDane(puntoTDV.getCodigoCiudad());
		transaccionInternaDTO.setCiudad(ciudadFondo);	
		
		consecutivoDia++;
		return TransaccionesInternasDTO.CONVERTER_DTO.apply(transaccionesInternasService.saveTransaccionesInternasById(transaccionInternaDTO));
	}
}
