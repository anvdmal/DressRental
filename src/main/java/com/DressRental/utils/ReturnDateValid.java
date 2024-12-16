package com.DressRental.utils;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = ReturnDateValidator.class)
public @interface ReturnDateValid {
    String message() default "Дата возврата должна быть позже даты аренды";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}

