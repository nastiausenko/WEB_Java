package dev.usenkonastia.api.domain.product;

import lombok.*;

@Value
@Builder
public class Product {
    Long id;
    String categoryId;
    String productName;
    String description;
    Double price;
    int quantity;
}
