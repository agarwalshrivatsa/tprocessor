package com.pmspod.validation.validator;

import com.pmspod.validation.annotations.ValidCurrency;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CurrencyValidator implements ConstraintValidator<ValidCurrency, String> {
    @Override
    public void initialize(ValidCurrency constraintAnnotation) {
    }

    @Override
    public boolean isValid(String currency, ConstraintValidatorContext context) {
        if (currency == null) {
            return false;
        }
        return "HKD".equals(currency);
    }


}
