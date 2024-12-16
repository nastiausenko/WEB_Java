package dev.usenkonastia.api.repository.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import static jakarta.persistence.CascadeType.PERSIST;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "order_item")
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    UUID productId;
    Integer quantity;
    Double price;

    @ManyToOne(cascade = PERSIST)
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false)
    OrderEntity order;
}
