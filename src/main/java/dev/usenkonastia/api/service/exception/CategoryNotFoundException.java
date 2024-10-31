package dev.usenkonastia.api.service.exception;

import java.util.UUID;

public class CategoryNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Product with id %s not found";
    public CategoryNotFoundException(UUID id) {
            super(String.format(MESSAGE, id));
    }
}
