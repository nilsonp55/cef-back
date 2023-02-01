package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
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
import com.ath.adminefectivo.dto.ArchivosCargadosDTO;
import com.ath.adminefectivo.dto.compuestos.ErroresCamposDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionLineasDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.ArchivosCargados;
import com.ath.adminefectivo.entities.FallasArchivo;
import com.ath.adminefectivo.entities.FallasRegistro;
import com.ath.adminefectivo.entities.RegistrosCargados;
import com.ath.adminefectivo.entities.id.FallasRegistroPK;
import com.ath.adminefectivo.entities.id.RegistrosCargadosPK;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.ArchivosCargadosRepository;
import com.ath.adminefectivo.repositories.IRegistrosCargadosRepository;
import com.ath.adminefectivo.service.IArchivosCargadosService;
import com.ath.adminefectivo.service.IMaestroDefinicionArchivoService;
import com.ath.adminefectivo.service.IParametroService;
import com.ath.adminefectivo.utils.UtilsString;
import com.querydsl.core.types.Predicate;

@Service
public class ArchivosCargadosServiceImpl implements IArchivosCargadosService {

	@Autowired
	ArchivosCargadosRepository archivosCargadosRepository;
	
	@Autowired
	IRegistrosCargadosRepository registrosCargadosRepository;

	@Autowired
	IMaestroDefinicionArchivoService maestroDefinicionArchivoService;
	
