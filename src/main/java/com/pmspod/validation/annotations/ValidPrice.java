package com.pmspod.validation.annotations;

import com.pmspod.validation.validator.PriceValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PriceValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPrice {
    String message() default "Price must be a valid numeric value";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}