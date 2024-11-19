package dev.usenkonastia.api.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@Builder
public class Category {
    UUID id;
    String name;
    List<Product> products;
}
