package ru.otus.hw.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.otus.hw.models.MessageType;
import ru.otus.hw.utils.ValidMessageType;

import java.util.Set;

public class MessageTypeValidator implements ConstraintValidator<ValidMessageType, String> {

    private static final Set<String> VALID_TYPES =
        Set.of(MessageType.EMAIL.name(), MessageType.TELEGRAM.name());

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && VALID_TYPES.contains(value);
    }
}
