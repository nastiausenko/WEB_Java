package dev.usenkonastia.api.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ProductDto {

    @NotBlank(message = "Product name cannot be null")
    String productName;

    @PositiveOrZero(message = "Id must be positive")
    String categoryId;

    String productDescription;

    @PositiveOrZero(message = "Price must be positive")
    Double price;

    @PositiveOrZero(message = "Quantity must be positive")
    int quantity;
}
