package com.ath.adminefectivo.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.FechasConciliacionDTO;
import com.ath.adminefectivo.dto.OperacionesCertificadasDTO;
import com.ath.adminefectivo.dto.compuestos.RegistroTipo1ArchivosFondosDTO;
import com.ath.adminefectivo.dto.compuestos.SobrantesFaltantesDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.ArchivosCargados;
import com.ath.adminefectivo.entities.Fondos;
import com.ath.adminefectivo.entities.OperacionesCertificadas;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.IOperacionesCertificadasRepository;
import com.ath.adminefectivo.service.IBancosService;
import com.ath.adminefectivo.service.ICajerosService;
import com.ath.adminefectivo.service.IClientesCorporativosService;
import com.ath.adminefectivo.service.IDominioService;
import com.ath.adminefectivo.service.IFondosService;
import com.ath.adminefectivo.service.IOficinasService;
import com.ath.adminefectivo.service.IOperacionesCertificadasService;
import com.ath.adminefectivo.service.IPuntosCodigoTdvService;
import com.ath.adminefectivo.service.IPuntosService;
import com.ath.adminefectivo.service.ITransportadorasService;

@Service
public class OperacionesCertificadasServiceImpl implements IOperacionesCertificadasService {

	@Autowired
	IOperacionesCertificadasRepository operacionesCertificadasRepository;
	
	@Autowired
	IFondosService fondosService;
	
	@Autowired
	ITransportadorasService transportadoraService;
	
	@Autowired
	IDominioService dominioService;
	
	@Autowired
	IPuntosCodigoTdvService puntosCodigoTdvService;
	
	@Autowired
	IOficinasService oficinaService;
	
	@Autowired
	IClientesCorporativosService clientesCorporativosService;
	
	@Autowired
	ICajerosService cajerosService;
	
	@Autowired
	IBancosService bancosService;
	
	@Autowired
	IPuntosService puntosService;
	
