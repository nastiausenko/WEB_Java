package dev.usenkonastia.api.dto.category;

import dev.usenkonastia.api.dto.product.ProductEntryDto;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder
@Jacksonized
public class CategoryEntryDto {
    Long id;
    String name;
    List<ProductEntryDto> products;
}
