package dev.usenkonastia.api.service.exception;

public class CatNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Cat with id %s not found";

    public CatNotFoundException(String id) {
        super(String.format(MESSAGE, id));
    }
}
