package dev.usenkonastia.api.service.exception;

public class ProductNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Product with id %s not found";

    public ProductNotFoundException(Long id) {
        super(String.format(MESSAGE, id));
    }
}
