package dev.usenkonastia.api.dto.category;


import dev.usenkonastia.api.dto.product.ProductDto;
import dev.usenkonastia.api.dto.validation.CosmicWordCheck;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder
@Jacksonized
public class CategoryDto {
    @CosmicWordCheck
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    @NotBlank(message = "Category name cannot be null")
    String categoryName;

    List<ProductDto> products;
}
