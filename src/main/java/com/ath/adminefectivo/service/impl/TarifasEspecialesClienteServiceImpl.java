package com.ath.adminefectivo.service.impl;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.dto.TarifasEspecialesClienteDTO;
import com.ath.adminefectivo.dto.VTarifasEspecialesClienteDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.TarifasEspecialesCliente;
import com.ath.adminefectivo.repositories.IVTarifasEspecialesClienteRepository;
import com.ath.adminefectivo.repositories.ITarifasEspecialesRepository;
import com.ath.adminefectivo.service.IAuditoriaProcesosService;
import com.ath.adminefectivo.service.ITarifasEspecialesClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import com.ath.adminefectivo.entities.VTarifasEspecialesClienteEntity;
import com.ath.adminefectivo.exception.NegocioException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TarifasEspecialesClienteServiceImpl implements ITarifasEspecialesClienteService {

	@Autowired
	private ITarifasEspecialesRepository tarifasEspeciales;
	
	@Autowired
	private IVTarifasEspecialesClienteRepository vTarifasEspecialesClienteRepository;
	
	@Autowired
	IAuditoriaProcesosService auditoriaProcesosService;
	
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	@Override
	public List<TarifasEspecialesClienteDTO> consultar() {
		List<TarifasEspecialesCliente> entidades = tarifasEspeciales.findAll();
		return entidades.stream().map(TarifasEspecialesClienteDTO.CONVERTER_DTO).collect(Collectors.toList());
	}

	@Override
	public TarifasEspecialesClienteDTO guardar(TarifasEspecialesClienteDTO dto) {
		
		// HENRY BORRAR
		if (dto.getUnidadCobro() == null || dto.getUnidadCobro().trim().isEmpty()) {
	        dto.setUnidadCobro("UNIDAD");
	    }
		
		if (dto.getCodigoDane() == null || dto.getCodigoDane().trim().isEmpty()) {
	        dto.setCodigoDane("68001");
	    }
		// HENRY BORRAR
		
		validarCruceDeVigencias(dto);
		dto.setFechaCreacion(new Date());
		TarifasEspecialesCliente entity = TarifasEspecialesClienteDTO.CONVERTER_ENTITY.apply(dto);
		TarifasEspecialesCliente savedEntity = tarifasEspeciales.save(entity);
		return TarifasEspecialesClienteDTO.CONVERTER_DTO.apply(savedEntity);
	}

	@Override
	public TarifasEspecialesClienteDTO actualizar(TarifasEspecialesClienteDTO dto) {
		
		 if (dto.getIdTarifaEspecial() == null) {
			 throw new NegocioException(ApiResponseCode.ERROR_ID_TARIFA_NO_PROPORCIONADO.getCode(),
						ApiResponseCode.ERROR_ID_TARIFA_NO_PROPORCIONADO.getDescription(),
						ApiResponseCode.ERROR_ID_TARIFA_NO_PROPORCIONADO.getHttpStatus());
		    }
		 
		// HENRY BORRAR
			if (dto.getUnidadCobro() == null || dto.getUnidadCobro().trim().isEmpty()) {
		        dto.setUnidadCobro("UNIDAD");
		    }
			
			if (dto.getCodigoDane() == null || dto.getCodigoDane().trim().isEmpty()) {
		        dto.setCodigoDane("68001");
		    }
			// HENRY BORRAR

		validarCruceDeVigencias(dto);
		dto.setFechaModificacion(new Date());
		TarifasEspecialesCliente entity = TarifasEspecialesClienteDTO.CONVERTER_ENTITY.apply(dto);
		TarifasEspecialesCliente savedEntity = tarifasEspeciales.save(entity);
		return TarifasEspecialesClienteDTO.CONVERTER_DTO.apply(savedEntity);
	}

	@Override
	public void eliminar(Long idTarifaEspecial) {

		LocalDate primerDiaMesAnterior = LocalDate.now().minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
		String fechaLimiteFormato = primerDiaMesAnterior.format(FORMATTER);

		VTarifasEspecialesClienteEntity tarifa = vTarifasEspecialesClienteRepository
				.findByIdTarifaEspecial(idTarifaEspecial)
				.orElseThrow(() -> new NegocioException(ApiResponseCode.NOT_FOUND_ERROR.getCode(),
						ApiResponseCode.NOT_FOUND_ERROR.getDescription(),
						ApiResponseCode.NOT_FOUND_ERROR.getHttpStatus()));

		String reglaEdicion = tarifa.getReglaEdicion();

		if (!Constantes.REGLA_EDICION_COMPLETA.equals(reglaEdicion)) {
			String mensaje = String.format("%s %s (primer día del mes anterior)",
					ApiResponseCode.ERROR_ELIMINACION_REGISTRO_TARIFAS_ESPECIALES.getDescription(), fechaLimiteFormato);

			throw new NegocioException(ApiResponseCode.ERROR_ELIMINACION_REGISTRO_TARIFAS_ESPECIALES.getCode(), mensaje,
					ApiResponseCode.ERROR_ELIMINACION_REGISTRO_TARIFAS_ESPECIALES.getHttpStatus());
		}

		tarifasEspeciales.deleteById(idTarifaEspecial);
	}

	@Override
	public Page<VTarifasEspecialesClienteDTO> consultarPorCodigoCliente(Integer codigoCliente, String vigencia, Pageable pageable) {

		LocalDate primerDiaMesAnterior = LocalDate.now().withDayOfMonth(1).minusMonths(1);
		Date formatDate = java.sql.Date.valueOf(primerDiaMesAnterior);

		Page<VTarifasEspecialesClienteEntity> page;

		if (Constantes.VIGENCIAS_PASADAS.equalsIgnoreCase(vigencia)) {
		    page = vTarifasEspecialesClienteRepository.findByCodigoClienteAndFechaFinVigenciaBefore(codigoCliente, formatDate, pageable);
		} else if (Constantes.VIGENCIAS_ACTUALES.equalsIgnoreCase(vigencia)) {
		    page = vTarifasEspecialesClienteRepository.findByCodigoClienteAndFechaFinVigenciaAfterOrEquals(codigoCliente, formatDate, pageable);
		} else {
		    page = vTarifasEspecialesClienteRepository.findByCodigoCliente(codigoCliente, pageable);
		}

	    return page.map(VTarifasEspecialesClienteDTO.CONVERTER_DTO::apply);
	}
	
	private void validarCruceDeVigencias(TarifasEspecialesClienteDTO dto) {
		
	    List<VTarifasEspecialesClienteEntity> cruces = vTarifasEspecialesClienteRepository.findCrucesDeVigencia(
	    	dto.getCodigoBanco(),
	    	dto.getCodigoCliente(),
	        dto.getCodigoTdv(),
	        dto.getCodigoPunto(),
	        dto.getCodigoDane(),
	        dto.getTipoComision(),
	        dto.getTipoOperacion(),
	        dto.getTipoServicio(),
	        dto.getEscala(),
	        dto.getFechaInicioVigencia(),
	        dto.getFechaFinVigencia(),
	        dto.getIdTarifaEspecial() // puede ser null en caso de guardado
	    );

	    if (!cruces.isEmpty()) {
	    	throw new NegocioException(ApiResponseCode.ERROR_TARIFA_ESPECIAL_DUPLICADA.getCode(),
					ApiResponseCode.ERROR_TARIFA_ESPECIAL_DUPLICADA.getDescription(),
					ApiResponseCode.ERROR_TARIFA_ESPECIAL_DUPLICADA.getHttpStatus());
	    }
	}



}
