package com.pmspod.validation.validator;

import com.pmspod.validation.annotations.ValidCurrency;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class CurrencyValidator implements ConstraintValidator<ValidCurrency, String> {

    // List of supported currencies
    private static final List<String> SUPPORTED_CURRENCIES = Arrays.asList("HKD", "USD", "CNY", "EUR", "GBP");

    @Override
    public boolean isValid(String currency, ConstraintValidatorContext context) {
        if (currency == null || currency.isEmpty()) {
            return false;
        }

        // Create a more specific error message
        if (!SUPPORTED_CURRENCIES.contains(currency)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Currency must be HKD")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}