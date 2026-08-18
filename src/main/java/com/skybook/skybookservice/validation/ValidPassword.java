package com.skybook.skybookservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "Пароль должен содержать от 6 до 8 символов и минимум одну цифру";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
