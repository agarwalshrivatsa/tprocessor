package com.pmspod.validation.validator;

import com.pmspod.validation.annotations.ValidDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateValidator implements ConstraintValidator<ValidDate, String> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        if(s != null) {
            try {
                LocalDate date= LocalDate.parse(s, DATE_FORMATTER);
                LocalDate today = LocalDate.now();

                // Check if date is in the future
                if (date.isAfter(today)) {
                    constraintValidatorContext.disableDefaultConstraintViolation();
                    constraintValidatorContext.buildConstraintViolationWithTemplate(
                            "Trade date cannot be in the future"
                    ).addConstraintViolation();
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
            return true;
        }
        return false;
    }
}