	private List<SobrantesFaltantesDTO> listaAjustesValor = new ArrayList<>();

	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public Boolean actualizarEstadoEnCertificadas(Integer idCertificacion, String estado) {

		Optional<OperacionesCertificadas> operaciones = operacionesCertificadasRepository.findById(idCertificacion);
		if (operaciones.isPresent()) {
			try {
				operaciones.get().setEstadoConciliacion(estado);
				operaciones.get().setIdCertificacion(idCertificacion);
				operaciones.get().setFechaModificacion(new Date());
				operaciones.get().setUsuarioModificacion("user1");
				operacionesCertificadasRepository.save(operaciones.get());
			} catch (Exception e) {
				e.getMessage();
			}
			
		} else {
			throw new NegocioException(ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getHttpStatus());
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer numeroOperacionesPorEstadoyFecha(FechasConciliacionDTO fechaConciliacion, String estado) {
		return operacionesCertificadasRepository.countByEstadoConciliacionAndFechaEjecucionBetween(estado,
				fechaConciliacion.getFechaConciliacionInicial(), fechaConciliacion.getFechaConciliacionFinal());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean procesarArchivosCertificaciones(List<ArchivosCargados> archivosCargados) {
		
		for (ArchivosCargados elemento : archivosCargados) {
			if(elemento.getIdModeloArchivo().equals(Constantes.NOMBRE_ARCHIVO_OTROS_FONDOS)) {
				procesarArchivoOtrosFondos(elemento);
				actualizarValorAjusteOtrosFondos();
			}
			if(elemento.getIdModeloArchivo().equals(Constantes.NOMBRE_ARCHIVO_BRINKS)) {
				procesarArchivoBrinks(elemento);
				actualizarValorAjusteBrinks();
			}
		}
		return true;
	}

	/**
	 * Metodo encargado de procesar los archivos de otros fondos (no Brinks)
	 * @param elemento
	 * @author cesar.castano
	 */
	private void procesarArchivoOtrosFondos(ArchivosCargados elemento) {
		
		var registro = new RegistroTipo1ArchivosFondosDTO();
		var ajusteValor = new SobrantesFaltantesDTO();
		
		for (var i = 0; i < elemento.getRegistrosCargados().size(); i++) {

			String[] fila = elemento.getRegistrosCargados().get(i).getContenido().split(", ");
			switch (fila[0]) {
			case "01": {
				Fondos fondo = asignarFondo(fila[7].trim(), fila[9].trim(), fila[1].trim());
				registro.setTdv(fondo.getTdv());
				registro.setCodigoPunto(fondo.getCodigoPunto());
				Date fechaEjecucion = asignarFecha(fila[6]);
				registro.setFechaEjecucion(fechaEjecucion);
				break;
			}
			case "02": {
				break;
			}
			case "03": {
					procesarOperacionTransporte(fila, registro, elemento, fila[7].trim(), fila[12].trim(), 
													fila[14].trim(), fila[5].trim(), 
													Constantes.NUMERO_INICIA_VALORES_OTROS_FONDOS);
				break;
			}
			case "04": {
				break;
			}
			case "05": {
				guardarSobrantesyFaltantes(fila[1].trim(), fila[5].trim(), fila[9].trim(), ajusteValor);
				break;
			}
			default:
				throw new NegocioException(ApiResponseCode.ERROR_TIPO_REGISTRO_NO_VALIDO.getCode(),
						ApiResponseCode.ERROR_TIPO_REGISTRO_NO_VALIDO.getDescription(),
						ApiResponseCode.ERROR_TIPO_REGISTRO_NO_VALIDO.getHttpStatus());
			}
		}
	}

	/**
	 * Metodo encargado de procesar las operaciones del registro tipo 03
	 * @param fila
	 * @param registro
	 * @param elemento
	 * @param entradaSalida
	 * @param codigoServicio
	 * @param tipoServicio
	 * @param codigoPropio
	 * @param numeroInicia
	 * @author cesar.castano
	 */
	private void procesarOperacionTransporte(String[] fila, RegistroTipo1ArchivosFondosDTO registro, 
											ArchivosCargados elemento, String entradaSalida, 
											String codigoServicio, String tipoServicio, String codigoPropio, 
											Integer numeroInicia) {

		var operaciones = new OperacionesCertificadasDTO();
		operaciones.setCodigoFondoTDV(registro.getCodigoPunto());
		operaciones.setCodigoPuntoDestino(asignarCodigoPuntoDestino(entradaSalida, registro.getTdv(), 
											registro.getCodigoPunto(), elemento.getIdModeloArchivo(), 
											codigoPropio));
		operaciones.setCodigoPuntoOrigen(asignarCodigoPuntoOrigen(entradaSalida, registro.getTdv(), 
											registro.getCodigoPunto(), elemento.getIdModeloArchivo(), 
											codigoPropio));
		operaciones.setCodigoServicioTdv(codigoServicio);
		operaciones.setConciliable(null);
		operaciones.setEntradaSalida(asignarEntradaSalida(entradaSalida, elemento.getIdModeloArchivo()));
		operaciones.setEstadoConciliacion(dominioService.valorTextoDominio(
						Constantes.DOMINIO_ESTADO_CONCILIACION,
						Dominios.ESTADO_CONCILIACION_NO_CONCILIADO));
		operaciones.setFallidaOficina(null);
		operaciones.setFechaCreacion(new Date());
		operaciones.setFechaEjecucion(registro.getFechaEjecucion());
		operaciones.setFechaModificacion(new Date());
		operaciones.setIdArchivoCargado(elemento.getIdArchivo());
		operaciones.setTipoOperacion(asignarTipoOperacion(entradaSalida, codigoPropio, registro.getTdv(), 
									elemento.getIdModeloArchivo()));
		operaciones.setTipoServicio(dominioService.valorTextoDominio(
									Constantes.DOMINIO_TIPO_SERVICIO, tipoServicio));
		operaciones.setUsuarioCreacion("user1");
		operaciones.setUsuarioModificacion("user1");
		operaciones.setValorFaltante(0.0);
		operaciones.setValorSobrante(0.0);
		Integer longitud = 0;
		if (elemento.getIdModeloArchivo().equals(Constantes.NOMBRE_ARCHIVO_OTROS_FONDOS)) {
			longitud = fila.length;
		}else {
			longitud = fila.length - 1;
		}
		operaciones.setValorTotal(asignarValorTotal(fila, numeroInicia, longitud));
		operacionesCertificadasRepository.save(OperacionesCertificadasDTO.CONVERTER_ENTITY.apply(operaciones));
	}

	/**
	 * Metodo encargado de asignar la entrada o la salida
	 * @param entSal
	 * @param idModeloArchivo
	 * @return String
	 * @author cesar.castano
	 */
	private String asignarEntradaSalida(String entSal, String idModeloArchivo) {
		var entradaSalida = "";
		if (idModeloArchivo.equals(Constantes.NOMBRE_ARCHIVO_OTROS_FONDOS)) {
			if (entSal.equals("1")) {
				entradaSalida = Constantes.ENTRADA;
			}
			if (entSal.equals("2")) {
				entradaSalida = Constantes.SALIDA;
			}
		}else {
			if (entSal.equals("0")) {
				entradaSalida = Constantes.ENTRADA;
			}
			if (entSal.equals("1")) {
				entradaSalida = Constantes.SALIDA;
			}
		}
		return entradaSalida;
	}

	/**
	 * Metodo encargado de asignar el codigo fondo TDV del registro tipo 01
	 * @param codTransp
	 * @param nombreBanco
	 * @param codigoCiudad
	 * @return Fondos
	 * @author cesar.castano
	 */
	private Fondos asignarFondo(String codTransp, String nombreBanco, String codigoCiudad) {
		var fondo = fondosService.getCodigoFondoCertificacion(
									codTransp, 
									dominioService.valorTextoDominio(
											Constantes.DOMINIO_TIPOS_PUNTO, 
											Dominios.TIPOS_PUNTO_BANCO),
									nombreBanco, 
									codigoCiudad);
		if(Objects.isNull(fondo)) {
			throw new NegocioException(ApiResponseCode.ERROR_FONDOS_NO_ENCONTRADO.getCode(),
						ApiResponseCode.ERROR_FONDOS_NO_ENCONTRADO.getDescription(),
						ApiResponseCode.ERROR_FONDOS_NO_ENCONTRADO.getHttpStatus());
		}
		return fondo;
	}
	
	/**
     * Metodo encargado de asignar la fecha
     * @param fila
     * @return Date
     * @author cesar.castano
     */
	private Date asignarFecha(String fila) {
		Date fecha = null;
		try {
			DateFormat formato = new SimpleDateFormat(
									dominioService.valorTextoDominio(
									Constantes.DOMINIO_FORMATO_FECHA_F1,
									Dominios.FORMATO_FECHA_F1));
			fecha = formato.parse(fila);
		} catch (ParseException e) {
			e.getMessage();
		}
		return fecha;
	}
	
	/**
	 * Metodo encargado de asignar el codigoPuntoOrigen
	 * @param entradaSalida
	 * @param tdv
	 * @param codigoPunto
	 * @param idModeloArchivo
	 * @param codigoPropio
	 * @return Integer
	 * @author cesar.castano
	 */
	private Integer asignarCodigoPuntoOrigen(String entradaSalida, String tdv, Integer codigoPunto, 
											String idModeloArchivo, String codigoPropio) {
		Integer codigoPuntoOrigen = 0;
		if (asignarEntradaSalida(entradaSalida, idModeloArchivo).equals(Constantes.ENTRADA)) {
			codigoPuntoOrigen = puntosCodigoTdvService.getCodigoPunto(codigoPropio, tdv);

		}else {
			if (asignarEntradaSalida(entradaSalida, idModeloArchivo).equals(Constantes.SALIDA)) {
				codigoPuntoOrigen = codigoPunto;
			}
		}
		return codigoPuntoOrigen;
	}
	
	/**
	 * Metodo encargado de asignar el codigoPuntoDestino
	 * @param entradaSalida
	 * @param tdv
	 * @param codigoPunto
	 * @param idModeloArchivo
	 * @param codigoPropio
	 * @return Integer
	 * @author cesar.castano
	 */
	private Integer asignarCodigoPuntoDestino(String entradaSalida, String tdv, Integer codigoPunto, 
											String idModeloArchivo, String codigoPropio) {
		Integer codigoPuntoDestino = 0;
		if (asignarEntradaSalida(entradaSalida, idModeloArchivo).equals(Constantes.ENTRADA)) {
			codigoPuntoDestino = codigoPunto;
		}else {
			if (asignarEntradaSalida(entradaSalida, idModeloArchivo).equals(Constantes.SALIDA)) {
				codigoPuntoDestino = puntosCodigoTdvService.getCodigoPunto(codigoPropio, tdv);
			}
		}
		return codigoPuntoDestino;
	}
	
	/**
	 * Metodo encargado de asignar el tipo de operacion
	 * @param entradaSalida
	 * @param codigoPropio
	 * @param tdv
	 * @param idModeloArchivo
	 * @return String
	 * @author cesar.castano
	 */
	private String asignarTipoOperacion(String entradaSalida, String codigoPropio, String tdv, 
										String idModeloArchivo ) {
		
		Integer codigoPunto = 0;
		var tipoOperacion = "";
		codigoPunto = puntosCodigoTdvService.getCodigoPunto(codigoPropio, tdv);
		if (asignarEntradaSalida(entradaSalida, idModeloArchivo).equals(Constantes.SALIDA)) {
			tipoOperacion = procesarProvisiones(codigoPunto);
			if (tipoOperacion.isEmpty()) {
				tipoOperacion = procesarConsignaciones(codigoPunto);
			}
		}else {
			if (asignarEntradaSalida(entradaSalida, idModeloArchivo).equals(Constantes.ENTRADA)) {
				tipoOperacion = procesarRecolleciones(codigoPunto);
				if (tipoOperacion.isEmpty()) {
					tipoOperacion = procesarRetiros(codigoPunto);
				}
			}
		}
		if (tipoOperacion.isEmpty()) {
			if(Boolean.TRUE.equals(fondosService.getCodigoPuntoFondo(codigoPunto))) {
				tipoOperacion = dominioService.valorTextoDominio(
								Constantes.DOMINIO_TIPO_OPERACION,
								Dominios.TIPO_OPERA_TRASLADO);
			}else if(Boolean.TRUE.equals(bancosService.getCodigoPunto(codigoPunto))) {
				tipoOperacion = dominioService.valorTextoDominio(
								Constantes.DOMINIO_TIPO_OPERACION,
								Dominios.TIPO_OPERA_VENTA);
			}
		}
		return tipoOperacion;
	}
	
	/**
	 * Metodo que asigna las provisiones
	 * @param codigoPunto
	 * @return String
	 * @author cesar.castano
	 */
	private String procesarProvisiones(Integer codigoPunto) {
		var tipoOperacion = "";
		if (Boolean.TRUE.equals(oficinaService.getCodigoPuntoOficina(codigoPunto)) ||
			Boolean.TRUE.equals(clientesCorporativosService.getCodigoPuntoCliente(codigoPunto)) ||
			Boolean.TRUE.equals(cajerosService.getCodigoPuntoCajero(codigoPunto))) {
			tipoOperacion = dominioService.valorTextoDominio(
					Constantes.DOMINIO_TIPO_OPERACION,
					Dominios.TIPO_OPERA_PROVISION);
		}
		return tipoOperacion;
	}
	
	/**
	 * Metodo que asigna las recolecciones
	 * @param codigoPunto
	 * @return String
	 * @author cesar.castano
	 */
	private String procesarRecolleciones(Integer codigoPunto) {
		var tipoOperacion = "";
		if (Boolean.TRUE.equals(oficinaService.getCodigoPuntoOficina(codigoPunto)) ||
			Boolean.TRUE.equals(clientesCorporativosService.getCodigoPuntoCliente(codigoPunto)) ||
			Boolean.TRUE.equals(cajerosService.getCodigoPuntoCajero(codigoPunto))) {
			tipoOperacion = dominioService.valorTextoDominio(
						Constantes.DOMINIO_TIPO_OPERACION,
						Dominios.TIPO_OPERA_RECOLECCION);
		}
		return tipoOperacion;
	}
	
	/**
	 * Metodo que asigna las consignaciones
	 * @param codigoPunto
	 * @return String
	 * @author cesar.castano
	 */
	private String procesarConsignaciones(Integer codigoPunto) {
		var tipoOperacion = "";
		if(Boolean.TRUE.equals(puntosService.getEntidadPuntoBanrep(
								dominioService.valorTextoDominio(
								Constantes.DOMINIO_TIPOS_PUNTO,
								Dominios.TIPOS_PUNTO_BAN_REP), 
								codigoPunto))) {
			tipoOperacion = dominioService.valorTextoDominio(
								Constantes.DOMINIO_TIPO_OPERACION,
								Dominios.TIPO_OPERA_CONSIGNACION);
		}
		return tipoOperacion;
	}
	
	/**
	 * Metodo que asigna los retiros
	 * @param codigoPunto
	 * @return String
	 * @author cesar.castano
	 */
	private String procesarRetiros(Integer codigoPunto) {
		var tipoOperacion = "";
		if(Boolean.TRUE.equals(puntosService.getEntidadPuntoBanrep(
				dominioService.valorTextoDominio(
				Constantes.DOMINIO_TIPOS_PUNTO,
				Dominios.TIPOS_PUNTO_BAN_REP), 
				codigoPunto))) {
			tipoOperacion = dominioService.valorTextoDominio(
								Constantes.DOMINIO_TIPO_OPERACION,
								Dominios.TIPO_OPERA_RETIRO);
		}
		return tipoOperacion;
	}
		
	/**
     * Metodo encargado de asignar el valor del detalle
     * @param fila
     * @param numeroInicia
     * @return Integer
     * @author cesar.castano
     */
	private Double asignarValorTotal(String[] fila, Integer numeroInicia, Integer longitud) {

		Double valorAcumulado = 0.0;
		for (var i = numeroInicia; i < longitud; i=i+2) {
			valorAcumulado = valorAcumulado + 
							(Double.parseDouble(fila[i].trim()) * Double.parseDouble(fila[i+1].trim()));
		}
		return valorAcumulado;
	}
	
	/**
	 * Metodo encargado de guardar los valores sobrantes y faltantes en una lista
	 * @param tipoAjuste
	 * @param codigoServicio
	 * @param valor
	 * @param ajusteValor
	 * @author cesar.castano
	 */
	private void guardarSobrantesyFaltantes(String tipoAjuste, String codigoServicio, String valor, 
											SobrantesFaltantesDTO ajusteValor) {
		ajusteValor.setTipoAjuste(tipoAjuste);
		ajusteValor.setCodigoServicio(codigoServicio);
		ajusteValor.setValor(Double.parseDouble(valor));
		listaAjustesValor.add(ajusteValor);
	}
	
	/**
	 * Metodo encargado de actualizar los valores faltantes y sobrantes del registro tipo 05
	 * Otros Fondos
	 * @author cesar.castano
	 */
	private void actualizarValorAjusteOtrosFondos() {

		for (var i=0;i<listaAjustesValor.size();i++) {
			if (listaAjustesValor.get(i).getTipoAjuste().equals(Constantes.SOBRANTE_OTROS_FONDOS)){
				actualizarValorSobrante(listaAjustesValor.get(i).getCodigoServicio(), 
						listaAjustesValor.get(i).getValor());
			}else {
				if (listaAjustesValor.get(i).getTipoAjuste().equals(Constantes.FALTANTE_OTROS_FONDOS)){
				actualizarValorFaltante(listaAjustesValor.get(i).getCodigoServicio(), 
						listaAjustesValor.get(i).getValor());
				}
			}
		}
	}

	/**
	 * Metodo encargado de actualizar los valores faltantes y sobrantes del registro tipo 05
	 * Brinks
	 * @author cesar.castano
	 */
	private void actualizarValorAjusteBrinks() {
		for (var i=0;i<listaAjustesValor.size();i++) {
			if (listaAjustesValor.get(i).getTipoAjuste().equals(Constantes.SOBRANTE_BRINKS)){
				actualizarValorSobrante(listaAjustesValor.get(i).getCodigoServicio(), 
						listaAjustesValor.get(i).getValor());
			}else {
				if (listaAjustesValor.get(i).getTipoAjuste().equals(Constantes.FALTANTE_BRINKS)){
				actualizarValorFaltante(listaAjustesValor.get(i).getCodigoServicio(), 
						listaAjustesValor.get(i).getValor());
				}
			}
		}
	}

	/**
	 * Metodo encargado de actualizar los valores sobrantes del registro 05
	 * @param codigoServicio
	 * @param valor
	 * @author cesar.castano
	 */
	private void actualizarValorSobrante(String codigoServicio, Double valor) {
		var certificadas = operacionesCertificadasRepository.findByCodigoServicioTdv(
												codigoServicio);
		if (certificadas == null) {
			throw new NegocioException(ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getHttpStatus());
		}else {
			certificadas.setValorSobrante(valor);
			operacionesCertificadasRepository.save(certificadas);
		}
	}
	
	/**
	 * Metodo encargado de actualizar los valores faltantes del registro 05
	 * @param codigoServicio
	 * @param valor
	 * @author cesar.castano
	 */
	private void actualizarValorFaltante(String codigoServicio, Double valor) {
		var certificadas = operacionesCertificadasRepository.findByCodigoServicioTdv(
				codigoServicio);
		if (certificadas == null) {
			throw new NegocioException(ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getHttpStatus());
		}else {
			certificadas.setValorFaltante(valor);
			operacionesCertificadasRepository.save(certificadas);
		}
	}

	/**
	 * Metodo encargado de procesar los archivos de la Brinks
	 * @param elemento
	 * @author cesar.castano
	 */
	private void procesarArchivoBrinks(ArchivosCargados elemento) {

		var registro = new RegistroTipo1ArchivosFondosDTO();
		var ajusteValor = new SobrantesFaltantesDTO();
		
		for (var i = 0; i < elemento.getRegistrosCargados().size(); i++) {

			String[] fila = elemento.getRegistrosCargados().get(i).getContenido().split(", ");
			switch (fila[0]) {
			case "01": {
				Fondos fondo = asignarFondo(fila[9].trim(), fila[11].trim(), fila[3].trim());
				registro.setTdv(fondo.getTdv());
				registro.setCodigoPunto(fondo.getCodigoPunto());
				Date fechaEjecucion = asignarFecha(fila[4]);
				registro.setFechaEjecucion(fechaEjecucion);
				break;
			}
			case "02": {
				break;
			}
			case "03": {
					procesarOperacionTransporte(fila, registro, elemento, fila[9].trim(), fila[13].trim(), 
													fila[15].trim(), fila[7].trim(), 
													Constantes.NUMERO_INICIA_VALORES_BRINKS);
				break;
			}
			case "04": {
				break;
			}
			case "05": {
				guardarSobrantesyFaltantes(fila[3].trim(), fila[7].trim(), fila[15].trim(), ajusteValor);
				break;
			}
			default:
				throw new NegocioException(ApiResponseCode.ERROR_TIPO_REGISTRO_NO_VALIDO.getCode(),
						ApiResponseCode.ERROR_TIPO_REGISTRO_NO_VALIDO.getDescription(),
						ApiResponseCode.ERROR_TIPO_REGISTRO_NO_VALIDO.getHttpStatus());
			}
		}
	}
}
