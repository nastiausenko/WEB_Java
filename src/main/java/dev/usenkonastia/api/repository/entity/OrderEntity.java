package dev.usenkonastia.api.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.util.List;
import java.util.UUID;

import static jakarta.persistence.CascadeType.PERSIST;

@Entity
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @NaturalId
    @Column(unique = true, nullable = false)
    String cartId;
    Double totalPrice;
    String paymentReference;

    @ManyToOne(cascade = PERSIST)
    @JoinColumn(name = "cat_id", nullable = false)
    CosmoCatEntity customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST, orphanRemoval = true)
    List<OrderItemEntity> products;
}
