package com.ath.adminefectivo.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TarifasEspecialesValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidarTarifasEspecialesCliente {
    String message() default "Datos inconsistentes en la tarifa especial del cliente";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}