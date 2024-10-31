package dev.usenkonastia.api.domain;

import lombok.*;

import java.util.UUID;

@Value
@Builder
public class Product {
    UUID id;
    String categoryId;
    String productName;
    String description;
    Double price;
    int quantity;
}
