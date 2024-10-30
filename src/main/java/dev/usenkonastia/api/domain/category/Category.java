package dev.usenkonastia.api.domain.category;

import dev.usenkonastia.api.domain.product.Product;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class Category {
    Long id;
    String name;
    List<Product> products;
}
