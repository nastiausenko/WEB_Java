package dev.usenkonastia.api.dto.product;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;


@Value
@Builder
@Jacksonized
public class ProductEntryDto {
    UUID id;
    String categoryId;
    String productName;
    String productDescription;
    Double price;
    int quantity;
}
