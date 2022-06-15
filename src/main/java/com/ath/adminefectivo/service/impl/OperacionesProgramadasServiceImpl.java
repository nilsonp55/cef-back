package com.ath.adminefectivo.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.ArchivosCargadosDTO;
import com.ath.adminefectivo.dto.BancosDTO;
import com.ath.adminefectivo.dto.CiudadesDTO;
import com.ath.adminefectivo.dto.DetallesDefinicionArchivoDTO;
import com.ath.adminefectivo.dto.FechasConciliacionDTO;
import com.ath.adminefectivo.dto.FondosDTO;
import com.ath.adminefectivo.dto.MaestrosDefinicionArchivoDTO;
import com.ath.adminefectivo.dto.OperacionesProgramadasDTO;
import com.ath.adminefectivo.dto.PuntosDTO;
import com.ath.adminefectivo.dto.RegistrosCargadosDTO;
import com.ath.adminefectivo.dto.TransportadorasDTO;
import com.ath.adminefectivo.dto.compuestos.DetalleOperacionesDTO;
import com.ath.adminefectivo.dto.compuestos.OperacionesProgramadasNombresDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.ArchivosCargados;
import com.ath.adminefectivo.entities.DetalleOperacionesProgramadas;
import com.ath.adminefectivo.entities.OperacionesProgramadas;
import com.ath.adminefectivo.entities.RegistrosCargados;
import com.ath.adminefectivo.entities.id.DetallesDefinicionArchivoPK;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.IOperacionesProgramadasRepository;
import com.ath.adminefectivo.service.IArchivosCargadosService;
import com.ath.adminefectivo.service.IBancosService;
import com.ath.adminefectivo.service.ICajerosService;
import com.ath.adminefectivo.service.ICiudadesService;
import com.ath.adminefectivo.service.IDetalleDefinicionArchivoService;
import com.ath.adminefectivo.service.IClientesCorporativosService;
import com.ath.adminefectivo.service.IDetalleOperacionesProgramadasService;
import com.ath.adminefectivo.service.IDominioService;
import com.ath.adminefectivo.service.IFondosService;
import com.ath.adminefectivo.service.ILecturaArchivoService;
import com.ath.adminefectivo.service.IMaestroDefinicionArchivoService;
import com.ath.adminefectivo.service.IOficinasService;
import com.ath.adminefectivo.service.IOperacionesProgramadasService;
import com.ath.adminefectivo.service.IPuntosService;
import com.ath.adminefectivo.service.ISitiosClientesService;
import com.ath.adminefectivo.service.IRegistrosCargadosService;
import com.ath.adminefectivo.service.ITransportadorasService;
import com.ath.adminefectivo.utils.UtilsString;
import com.querydsl.core.types.Predicate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Service
public class OperacionesProgramadasServiceImpl implements IOperacionesProgramadasService {

	@Autowired
	IOperacionesProgramadasRepository operacionesProgramadasRepository;

	@Autowired
	IFondosService fondosService;

	@Autowired
	ITransportadorasService transportadorasService;

	@Autowired
	IPuntosService puntosService;

	@Autowired
	ICiudadesService ciudadService;

	@Autowired
	IDominioService dominioService;
	
	@Autowired
	IOficinasService oficinaService;

	@Autowired
	IDetalleDefinicionArchivoService detalleDefinicionArchivoService;

	@Autowired
	IMaestroDefinicionArchivoService maestroDefinicionArchivoService;

	@Autowired
	IRegistrosCargadosService registrosCargadosService;

	@Autowired
	ILecturaArchivoService lecturaArchivoService;

	@Autowired
	IBancosService bancosService;
	
	@Autowired
	ISitiosClientesService sitiosClientesService;
	
	@Autowired
	ICajerosService cajerosService;

	@Autowired
	IArchivosCargadosService archivosCargadosService;
	
	@Autowired
	IClientesCorporativosService clientesCorporativosService;

	@Autowired
	IDetalleOperacionesProgramadasService detalleOperacionesProgramadasService;


