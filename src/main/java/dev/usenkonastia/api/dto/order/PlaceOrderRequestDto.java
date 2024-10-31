package dev.usenkonastia.api.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder
@Jacksonized
public class PlaceOrderRequestDto {

    @NotNull(message = "Entries cannot be null")
    List<OrderItemDto> products;

    @NotNull(message = "Total price cannot be null")
    @Min(value = 0)
    Double totalPrice;
}
