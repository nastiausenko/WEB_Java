package dev.usenkonastia.api.repository;

import dev.usenkonastia.api.repository.entity.ProductEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends CrudRepository<ProductEntity, UUID> {
    ProductEntity findByProductName(String name);
}
