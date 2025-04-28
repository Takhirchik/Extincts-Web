package com.warlock.model.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = URLValidator.class)
public @interface URL {
    String message() default "Некорректный URL";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}