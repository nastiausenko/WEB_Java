package dev.usenkonastia.api.repository.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "cosmo_cats")
public class CosmoCatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String catName;

    @NaturalId
    @Column(unique = true, nullable = false)
    String email;

    @OneToMany(mappedBy = "customer", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.LAZY)
    List<OrderEntity> orders;
}
