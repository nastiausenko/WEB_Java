package dev.usenkonastia.api.service.exception;

import java.util.UUID;

public class CategoryNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Category with id %s not found";
    public CategoryNotFoundException(UUID categoryId) {
        super(String.format(MESSAGE, categoryId));
    }
}
