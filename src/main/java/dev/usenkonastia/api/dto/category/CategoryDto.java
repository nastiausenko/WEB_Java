package dev.usenkonastia.api.dto.category;


import dev.usenkonastia.api.dto.product.ProductDto;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder
@Jacksonized
public class CategoryDto {
    @NotNull(message = "Category name cannot be null")
    String categoryName;

    List<ProductDto> products;
}
