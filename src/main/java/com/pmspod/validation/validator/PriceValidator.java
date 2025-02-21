package com.pmspod.validation.validator;

import com.pmspod.validation.annotations.ValidPrice;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PriceValidator implements ConstraintValidator<ValidPrice, String> {
    @Override
    public boolean isValid(String price, ConstraintValidatorContext context) {
        if (price == null) {
            return false;
        }
        try {
            double value = Double.parseDouble(price);
            if (value <= 0) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        "Price must be greater than 0"
                ).addConstraintViolation();
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Price must be a valid number"
            ).addConstraintViolation();
            return false;
        }
    }
}