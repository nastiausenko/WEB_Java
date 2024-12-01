package dev.usenkonastia.api.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(name = "product_name", nullable = false, unique = true)
    String productName;

    @Column(nullable = false)
    String description;

    @Column(nullable = false)
    Double price;

    @Column(nullable = false)
    Integer quantity;

    @ManyToOne
    @JoinColumn(name = "category_id")
    CategoryEntity category;

}
