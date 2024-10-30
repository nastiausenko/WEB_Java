package dev.usenkonastia.api.dto.product;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder
@Jacksonized
public class ProductListDto {
    List<ProductEntryDto> products;
}