	@Autowired
	IParametroService parametrosService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ArchivosCargadosDTO> getAll() {
		var archivos = archivosCargadosRepository.findAll();
		System.out.println(archivos);
		List<ArchivosCargadosDTO> listArchivosDto = new ArrayList<>();
		archivos.forEach(entity -> listArchivosDto.add(ArchivosCargadosDTO.CONVERTER_DTO.apply(entity)));

		return listArchivosDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ArchivosCargados> guardarArchivos(List<ArchivosCargadosDTO> archivosCargados) {
		var archivosEntidad = archivosCargados.stream().map(ArchivosCargadosDTO.CONVERTER_ENTITY).toList();
		return archivosCargadosRepository.saveAll(archivosEntidad);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<ArchivosCargadosDTO> getAll(Predicate predicate, Pageable page) {
		var archivos = archivosCargadosRepository.findAll(predicate, page);
		return new PageImpl<>(archivos.getContent().stream().map(ArchivosCargadosDTO.CONVERTER_DTO).toList(),archivos.getPageable(), archivos.getTotalElements());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<ArchivosCargadosDTO> getAllByAgrupador(String agrupador, Pageable page) {
		
		Page<ArchivosCargados> archivosCargados = archivosCargadosRepository.getArchivosByAgrupador(agrupador,"ACT", page);
		return new PageImpl<>(archivosCargados.getContent().stream().map(ArchivosCargadosDTO
		.CONVERTER_DTO).toList(), archivosCargados.getPageable(), archivosCargados.getTotalElements());

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArchivosCargadosDTO consultarArchivo(Long idArchivo) {
		var archivo = archivosCargadosRepository.findById(idArchivo);

		if (archivo.isPresent()) {
			return ArchivosCargadosDTO.CONVERTER_DTO.apply(archivo.get());
		} else {
			throw new NegocioException(ApiResponseCode.ERROR_ARCHIVOS_NO_EXISTE_BD.getCode(),
					ApiResponseCode.ERROR_ARCHIVOS_NO_EXISTE_BD.getDescription(),
					ApiResponseCode.ERROR_ARCHIVOS_NO_EXISTE_BD.getHttpStatus());
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArchivosCargadosDTO eliminarArchivoCargado(Long idArchivo) {

		var archivo = archivosCargadosRepository.findById(idArchivo);

		if (archivo.isPresent()) {
			var archivoEntity = archivo.get();
			archivoEntity.setEstado(Constantes.REGISTRO_INACTIVO);
			archivosCargadosRepository.save(archivoEntity);
			return ArchivosCargadosDTO.CONVERTER_DTO.apply(archivo.get());
		} else {
			throw new NegocioException(ApiResponseCode.ERROR_ARCHIVOS_NO_EXISTE_BD.getCode(),
					ApiResponseCode.ERROR_ARCHIVOS_NO_EXISTE_BD.getDescription(),
					ApiResponseCode.ERROR_ARCHIVOS_NO_EXISTE_BD.getHttpStatus());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValidacionArchivoDTO consultarDetalleArchivo(Long idArchivo) {
		var archivoOp = archivosCargadosRepository.findById(idArchivo);

		if (!archivoOp.isPresent()) {
			throw new NegocioException(ApiResponseCode.ERROR_ARCHIVOS_NO_EXISTE_BD.getCode(),
					ApiResponseCode.ERROR_ARCHIVOS_NO_EXISTE_BD.getDescription(),
					ApiResponseCode.ERROR_ARCHIVOS_NO_EXISTE_BD.getHttpStatus());
		}

		var archivo = archivoOp.get();
		ValidacionArchivoDTO validacionArchivo = ValidacionArchivoDTO.builder().idArchivo(idArchivo)
				.nombreArchivo(archivo.getNombreArchivo()).numeroRegistros(archivo.getNumeroRegistros())
				.numeroErrores(archivo.getNumeroErrores()).usuarioCreacion(archivo.getUsuarioCreacion())
				.fechaInicioCargue(archivo.getFechaInicioCargue()).estadoValidacion(archivo.getEstadoCargue())
				.fechaArchivo(archivo.getFechaArchivo()).build();

		if (Objects.nonNull(archivo.getFallasArchivos())
				&& Objects.nonNull(archivo.getFallasArchivos().getDescripcionError())) {
			validacionArchivo.setDescripcionErrorEstructura(archivo.getFallasArchivos().getDescripcionError());
		}

		if (Objects.nonNull(archivo.getRegistrosCargados())) {
			validacionArchivo.setValidacionLineas(this.obtenerValorLineas(archivo.getRegistrosCargados()));
		}

		return validacionArchivo;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public Long persistirDetalleArchivoCargado(ValidacionArchivoDTO validacionArchivo, boolean soloErrores) {
		Date fechaGuardar;
		fechaGuardar = parametrosService.valorParametroDate(Constantes.FECHA_DIA_PROCESO);

		if (Dominios.ESTADO_VALIDACION_CORRECTO.equals(validacionArchivo.getEstadoValidacion()) ) {
			this.cambiarEstadoArchivoOK(validacionArchivo);
		}
		ArchivosCargados archivosCargados = ArchivosCargados.builder()
				.estado(Constantes.REGISTRO_ACTIVO)
				.estadoCargue(validacionArchivo.getEstadoValidacion())
				.idModeloArchivo(validacionArchivo.getMaestroDefinicion().getIdMaestroDefinicionArchivo())
				.nombreArchivo(validacionArchivo.getNombreArchivo())
				.numeroErrores(validacionArchivo.getNumeroErrores())
				.numeroRegistros(validacionArchivo.getNumeroRegistros())
				.fechaArchivo(fechaGuardar)
				.fechaInicioCargue(new Date())
				.usuarioCreacion("ATH")
				.fechaCreacion(new Date()).build();
		var archivoCargadoEntity = archivosCargadosRepository.save(archivosCargados);
		
		if (Objects.nonNull(validacionArchivo.getDescripcionErrorEstructura())) {

			FallasArchivo fallasArchivo = FallasArchivo.builder()
					.idArchivo(archivoCargadoEntity.getIdArchivo())
					.descripcionError(validacionArchivo.getDescripcionErrorEstructura())
					.estado(Constantes.REGISTRO_ACTIVO)
					.usuarioCreacion("ATH")
					.fechaCreacion(new Date()).build();

			archivosCargados.setFallasArchivos(fallasArchivo);
		}

		if (Objects.nonNull(validacionArchivo.getValidacionLineas())
				&& !validacionArchivo.getValidacionLineas().isEmpty()) {

			archivosCargados.setRegistrosCargados(this.organizacionLineasPersistencia(
					validacionArchivo.getValidacionLineas(), archivoCargadoEntity.getIdArchivo(), soloErrores));
		}

		return archivoCargadoEntity.getIdArchivo();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ArchivosCargadosDTO> getArchivosCargadosSinProcesar(String idModeloArchivo) {
		List<ArchivosCargadosDTO> resultado = new ArrayList<>();

		List<ArchivosCargados> archivosCargados = archivosCargadosRepository
				.findByEstadoCargueAndIdModeloArchivo(Dominios.ESTADO_VALIDACION_CORRECTO, idModeloArchivo);

		if (!Objects.isNull(archivosCargados)) {
			archivosCargados.forEach(arch -> {
				resultado.add(ArchivosCargadosDTO.CONVERTER_DTO.apply(arch));
			});
			return resultado;
		}
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void actualizarArchivosCargados(ArchivosCargadosDTO archivosCargadosDTO) {
		archivosCargadosRepository.save(ArchivosCargadosDTO.CONVERTER_ENTITY.apply(archivosCargadosDTO));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ArchivosCargados> listadoArchivosCargadosSinProcesarDefinitiva(String agrupador, Date fecha, String estado) {
		return archivosCargadosRepository.getRegistrosCargadosSinProcesarDeHoy(agrupador, fecha, estado);

	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void actualizarArchivosCargados(ArchivosCargados archivosCargados) {
		archivosCargadosRepository.save(archivosCargados);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ArchivosCargados> consultarArchivosPorFecha(Date fechaActual) {
	
		return archivosCargadosRepository.findByFechaArchivo(fechaActual);
	}

	/**
	 * Método encargado de organizar y separar las informacion de las lineas
	 * 
	 * @param listValidacion
	 * @param idArchivo
	 * @return
	 * @return List<RegistrosCargados>
	 * @author CamiloBenavides
	 */
	private List<RegistrosCargados> organizacionLineasPersistencia(List<ValidacionLineasDTO> listValidacion,
			Long idArchivo, boolean soloErrores) {
		List<RegistrosCargados> registrosCargados = new ArrayList<>();

		var listaPersistencia = listValidacion.stream()
				.filter(x -> !soloErrores || Objects.equals(x.getEstado(), Dominios.ESTADO_VALIDACION_REGISTRO_ERRADO))
				.toList();
		for (ValidacionLineasDTO lineas : listaPersistencia) {

			var registrosCargadosPk = RegistrosCargadosPK.builder().consecutivoRegistro(lineas.getNumeroLinea())
					.idArchivo(idArchivo).build();
			var registroCargado = RegistrosCargados.builder()
					.estado(Constantes.REGISTRO_ACTIVO)
					.estadoRegistro(lineas.getEstado())
					.contenido(lineas.getContenidoTxt())
					.tipoRegistro(lineas.getTipo())
					.usuarioCreacion("ATH")
					.fechaCreacion(new Date())
					.id(registrosCargadosPk).build();

			if (Objects.nonNull(lineas.getCampos()) && !lineas.getCampos().isEmpty()) {
				List<FallasRegistro> fallasRegistro = new ArrayList<>();
				lineas.getCampos().forEach(camp -> {

					var fallasRegistroPk = FallasRegistroPK.builder()
							.idArchivo(idArchivo)
							.consecutivoRegistro((long) lineas.getNumeroLinea())
							.numeroCampo((long) camp.getNumeroCampo()).build();
					fallasRegistro.add(FallasRegistro.builder()
							.contenido(camp.getContenido())
							.descripcionError(camp.getMensajeErrorTxt())
							.estado(Constantes.REGISTRO_ACTIVO)
							.usuarioCreacion("ATH")
							.fechaCreacion(new Date())
							.id(fallasRegistroPk).build());
				});
				registroCargado.setFallasRegistro(fallasRegistro);
			}
			registrosCargados.add(registroCargado);
		}

		return registrosCargados;

	}

	/**
	 * Retornal el objeto de validacionLineasDTO organizado con la informacion de
	 * una lista de registros cargados
	 * 
	 * @param registrosCargados
	 * @return List<ValidacionLineasDTO>
	 * @author CamiloBenavides
	 */
	private List<ValidacionLineasDTO> obtenerValorLineas(List<RegistrosCargados> registrosCargados) {
		var errores = registrosCargados.stream()
				.filter(x -> Objects.equals(x.getEstadoRegistro(), Dominios.ESTADO_VALIDACION_REGISTRO_ERRADO)
						&& Objects.nonNull(x.getFallasRegistro()))
				.toList();

		List<ValidacionLineasDTO> validacionLineas = new ArrayList<>();

		errores.forEach(x -> validacionLineas.add(ValidacionLineasDTO.builder()
				.numeroLinea(x.getId().getConsecutivoRegistro()).estado(x.getEstadoRegistro()).tipo(x.getTipoRegistro())
				.campos(this.obtenerErrores(x.getFallasRegistro())).build()));

		return validacionLineas;

	}

	/**
	 * Metodo encargado de organizar la data de una lista de fallas de registro y
	 * retorna un objeto de ErroresCamposDTO
	 * 
	 * @param fallasRegistro
	 * @return List<ErroresCamposDTO>
	 * @author CamiloBenavides
	 */
	private List<ErroresCamposDTO> obtenerErrores(List<FallasRegistro> fallasRegistro) {

		List<ErroresCamposDTO> erroresCamposDTO = new ArrayList<>();
		for (FallasRegistro fallasIt : fallasRegistro) {

			erroresCamposDTO.add(ErroresCamposDTO.builder().numeroCampo(fallasIt.getId().getNumeroCampo().intValue())
					.mensajeErrorTxt(fallasIt.getDescripcionError()).contenido(fallasIt.getContenido())
					.mensajeError(List.of(fallasIt.getDescripcionError().split(Constantes.SEPARADOR_PUNTO_Y_COMA)))
					.build());

		}

		return erroresCamposDTO;
	}
	
	/**
	 * Revisa si ya existe un archivo en estado OK para la fecha
	 * Si existe le cambia el estado a REEMPLAZADO
	 * @param validacionArchivo
	 * @author RParra
	 */
	private void cambiarEstadoArchivoOK (ValidacionArchivoDTO validacionArchivo) {
		
		List<ArchivosCargados> archivosCargados = archivosCargadosRepository
				.getRegistrosCargadosPorNombreyEstado(Dominios.ESTADO_VALIDACION_CORRECTO, 
						validacionArchivo.getNombreArchivo(),
						validacionArchivo.getMaestroDefinicion().getIdMaestroDefinicionArchivo());

		if (!Objects.isNull(archivosCargados)) {
			archivosCargados.forEach(arch -> {
				var archivoEntity = arch;
				archivoEntity.setEstadoCargue(Dominios.ESTADO_VALIDACION_REEMPLAZADO);
				archivosCargadosRepository.save(archivoEntity);
				
			});	
		}
	}


}
