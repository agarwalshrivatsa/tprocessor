package com.pmspod.validation.annotations;

import com.pmspod.validation.validator.QuantityValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = QuantityValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidQuantity {
    String message() default "Quantity must be a valid numeric value";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}