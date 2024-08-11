package ru.otus.hw.utils;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.otus.hw.validators.MessageTypeValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = { MessageTypeValidator.class })
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidMessageType {

    String message() default "Invalid message type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
