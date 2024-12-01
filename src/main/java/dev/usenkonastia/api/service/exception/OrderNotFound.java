package dev.usenkonastia.api.service.exception;

public class OrderNotFound extends RuntimeException {
  private static final String MESSAGE = "Order with cartId %s not found";
    public OrderNotFound(String cartId) {
        super(String.format(MESSAGE, cartId));
    }
}
