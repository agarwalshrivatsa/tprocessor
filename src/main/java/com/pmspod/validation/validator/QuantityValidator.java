package com.pmspod.validation.validator;

import com.pmspod.validation.annotations.ValidQuantity;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class QuantityValidator implements ConstraintValidator<ValidQuantity, String> {
    @Override
    public boolean isValid(String quantity, ConstraintValidatorContext context) {
        if (quantity == null) {
            return false;
        }
        try {
            double value = Double.parseDouble(quantity);
            if (value <= 0) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        "Quantity must be greater than 0"
                ).addConstraintViolation();
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Quantity must be a valid number"
            ).addConstraintViolation();
            return false;
        }
    }
}