package dev.usenkonastia.api.service.exception;

public class CategoryNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Product with id %s not found";
    public CategoryNotFoundException(Long id) {
            super(String.format(MESSAGE, id));
    }
}
