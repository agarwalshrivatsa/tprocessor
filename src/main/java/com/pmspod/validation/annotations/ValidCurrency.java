package com.pmspod.validation.annotations;

import com.pmspod.validation.validator.CurrencyValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = CurrencyValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})  // Added ANNOTATION_TYPE
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCurrency {  // Change to @interface instead of interface
    String message() default "Currency must be HKD";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}