	private List<TransportadorasDTO> listaTransportadoras;
	private List<FondosDTO> listaFondos;
	private List<PuntosDTO> listaPuntos;
	private List<CiudadesDTO> listaCiudades;

	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public Boolean actualizarEstadoEnProgramadas(Integer idOperacion, String estado) {

		Optional<OperacionesProgramadas> operaciones = operacionesProgramadasRepository.findById(idOperacion);
		if (operaciones.isPresent()) {
			try {
				operaciones.get().setEstadoConciliacion(estado);
				operaciones.get().setIdOperacion(idOperacion);
				operaciones.get().setFechaModificacion(new Date());
				operaciones.get().setUsuarioModificacion("user1");
				operacionesProgramadasRepository.save(operaciones.get());
			} catch (Exception e) {
				e.getMessage();
			}
		} else {
			throw new NegocioException(ApiResponseCode.ERROR_OPERACIONES_PROGRAMADAS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_OPERACIONES_PROGRAMADAS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_OPERACIONES_PROGRAMADAS_NO_ENCONTRADO.getHttpStatus());
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<OperacionesProgramadasNombresDTO> getNombresProgramadasConciliadas(
			List<OperacionesProgramadas> operacionesProgramadasList, Predicate predicate) {
		
		List<OperacionesProgramadasNombresDTO> listOperacionesProgramas = new ArrayList<>();
		this.getListados(predicate);
		for (OperacionesProgramadas programadas : operacionesProgramadasList) {
			var operacionesProgramadas = new OperacionesProgramadasNombresDTO();
			// Obtiene nombres de transportadora y Banco dueño del fondo
			operacionesProgramadas.setNombreTransportadora(this.getNombreTransportadora(
						programadas.getCodigoFondoTDV()));	
			operacionesProgramadas.setNombreBanco(this.getNombreBanco(programadas.getCodigoFondoTDV()));	
			// Obtiene nombres de tipo origen y nombre ciudad origen
			operacionesProgramadas.setNombrePuntoOrigen(this.getNombrePunto(programadas.getCodigoPuntoOrigen()));
			operacionesProgramadas.setNombreCiudadOrigen(this.getNombreCiudad(programadas.getCodigoPuntoOrigen()));
			// Obtiene nombres de tipo destino y nombre ciudad destino
			operacionesProgramadas.setNombrePuntoDestino(this.getNombrePunto(programadas.getCodigoPuntoDestino()));
			operacionesProgramadas.setNombreCiudadDestino(this.getNombreCiudad(programadas.getCodigoPuntoDestino()));
			// Datos sin nombres
			operacionesProgramadas.setTipoOperacion(programadas.getTipoOperacion());
			operacionesProgramadas.setValorTotal(programadas.getValorTotal());
			operacionesProgramadas.setFechaEjecucion(programadas.getFechaOrigen());
			operacionesProgramadas.setEstadoConciliacion(programadas.getEstadoConciliacion());
			// Obtiene datos de la tabla de Conciliacion Servicios
			operacionesProgramadas
						.setTipoConciliacion(programadas.getConciliacionServicios().get(0).getTipoConciliacion());
			operacionesProgramadas
						.setIdConciliacion(programadas.getConciliacionServicios().get(0).getIdConciliacion());
			listOperacionesProgramas.add(operacionesProgramadas);
		}
		return new PageImpl<>(listOperacionesProgramas);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer numeroOperacionesPorEstadoyFecha(FechasConciliacionDTO fechaConciliacion, String estado) {
		return operacionesProgramadasRepository.countByEstadoConciliacionAndFechaOrigenBetween(estado,
				fechaConciliacion.getFechaConciliacionInicial(), fechaConciliacion.getFechaConciliacionFinal());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<OperacionesProgramadasDTO> generarOperacionesProgramadas(List<ArchivosCargadosDTO> archivos) {
		List<OperacionesProgramadasDTO> listadoOperacionesProgramadas = new ArrayList<>();
		archivos.forEach(archivo -> {
			List<OperacionesProgramadasDTO> listaOperacionesProgramadas = this
					.procesarRegistrosCargadosArchivo(archivo);
			listadoOperacionesProgramadas.addAll(listaOperacionesProgramadas);
			if (!listaOperacionesProgramadas.isEmpty()) {
				archivo.setEstadoCargue(Dominios.ESTADO_VALIDACION_ACEPTADO);
				archivosCargadosService.actualizarArchivosCargados(archivo);
			}
		});
		return listadoOperacionesProgramadas;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean procesarArchivos(List<ArchivosCargados> archivosCargados) {

		for (ArchivosCargados elemento : archivosCargados) {
			if(elemento.getIdModeloArchivo().equals("ISRPO")) {
				procesarArchivoOficinas(elemento);
				actualizarValorTotal(elemento.getIdArchivo().intValue());
			}
			if(elemento.getIdModeloArchivo().equals("CAJER")) {
				procesarArchivoCajeros(elemento);
			}
		}
		return true;
	}


	/**
	 * Metodo encargado de realizar las consultas necesarias del archivo recibido
	 * para iniciar con el proceso de generacion de las operaciones programadas
	 * 
	 * @param archivo
	 * @return List<OperacionesProgramadasDTO>
	 * @author duvan.naranjo
	 */
	private List<OperacionesProgramadasDTO> procesarRegistrosCargadosArchivo(ArchivosCargadosDTO archivo) {
		List<OperacionesProgramadasDTO> listadoOperacionesProgramadas = new ArrayList<>();

		MaestrosDefinicionArchivoDTO maestrosDefinicionArchivoDTO = maestroDefinicionArchivoService
				.consultarDefinicionArchivoById(archivo.getIdModeloArchivo());

		String delimitador = lecturaArchivoService.obtenerDelimitadorArchivo(maestrosDefinicionArchivoDTO);

		List<RegistrosCargadosDTO> listadoRegistrosCargados = registrosCargadosService
				.consultarRegistrosCargadosPorIdArchivo(archivo.getIdArchivo());

		List<DetallesDefinicionArchivoDTO> listadoDetalleArchivo = detalleDefinicionArchivoService
				.consultarDetalleDefinicionArchivoByIdMaestro(archivo.getIdModeloArchivo());

		if (!listadoRegistrosCargados.isEmpty() && !Objects.isNull(listadoDetalleArchivo)) {
			listadoRegistrosCargados
					.forEach(registroCargado -> listadoOperacionesProgramadas.add(this.procesarRegistroCargado(
							registroCargado.getContenido().split(delimitador), listadoDetalleArchivo, archivo)));

		}
		return listadoOperacionesProgramadas;
	}

	/**
	 * Funcion encargada de obtener el tipo de operacion y llamar la función
	 * encargada de realizar cada operacion
	 * 
	 * @param contenido
	 * @param detalleArchivo
	 * @param archivo
	 * @return OperacionesProgramadasDTO
	 * @author duvan.naranjo
	 */
	private OperacionesProgramadasDTO procesarRegistroCargado(String[] contenido,
			List<DetallesDefinicionArchivoDTO> detalleArchivo, ArchivosCargadosDTO archivo) {
		OperacionesProgramadasDTO operacionProgramada = null;
		String tipoServicio = contenido[this.obtenerNumeroCampoTipoServ(detalleArchivo)];

		if (tipoServicio.toUpperCase().trim().contains(Dominios.TIPO_OPERA_CONSIGNACION)) {
			operacionProgramada = this.generarOperacionConsignacion(contenido, detalleArchivo, archivo, false);
		} else if (tipoServicio.toUpperCase().trim().contains(Dominios.TIPO_OPERA_RETIRO)) {
			operacionProgramada = this.generarOperacionRetiro(contenido, detalleArchivo, archivo, false);
		} else if (tipoServicio.toUpperCase().trim().contains(Dominios.TIPO_OPERA_VENTA)) {
			operacionProgramada = this.generarOperacionVenta(contenido, detalleArchivo, archivo);
		} else if (tipoServicio.toUpperCase().trim().contains(Dominios.TIPO_OPERA_CAMBIO)) {
			operacionProgramada = this.generarOperacionCambio(contenido, detalleArchivo, archivo);
		}
		return operacionProgramada;
	}

	/**
	 * Funcion encargada de obtener el numero de campo del tipo registro del detalle
	 * del archivo
	 * 
	 * @param detallesArchivo
	 * @return int
	 * @author duvan.naranjo
	 */
	private int obtenerNumeroCampoTipoServ(List<DetallesDefinicionArchivoDTO> detallesArchivo) {
		DetallesDefinicionArchivoDTO detalle = detallesArchivo.stream().filter(
				deta -> deta.getNombreCampo().toUpperCase().trim().equals(Constantes.CAMPO_ARCHIVO_TIPO_SERVICIO))
				.findFirst().orElse(null);
		if (!Objects.isNull(detalle)) {
			return detalle.getId().getNumeroCampo()-1;
		}
		return 1;
	}
	

	/**
	 * Funcion encargada de realizar la logica de la operacion consignacion
	 * 
	 * @param contenido
	 * @param detalleArchivo
	 * @param archivo
	 * @param esCambio
	 * @return OperacionesProgramadasDTO
	 * @author duvan.naranjo
	 */
	private OperacionesProgramadasDTO generarOperacionConsignacion(String[] contenido,
			List<DetallesDefinicionArchivoDTO> detalleArchivo, ArchivosCargadosDTO archivo, boolean esCambio) {

		OperacionesProgramadasDTO operacionesProgramadasDTO = null;
		PuntosDTO puntoFondoOrigen = this.consultarPuntoPorDetalle(contenido, detalleArchivo,
				Constantes.CAMPO_DETALLE_ARCHIVO_FONDO_ORIGEN);
		PuntosDTO puntoBancoDestino = this.consultarPuntoPorDetalle(contenido, detalleArchivo,
				Constantes.CAMPO_DETALLE_ARCHIVO_FONDO_DESTINO);

		if (!puntoFondoOrigen.getTipoPunto().toUpperCase().trim().equals(Dominios.TIPOS_PUNTO_FONDO)) {
			throw new NegocioException(ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getHttpStatus());
		} else if (!esCambio
				&& !puntoBancoDestino.getTipoPunto().toUpperCase().trim().equals(Dominios.TIPOS_PUNTO_BAN_REP)) {
			throw new AplicationException(ApiResponseCode.ERROR_NO_ES_BANREP.getCode(),
					ApiResponseCode.ERROR_NO_ES_BANREP.getDescription(),
					ApiResponseCode.ERROR_NO_ES_BANREP.getHttpStatus());
		}

		operacionesProgramadasDTO = OperacionesProgramadasDTO.builder()
				.codigoFondoTDV(puntoFondoOrigen.getCodigoPunto()).entradaSalida(Constantes.VALOR_SALIDA)
				.codigoPuntoOrigen(puntoFondoOrigen.getCodigoPunto())
				.codigoPuntoDestino(puntoBancoDestino.getCodigoPunto())
				.idArchivoCargado(Math.toIntExact(archivo.getIdArchivo())).build();

		var operacionProgramadaEnt = operacionesProgramadasRepository.save(OperacionesProgramadasDTO.CONVERTER_ENTITY
				.apply(this.completarOperacionesProgramadas(operacionesProgramadasDTO, contenido, detalleArchivo)));

		return OperacionesProgramadasDTO.CONVERTER_DTO.apply(operacionProgramadaEnt);
	}

	/**
	 * Funcion encargada de realizar la logica de la operacion retiro
	 * 
	 * @param contenido
	 * @param detalleArchivo
	 * @param archivo
	 * @param esCambio
	 * @return OperacionesProgramadasDTO
	 * @author duvan.naranjo
	 */
	private OperacionesProgramadasDTO generarOperacionRetiro(String[] contenido,
			List<DetallesDefinicionArchivoDTO> detalleArchivo, ArchivosCargadosDTO archivo, boolean esCambio) {

		OperacionesProgramadasDTO operacionesProgramadasDTO = null;

		PuntosDTO puntoFondoDestino = this.consultarPuntoPorDetalle(contenido, detalleArchivo,
				Constantes.CAMPO_DETALLE_ARCHIVO_FONDO_DESTINO);
		PuntosDTO puntoBancoOrigen = this.consultarPuntoPorDetalle(contenido, detalleArchivo,
				Constantes.CAMPO_DETALLE_ARCHIVO_FONDO_ORIGEN);

		if (!esCambio && !puntoBancoOrigen.getTipoPunto().toUpperCase().trim().equals(Dominios.TIPOS_PUNTO_BAN_REP)) {
			throw new NegocioException(ApiResponseCode.ERROR_NO_ES_BANREP.getCode(),
					ApiResponseCode.ERROR_NO_ES_BANREP.getDescription(),
					ApiResponseCode.ERROR_NO_ES_BANREP.getHttpStatus());
		} else if (!puntoFondoDestino.getTipoPunto().toUpperCase().trim().equals(Dominios.TIPOS_PUNTO_FONDO)) {
			throw new AplicationException(ApiResponseCode.ERROR_NO_ES_FONDO.getCode(),
					ApiResponseCode.ERROR_NO_ES_FONDO.getDescription(),
					ApiResponseCode.ERROR_NO_ES_FONDO.getHttpStatus());
		}

		operacionesProgramadasDTO = OperacionesProgramadasDTO.builder()
				.codigoFondoTDV(puntoFondoDestino.getCodigoPunto()).entradaSalida(Constantes.VALOR_ENTRADA)
				.codigoPuntoOrigen(puntoBancoOrigen.getCodigoPunto())
				.codigoPuntoDestino(puntoFondoDestino.getCodigoPunto())
				.idArchivoCargado(Math.toIntExact(archivo.getIdArchivo())).build();

		var operacionProgramadaEnt = operacionesProgramadasRepository
				.save(OperacionesProgramadasDTO.CONVERTER_ENTITY.apply(this.completarOperacionesProgramadas(operacionesProgramadasDTO, contenido,
						detalleArchivo)));
		return OperacionesProgramadasDTO.CONVERTER_DTO.apply(operacionProgramadaEnt);
	}

	/**
	 * Funcion encargada de realizar la logica de la operacion venta
	 * 
	 * @param contenido
	 * @param detalleArchivo
	 * @param archivo
	 * @return OperacionesProgramadasDTO
	 * @author duvan.naranjo
	 */
	@Transactional
	private OperacionesProgramadasDTO generarOperacionVenta(String[] contenido,
			List<DetallesDefinicionArchivoDTO> detalleArchivo, ArchivosCargadosDTO archivo) {

		OperacionesProgramadasDTO operacionesProgramadasDTO = null;

		PuntosDTO puntoFondoOrigen = this.consultarPuntoPorDetalle(contenido, detalleArchivo,
				Constantes.CAMPO_DETALLE_ARCHIVO_FONDO_ORIGEN);
		PuntosDTO puntoFondoDestino = this.consultarPuntoPorDetalle(contenido, detalleArchivo,
				Constantes.CAMPO_DETALLE_ARCHIVO_FONDO_DESTINO);
		BancosDTO bancoDestino = this.consultarBancoPorDetalle(contenido, detalleArchivo, Constantes.CAMPO_DETALLE_ARCHIVO_ENTIDAD_DESTINO);

		if (!Objects.isNull(puntoFondoOrigen) && !puntoFondoOrigen.getTipoPunto().toUpperCase().trim().equals(Dominios.TIPOS_PUNTO_FONDO)) {
			throw new AplicationException(ApiResponseCode.ERROR_NO_ES_FONDO.getCode(),
					ApiResponseCode.ERROR_NO_ES_FONDO.getDescription(),
					ApiResponseCode.ERROR_NO_ES_FONDO.getHttpStatus());
		}

		operacionesProgramadasDTO = OperacionesProgramadasDTO.builder()
				.codigoFondoTDV(puntoFondoOrigen.getCodigoPunto()).entradaSalida(Constantes.VALOR_SALIDA)
				.codigoPuntoOrigen(puntoFondoOrigen.getCodigoPunto())
				.idArchivoCargado(Math.toIntExact(archivo.getIdArchivo())).build();

		OperacionesProgramadasDTO operacionesProgramadasCompra = null;
		if (!Objects.isNull(bancoDestino) && bancoDestino.getEsAVAL()) {
			operacionesProgramadasDTO.setCodigoPuntoDestino(bancoDestino.getCodigoPunto());
			operacionesProgramadasCompra = this.generarOperacionVentaCompra(contenido, detalleArchivo, archivo);
			var operacionProgramadaEntity = operacionesProgramadasRepository
					.save(OperacionesProgramadasDTO.CONVERTER_ENTITY.apply(operacionesProgramadasCompra));
			operacionesProgramadasDTO.setIdOperacionRelac(operacionProgramadaEntity.getIdOperacion());
		} else {
			PuntosDTO puntoBancoDestino = this.consultarPuntoPorDetalle(contenido, detalleArchivo, Constantes.CAMPO_DETALLE_ARCHIVO_ENTIDAD_DESTINO);
			if (!Objects.isNull(puntoFondoDestino) && puntoFondoDestino.getTipoPunto().equals(Dominios.TIPOS_PUNTO_BANCO)) {
				operacionesProgramadasDTO.setCodigoPuntoDestino(bancoDestino.getCodigoPunto());
			}
		}

		var operacionProgramadaEnt = operacionesProgramadasRepository
				.save(OperacionesProgramadasDTO.CONVERTER_ENTITY.apply(this.completarOperacionesProgramadas(operacionesProgramadasDTO, contenido,
						detalleArchivo)));
		return OperacionesProgramadasDTO.CONVERTER_DTO.apply(operacionProgramadaEnt);

	}
	
	/**
	 * Metodo encargado de realizar la logica para la operacion venta compra
	 * 
	 * @param contenido
	 * @param detalleArchivo
	 * @param archivo
	 * @return OperacionesProgramadasDTO
	 * @author duvan.naranjo
	 */
	private OperacionesProgramadasDTO generarOperacionVentaCompra(String[] contenido,
			List<DetallesDefinicionArchivoDTO> detalleArchivo, ArchivosCargadosDTO archivo) {

		OperacionesProgramadasDTO operacionesProgramadasDTO = null;
		PuntosDTO puntoFondoDestino = this.consultarPuntoPorDetalle(contenido, detalleArchivo,
				Constantes.CAMPO_DETALLE_ARCHIVO_FONDO_DESTINO);
		BancosDTO bancoDestino = this.consultarBancoPorDetalle(contenido, detalleArchivo, Constantes.CAMPO_DETALLE_ARCHIVO_ENTIDAD_DESTINO);

		if (!Objects.isNull(puntoFondoDestino) && !puntoFondoDestino.getTipoPunto().toUpperCase().trim().equals(Dominios.TIPOS_PUNTO_FONDO)) {
			throw new AplicationException(ApiResponseCode.ERROR_NO_ES_FONDO.getCode(),
					ApiResponseCode.ERROR_NO_ES_FONDO.getDescription(),
					ApiResponseCode.ERROR_NO_ES_FONDO.getHttpStatus());
		}

		operacionesProgramadasDTO = OperacionesProgramadasDTO.builder()
				.codigoFondoTDV(puntoFondoDestino.getCodigoPunto()).entradaSalida(Constantes.VALOR_ENTRADA)
				.codigoPuntoOrigen(bancoDestino.getCodigoPunto()).codigoPuntoDestino(puntoFondoDestino.getCodigoPunto())
				.idArchivoCargado(Math.toIntExact(archivo.getIdArchivo())).build();

		return this.completarOperacionesProgramadas(operacionesProgramadasDTO, contenido, detalleArchivo);

	}

	/**
	 * Funcion encargada de realizar la logica de la operacion cambio
	 * 
	 * @param contenido
	 * @param detallesArchivo
	 * @param archivo
	 * @return OperacionesProgramadasDTO
	 * @author duvan.naranjo
	 */
	@Transactional
	private OperacionesProgramadasDTO generarOperacionCambio(String[] contenido,
			List<DetallesDefinicionArchivoDTO> detallesArchivo, ArchivosCargadosDTO archivo) {

		OperacionesProgramadasDTO operacionesProgramadaConsignacion = this.generarOperacionConsignacion(contenido,
				detallesArchivo, archivo, true);
		var codigoPuntoDestino = this.consultarBancoPorCiudad(contenido, detallesArchivo,
				Constantes.CAMPO_DETALLE_ARCHIVO_FONDO_ORIGEN);
		operacionesProgramadaConsignacion.setCodigoPuntoDestino(codigoPuntoDestino);
		OperacionesProgramadasDTO operacionesProgramadaRetiro = this.generarOperacionRetiro(contenido, detallesArchivo,
				archivo, true);
		operacionesProgramadaRetiro.setCodigoPuntoOrigen(codigoPuntoDestino);
		operacionesProgramadaRetiro.setEsCambio(true);
		operacionesProgramadaConsignacion.setEsCambio(true);
		operacionesProgramadaRetiro.setTipoOperacion(Dominios.TIPO_OPERA_RETIRO);
		operacionesProgramadaConsignacion.setTipoOperacion(Dominios.TIPO_OPERA_CONSIGNACION);
		operacionesProgramadaConsignacion.setIdOperacionRelac(operacionesProgramadaRetiro.getIdOperacion());

		operacionesProgramadasRepository
				.save(OperacionesProgramadasDTO.CONVERTER_ENTITY.apply(operacionesProgramadaRetiro));
		operacionesProgramadasRepository
				.save(OperacionesProgramadasDTO.CONVERTER_ENTITY.apply(operacionesProgramadaConsignacion));
		return operacionesProgramadaConsignacion;
	}

	/**
	 * Metodo encargado de realizar el llenado de campos de operaciones programadas
	 * provenientes del archivo cargado
	 * 
	 * @param operacionesProgramadasDTO
	 * @param contenido
	 * @param detalleArchivo
	 * @return OperacionesProgramadasDTO
	 * @author duvan.naranjo
	 */
	private OperacionesProgramadasDTO completarOperacionesProgramadas(
			OperacionesProgramadasDTO operacionesProgramadasDTO, String[] contenido,
			List<DetallesDefinicionArchivoDTO> detalleArchivo) {

		String fechaProgramacion = contenido[detalleArchivo.stream()
				.filter(deta -> deta.getNombreCampo().toUpperCase().trim()
						.equals(Constantes.CAMPO_DETALLE_ARCHIVO_FECHAPROGRAMACION))
				.findFirst().orElse(null).getId().getNumeroCampo() - 1].trim();

		String fechaOrigen = contenido[detalleArchivo.stream()
				.filter(deta -> deta.getNombreCampo().toUpperCase().trim()
						.equals(Constantes.CAMPO_DETALLE_ARCHIVO_FECHAORIGEN))
				.findFirst().orElse(null).getId().getNumeroCampo() - 1].trim();
		String fechaDestino = contenido[detalleArchivo.stream()
				.filter(deta -> deta.getNombreCampo().toUpperCase().trim()
						.equals(Constantes.CAMPO_DETALLE_ARCHIVO_FECHADESTINO))
				.findFirst().orElse(null).getId().getNumeroCampo() - 1].trim();
		String tipoOperacion = contenido[detalleArchivo.stream()
				.filter(deta -> deta.getNombreCampo().toUpperCase().trim()
						.equals(Constantes.CAMPO_DETALLE_ARCHIVO_TIPOSERVICIO))
				.findFirst().orElse(null).getId().getNumeroCampo() - 1].trim();

		Double valorTotal = Double.parseDouble(contenido[detalleArchivo.stream()
				.filter(deta -> deta.getNombreCampo().toUpperCase().trim()
						.equals(Constantes.CAMPO_DETALLE_ARCHIVO_VALORTOTAL))
				.findFirst().orElse(null).getId().getNumeroCampo() - 1].trim());

		String idNegociacion = contenido[detalleArchivo.stream()
				.filter(deta -> deta.getNombreCampo().toUpperCase().trim()
						.equals(Constantes.CAMPO_DETALLE_ARCHIVO_IDNEGOCIACION))
				.findFirst().orElse(null).getId().getNumeroCampo() - 1].trim();
		String tasaNegociacion = contenido[detalleArchivo.stream()
				.filter(deta -> deta.getNombreCampo().toUpperCase().trim()
						.equals(Constantes.CAMPO_DETALLE_ARCHIVO_TASA))
				.findFirst().orElse(null).getId().getNumeroCampo() - 1].trim();

		List<String> listaDominioFecha = dominioService.consultaListValoresPorDominio(Constantes.DOMINIO_FORMATO_FECHA);

		operacionesProgramadasDTO
				.setFechaProgramacion(UtilsString.convertirFecha(fechaProgramacion, listaDominioFecha));
		operacionesProgramadasDTO.setFechaOrigen(UtilsString.convertirFecha(fechaOrigen, listaDominioFecha));
		operacionesProgramadasDTO.setFechaDestino(UtilsString.convertirFecha(fechaDestino, listaDominioFecha));
		operacionesProgramadasDTO.setTipoOperacion(tipoOperacion);
		operacionesProgramadasDTO.setValorTotal(valorTotal);
		operacionesProgramadasDTO.setIdNegociacion(idNegociacion);
		operacionesProgramadasDTO.setTasaNegociacion(tasaNegociacion);
		operacionesProgramadasDTO.setEstadoOperacion(Dominios.ESTADOS_OPERA_PROGRAMADO);
		operacionesProgramadasDTO.setEstadoConciliacion(Dominios.ESTADO_CONCILIACION_NO_CONCILIADO);
		operacionesProgramadasDTO.setTipoServicio(Dominios.TIPO_SERVICIO_PROGRAMADA);
		operacionesProgramadasDTO.setUsuarioCreacion("ATH");
		operacionesProgramadasDTO.setFechaCreacion(new Date());
		operacionesProgramadasDTO.setEsCambio(false);

		return operacionesProgramadasDTO;
	}
	
	/**
	 * Metodo encargado de realizar la filtracion de un detalle por nombre campo
	 * para obtener el banco por una abreviatura
	 * 
	 * @param contenido
	 * @param detallesArchivo
	 * @param nombreCampo
	 * @return BancosDTO
	 * @author duvan.naranjo
	 */
	private BancosDTO consultarBancoPorDetalle(String[] contenido, List<DetallesDefinicionArchivoDTO> detallesArchivo,
			String nombreCampo) {
		DetallesDefinicionArchivoDTO detalle = detallesArchivo.stream()
				.filter(deta -> deta.getNombreCampo().toUpperCase().equals(nombreCampo)).findFirst().orElse(null);
		if(!Objects.isNull(detalle)) {
			return bancosService.findBancoByAbreviatura(contenido[detalle.getId().getNumeroCampo() - 1].trim());
		}
		return null;
	}
	
	/**
	 * Metodo encargado de realizar la consulta de un punto por detalle y nombre campo
	 * 
	 * @param contenido
	 * @param detalle
	 * @return PuntosDTO
	 * @author duvan.naranjo
	 */
	private PuntosDTO consultarPuntoPorDetalle(String[] contenido, List<DetallesDefinicionArchivoDTO> detallesArchivo,
			String nombreCampo) {

		DetallesDefinicionArchivoDTO detalle = detallesArchivo.stream()
				.filter(deta -> deta.getNombreCampo().toUpperCase().equals(nombreCampo)).findFirst().orElse(null);
		if(!Objects.isNull(detalle)) {
			return puntosService.getPuntoByNombrePunto(contenido[detalle.getId().getNumeroCampo() - 1].trim());
		}
		return null;
	}
	
	/**
	 * Metodo encargado de consultar un banco por medio del detalle de un archivo
	 * y su ciudad
	 * 
	 * @param contenido
	 * @param detallesArchivo
	 * @param nombreCampo
	 * @return Integer
	 * @author duvan.naranjo
	 */
	private Integer consultarBancoPorCiudad(String[] contenido, List<DetallesDefinicionArchivoDTO> detallesArchivo,
			String nombreCampo) {
		PuntosDTO puntoFondoDestino = this.consultarPuntoPorDetalle(contenido, detallesArchivo, nombreCampo);
		if(!Objects.isNull(puntoFondoDestino)) {
			PuntosDTO puntoBancoDestino = puntosService.getPuntoByTipoPuntoAndCodigoCiudad(Dominios.TIPOS_PUNTO_BAN_REP,
					puntoFondoDestino.getCodigoCiudad());
			return puntoBancoDestino.getCodigoPunto();
		}
		return null;
	}

	/**
	 * Metodo que consiste en obtener los listados de transportadoras, puntos
	 * codigo, puntos y ciudades en memoria
	 * 
	 * @param predicate
	 * @author cesar.castano
	 */
	private void getListados(Predicate predicate) {

		this.setListaTransportadoras(transportadorasService.getTransportadoras(predicate));
		this.setListaFondos(fondosService.getFondos(predicate));
		this.setListaPuntos(puntosService.getPuntos(predicate));
		this.setListaCiudades(ciudadService.getCiudades(predicate));
	}

	/**
	 * Metodo que obtiene el nombre del punto de una lista cuando el codigo punto es
	 * un Banco
	 * 
	 * @param codigoFondoTDV
	 * @return String
	 * @author cesar.castano
	 */
	private String getNombreBanco(Integer codigoFondoTDV) {
		FondosDTO fondos = listaFondos.stream().filter(fondo ->
				fondo.getCodigoPunto().equals(codigoFondoTDV)).findFirst().orElse(null);
		if(!Objects.isNull(fondos)) {
			var punto = listaPuntos.stream().filter(puntoT -> 
				puntoT.getCodigoPunto().equals(fondos.getBancoAVAL()) && 
				puntoT.getTipoPunto().equals(dominioService.valorTextoDominio(
						Constantes.DOMINIO_TIPOS_PUNTO,
						Dominios.TIPOS_PUNTO_BANCO))).findFirst().orElse(null);
			if(Objects.isNull(punto)) {
				throw new NegocioException(ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getCode(),
						ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getDescription(),
						ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getHttpStatus());
			}
			return punto.getNombrePunto();
		}else {
			throw new NegocioException(ApiResponseCode.ERROR_FONDOS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_FONDOS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_FONDOS_NO_ENCONTRADO.getHttpStatus());
		}
	}

	/**
	 * Metodo que se encarga de obtener el nombre de la transportadora de la lista
	 * de transportadoras
	 * 
	 * @param codigoFondoTDV
	 * @return String
	 * @author cesar.castano
	 */
	private String getNombreTransportadora(Integer codigoFondoTDV) {
		FondosDTO fondos = listaFondos.stream().filter(fondo -> fondo.getCodigoPunto().equals(codigoFondoTDV))
				.findFirst().orElse(null);
		if (!Objects.isNull(fondos)) {
			TransportadorasDTO transportadora = listaTransportadoras.stream()
					.filter(trans -> trans.getCodigo().equals(fondos.getTdv())).findFirst().orElse(null);
			if (!Objects.isNull(transportadora)) {
				return transportadora.getNombreTransportadora();
			} else {
				throw new NegocioException(ApiResponseCode.ERROR_TRANSPORTADORAS_NO_ENCONTRADO.getCode(),
						ApiResponseCode.ERROR_TRANSPORTADORAS_NO_ENCONTRADO.getDescription(),
						ApiResponseCode.ERROR_TRANSPORTADORAS_NO_ENCONTRADO.getHttpStatus());
			}
		} else {
			throw new NegocioException(ApiResponseCode.ERROR_FONDOS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_FONDOS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_FONDOS_NO_ENCONTRADO.getHttpStatus());
		}
	}

	/**
	 * Metodo que se encarga de obtener el nombre del punto de la lista de puntos
	 * 
	 * @param tipoPunto
	 * @param codigoPunto
	 * @return String
	 * @author cesar.castano
	 */
	private String getNombrePunto(Integer codigoPunto) {
		PuntosDTO puntos = listaPuntos.stream().filter(punto -> 
				punto.getCodigoPunto().equals(codigoPunto)).findFirst().orElse(null);
		if(!Objects.isNull(puntos)) {
			return puntos.getNombrePunto();
		}else {
			throw new NegocioException(ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getHttpStatus());
		}
	}

	/**
	 * Metodo que se encarga de obtener el nombre de la ciudad de la lista de
	 * ciudades
	 * 
	 * @param codigoPunto
	 * @return String
	 * @author cesar.castano
	 */
	private String getNombreCiudad(Integer codigoPunto) {
		PuntosDTO puntos = listaPuntos.stream().filter(punto -> 
					punto.getCodigoPunto().equals(codigoPunto)).findFirst().orElse(null);
		if(!Objects.isNull(puntos)) {
			CiudadesDTO ciudad = listaCiudades.stream().filter(ciud ->
					ciud.getCodigoDANE().equals(puntos.getCodigoCiudad())).findFirst().orElse(null);
			if(Objects.isNull(ciudad)) {
				throw new NegocioException(ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getCode(),
						ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getDescription(),
						ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getHttpStatus());
			}else {
				return ciudad.getNombreCiudad();
			}
		}else {
			throw new NegocioException(ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getCode(),
				ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getDescription(),
				ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getHttpStatus());
		}
	}
	
	/**
	 * Metodo encargado de persistir el archivo de cargue de Oficinas en OperacionesProgramadas
	 * @param elemento
	 * @return void
	 */
	private void procesarArchivoOficinas(ArchivosCargados elemento) {
		Integer numeroAnterior = 0;
		Integer idOperacion = 0;
		for (var i = 0; i < elemento.getRegistrosCargados().size(); i++) {
			String[] fila = elemento.getRegistrosCargados().get(i).getContenido().split(", ");
			if(Integer.parseInt(fila[0]) != numeroAnterior) {
				if (Integer.parseInt(fila[10].trim()) + Integer.parseInt(fila[11].trim()) != 0){
					var operaciones = new OperacionesProgramadasDTO();
					operaciones.setCodigoFondoTDV(fondosService
								.getCodigoFondo(fila[9], determinarCodigoCompensacion(fila), fila[8])
								.getCodigoPunto());
					operaciones.setEntradaSalida(asignarEntradaSalida(asignarTipoOperacion(fila)));
					operaciones.setCodigoPuntoOrigen(determinarPuntoOrigen(fila));
					operaciones.setCodigoPuntoDestino(determinarPuntoDestino(fila));
					operaciones.setEstadoConciliacion(dominioService.valorTextoDominio(
								Constantes.DOMINIO_ESTADO_CONCILIACION,
								Dominios.ESTADO_CONCILIACION_NO_CONCILIADO));
					operaciones.setEstadoOperacion(asignarEstadoOperacion(fila[2]));
					operaciones.setFechaCreacion(new Date());
					operaciones.setFechaDestino(asignarFecha(fila[4]));
					operaciones.setFechaModificacion(new Date());
					operaciones.setFechaOrigen(asignarFecha(fila[4]));
					operaciones.setFechaProgramacion(asignarFecha(fila[4]));
					operaciones.setIdArchivoCargado(elemento.getIdArchivo().intValue());
					operaciones.setIdNegociacion(null);
					operaciones.getIdOperacionRelac();
					operaciones.setTasaNegociacion(null);
					operaciones.setTipoOperacion(asignarTipoOperacion(fila));
					operaciones.setTipoServicio(asignarTipoServicio(fila[4]));
					operaciones.setTipoTransporte(null);
					operaciones.setUsuarioCreacion("User1");
					operaciones.setUsuarioModificacion("User1");
					operaciones.setValorTotal(asignarValorTotal(fila));
					operaciones.setIdServicio(fila[0]);
					var operacionesP = operacionesProgramadasRepository.save(
							OperacionesProgramadasDTO.CONVERTER_ENTITY.apply(operaciones));
					idOperacion = operacionesP.getIdOperacion();
					crearDetalleOperacionesProgramadas(fila, idOperacion);
					numeroAnterior = Integer.parseInt(fila[0]);
				}
			}else {
				if (Integer.parseInt(fila[10].trim()) + Integer.parseInt(fila[11].trim()) != 0){
					crearDetalleOperacionesProgramadas(fila, idOperacion);
				}
			}
		}
	}
	
	/**
	 * Metodo encargado de obtener el codigo de compensacion
	 * @param fila
	 * @return Integer
	 * @author cesar.castano
	 */
	private Integer determinarCodigoCompensacion(String[] fila) {
		Integer compensacion = 0;
		String[] fila1 = fila[5].split("-");
		if(fila1[0].equals("CLI")) {
			compensacion = Integer.parseInt(fila1[1].trim());
    	}else {
    		if(fila1[1].equals("SUC")) {
    			compensacion = Integer.parseInt(fila1[0].trim());
    		}
    	}
    	return compensacion;
	}

	/**
	 * Metodo encargado de determinar el tipo de punto origen
	 * @param fila
	 * @return Integer
	 * @author cesar.castano
	 */
    private Integer determinarPuntoOrigen(String[] fila) {
    	Integer codigo = 0;
		if(this.asignarEntradaSalida(asignarTipoOperacion(fila)).equals("O")) {
			return fondosService.getCodigoFondo(fila[9], determinarCodigoCompensacion(fila), fila[8]).getCodigoPunto();
		}else {
			codigo = validarEntradaSalida(fila);
		}
		return codigo;
	}

    /**
	 * Metodo encargado de determinar el tipo de punto destino
	 * @param fila
	 * @return Integer
	 * @author cesar.castano
	 */
    private Integer determinarPuntoDestino(String[] fila) {
    	Integer codigo = 0;
		if(this.asignarEntradaSalida(asignarTipoOperacion(fila)).equals("O")) {
			codigo = validarEntradaSalida(fila);
		}else {
			return fondosService.getCodigoFondo(fila[9], determinarCodigoCompensacion(fila), fila[8]).getCodigoPunto();
		}
		return codigo;
	}
    
	/**
     * Metodo que valida la entrada o la salida
     * @param fila
     * @return Integer
     */
    private Integer validarEntradaSalida(String[] fila) {
    	Integer codigo = 0;
    	String[] fila1 = fila[5].split("-");
    	if(fila1[0].equals("CLI")) {
    		codigo = asignarCodigoCliente(fila);
    	}else {
    		if(fila1[1].equals("SUC")) {
    			codigo = asignarCodigoOficina(fila);
    		}
    	}
    	return codigo;
	}

	/**
     * Metodo encargado de asignar el codigo punto del cliente
     * @param fila
     * @return Integer
     * @author cesar.castano
     */
	private Integer asignarCodigoCliente(String[] fila) {
		
		String[] fila1 = fila[5].split("-");
		Integer codigoPunto = bancosService.getCodigoPuntoBanco(Integer.parseInt(fila1[1]));
		Integer cliente = clientesCorporativosService.getCodigoCliente(codigoPunto, fila1[2].trim());
		return 9999;
//		return sitiosClientesService.getCodigoPuntoSitio(cliente);

	}
	
	/**
     * Metodo encargado de asignar el codigo punto de la oficina
     * @param fila
     * @return Integer
     * @author cesar.castano
     */
	private Integer asignarCodigoOficina(String[] fila) {
		
		String[] fila1 = fila[5].split("-");
		Integer codigoPunto = bancosService.getCodigoPuntoBanco(Integer.parseInt(fila1[0]));
		return oficinaService.getCodigoPunto(Integer.parseInt(fila1[2]), codigoPunto);

	}

	/**
     * Metodo encargado de asignar el valor del detalle
     * @param fila
     * @return Integer
     * @author cesar.castano
     */
	private Double asignarValorTotal(String[] fila) {
		Double valorTotal = 0.0;
		if(!fila[10].equals("0")) {
			valorTotal = Double.parseDouble(fila[11].trim());

		}else {
			if(!fila[11].equals("0")) {
				valorTotal = Double.parseDouble(fila[11].trim());
			}
		}
		return valorTotal;
	}

	/**
     * Metodo encargado de asignar la entrada o la salida de la operacion
     * @param tipoOperacion
     * @return String
     * @author cesar.castano
     */
	private String asignarEntradaSalida(String tipoOperacion) {
		var entradaSalida = " ";
		if (tipoOperacion.equals(dominioService.valorTextoDominio(
									Constantes.DOMINIO_TIPO_OPERACION,
									Dominios.TIPO_OPERA_PROVISION))) {
			entradaSalida = "O";
		}else {
			if (tipoOperacion.equals(dominioService.valorTextoDominio(
										Constantes.DOMINIO_TIPO_OPERACION,
										Dominios.TIPO_OPERA_RECOLECCION))) {
				entradaSalida = "I";
			}
		}
		return entradaSalida;
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
									Constantes.DOMINIO_FORMATO_FECHA_HORA_F3,
									Dominios.FORMATO_FECHA_HORA_3));
			fecha = formato.parse(fila);
		} catch (ParseException e) {
			e.getMessage();
		}
		return fecha;
	}

	/**
     * Metodo encargado de asignar el estado de la oepracion
     * @param fila
     * @return String
     * @author cesar.castano
     */
	private String asignarEstadoOperacion(String fila) {
		var estadoOperacion = "";
		switch (fila) {
			case "APRVD": {
			estadoOperacion = dominioService.valorTextoDominio(
					Constantes.DOMINIO_ESTADOS_OPERACION,
					Dominios.ESTADOS_OPERA_EJECUTADO);
			break;
		}
			case "DELIV": {
			estadoOperacion = dominioService.valorTextoDominio(
					Constantes.DOMINIO_ESTADOS_OPERACION,
					Dominios.ESTADOS_OPERA_EJECUTADO);
			break;
		}
			case "CANCEL": {
			estadoOperacion = dominioService.valorTextoDominio(
					Constantes.DOMINIO_ESTADOS_OPERACION,
					Dominios.ESTADOS_OPERA_CANCELADO);
			break;
		}
			case "DECLI": {
			estadoOperacion = dominioService.valorTextoDominio(
					Constantes.DOMINIO_ESTADOS_OPERACION,
					Dominios.ESTADOS_OPERA_FALLIDO);
			break;
		}
		default:
			throw new NegocioException(ApiResponseCode.ERROR_ESTADOS_OPERACION_NO_VALIDO.getCode(),
					ApiResponseCode.ERROR_ESTADOS_OPERACION_NO_VALIDO.getDescription(),
					ApiResponseCode.ERROR_ESTADOS_OPERACION_NO_VALIDO.getHttpStatus());
		}
		return estadoOperacion;
	}

	/**
	 * Metodo que retorna el tipo de Operacion
	 * @param fila
	 * @return String
	 * @author cesar.castano
	 */
	private String asignarTipoOperacion(String[] fila) {
		var tipoOperacion = "";
		if(!fila[10].trim().equals("0")) {
			tipoOperacion = dominioService.valorTextoDominio(
						Constantes.DOMINIO_TIPO_OPERACION,
						Dominios.TIPO_OPERA_PROVISION);
		} else {
			if(!fila[11].trim().equals("0")) {
				tipoOperacion = dominioService.valorTextoDominio(
						Constantes.DOMINIO_TIPO_OPERACION,
						Dominios.TIPO_OPERA_RECOLECCION);
			}
		}
		return tipoOperacion;
	}

	/**
	 * Metodo encargado de crear el detalle de las operaciones programadas
	 * @param fila
	 * @param idOperacion
	 * @return DetalleOperacionesProgramadas
	 * @author cesar.castano
	 */
	private DetalleOperacionesProgramadas crearDetalleOperacionesProgramadas(String[] fila, Integer idOperacion) {
		String[] fila1 = fila[12].split(" - ");
		var detalleOperacionesDTO = new DetalleOperacionesDTO();
		detalleOperacionesDTO.setIdOperacion(idOperacion);
		if (fila1.length > 1) {
			detalleOperacionesDTO.setFamilia(fila1[1].trim());
		}else {
			detalleOperacionesDTO.setFamilia(null);
		}
		detalleOperacionesDTO.setCalidad(null);
		detalleOperacionesDTO.setDenominacion(fila1[0].trim());
		detalleOperacionesDTO.setValorDetalle(this.asignarValorTotal(fila));
		return detalleOperacionesProgramadasService.crearRegistroDetalle(detalleOperacionesDTO);
	}
	
	/**
	 * Metodo que se encarga de restar dias a una fecha
	 * @param fecha
	 * @param dias
	 * @return Date
	 * @author cesar.castano
	 */
	public Date sumarRestarDiasFecha(Date fecha, int dias){
	      var calendar = Calendar.getInstance();
	      calendar.setTime(fecha); // Configuramos la fecha que se recibe
	      calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de días a añadir, o restar en caso de días<0
	      return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos
	 }
	
	/**
	 * Metodo encargado de asignar el tipo de Servicio
	 * @param fila
	 * @return String
	 * @author cesar.castano
	 */
	private String asignarTipoServicio(String fila) {
		Date fecha = null;
		var tipoServicio = "";
		fecha = asignarFecha(fila);
		Date fechaDiaAnterior = this.sumarRestarDiasFecha(fecha, -1);
		if (Integer.parseInt(fechaDiaAnterior.toString().substring(12, 13)) <= 18) {
			tipoServicio = dominioService.valorTextoDominio(
					Constantes.DOMINIO_TIPO_SERVICIO,
					Dominios.TIPO_SERVICIO_PROGRAMADA);
		}else {
			tipoServicio =  dominioService.valorTextoDominio(
					Constantes.DOMINIO_TIPO_SERVICIO,
					Dominios.TIPO_SERVICIO_ESPECIAL);
		}
		return tipoServicio;
	}
	
	/**
	 * Metodp encargado de  actualizar el valor total en OperacionesProgramadas
	 * @param idArchivoCargado
	 * @author cesar.castano
	 */
	private void actualizarValorTotal(Integer idArchivoCargado) {
		List<OperacionesProgramadas> operaciones = operacionesProgramadasRepository.
				findByIdArchivoCargado(idArchivoCargado);
		if(operaciones.isEmpty()) {
			throw new NegocioException(ApiResponseCode.ERROR_OPERACIONES_PROGRAMADAS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_OPERACIONES_PROGRAMADAS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_OPERACIONES_PROGRAMADAS_NO_ENCONTRADO.getHttpStatus());
		}else {
			for (OperacionesProgramadas operacionesProgramadas : operaciones) {
				Double valorDetalle = detalleOperacionesProgramadasService.obtenerValorDetalle(operacionesProgramadas.getIdOperacion());
				operacionesProgramadas.setIdOperacion(operacionesProgramadas.getIdOperacion());
				operacionesProgramadas.setValorTotal(valorDetalle);
				operacionesProgramadasRepository.save(operacionesProgramadas);
			}
		}
	}
	
	/**
	 * Metodo que se encarga de persistir en la tabla de OperacionesProgramadas, el archivo plano de CAJEROS
	 * @param elemento
	 * @author cesar.castano
	 */
	private void procesarArchivoCajeros(ArchivosCargados elemento) {
		
		for (var i = 0; i < elemento.getRegistrosCargados().size(); i++) {
			String[] fila = elemento.getRegistrosCargados().get(i).getContenido().split(", ");
			var operaciones = new OperacionesProgramadasDTO();
			operaciones.setCodigoFondoTDV(fondosService.getCodigoFondo(
											fila[7], 
											dominioService.valorTextoDominio(Constantes.DOMINIO_TIPOS_PUNTO, 
																				Dominios.TIPOS_PUNTO_BANCO), 
											fila[6], 
											fila[8])
											.getCodigoPunto());
			operaciones.setEntradaSalida(Constantes.SALIDA);
			operaciones.setCodigoPuntoOrigen(fondosService.getCodigoFondo(
											fila[7], 
											dominioService.valorTextoDominio(Constantes.DOMINIO_TIPOS_PUNTO, 
																				Dominios.TIPOS_PUNTO_BANCO), 
											fila[6], 
											fila[8])
											.getCodigoPunto());
			operaciones.setCodigoPuntoDestino(cajerosService.getCodigoPunto(fila[4]));
			operaciones.setEstadoConciliacion(dominioService.valorTextoDominio(
											Constantes.DOMINIO_ESTADO_CONCILIACION,
											Dominios.ESTADO_CONCILIACION_NO_CONCILIADO));
			operaciones.setEstadoOperacion(dominioService.valorTextoDominio(
											Constantes.DOMINIO_ESTADOS_OPERACION,
											Dominios.ESTADOS_OPERA_EJECUTADO));
			operaciones.setFechaCreacion(new Date());
			operaciones.setFechaDestino(asignarFecha(fila[3]));
			operaciones.setFechaModificacion(new Date());
			operaciones.setFechaOrigen(asignarFecha(fila[3]));
			operaciones.setFechaProgramacion(asignarFecha(fila[3]));
			operaciones.setIdArchivoCargado(elemento.getIdArchivo().intValue());
			operaciones.setIdNegociacion(null);
			operaciones.setIdOperacionRelac(null);
			operaciones.setTasaNegociacion(null);
			operaciones.setTipoOperacion(dominioService.valorTextoDominio(
											Constantes.DOMINIO_TIPO_OPERACION,
											Dominios.TIPO_OPERA_PROVISION));
			operaciones.setTipoServicio(dominioService.valorTextoDominio(
											Constantes.DOMINIO_TIPO_SERVICIO,
											Dominios.TIPO_SERVICIO_PROGRAMADA));
			operaciones.setTipoTransporte(null);
			operaciones.setUsuarioCreacion("User1");
			operaciones.setUsuarioModificacion("User1");
			operaciones.setValorTotal(Double.parseDouble(fila[9].trim()));
			operaciones.setIdServicio(fila[0]);
			operacionesProgramadasRepository.save(OperacionesProgramadasDTO.CONVERTER_ENTITY.apply(operaciones));
		}
	}
}
