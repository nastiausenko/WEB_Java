package dev.usenkonastia.api.dto.order;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Value
@Builder
@Jacksonized
public class PlaceOrderResponseDto {
    UUID orderId;
    String transactionId;
}
