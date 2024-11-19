package dev.usenkonastia.api.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class OrderItemDto {

    @NotNull(message = "Product id cannot be null")
    UUID productId;

    @NotNull(message = "Quantity cannot be null")
    int quantity;
}
