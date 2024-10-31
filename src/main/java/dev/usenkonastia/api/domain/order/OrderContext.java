package dev.usenkonastia.api.domain.order;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class OrderContext {
    String cartId;
    String customerReference;
    List<OrderItem> products;
    Double totalPrice;
}
