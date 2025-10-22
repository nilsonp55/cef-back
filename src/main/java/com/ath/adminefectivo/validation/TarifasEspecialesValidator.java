package com.ath.adminefectivo.validation;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.dto.TarifasEspecialesClienteDTO;
import com.ath.adminefectivo.entities.VTarifasEspecialesClienteEntity;
import com.ath.adminefectivo.repositories.IVTarifasEspecialesClienteRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.Optional;

public class TarifasEspecialesValidator
		implements ConstraintValidator<ValidarTarifasEspecialesCliente, TarifasEspecialesClienteDTO> {

	@Autowired
	private IVTarifasEspecialesClienteRepository tarifasRepository;

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	@Override
	public boolean isValid(TarifasEspecialesClienteDTO dto, ConstraintValidatorContext context) {
		if (dto == null) {
			return true;
		}

		context.disableDefaultConstraintViolation();

		if (dto.getIdTarifaEspecial() == null) {
			return validarFechas(dto, context, LocalDate.now().minusMonths(1).with(TemporalAdjusters.firstDayOfMonth()),
					"primer día del mes anterior");
		}

		return validarReglaEdicion(dto, context);
	}

	private boolean validarReglaEdicion(TarifasEspecialesClienteDTO dto, ConstraintValidatorContext context) {
		Optional<VTarifasEspecialesClienteEntity> tarifaOpt = tarifasRepository
				.findByIdTarifaEspecial(dto.getIdTarifaEspecial());

		if (!tarifaOpt.isPresent()) {
			addViolation(context, "idTarifaEspecial",
					"No se encontró el registro para el idTarifaEspecial proporcionado.");
			return false;
		}

		String regla = tarifaOpt.get().getReglaEdicion();
		LocalDate primerDiaMesAnterior = LocalDate.now().minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
		LocalDate fechaLimiteParcial = primerDiaMesAnterior.minusDays(1);

		switch (regla) {
		case Constantes.REGLA_EDICION_COMPLETA:
			return validarFechas(dto, context, primerDiaMesAnterior, "primer día del mes anterior");

		case Constantes.REGLA_EDICION_PARCIAL:
			return validarFechaFin(dto, context, fechaLimiteParcial, "primer día del mes anterior menos un día");

		case Constantes.REGLA_NO_EDITABLE:
			addViolation(context, "idTarifaEspecial", "Este registro no puede ser editado.");
			return false;

		default:
			return true;
		}
	}

	private boolean validarFechas(TarifasEspecialesClienteDTO dto, ConstraintValidatorContext context,
			LocalDate fechaMinima, String descripcionFecha) {
		boolean esValido = true;
		LocalDate inicio = toLocalDate(dto.getFechaInicioVigencia());
		LocalDate fin = toLocalDate(dto.getFechaFinVigencia());
		String fechaFormato = fechaMinima.format(FORMATTER);

		// Validar inicio
		if (inicio == null) {
			addViolation(context, "fechaInicioVigencia", "Es un campo requerido.");
			esValido = false;
		} else {
			if (inicio.isBefore(fechaMinima)) {
				addViolation(context, "fechaInicioVigencia", mensajeFechaMinima(fechaFormato, descripcionFecha));
				esValido = false;
			}
			if (fin != null && inicio.isAfter(fin)) {
				addViolation(context, "fechaInicioVigencia", "No puede ser mayor que la fecha de fin de vigencia.");
				esValido = false;
			}
		}

		// Validar fin
		if (fin == null) {
			addViolation(context, "fechaFinVigencia", "Es un campo requerido.");
			esValido = false;
		} else {
			if (fin.isBefore(fechaMinima)) {
				addViolation(context, "fechaFinVigencia", mensajeFechaMinima(fechaFormato, descripcionFecha));
				esValido = false;
			}
			if (inicio != null && fin.isBefore(inicio)) {
				addViolation(context, "fechaFinVigencia", "No puede ser menor que la fecha de inicio de vigencia.");
				esValido = false;
			}
		}

		return esValido;
	}

	private boolean validarFechaFin(TarifasEspecialesClienteDTO dto, ConstraintValidatorContext context,
			LocalDate fechaMinima, String descripcionFecha) {
		LocalDate fin = toLocalDate(dto.getFechaFinVigencia());
		String fechaFormato = fechaMinima.format(FORMATTER);

		if (fin == null) {
			addViolation(context, "fechaFinVigencia", "Es un campo requerido.");
			return false;
		}
		if (fin.isBefore(fechaMinima)) {
			addViolation(context, "fechaFinVigencia", mensajeFechaMinima(fechaFormato, descripcionFecha));
			return false;
		}
		return true;
	}

	private LocalDate toLocalDate(Date fecha) {
		return fecha != null ? fecha.toInstant().atZone(ZoneOffset.UTC).toLocalDate() : null;
	}

	private String mensajeFechaMinima(String fechaFormato, String descripcion) {
		return "Debe ser mayor o igual a la fecha mínima permitida: " + fechaFormato + " (" + descripcion + ")";
	}

	private void addViolation(ConstraintValidatorContext context, String property, String mensaje) {
		context.buildConstraintViolationWithTemplate(mensaje).addPropertyNode(property).addConstraintViolation();
	}
}
