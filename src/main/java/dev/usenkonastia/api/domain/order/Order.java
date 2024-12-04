package dev.usenkonastia.api.domain.order;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@Builder
public class Order {
    UUID id;
    String cartId;
    UUID transactionId;
    String customerReference;
    List<OrderItem> products;
    Double totalPrice;
    String status;
}
