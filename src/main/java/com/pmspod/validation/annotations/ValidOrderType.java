package com.pmspod.validation.annotations;

import com.pmspod.validation.validator.DateValidator;
import com.pmspod.validation.validator.OrderTypeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy= OrderTypeValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidOrderType {
    String message() default "Invalid Order Type";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
