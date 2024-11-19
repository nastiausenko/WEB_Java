package dev.usenkonastia.api.service.exception;

import java.util.UUID;

public class ProductNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Product with id %s not found";

    public ProductNotFoundException(UUID id) {
        super(String.format(MESSAGE, id));
    }
}
