package com.pmspod.validation.validator;

import com.pmspod.enums.OrderType;
import com.pmspod.validation.annotations.ValidOrderType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OrderTypeValidator implements ConstraintValidator<ValidOrderType, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(s.equals(OrderType.BUY.getValue()) || s.equals(OrderType.SELL.getValue())) return true;
        return false;
    }
}
