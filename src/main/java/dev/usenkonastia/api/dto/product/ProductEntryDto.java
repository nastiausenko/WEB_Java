package dev.usenkonastia.api.dto.product;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;


@Value
@Builder
@Jacksonized
public class ProductEntryDto {
    Long id;
    String categoryId;
    String productName;
    String productDescription;
    Double price;
    int quantity;
}
