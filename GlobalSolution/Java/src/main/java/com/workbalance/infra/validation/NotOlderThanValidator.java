package com.workbalance.infra.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

/**
 * Validator implementation for @NotOlderThan annotation
 */
public class NotOlderThanValidator implements ConstraintValidator<NotOlderThan, LocalDate> {

    private int days;

    @Override
    public void initialize(NotOlderThan constraintAnnotation) {
        this.days = constraintAnnotation.days();
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Use @NotNull for null checks
        }
        
        LocalDate threshold = LocalDate.now().minusDays(days);
        return !value.isBefore(threshold);
    }
}
