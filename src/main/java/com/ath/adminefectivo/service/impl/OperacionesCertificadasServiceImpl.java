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
import com.ath.adminefectivo.dto.DetallesDefinicionArchivoDTO;
import com.ath.adminefectivo.dto.FechasConciliacionDTO;
import com.ath.adminefectivo.dto.OperacionesCertificadasDTO;
import com.ath.adminefectivo.dto.compuestos.CodigoPuntoOrigenDestinoDTO;
import com.ath.adminefectivo.dto.compuestos.RegistroTipo1ArchivosFondosDTO;
import com.ath.adminefectivo.dto.compuestos.SobrantesFaltantesDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.ArchivosCargados;
import com.ath.adminefectivo.entities.Fondos;
import com.ath.adminefectivo.entities.OperacionesCertificadas;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.IOperacionesCertificadasRepository;
import com.ath.adminefectivo.service.IArchivosCargadosService;
import com.ath.adminefectivo.service.IBancosService;
import com.ath.adminefectivo.service.ICajerosService;
import com.ath.adminefectivo.service.IClientesCorporativosService;
import com.ath.adminefectivo.service.IDetalleDefinicionArchivoService;
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
	
	@Autowired
	IDetalleDefinicionArchivoService detalleDefinicionArchivoService;
	
	@Autowired
	IArchivosCargadosService archivosCargadosService;
	
	private List<SobrantesFaltantesDTO> listaAjustesValor = new ArrayList<>();
	private OperacionesCertificadas certificadas;
	private List<OperacionesCertificadas> operacionesc;

	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public Boolean actualizarEstadoEnCertificadas(Integer idCertificacion, String estado) {

		Optional<OperacionesCertificadas> operaciones = operacionesCertificadasRepository.findById(
										idCertificacion);
		if (operaciones.isPresent()) {
			try {
				operaciones.get().setEstadoConciliacion(estado);
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
		 Integer cuentaCertificadas = operacionesCertificadasRepository
				 					.countByEstadoConciliacionAndFechaEjecucionBetween(estado,
				fechaConciliacion.getFechaConciliacionInicial(), fechaConciliacion.getFechaConciliacionFinal());
		if(Objects.isNull(cuentaCertificadas)) {
			throw new NegocioException(ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getHttpStatus());
		}
		return cuentaCertificadas;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean procesarArchivosCertificaciones(List<ArchivosCargados> archivosCargados) {
		
		for (ArchivosCargados elemento : archivosCargados) {
			List<DetallesDefinicionArchivoDTO> listadoDetalleArchivo = detalleDefinicionArchivoService
					.consultarDetalleDefinicionArchivoByIdMaestro(elemento.getIdModeloArchivo());
			if(elemento.getIdModeloArchivo().equals(Dominios.TIPO_ARCHIVO_IBBCS) || 
			   elemento.getIdModeloArchivo().equals(Dominios.TIPO_ARCHIVO_IBMCS)) {
				procesarArchivoBrinks(elemento, listadoDetalleArchivo);
				procesarSobranteFaltanteBrinks();
			}else {
				procesarArchivoOtrosFondos(elemento, listadoDetalleArchivo);
				procesarSobranteFaltanteNoBrinks();
			}
			elemento.setEstadoCargue(Dominios.ESTADO_VALIDACION_ACEPTADO);
			archivosCargadosService.actualizarArchivosCargados(elemento);
		}
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<OperacionesCertificadas> obtenerOperacionesCertificaciones() {
		operacionesc = operacionesCertificadasRepository.conciliacionAutomatica(
				dominioService.valorTextoDominio(Constantes.DOMINIO_ESTADO_CONCILIACION,
						Dominios.ESTADO_CONCILIACION_NO_CONCILIADO));
		if (Objects.isNull(operacionesc)) {
			throw new NegocioException(ApiResponseCode.ERROR_OPERACIONES_A_CONCILIAR_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_OPERACIONES_A_CONCILIAR_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_OPERACIONES_A_CONCILIAR_NO_ENCONTRADO.getHttpStatus());
		}
		return operacionesc;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public OperacionesCertificadas obtenerEntidadOperacionesCertificacionesporId(
													Integer idCertificacion) {
		OperacionesCertificadas operacionesC = operacionesCertificadasRepository.findById(idCertificacion).get();
		if (Objects.isNull(operacionesC)) {
			throw new NegocioException(ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getHttpStatus());
		}
		return operacionesC;
	}

	/**
	 * Metodo encargado de procesar los archivos de otros fondos (no Brinks)
	 * @param elemento
	 * @author cesar.castano
	 */
	@Transactional
	private void procesarArchivoOtrosFondos(ArchivosCargados elemento, 
										List<DetallesDefinicionArchivoDTO> detalleArchivo) {
		
		var registro = new RegistroTipo1ArchivosFondosDTO();
		
		for (var i = 0; i < elemento.getRegistrosCargados().size(); i++) {
			
			var ajusteValor = new SobrantesFaltantesDTO();
			String[] fila = elemento.getRegistrosCargados().get(i).getContenido().split(", ");
			String tipoRegistro = determinarTipoRegistro(fila, detalleArchivo);
			switch (Integer.parseInt(tipoRegistro)) {
			case 1: {
				procesarRegistroTipo1(fila, detalleArchivo, Integer.parseInt(tipoRegistro), registro);
				break;
			}
			case 2: {
				break;
			}
			case 3: {
				procesarRegistroTipo3(fila, detalleArchivo, Integer.parseInt(tipoRegistro), registro, 
										elemento);
				break;
			}
			case 4: {
				break;
			}
			case 5: {
				procesarRegistroTipo5(fila, detalleArchivo, Integer.parseInt(tipoRegistro), ajusteValor, elemento.getFechaArchivo());
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
	 * Metodo encargado de procesar el tipo de registro 5 de los archivos No Brinks
	 * @param fila
	 * @param detalleArchivo
	 * @param tipoRegistro
	 * @param registro
	 * @author cesar.castano
	 */
	private void procesarRegistroTipo5(String[] fila, 
										List<DetallesDefinicionArchivoDTO> detalleArchivo, 
										Integer tipoRegistro,
										SobrantesFaltantesDTO ajusteValor,
										Date fecha) {
		String tipoAjuste = determinarCampo(fila, detalleArchivo, 
										tipoRegistro,
										Constantes.CAMPO_DETALLE_ARCHIVO_TIPONOVEDAD);
		String codigoPunto = determinarCampo(fila, detalleArchivo, 
										tipoRegistro,
										Constantes.CAMPO_DETALLE_ARCHIVO_CODIGOPUNTO_TIPO_5);
		String montoTotal = determinarCampo(fila, detalleArchivo, 
										tipoRegistro,
										Constantes.CAMPO_DETALLE_ARCHIVO_MONTOTOTAL);
		
		guardarSobrantesyFaltantes(tipoAjuste, codigoPunto, 
									Double.parseDouble(montoTotal), ajusteValor, fecha);
	}

	/**
	 * Metodo encargado de procesar el tipo de registro 3 de los archivos No Brinks
	 * @param fila
	 * @param detalleArchivo
	 * @param tipoRegistro
	 * @param registro
	 * @author cesar.castano
	 */
	private void procesarRegistroTipo3(String[] fila, 
										List<DetallesDefinicionArchivoDTO> detalleArchivo, 
										Integer tipoRegistro,
										RegistroTipo1ArchivosFondosDTO registro,
										ArchivosCargados elemento) {
		String codigoServicio = determinarCampo(fila, detalleArchivo, 
												tipoRegistro,
												Constantes.CAMPO_DETALLE_ARCHIVO_CODIGOSERVICIO);
		String entradaSalida = determinarCampo(fila, detalleArchivo, 
												tipoRegistro,
												Constantes.CAMPO_DETALLE_ARCHIVO_ENTRADASALIDA);
		String codigoPunto = determinarCampo(fila, detalleArchivo, 
												tipoRegistro,
												Constantes.CAMPO_DETALLE_ARCHIVO_CODIGOPUNTO);
		String tipoServicio = determinarCampo(fila, detalleArchivo, 
												tipoRegistro,
												Constantes.CAMPO_DETALLE_ARCHIVO_TIPOSERVICIOF);
		procesarOperacionTransporte(fila, registro, elemento, codigoServicio, 
									entradaSalida.toUpperCase(), 
									codigoPunto, tipoServicio);
	}

	/**
	 * Metodo encargado de procesar el tipo de registro 1 de los archivos No Brinks
	 * @param fila
	 * @param detalleArchivo
	 * @param tipoRegistro
	 * @param registro
	 * @author cesar.castano
	 */
	private void procesarRegistroTipo1(String[] fila, 
										List<DetallesDefinicionArchivoDTO> detalleArchivo, 
										Integer tipoRegistro, 
										RegistroTipo1ArchivosFondosDTO registro) {
		String tdv = determinarCampo(fila, detalleArchivo, tipoRegistro,
										Constantes.CAMPO_DETALLE_ARCHIVO_SIGLATDV);
		String nit = determinarCampo(fila, detalleArchivo, tipoRegistro,
										Constantes.CAMPO_DETALLE_ARCHIVO_NITBANCO);
		String nitbanco = nit.substring(0, 9);
		String ciudad = determinarCampo(fila, detalleArchivo, tipoRegistro,
										Constantes.CAMPO_DETALLE_ARCHIVO_CODIGOCIUDAD);
		String ciudad2 = ciudad.valueOf(Integer.parseInt(ciudad.trim()));
		Fondos fondo = asignarFondo(tdv, nitbanco, ciudad2);
		registro.setTdv(fondo.getTdv());
		registro.setCodigoPunto(fondo.getCodigoPunto());
		Date fecha = determinarFechaEjecucion(fila, detalleArchivo, tipoRegistro,
										Constantes.CAMPO_DETALLE_ARCHIVO_FECHAEJECUCION);
		registro.setFechaEjecucion(fecha);
		registro.setBanco_aval(fondo.getBancoAVAL());
	}

	/**
	 * Metodo encargado de procesar las operaciones del registro tipo 03
	 * @param fila
	 * @param registro
	 * @param elemento
	 * @author cesar.castano
	 */
	private void procesarOperacionTransporte(String[] fila, RegistroTipo1ArchivosFondosDTO registro, 
											ArchivosCargados elemento, String codigoServicio, 
											String entradaSalida, String codigoPropio, String tipoServicio) {

		CodigoPuntoOrigenDestinoDTO codigoPuntoOrigenDestino;
		Integer longitud = 0;
		if ((elemento.getIdModeloArchivo().equals(Dominios.TIPO_ARCHIVO_ITVCS)) ||
			(elemento.getIdModeloArchivo().equals(Dominios.TIPO_ARCHIVO_IATCS)) ||
			(elemento.getIdModeloArchivo().equals(Dominios.TIPO_ARCHIVO_IPRCS))) {
			
			codigoPuntoOrigenDestino = obtenerCodigoPuntoOrigenDestino(
							entradaSalida, registro, codigoPropio, codigoServicio);
			longitud = fila.length;

		}else {
			codigoPuntoOrigenDestino = obtenerCodigoPuntoOrigenDestino(
							entradaSalida, registro, codigoPropio, codigoServicio);
			longitud = fila.length - 1;
		}
   		if(Objects.isNull(codigoPuntoOrigenDestino.getCertificadas())){
			var operaciones = new OperacionesCertificadasDTO();
			operaciones.setCodigoFondoTDV(registro.getCodigoPunto());
			operaciones.setCodigoPuntoDestino(codigoPuntoOrigenDestino.getCodigoPuntoDestino());
			operaciones.setCodigoPuntoOrigen(codigoPuntoOrigenDestino.getCodigoPuntoOrigen());
			operaciones.setCodigoServicioTdv(codigoServicio);
			operaciones.setConciliable(null);
			operaciones.setEntradaSalida(asignarEntradaSalida(entradaSalida));
			operaciones.setEstadoConciliacion(dominioService.valorTextoDominio(
											Constantes.DOMINIO_ESTADO_CONCILIACION,
											Dominios.ESTADO_CONCILIACION_NO_CONCILIADO));
			operaciones.setFallidaOficina(null);
			operaciones.setFechaCreacion(new Date());
			operaciones.setFechaEjecucion(registro.getFechaEjecucion());
			operaciones.setFechaModificacion(new Date());
			operaciones.setIdArchivoCargado(elemento.getIdArchivo());
			operaciones.setTipoOperacion(asignarTipoOperacion(entradaSalida, codigoPropio, 
											registro.getTdv(), registro.getBanco_aval()));
			operaciones.setTipoServicio(dominioService.valorTextoDominio(
										Constantes.DOMINIO_TIPO_SERVICIO, tipoServicio));
			operaciones.setUsuarioCreacion("user1");
			operaciones.setUsuarioModificacion("user1");
			operaciones.setValorFaltante(0.0);
			operaciones.setValorSobrante(0.0);
			if ((elemento.getIdModeloArchivo().equals(Dominios.TIPO_ARCHIVO_ITVCS)) ||
					(elemento.getIdModeloArchivo().equals(Dominios.TIPO_ARCHIVO_IATCS)) ||
					(elemento.getIdModeloArchivo().equals(Dominios.TIPO_ARCHIVO_IPRCS))) {
				operaciones.setValorTotal(asignarValorTotal(
												fila, Constantes.INICIA_DENOMINACION_OTROS_FONDOS, longitud));
			}else {
				operaciones.setValorTotal(asignarValorTotal(
												fila, Constantes.INICIA_DENOMINACION_BRINKS, longitud));
			}
			if (!operaciones.getValorTotal().equals(0.0)) {
				operacionesCertificadasRepository.save(OperacionesCertificadasDTO.CONVERTER_ENTITY.apply(operaciones));
			}
		}else {
			if ((elemento.getIdModeloArchivo().equals(Dominios.TIPO_ARCHIVO_ITVCS)) ||
					(elemento.getIdModeloArchivo().equals(Dominios.TIPO_ARCHIVO_IATCS)) ||
					(elemento.getIdModeloArchivo().equals(Dominios.TIPO_ARCHIVO_IPRCS))) {
				certificadas.setValorTotal(certificadas.getValorTotal() + asignarValorTotal(
												fila, Constantes.INICIA_DENOMINACION_OTROS_FONDOS, longitud));
			}else {
				certificadas.setValorTotal(certificadas.getValorTotal() + asignarValorTotal(
												fila, Constantes.INICIA_DENOMINACION_BRINKS, longitud));
			}
			if (!certificadas.getValorTotal().equals(0.0)) {
				operacionesCertificadasRepository.save(certificadas);
			}
		}
	}

	/**
	 * Metodo encargado de obtener el codigo Punto Destino y Origen
	 * @param entradaSalida
	 * @param codigoPunto
	 * @param codigoPropio
	 * @param tdv
	 * @param codigoServicio
	 * @return CodigoPuntoOrigenDestinoDTO
	 */
	private CodigoPuntoOrigenDestinoDTO obtenerCodigoPuntoOrigenDestino(
							String entradaSalida, RegistroTipo1ArchivosFondosDTO registro,
							String codigoPropio, String codigoServicio) {
		var codigoPuntoOrigenDestino = new CodigoPuntoOrigenDestinoDTO();
		Integer codigoPuntoOrigen = 0;
		Integer codigoPuntoDestino = 0;
		if(asignarEntradaSalida(entradaSalida).equals(Constantes.NOMBRE_ENTRADA)) {
			codigoPuntoDestino = registro.getCodigoPunto();
			codigoPuntoOrigen = puntosCodigoTdvService.getCodigoPunto(codigoPropio, registro.getTdv(), 
														registro.getBanco_aval());
			certificadas = operacionesCertificadasRepository.
					findByCodigoPuntoOrigenAndCodigoServicioTdvAndEntradaSalidaAndFechaEjecucion(
							codigoPuntoOrigen, codigoServicio, Constantes.NOMBRE_ENTRADA, 
							registro.getFechaEjecucion());
		}else {
			codigoPuntoOrigen = registro.getCodigoPunto();
			codigoPuntoDestino = puntosCodigoTdvService.getCodigoPunto(codigoPropio, registro.getTdv(), 
														registro.getBanco_aval());
			certificadas = operacionesCertificadasRepository.
					findByCodigoPuntoDestinoAndCodigoServicioTdvAndEntradaSalidaAndFechaEjecucion(
							codigoPuntoDestino, codigoServicio, Constantes.NOMBRE_SALIDA, 
							registro.getFechaEjecucion());
		}
		codigoPuntoOrigenDestino.setCertificadas(certificadas);
		codigoPuntoOrigenDestino.setCodigoPuntoDestino(codigoPuntoDestino);
		codigoPuntoOrigenDestino.setCodigoPuntoOrigen(codigoPuntoOrigen);
		return codigoPuntoOrigenDestino;
	}

	/**
	 * Metodo encargado de obtener la entrada o la salida
	 * @param entSal
	 * @return String
	 * @author cesar.castano
	 */
	private String asignarEntradaSalida(String entSal) {
		var entradaSalida = "";
		if (entSal.equals(Constantes.ENTRADA)) {
			entradaSalida = Constantes.NOMBRE_ENTRADA;
		}
		if (entSal.equals(Constantes.SALIDA)) {
			entradaSalida = Constantes.NOMBRE_SALIDA;
		}
		if (entSal.equals(Constantes.SALIDA_BRINKS)) {
			entradaSalida = Constantes.NOMBRE_SALIDA;
		}
		return entradaSalida;
	}

	/**
	 * Metodo encargado de asignar el codigo fondo TDV del registro tipo 01
	 * @param fila
	 * @return Fondos
	 * @author cesar.castano
	 */
	private Fondos asignarFondo(String transportadora, String nit, String ciudad) {
		if(transportadora.equals("SEG")) {
			transportadora = "TVS";
		}
		var fondo = fondosService.getCodigoFondoCertificacion(transportadora, nit, ciudad);
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
	 * Metodo encargado de asignar el tipo de operacion
	 * @param fila
	 * @param tdv
	 * @return String
	 * @author cesar.castano
	 */
	private String asignarTipoOperacion(String entradaSalida, String codigoPropio, 
												String tdv, Integer banco_aval) {
		
		Integer codigoPunto = 0;
		var tipoOperacion = "";
		codigoPunto = puntosCodigoTdvService.getCodigoPunto(codigoPropio, tdv, banco_aval);
		if (asignarEntradaSalida(entradaSalida).equals(Constantes.NOMBRE_SALIDA)) {
			tipoOperacion = procesarProvisiones(codigoPunto);
			if (tipoOperacion.isEmpty()) {
				tipoOperacion = procesarConsignaciones(codigoPunto);
			}
		}else {
			if (asignarEntradaSalida(entradaSalida).equals(Constantes.NOMBRE_ENTRADA)) {
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
	 * Metodo que asignar las provisiones
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
	 * Metodo que asignar las recolecciones
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
	 * Metodo que asignar las consignaciones
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
     * @param longitud
     * @return Double
     * @author cesar.castano
     */
	private Double asignarValorTotal(String[] fila, Integer numeroInicia, Integer longitud) {
		Double valorAcumulado = 0.0;
		for (var i = numeroInicia; i < longitud; i=i+2) {
			
			Double denonimacion = Double.parseDouble(fila[i].trim());
			if(denonimacion >= 50 || numeroInicia.compareTo(Constantes.INICIA_DENOMINACION_BRINKS) != 0) {
				valorAcumulado = valorAcumulado + 
						(denonimacion * Double.parseDouble(fila[i+1].trim()));
			}
			
		}
		return valorAcumulado;
	}
	
	/**
	 * Metodo encargado de guardar los valores sobrantes y faltantes en una lista
	 * @param fila
	 * @param ajusteValor
	 * @author cesar.castano
	 */
	private void guardarSobrantesyFaltantes(String tipoAjuste, String codigoServicio, 
									Double valor, SobrantesFaltantesDTO ajusteValor, Date fecha) {
		ajusteValor.setTipoAjuste(tipoAjuste);
		ajusteValor.setCodigoServicio(codigoServicio);
		ajusteValor.setValor(valor);
		ajusteValor.setFecha(fecha);
		listaAjustesValor.add(ajusteValor);
	}
	
	/**
	 * Metodo encargado de procesar los valores faltantes y sobrantes del registro tipo 05
	 * @author cesar.castano
	 */
	private void procesarSobranteFaltanteBrinks() {
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
	 * Metodo encargado de procesar los valores faltantes y sobrantes del registro tipo 05
	 * @author cesar.castano
	 */
	private void procesarSobranteFaltanteNoBrinks() {
		for (var i=0;i<listaAjustesValor.size();i++) {
			if (listaAjustesValor.get(i).getTipoAjuste().equals(Constantes.SOBRANTE_OTROS_FONDOS) ||
				listaAjustesValor.get(i).getTipoAjuste().equals(Constantes.SOBRANTE_TVS)){
				actualizarValorSobranteNoBrinks(listaAjustesValor.get(i).getCodigoServicio(),
										listaAjustesValor.get(i).getValor(), listaAjustesValor.get(i).getFecha());
			}else {
				if (listaAjustesValor.get(i).getTipoAjuste().equals(Constantes.FALTANTE_OTROS_FONDOS) ||
					listaAjustesValor.get(i).getTipoAjuste().equals(Constantes.FALTANTE_TVS)	){
				actualizarValorFaltanteNoBrinks(listaAjustesValor.get(i).getCodigoServicio(),
										listaAjustesValor.get(i).getValor(), listaAjustesValor.get(i).getFecha());
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
		List<OperacionesCertificadas> ocertificadas = operacionesCertificadasRepository
									.findByCodigoServicioTdv(codigoServicio);
		if (Objects.isNull(ocertificadas)) {
			throw new NegocioException(ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getHttpStatus());
		}else {
			for (OperacionesCertificadas operacionesCertificadas : ocertificadas) {
				operacionesCertificadas.setValorSobrante(valor);
				operacionesCertificadasRepository.save(operacionesCertificadas);
			}
		}
	}
	
	/**
	 * Metodo encargado de actualizar los valores faltantes del registro 05
	 * @param codigoServicio
	 * @param valor
	 * @author cesar.castano
	 */
	private void actualizarValorFaltante(String codigoServicio, Double valor) {
		List<OperacionesCertificadas> ocertificadas = operacionesCertificadasRepository
									.findByCodigoServicioTdv(codigoServicio);
		if (Objects.isNull(ocertificadas)) {
			throw new NegocioException(ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getHttpStatus());
		}else {
			for (OperacionesCertificadas operacionesCertificadas : ocertificadas) {
				operacionesCertificadas.setValorFaltante(valor);
				operacionesCertificadasRepository.save(operacionesCertificadas);
			}
		}
	}
	
	/**
	 * Metodo encargado de actualizar los valores sobrantes del registro 05
	 * @param codigoServicio
	 * @param valor
	 * @author cesar.castano
	 */
	private void actualizarValorSobranteNoBrinks(String codigoServicio, Double valor, Date fecha) {
		List<OperacionesCertificadas> ocertificadas = operacionesCertificadasRepository
		.findByCodigoPuntoDestinoAndEntradaSalidaAndFechaEjecucion(codigoServicio, "SALIDAS", fecha);

		if(Objects.isNull(ocertificadas)) {
		ocertificadas = operacionesCertificadasRepository
		.findByCodigoPuntoOrigenAndEntradaSalidaAndFechaEjecucion(codigoServicio, "ENTRADAS", fecha);
		}

		if (Objects.isNull(ocertificadas)) {
			throw new NegocioException(ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getHttpStatus());
		}else {
			for (OperacionesCertificadas operacionesCertificadas : ocertificadas) {
				operacionesCertificadas.setValorSobrante(valor);
				operacionesCertificadasRepository.save(operacionesCertificadas);
				break;
			}
		}
	}
	
	/**
	 * Metodo encargado de actualizar los valores faltantes del registro 05
	 * @param codigoServicio
	 * @param valor
	 * @author cesar.castano
	 */
	private void actualizarValorFaltanteNoBrinks(String codigoServicio, Double valor, Date fecha) {
		List<OperacionesCertificadas> ocertificadas = operacionesCertificadasRepository
									.findByCodigoPuntoDestinoAndEntradaSalidaAndFechaEjecucion(codigoServicio, "SALIDAS", fecha);
		
		if(Objects.isNull(ocertificadas)) {
			ocertificadas = operacionesCertificadasRepository
					.findByCodigoPuntoOrigenAndEntradaSalidaAndFechaEjecucion(codigoServicio, "ENTRADAS", fecha);
		}
		
		if (Objects.isNull(ocertificadas)) {
			throw new NegocioException(ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getHttpStatus());
		}else {
			for (OperacionesCertificadas operacionesCertificadas : ocertificadas) {
				operacionesCertificadas.setValorFaltante(valor);
				operacionesCertificadasRepository.save(operacionesCertificadas);
				break;
			}
		}
	}
	
	/**
	 * Metodo encargado de procesar los archivos de la Brinks
	 * @param elemento
	 * @author cesar.castano
	 */
	@Transactional
	private void procesarArchivoBrinks(ArchivosCargados elemento, 
									List<DetallesDefinicionArchivoDTO> detalleArchivo) {

		var registro = new RegistroTipo1ArchivosFondosDTO();
		
		for (var i = 0; i < elemento.getRegistrosCargados().size(); i++) {
			
			var ajusteValor = new SobrantesFaltantesDTO();
			String[] fila = elemento.getRegistrosCargados().get(i).getContenido().split(", ");
			String tipoRegistro = determinarTipoRegistro(fila, detalleArchivo);
			switch (Integer.parseInt(tipoRegistro)) {
			case 1: {
				String tdv = determinarCampo(fila, detalleArchivo, Integer.parseInt(tipoRegistro),
											Constantes.CAMPO_DETALLE_ARCHIVO_SIGLATRANSPORTADORA);
				String nit = determinarCampo(fila, detalleArchivo, Integer.parseInt(tipoRegistro),
											Constantes.CAMPO_DETALLE_ARCHIVO_CODIGONITENTIDAD);
				String nitbanco = nit.substring(0, 9);
				String ciudad = determinarCampo(fila, detalleArchivo, Integer.parseInt(tipoRegistro),
											Constantes.CAMPO_DETALLE_ARCHIVO_CODIGODANE);
				String ciudad1 = String.valueOf(Integer.parseInt(ciudad.trim()));
				Fondos fondo = asignarFondo(tdv, nitbanco, ciudad1);
				registro.setTdv(fondo.getTdv());
				registro.setCodigoPunto(fondo.getCodigoPunto());
				Date fecha = determinarFechaEjecucion(fila, detalleArchivo, Integer.parseInt(tipoRegistro),
									Constantes.CAMPO_DETALLE_ARCHIVO_FECHAEMISION);
				registro.setFechaEjecucion(fecha);
				registro.setBanco_aval(fondo.getBancoAVAL());
				break;
			}
			case 2: {
				break;
			}
			case 3: {
				String codigoServicio = determinarCampo(fila, detalleArchivo, 
													Integer.parseInt(tipoRegistro),
													Constantes.CAMPO_DETALLE_ARCHIVO_CODIGOSERVTRANS);
				String entradaSalida = determinarCampo(fila, detalleArchivo, 
													Integer.parseInt(tipoRegistro),
													Constantes.CAMPO_DETALLE_ARCHIVO_ENTRADAOSALIDA);
				String codigoPunto = determinarCampo(fila, detalleArchivo, 
													Integer.parseInt(tipoRegistro),
													Constantes.CAMPO_DETALLE_ARCHIVO_CODIGOPUNTO);
				String tipoServicio = determinarCampo(fila, detalleArchivo, 
													Integer.parseInt(tipoRegistro),
													Constantes.CAMPO_DETALLE_ARCHIVO_TIPOSERVICIOF);
				procesarOperacionTransporte(fila, registro, elemento, codigoServicio, 
											entradaSalida.toUpperCase(), codigoPunto, tipoServicio);
				break;
			}
			case 4: {
				break;
			}
			case 5: {
				String tipoNovedad = determinarCampo(fila, detalleArchivo, 
						Integer.parseInt(tipoRegistro),
						Constantes.CAMPO_DETALLE_ARCHIVO_TIPONOVEDAD);
				String codigoServicio = determinarCampo(fila, detalleArchivo, 
						Integer.parseInt(tipoRegistro),
						Constantes.CAMPO_DETALLE_ARCHIVO_CODIGOSERVICIO);
				String montoTotal = determinarCampo(fila, detalleArchivo, 
						Integer.parseInt(tipoRegistro),
						Constantes.CAMPO_DETALLE_ARCHIVO_VALORNOVEDAD);
				guardarSobrantesyFaltantes(tipoNovedad, codigoServicio, 
										Double.parseDouble(montoTotal), ajusteValor, elemento.getFechaArchivo());
				break;
			}
			case 6: {
				break;
			}
			case 7: {
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
	 * Metodo para determinar el campo Tipo de Registro
	 * @param contenido
	 * @param detalleArchivo
	 * @return String
	 * @author cesar.castano
	 */
	private String determinarTipoRegistro(String[] contenido, 
										List<DetallesDefinicionArchivoDTO> detalleArchivo) {
		return contenido[detalleArchivo.stream()
		                     		.filter(deta -> deta.getNombreCampo().toUpperCase().trim()
		                     		.equals(Constantes.CAMPO_DETALLE_ARCHIVO_TIPOREGISTRO) && 
		                     		deta.getId().getTipoRegistro().equals(Integer.parseInt(contenido[0])))
		                     		.findFirst().orElse(null).getId().getNumeroCampo() - 1].trim();
	}
	
	/**
	 * Metodo para determinar el campo Fecha de Ejecucion
	 * @param contenido
	 * @param detalleArchivo
	 * @param tipoRegistro
	 * @return String
	 * @author cesar.castano
	 */
	private Date determinarFechaEjecucion(String[] contenido, 
									List<DetallesDefinicionArchivoDTO> detalleArchivo,
									Integer tipoRegistro,
									String constante) {
		return asignarFecha(contenido[detalleArchivo.stream()
		                              .filter(deta -> deta.getNombreCampo().toUpperCase().trim()
		                              .equals(constante) && deta.getId().getTipoRegistro().equals(tipoRegistro))
		                              .findFirst().orElse(null).getId().getNumeroCampo() - 1].trim());
	}
	
	/**
	 * Metodo para determinar el campo
	 * @param contenido
	 * @param detalleArchivo
	 * @param tipoRegistro
	 * @param constante
	 * @return String
	 * @author cesar.castano
	 */
	private String determinarCampo(String[] contenido, 
									List<DetallesDefinicionArchivoDTO> detalleArchivo,
									Integer tipoRegistro,
									String constante) {
		return contenido[detalleArchivo.stream()
		                 			.filter(detalle -> detalle.getNombreCampo().toUpperCase().trim()
		                 			.equals(constante) && detalle.getId().getTipoRegistro().equals(tipoRegistro))
		                 			.findFirst().orElse(null).getId().getNumeroCampo() -1].trim();
	}
	
}
