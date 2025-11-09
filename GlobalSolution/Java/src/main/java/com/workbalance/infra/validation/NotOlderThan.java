package com.workbalance.infra.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Custom validation annotation to ensure a date is not older than specified days
 * Solves the inconsistency between @PastOrPresent and service-level validation
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotOlderThanValidator.class)
@Documented
public @interface NotOlderThan {
    
    String message() default "Date must not be older than {days} days";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
    int days();
}
