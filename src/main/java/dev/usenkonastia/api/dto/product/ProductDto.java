package dev.usenkonastia.api.dto.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ProductDto {

    @NotNull(message = "Product name cannot be null")
    String productName;

    String categoryId;

    String productDescription;

    @Positive(message = "Price must be greater than 0")
    Double price;

    @Positive(message = "Quantity must be greater than 0")
    int quantity;
}
