package dev.usenkonastia.api.domain.order;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class OrderItem {
    UUID productId;
    int quantity;
}
